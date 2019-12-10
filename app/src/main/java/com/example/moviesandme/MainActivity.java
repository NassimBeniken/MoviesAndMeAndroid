package com.example.moviesandme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import com.example.moviesandme.Model.Movie;
import com.example.moviesandme.consumeApi.MovieCrudService;
import com.example.moviesandme.databaseHelper.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnMovieListener {

    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Movie> listMovies;
    private static final String TAG = "MainActivity";
    private MovieCrudService movieCrudService;
    private Retrofit retrofit;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listMovies = new ArrayList<>();

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieCrudService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieCrudService = retrofit.create(MovieCrudService.class);

        myDatabaseHelper = new MyDatabaseHelper(this);

        Call<List<Movie>> call = movieCrudService.listMovies();


        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                //ce qu'on va mettre dans les champs si reponse ok
                listMovies = response.body();
                rv = findViewById(R.id.recyclerView);
                rv.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(MainActivity.this);
                adapter = new MyAdapter(listMovies, MainActivity.this);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                //ce qu'on va mettre dans les champs si pas de reponse
                Log.d(TAG + ".onCreate", "Failure to get the information from API, using SQLite now");
                String query = "SELECT * FROM " + MyDatabaseHelper.MOVIE_TABLE;
                db = myDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery(query,null);

                if(cursor.moveToFirst()) {
                    do {
                        Movie movie = new Movie();
                        movie.setId(cursor.getInt(0));
                        movie.setTitle(cursor.getString(1));
                        movie.setYear(cursor.getString(2));
                        movie.setReleased(cursor.getString(3));
                        movie.setGenre(cursor.getString(4));
                        movie.setImage(cursor.getString(5));
                        movie.setSynopsis(cursor.getString(6));

                        listMovies.add(movie);
                    } while(cursor.moveToNext());
                }
                cursor.close();

                rv = findViewById(R.id.recyclerView);
                rv.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(MainActivity.this);
                adapter = new MyAdapter(listMovies, MainActivity.this);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onMovieClick(final int position) {
        Log.d(TAG, "onMovieClick: clicked.");

        Call<Movie> call = movieCrudService.getMovieById(listMovies.get(position).getId());
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("movie",response.body());
                startActivity(intent);
                overridePendingTransition(R.animator.slide_from_right, R.animator.slide_to_left);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("onMovieClick", "Failure to get the information from API, using SQLite now");
                Movie movie = new Movie();
                int id = listMovies.get(position).getId();
                String[] columns = new String[]{MyDatabaseHelper.MOVIE_ID,MyDatabaseHelper.MOVIE_TITLE,MyDatabaseHelper.MOVIE_YEAR,MyDatabaseHelper.MOVIE_RELEASED,MyDatabaseHelper.MOVIE_GENRE,MyDatabaseHelper.MOVIE_IMAGE,MyDatabaseHelper.MOVIE_SYNOPSIS};
                Cursor cursor = db.query(MyDatabaseHelper.MOVIE_TABLE,columns,"id = " + id, null, null, null, null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast())  {
                    movie.setId(cursor.getInt(0));
                    movie.setTitle(cursor.getString(1));
                    movie.setYear(cursor.getString(2));
                    movie.setReleased(cursor.getString(3));
                    movie.setGenre(cursor.getString(4));
                    movie.setImage(cursor.getString(5));
                    movie.setSynopsis(cursor.getString(6));
                    cursor.moveToNext();
                }
                cursor.close();
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("movie",movie);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_from_right, R.animator.slide_to_left);
                db.close();
            }
        });
    }
}
