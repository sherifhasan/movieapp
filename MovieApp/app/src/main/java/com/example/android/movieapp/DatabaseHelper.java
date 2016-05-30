package com.example.android.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MovieDB";

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

        // create fresh books table
        this.onCreate(db);
    }

    // Books table name
    private static final String TABLE_Movies = "movies";

    // Movies Table Columns names
    private static final String KEY_ID = "movie_id";
    private static final String KEY_overview = "overview";
    private static final String KEY_PosterPath = "poster_path";
    private static final String KEY_releaseDate = "release_date";
    private static final String KEY_vote_avg = "vote_avg";


    private static final String[] COLUMNS = {KEY_ID, KEY_overview, KEY_PosterPath, KEY_releaseDate, KEY_vote_avg};

    public void addMovie(Movie_Details movies) {

        //  get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, movies.movie_id); // get title

        values.put(KEY_overview, movies.overview);
        values.put(KEY_PosterPath, movies.poster_path);
        values.put(KEY_releaseDate, movies.release_date);
        values.put(KEY_vote_avg, movies.vote_average);


        // 3. insert
        db.insert(TABLE_Movies, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Movie_Details getMovie(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_Movies, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build movie object
        Movie_Details movie = new Movie_Details();
        movie.setMovie_id(cursor.getString(0));
        movie.setOverview(cursor.getString(1));
        movie.setPoster_path(cursor.getString(2));
        movie.setRelease_date(cursor.getString(3));
        movie.setVote_average(cursor.getString(4));


        // 5. return movie
        return movie;
    }

    public List<Movie_Details> getAllMovies() {
        List<Movie_Details> Movies = new ArrayList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_Movies;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Movie_Details  movie = new Movie_Details();
                movie.setMovie_id(cursor.getString(0));
                movie.setOverview(cursor.getString(1));
                movie.setPoster_path(cursor.getString(2));
                movie.setRelease_date(cursor.getString(3));
                movie.setVote_average(cursor.getString(4));
                Movies.add(movie);
            } while (cursor.moveToNext());
        }


        Log.d("dbSize",Movies.size()+"");
        // return Movies
        return Movies;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}