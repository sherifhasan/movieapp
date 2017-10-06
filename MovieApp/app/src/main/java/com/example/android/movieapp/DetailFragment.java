package com.example.android.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.Storege.DatabaseHelper;
import com.squareup.picasso.Picasso;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    public static final String API_KEY = "";
    TrailerAdapter trailerAdapter;
    String copy[];
    MovieObject movie_object;
    String keys[];
    ListView listView1;
    ListView listView2;
    ReviewAdapter reviewAdapter;
    boolean checkMovie = true;
    DatabaseHelper db;
    private ScrollView scrollView;
    int numofseen;

    public DetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        movie_object = (MovieObject) getArguments().getSerializable("value");
        scrollView = (ScrollView) rootView.findViewById(R.id.scroller);

        if (movie_object != null) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.INVISIBLE);
        }

        listView1 = (ListView) rootView.findViewById(R.id.trailer_list_item);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + copy[position]));
                startActivity(intent);

            }

        });

        listView2 = (ListView) rootView.findViewById(R.id.review_list_view);

        ((TextView) rootView.findViewById(R.id.relese))
                .setText(movie_object.getRelease_date());
        ((TextView) rootView.findViewById(R.id.overview))
                .setText(movie_object.getOverview());
        ((TextView) rootView.findViewById(R.id.vote_avg))
                .setText(movie_object.getVote_average());
        ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + movie_object.getPoster_path()).into(imageView);

        db = new DatabaseHelper(getActivity());
        Button button = (Button) rootView.findViewById(R.id.fav_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MovieObject> arrayList = new ArrayList();
                arrayList = db.getAllMovies();
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getMovie_id().equals(movie_object.getMovie_id())) {
                        checkMovie = false;
                    }
                }

                if (checkMovie) {
                    db.addMovie(new MovieObject(movie_object.getMovie_id(), movie_object.getPoster_path(), movie_object.getTitle(), movie_object.getVote_average(), movie_object.getRelease_date(), movie_object.getOverview()));
                    Toast.makeText(getActivity(), "Movie added to Favourite", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Movie already in Favourite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        numofseen++;
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


    private void fetchTrailers() {
        FetchTrailer fetchTrailer = new FetchTrailer();
        fetchTrailer.execute();

    }

    private void fetchReviews() {
        FetchReviews fetchReviews = new FetchReviews();
        fetchReviews.execute();

    }

    public void onStart() {
        super.onStart();
        fetchTrailers();
        fetchReviews();
    }


    public class FetchTrailer extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchTrailer.class.getSimpleName();


        public FetchTrailer() {

        }

        private String[] MoviesJasonPrase(String moviesPosterStr) throws JSONException {


            JSONObject moviesJson = new JSONObject(moviesPosterStr);
            JSONArray resultsArray = moviesJson.getJSONArray("results");
            keys = new String[resultsArray.length()];
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject movies = resultsArray.getJSONObject(i);
                keys[i] = movies.getString("key");
            }

            return keys;
        }


        @Override
        protected String[] doInBackground(String... params) {
// These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            //add api key

            final String API_param = "api_key";
            //  final String link = "http://api.themoviedb.org/3/movie/" + movie_object.movie_id + "?";
// Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            final Uri.Builder builder = new Uri.Builder();
            builder.scheme("https").authority("api.themoviedb.org").appendPath("3")
                    .appendPath("movie").appendPath(movie_object.getMovie_id()).appendPath("videos")
                    .appendQueryParameter(API_param, API_KEY);
            try {

                URL url = new URL(builder.toString());
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
                    }
                }
            }

            try {
                return MoviesJasonPrase(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(null);
            copy = new String[result.length];
            if (result != null) {
                for (int i = 0; i < result.length; i++) {
                    copy[i] = result[i];
                }
                trailerAdapter = new TrailerAdapter(getActivity(), result);
                listView1.setAdapter(trailerAdapter);


            }
        }

    }

    public class FetchReviews extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchTrailer.class.getSimpleName();


        public FetchReviews() {

        }

        private String[] MoviesJasonPrase(String moviesPosterStr) throws JSONException {


            JSONObject moviesJson = new JSONObject(moviesPosterStr);
            JSONArray resultsArray = moviesJson.getJSONArray("results");
            keys = new String[resultsArray.length()];
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject movies = resultsArray.getJSONObject(i);
                keys[i] = movies.getString("content");
            }

            return keys;
        }


        @Override
        protected String[] doInBackground(String... params) {
// These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            final String API_param = "api_key";
            //  final String link = "http://api.themoviedb.org/3/movie/" + movie_object.movie_id + "?";
// Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {

                final Uri.Builder builder = new Uri.Builder();
                builder.scheme("https").authority("api.themoviedb.org").appendPath("3")
                        .appendPath("movie").appendPath(movie_object.getMovie_id()).appendPath("reviews")
                        .appendQueryParameter(API_param, API_KEY);
                URL url = new URL(builder.toString());
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
        protected void onPostExecute(String[] result) {
            super.onPostExecute(null);
            String arr[] = new String[result.length];
            if (result != null) {
                reviewAdapter = new ReviewAdapter(getActivity(), result);
                listView2.setAdapter(reviewAdapter);
            }

        }
    }

}