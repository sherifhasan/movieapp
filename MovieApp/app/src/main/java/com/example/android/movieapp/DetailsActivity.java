package com.example.android.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.movieapp.Models.MovieObject;

public class DetailsActivity extends AppCompatActivity {
    private MovieObject movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (null == savedInstanceState) {
            Bundle extras = getIntent().getExtras();

            movie = getIntent().getParcelableExtra("value");
            Log.i("Movie : ", movie.getRelease_date());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment)
                    .commit();

        }
    }
}
