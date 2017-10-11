package com.example.android.movieapp.Network;


import com.example.android.movieapp.Models.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ReviewsResponse {


    @SerializedName("results")
    private List<Review> mResults;

    public List<Review> getResults() {
        return mResults;
    }
}

