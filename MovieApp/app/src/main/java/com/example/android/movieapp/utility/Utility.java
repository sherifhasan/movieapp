package com.example.android.movieapp.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.Storege.DatabaseContract;
import com.example.android.movieapp.Storege.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.movieapp.Storege.DatabaseContract.MoviesEntry.TABLE_Movies;

public class Utility {
    public static final String API_KEY = "45675498292e08e0720e1091fa4fb757";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342";


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean IsMovieInDatabase(Context context, MovieObject movie) {
        Cursor cursor = context.getContentResolver().query(
                DatabaseContract.MoviesEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String movieId = cursor.getString(
                        cursor.getColumnIndex(DatabaseContract.MoviesEntry.COLUMN_MOVIE_ID));
                if (movieId.equals(movie.getMovie_id())) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    public static void addMovieToDatabase(Context context, MovieObject movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getMovie_id());
        contentValues.put(DatabaseContract.MoviesEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(DatabaseContract.MoviesEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(DatabaseContract.MoviesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());
        contentValues.put(DatabaseContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(DatabaseContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_average());
        context.getContentResolver().insert(DatabaseContract.MoviesEntry.CONTENT_URI, contentValues);
    }

    public static void deleteMovieFromDatabase(Context context, MovieObject movie) {
        String selection = DatabaseContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] args = {String.valueOf(movie.getMovie_id())};
        context.getContentResolver().delete(DatabaseContract.MoviesEntry.CONTENT_URI, selection, args);
    }

    public static List<MovieObject> getAllMoviesFromDatabase(Context context) {
        List<MovieObject> Movies = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        String query = "SELECT  * FROM " + TABLE_Movies;


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                MovieObject movie = new MovieObject();
                movie.setMovie_id(cursor.getString(0));
                movie.setTitle(cursor.getString(1));
                movie.setOverview(cursor.getString(2));
                movie.setPoster_path(cursor.getString(3));
                movie.setRelease_date(cursor.getString(4));
                movie.setVote_average(cursor.getString(5));
                Movies.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("dbSize", Movies.size() + "");

        return Movies;
    }
}