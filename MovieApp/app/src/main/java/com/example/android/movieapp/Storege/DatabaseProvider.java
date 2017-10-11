package com.example.android.movieapp.Storege;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class DatabaseProvider extends ContentProvider {
    public final static int CODE_MOVIES = 100;
    public final static int CODE_MOVIE_WITH_ID = 101;
    public final static UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_MOVIES, CODE_MOVIES);
        uriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_MOVIES + "/#", CODE_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case CODE_MOVIES:
                cursor = db.query(DatabaseContract.MoviesEntry.TABLE_Movies,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("We are not implementing getType in MovieApp.");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);
        Uri URI;
        switch (matcher) {
            case CODE_MOVIES:
                long id = db.insert(DatabaseContract.MoviesEntry.TABLE_Movies, null, values);
                if (id > 0) {
                    URI = ContentUris.withAppendedId(DatabaseContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    db.close();
                    throw new SQLException("Illegal Insert " + uri);
                }
                break;
            default:
                db.close();
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return URI;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        DatabaseContract.MoviesEntry.TABLE_Movies,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}