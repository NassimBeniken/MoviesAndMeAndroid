package com.example.moviesandme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesandme.Model.Movie;
import com.example.moviesandme.consumeApi.MovieCrudService;
import com.example.moviesandme.databaseHelper.MyDatabaseHelper;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private Button updateButton;
    private Button deleteButton;
    private MovieCrudService movieCrudService;
    private Retrofit retrofit;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);

        myDatabaseHelper = new MyDatabaseHelper(this);

        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("movie");

        retrofit = new Retrofit.Builder()
                .baseUrl(MovieCrudService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieCrudService = retrofit.create(MovieCrudService.class);

        final int id = movie.getId();
        final String imageRes = movie.getImage();
        final String year = movie.getYear();
        final String title = movie.getTitle();
        String date = movie.getReleased();
        String genre = movie.getGenre();
        String synopsis = movie.getSynopsis();

        final ImageView imageView = findViewById(R.id.image_details);
        Picasso.get().load(imageRes).centerCrop().fit().into(imageView);

        final TextView titleText = findViewById(R.id.title_details);
        titleText.setText(title);

        final TextView dateText = findViewById(R.id.date_details);
        dateText.setText(date);

        final TextView genreText = findViewById(R.id.genre_details);
        genreText.setText(genre);

        final TextView synopsisText = findViewById(R.id.synopsis_details);
        synopsisText.setText(synopsis);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //faire le put ici
                Movie modifiedMovie = new Movie(id,titleText.getText().toString(),year,dateText.getText().toString(),genreText.getText().toString(),imageRes,synopsisText.getText().toString());
                Call<Movie> call = movieCrudService.updateById(modifiedMovie.getId(),modifiedMovie);
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Intent returnIntent = new Intent(MovieDetailsActivity.this,MainActivity.class);
                        finish();
                        startActivity(returnIntent);
                        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right);
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Log.d("MovieDetailsActivity", "Aucune reponse reçue de l'API pour updateButton, utilisation de SQLite");
                        Movie modifiedMovie = new Movie(id,titleText.getText().toString(),year,dateText.getText().toString(),genreText.getText().toString(),imageRes,synopsisText.getText().toString());
                        ContentValues values= new ContentValues();

                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //faire le delete ici
                Call<Movie> call = movieCrudService.deleteMovieById(movie.getId());
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Intent returnIntent = new Intent(MovieDetailsActivity.this,MainActivity.class);
                        finish();
                        startActivity(returnIntent);
                        overridePendingTransition(R.animator.slide_from_left, R.animator.slide_to_right);
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Log.d("MovieDetailsActivity","Aucune reponse reçue de l'API pour deleteButton");
                    }
                });
            }
        });
    }
}
