package com.example.android.movieapp.Network.retrofit;


import com.example.android.movieapp.Network.MoviesResponse;
import com.example.android.movieapp.Network.ReviewsResponse;
import com.example.android.movieapp.Network.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getTrailers(@Path("id") String id, @Query("api_key") String apiKey);

}
