package com.example.android.movieapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.movieapp.Adapters.ReviewAdapter;
import com.example.android.movieapp.Adapters.TrailerAdapter;
import com.example.android.movieapp.Models.MovieObject;
import com.example.android.movieapp.Models.Review;
import com.example.android.movieapp.Models.Trailer;
import com.example.android.movieapp.Network.ReviewsResponse;
import com.example.android.movieapp.Network.TrailersResponse;
import com.example.android.movieapp.Network.retrofit.ApiClient;
import com.example.android.movieapp.Network.retrofit.ApiInterface;
import com.example.android.movieapp.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.movieapp.utility.Utility.API_KEY;
import static com.example.android.movieapp.utility.Utility.BASE_POSTER_URL;
import static com.example.android.movieapp.utility.Utility.IsMovieInDatabase;
import static com.example.android.movieapp.utility.Utility.isNetworkConnected;


public class DetailFragment extends Fragment {

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    TrailerAdapter mTrailerAdapter;
    List<Review> mReviews;
    List<Trailer> mTrailers;
    MovieObject mMovie;
    @BindView(R.id.trailer_recycler_view)
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.review_Recycler_view)
    RecyclerView mReviewsRecyclerView;
    ReviewAdapter mReviewAdapter;
    @BindView(R.id.scroller)
    ScrollView scrollView;
    int mCurrentPostion;
    RecyclerView.LayoutManager mLayoutManagerTrailers;
    RecyclerView.LayoutManager mLayoutManagerReviews;
    @BindView(R.id.detail_image)
    ImageView mImageView;
    @BindView(R.id.overview)
    TextView mOverviewTextView;
    @BindView(R.id.relese)
    TextView mReleaseDateTextView;
    @BindView(R.id.vote_avg)
    TextView mVoteAvgTextView;
    @BindView(R.id.fav_btn)
    Button mFavouriteButton;
    @BindView(R.id.unfav_btn)
    Button mUnFavouriteButton;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        mMovie =  getArguments().getParcelable("value");
        if (mMovie != null) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.INVISIBLE);
        }
        mTrailerAdapter = new TrailerAdapter();
        mLayoutManagerTrailers = new LinearLayoutManager(getActivity());
        mTrailersRecyclerView.setLayoutManager(mLayoutManagerTrailers);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
        mTrailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        mTrailerAdapter.setListener(new TrailerAdapter.Listener() {
            @Override
            public void onClick(Trailer trailer) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(R.string.youtube_link + mMovie.getMovie_id()));
                startActivity(intent);
            }
        });
        mReviewAdapter = new ReviewAdapter();
        mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        mReviewsRecyclerView.setLayoutManager(mLayoutManagerReviews);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
        mReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

        mReleaseDateTextView.setText(mMovie.getRelease_date());
        mOverviewTextView.setText(mMovie.getOverview());
        mVoteAvgTextView.setText(mMovie.getVote_average());
        Picasso.with(getActivity()).load(BASE_POSTER_URL + mMovie.getPoster_path()).placeholder(R.drawable.placeholder_image).into(mImageView);

        if (IsMovieInDatabase(getContext(), mMovie)) {
            mUnFavouriteButton.setVisibility(View.VISIBLE);
        } else {
            mFavouriteButton.setVisibility(View.VISIBLE);
        }
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsMovieInDatabase(getContext(), mMovie)) {
                    Utility.addMovieToDatabase(getContext(), mMovie);
                    mFavouriteButton.setVisibility(View.GONE);
                    mUnFavouriteButton.setVisibility(View.VISIBLE);
                }
            }
        });
        mUnFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.deleteMovieFromDatabase(getContext(), mMovie);
                mUnFavouriteButton.setVisibility(View.GONE);
                mFavouriteButton.setVisibility(View.VISIBLE);
            }
        });
        getTrailersFromApi();
        getReviewsFromApi();
        return rootView;
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

    public void getReviewsFromApi() {
        if (isNetworkConnected(getContext())) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ReviewsResponse> call = apiService.getReviews(mMovie.getMovie_id(), API_KEY);
            call.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {

                    if (response != null && response.body() != null && response.isSuccessful()) {
                        mReviews = response.body().getResults();
                        mReviewAdapter.updateAdapter(mReviews);
                    }
                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    Log.d(LOG_TAG, t.toString());
                }
            });
        }
    }

    public void getTrailersFromApi() {
        if (isNetworkConnected(getContext())) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<TrailersResponse> call = apiService.getTrailers(mMovie.getMovie_id(), API_KEY);
            call.enqueue(new Callback<TrailersResponse>() {
                @Override
                public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {

                    if (response != null && response.body() != null && response.isSuccessful()) {
                        mTrailers = response.body().getResults();
                        mTrailerAdapter.updateAdapter(mTrailers);
                    }
                }

                @Override
                public void onFailure(Call<TrailersResponse> call, Throwable t) {
                    Log.d(LOG_TAG, t.toString());
                }
            });
        }
    }
}