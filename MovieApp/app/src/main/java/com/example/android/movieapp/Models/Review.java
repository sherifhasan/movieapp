package com.example.android.movieapp.Models;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("content")
    private String mContent;

    @SerializedName("author")
    private String mAuthor;

    public String getContent() {
        return mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
