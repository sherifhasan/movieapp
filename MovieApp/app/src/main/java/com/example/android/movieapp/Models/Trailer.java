package com.example.android.movieapp.Models;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("key")
    private String mTrailerKey;

    public String getTrailerKey() {
        return mTrailerKey;
    }
}
