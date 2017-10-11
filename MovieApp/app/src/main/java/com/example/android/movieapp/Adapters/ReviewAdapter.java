package com.example.android.movieapp.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.Models.Review;
import com.example.android.movieapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private List<Review> mReviews;

    public void updateAdapter(List<Review> reviews) {

        if (reviews != null) {
            this.mReviews = reviews;
        } else {
            this.mReviews = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public ReviewAdapter() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutPhotoItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutPhotoItem, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Review review = mReviews.get(position);
        holder.reviewAuthorTextView.setText(review.getAuthor());
        holder.reviewContentTextView.setText(review.getContent());
    }


    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author)
        TextView reviewAuthorTextView;

        @BindView(R.id.review_content)
        TextView reviewContentTextView;

        MyViewHolder(View convertView) {
            super(convertView);

            ButterKnife.bind(this, convertView);
        }
    }
}