package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.movieapp.utility.Utility.BASE_POSTER_URL;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    Listener mListener;
    private List<MovieObject> mMovies;

    public MovieAdapter() {

    }

    public void setListener(Listener mListner) {
        this.mListener = mListner;
    }

    public void updateAdapter(List<MovieObject> movies) {

        if (movies != null) {
            this.mMovies = movies;
        } else {
            this.mMovies = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutPhotoItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutPhotoItem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final MovieObject movie = mMovies.get(position);

        Picasso.with(holder.movieImageView.getContext())
                .load(BASE_POSTER_URL + movie.getPoster_path())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.movieImageView);


        holder.movieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClick(movie);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovies == null ? 0 : mMovies.size();
    }


    public static interface Listener {
        void onClick(MovieObject movie);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        ImageView movieImageView;

        MyViewHolder(View convertView) {
            super(convertView);

            ButterKnife.bind(this, convertView);
        }
    }
}
