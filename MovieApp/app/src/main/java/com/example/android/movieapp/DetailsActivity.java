package com.example.android.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DetailsActivity extends AppCompatActivity {
    private Movie_Details movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (null == savedInstanceState) {
            Bundle extras = getIntent().getExtras();

            movie = (Movie_Details) getIntent().getSerializableExtra("value");
            Log.i("Movie : ", movie.release_date);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment)
                    .commit();

        }
    }
}
