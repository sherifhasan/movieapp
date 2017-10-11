package com.example.android.movieapp.Storege;


import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {


    public static final String CONTENT_AUTHORITY = "com.example.android.movieapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        // Movies table name
        public static final String TABLE_Movies = "movies";
        // Movies Table Columns names
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_posterPath";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_releaseDate";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_voteAverage";

        public static final String CREATE_TABLE_MOVIES = "CREATE TABLE " +
                MoviesEntry.TABLE_Movies + "(" +
                MoviesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " LONG NOT NULL" +
                ");";
    }
}
