package com.example.android.movieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.movieapp.Adapters.MovieAdapter;
import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.Network.MoviesResponse;
import com.example.android.movieapp.Network.retrofit.ApiClient;
import com.example.android.movieapp.Network.retrofit.ApiInterface;
import com.example.android.movieapp.utility.PanesHandler;
import com.example.android.movieapp.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.movieapp.utility.Utility.API_KEY;
import static com.example.android.movieapp.utility.Utility.getSpanCount;
import static com.example.android.movieapp.utility.Utility.isNetworkConnected;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static int SPAN_COUNT;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    String choice = "";
    List<MovieObject> mMovies;
    @BindView(R.id.movieGrid)
    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    PanesHandler panesHandler;
    int mCurrentPostion;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        SPAN_COUNT = getSpanCount(getContext());
        mMovieAdapter = new MovieAdapter();
        mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mMovieAdapter);
        mMovieAdapter.setListener(new MovieAdapter.Listener() {
            @Override
            public void onClick(MovieObject movie) {
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
        List<MovieObject> list = new ArrayList();
        if (Utility.getAllMoviesFromDatabase(getContext()) != null) {
            list = Utility.getAllMoviesFromDatabase(getContext());
            mMovieAdapter.updateAdapter(list);
        }
    }

    private void updateMovie() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        choice = prefs.getString(getString(R.string.pref_key), getString(R.string.pref_popular_default));
        if (choice.equals("Favourite")) {
            favourite();
        } else if (choice.equals("top_rated")) {
            getTopRatedMoviesFromApi();
        } else if (choice.equals("popular")) {
            getPopularMoviesFromApi();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPosition", mCurrentPostion);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurrentPostion = savedInstanceState.getInt("currentPosition", 0);
        }
    }

    public void setNameListener(PanesHandler nameListener) {
        panesHandler = nameListener;
    }

    private void getPopularMoviesFromApi() {
        if (isNetworkConnected(getContext())) {

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    if (response != null && response.body() != null && response.isSuccessful()) {
                        mMovies = response.body().getResults();
                        mMovieAdapter.updateAdapter(mMovies);
                    }
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d(LOG_TAG, t.toString());
                }
            });

        }
    }

    private void getTopRatedMoviesFromApi() {
        if (isNetworkConnected(getContext())) {

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    if (response != null && response.body() != null && response.isSuccessful()) {
                        mMovies = response.body().getResults();
                        mMovieAdapter.updateAdapter(mMovies);
                    }
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d(LOG_TAG, t.toString());
                }
            });

        }
    }
}