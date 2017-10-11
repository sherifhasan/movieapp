package com.example.android.movieapp.Network;

import com.example.android.movieapp.Models.MovieObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

    @SerializedName("results")
    private List<MovieObject> mResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    public List<MovieObject> getResults() {
        return mResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }
}
