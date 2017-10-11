package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.Models.Trailer;
import com.example.android.movieapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {
    private List<Trailer> mTrailers;

    Listener mListener;

    public static interface Listener {
        void onClick(Trailer trailer);
    }

    public void setListener(Listener mListner) {
        this.mListener = mListner;
    }

    public void updateAdapter(List<Trailer> trailers) {

        if (trailers != null) {
            this.mTrailers = trailers;
        } else {
            this.mTrailers = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public TrailerAdapter() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutPhotoItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutPhotoItem, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Trailer trailer = mTrailers.get(position);
        holder.trailerTextView.setText(holder.parentView.getContext().getString(R.string.trailer, (position + 1)));
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.parentView.getContext().getString(R.string.youtube_link) + trailer.getTrailerKey()));
                holder.parentView.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTrailers == null ? 0 : mTrailers.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_item_textview)
        TextView trailerTextView;

        @BindView(R.id.trailer_item_parent)
        View parentView;

        MyViewHolder(View convertView) {
            super(convertView);

            ButterKnife.bind(this, convertView);
        }
    }

}