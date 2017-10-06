package com.example.android.movieapp.Storege;


public class DatabaseContract {

    // Database Version
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "MovieDB";
    // Movies table name
    public static final String TABLE_Movies = "movies";

    // Movies Table Columns names
    public static final String KEY_ID = "movie_id";
    public static final String KEY_overview = "overview";
    public static final String KEY_PosterPath = "poster_path";
    public static final String KEY_releaseDate = "release_date";
    public static final String KEY_vote_avg = "vote_avg";
}
