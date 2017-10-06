package com.example.android.movieapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.movieapp.Adapters.ImageAdapter;
import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.Storege.DatabaseHelper;
import com.example.android.movieapp.utility.PanesHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.android.movieapp.utility.Utility.API_KEY;
import static com.example.android.movieapp.utility.Utility.isNetworkConnected;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static MovieObject[] MoviePoster;
    String choice = "";
    MovieObject movie;
    MovieObject[] items, copy;

    GridView gridView;
    ImageAdapter image_adapter;
    DatabaseHelper db;
    PanesHandler panesHandler;
    int numofseen;

    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.movieGrid);
        db = new DatabaseHelper(getActivity());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final MovieObject movie = image_adapter.getItem(position);
                ((PanesHandler) getActivity()).setSelectedName(movie);
            }


        });

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void favourite() {
        List<MovieObject> list = new ArrayList<>();
        list = db.getAllMovies();

        for (MovieObject movie1 : list) {
            Log.i("Movie", movie1.getRelease_date());
        }
        image_adapter = new ImageAdapter(getActivity(), list);
        gridView.setAdapter(image_adapter);
    }

    private void updateMovie() {

        MovieFetch MovieTask = new MovieFetch();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        choice = prefs.getString(getString(R.string.pref_key), getString(R.string.pref_popular_default));
        if (choice.equals("Favourite")) {
            favourite();
        } else {
            if (isNetworkConnected(getActivity())) {
                MovieTask.execute(choice);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        numofseen++;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("numofseen", numofseen);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            numofseen = savedInstanceState.getInt("numofseen");
        }
    }


    public void setNameListener(PanesHandler nameListener) {
        panesHandler = nameListener;
    }

    public class MovieFetch extends AsyncTask<String, Void, MovieObject[]> {
        private final String LOG_TAG = MovieFetch.class.getSimpleName();


        public MovieFetch() {

        }

        private MovieObject[] MoviesJasonPrase(String moviesPosterStr) throws JSONException {


            JSONObject moviesJson = new JSONObject(moviesPosterStr);
            JSONArray resultsArray = moviesJson.getJSONArray("results");
            items = new MovieObject[resultsArray.length()];

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject movies = resultsArray.getJSONObject(i);
                items[i] = new MovieObject(movies.getString("id"), movies.getString("poster_path"), movies.getString("title"), movies.getString("vote_average"), movies.getString("release_date"), movies.getString("overview"));
            }

            return items;
        }


        @Override
        protected MovieObject[] doInBackground(String... params) {
// These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // add API key
            final String API_param = "api_key";
            final String link = "http://api.themoviedb.org/3/movie/" + choice + "?" + "page" + "?";
// Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {

                Uri buildUri = Uri.parse(link).buildUpon().appendQueryParameter(API_param, API_KEY).build();
                URL url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                movieJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    } catch (final RuntimeException i) {
                        i.printStackTrace();
                    }
                }
            }

            try {
                return MoviesJasonPrase(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } catch (final RuntimeException i) {
                i.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieObject[] result) {
            copy = new MovieObject[result.length];
            if (result != null) {
                for (int i = 0; i < result.length; i++)
                    copy[i] = result[i];
                image_adapter = new ImageAdapter(getActivity(), Arrays.asList(result));
                gridView.setAdapter(image_adapter);
            }
        }
    }
}