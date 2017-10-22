package com.example.android.movieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MovieObject implements Parcelable {
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
    @SerializedName("backdrop_path")
    private String backdrop_path;

    public MovieObject() {
    }

    public MovieObject(String movie_id, String poster_path, String title, String vote_average, String release_date, String overview, String backdrop_path) {
        this.poster_path = poster_path;
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.overview = overview;
        this.movie_id = movie_id;
        this.backdrop_path = backdrop_path;
    }

    protected MovieObject(Parcel in) {
        overview = in.readString();
        release_date = in.readString();
        movie_id = in.readString();
        poster_path = in.readString();
        title = in.readString();
        vote_average = in.readString();
        backdrop_path = in.readString();
    }

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };

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

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(movie_id);
        dest.writeString(poster_path);
        dest.writeString(title);
        dest.writeString(vote_average);
        dest.writeString(backdrop_path);
    }
}