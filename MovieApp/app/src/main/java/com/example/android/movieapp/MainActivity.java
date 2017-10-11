package com.example.android.movieapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.utility.PanesHandler;

import static com.example.android.movieapp.utility.Utility.isNetworkConnected;

public class MainActivity extends AppCompatActivity implements PanesHandler {
    boolean twopane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkConnected(getApplicationContext())) {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet connection")
                    .setMessage("No Internet connection please connect to internet")
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FrameLayout flPanel2 = (FrameLayout) findViewById(R.id.detail_container);


        twopane = findViewById(R.id.detail_container) != null;
        MainActivityFragment fragment =
                (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, com.example.android.movieapp.SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setSelectedName(MovieObject movie) {
        if (twopane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putSerializable("value", movie);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment, DetailFragment.class.getSimpleName())
                    .commit();
        } else {

            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("value", movie);
            startActivity(intent);
        }
    }
}