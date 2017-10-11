package com.example.android.movieapp.Network;


import com.example.android.movieapp.Models.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TrailersResponse {
    @SerializedName("results")
    private List<Trailer> mResults;

    public List<Trailer> getResults() {
        return mResults;
    }
}
