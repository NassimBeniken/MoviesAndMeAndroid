package com.example.moviesandme;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesandme.Model.Movie;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Movie> movieList;
    private OnMovieListener onMovieListener;

    public MyAdapter(List<Movie> movieList, OnMovieListener onMovieListener) {
        this.movieList = movieList;
        this.onMovieListener = onMovieListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_cards, parent, false);
        MyViewHolder mvh = new MyViewHolder(view, onMovieListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
        //holder.getImageView().setImageURI(Uri.parse(movie.getImage()));
        //holder.getImageView().setImageResource(movie.getImage());
        //holder.getTextView().setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public interface OnMovieListener {
        public void onMovieClick(int position);
    }
}
