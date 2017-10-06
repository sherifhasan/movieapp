package com.example.android.movieapp.Storege;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.movieapp.Models.MovieObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.movieapp.Storege.DatabaseContract.DATABASE_NAME;
import static com.example.android.movieapp.Storege.DatabaseContract.DATABASE_VERSION;
import static com.example.android.movieapp.Storege.DatabaseContract.KEY_ID;
import static com.example.android.movieapp.Storege.DatabaseContract.KEY_PosterPath;
import static com.example.android.movieapp.Storege.DatabaseContract.KEY_overview;
import static com.example.android.movieapp.Storege.DatabaseContract.KEY_releaseDate;
import static com.example.android.movieapp.Storege.DatabaseContract.KEY_vote_avg;
import static com.example.android.movieapp.Storege.DatabaseContract.TABLE_Movies;

public class DatabaseHelper extends SQLiteOpenHelper {



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_Movie_TABLE = "CREATE TABLE Movies ( " +
                "movie_id INTEGER PRIMARY KEY , " +
                "overview TEXT, " +
                "poster_path TEXT, " + "vote_avg TEXT," + "release_date TEXT  )";


        db.execSQL(CREATE_Movie_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS movies");

        // create fresh Movies table
        this.onCreate(db);
    }




    private static final String[] COLUMNS = {KEY_ID, KEY_overview, KEY_PosterPath, KEY_releaseDate, KEY_vote_avg};

    public void addMovie(MovieObject movies) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movies.getMovie_id());

        values.put(KEY_overview, movies.getOverview());
        values.put(KEY_PosterPath, movies.getPoster_path());
        values.put(KEY_releaseDate, movies.getRelease_date());
        values.put(KEY_vote_avg, movies.getVote_average());

        db.insert(TABLE_Movies, null, values);
        db.close();
    }

    public MovieObject getMovie(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_Movies, COLUMNS, " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        MovieObject movie = new MovieObject();
        movie.setMovie_id(cursor.getString(0));
        movie.setOverview(cursor.getString(1));
        movie.setPoster_path(cursor.getString(2));
        movie.setRelease_date(cursor.getString(3));
        movie.setVote_average(cursor.getString(4));

        return movie;
    }

    public List<MovieObject> getAllMovies() {
        List<MovieObject> Movies = new ArrayList<>();


        String query = "SELECT  * FROM " + TABLE_Movies;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                MovieObject movie = new MovieObject();
                movie.setMovie_id(cursor.getString(0));
                movie.setOverview(cursor.getString(1));
                movie.setPoster_path(cursor.getString(2));
                movie.setRelease_date(cursor.getString(3));
                movie.setVote_average(cursor.getString(4));
                Movies.add(movie);
            } while (cursor.moveToNext());
        }


        Log.d("dbSize", Movies.size() + "");

        return Movies;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}