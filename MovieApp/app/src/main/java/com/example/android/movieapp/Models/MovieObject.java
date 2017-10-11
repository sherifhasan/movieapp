package com.example.android.movieapp.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MovieObject implements Serializable {
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("id")
    private String movie_id;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private String vote_average;

    public MovieObject() {
    }

    public MovieObject(String movie_id, String poster_path, String title, String vote_average, String release_date, String overview) {
        this.poster_path = poster_path;
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.overview = overview;
        this.movie_id = movie_id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }
}