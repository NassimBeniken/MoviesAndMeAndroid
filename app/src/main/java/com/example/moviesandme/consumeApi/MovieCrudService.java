package com.example.moviesandme.consumeApi;

import com.example.moviesandme.Model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MovieCrudService {

    String ENDPOINT = "https://calm-temple-90298.herokuapp.com/";

    @GET("Movies/")
    Call<List<Movie>> listMovies();

    @GET("Movies/{id}")
    Call<Movie> getMovieById(@Path("id") int id);

    @DELETE("Movies/Delete/{id}")
    Call<Movie> deleteMovieById(@Path("id") int id);

    @PUT("Movies/Update/{id}")
    Call<Movie> updateById(@Path("id") int id, @Body Movie movie);

}
