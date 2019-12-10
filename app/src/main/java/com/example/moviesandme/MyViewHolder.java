package com.example.moviesandme;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesandme.Model.Movie;
import com.squareup.picasso.Picasso;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView textView;
    private ImageView imageView;
    private MyAdapter.OnMovieListener onMovieListener;

    public MyViewHolder(@NonNull View itemView, MyAdapter.OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        textView = itemView.findViewById(R.id.text);
        imageView = itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(this);
    }

    public void bind(Movie movie) {
        textView.setText(movie.getTitle());
        Picasso.get().load(movie.getImage()).centerCrop().fit().into(imageView);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
