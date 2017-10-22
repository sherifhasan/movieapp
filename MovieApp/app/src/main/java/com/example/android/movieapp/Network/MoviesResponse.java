package com.example.android.movieapp.Network;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.movieapp.Models.MovieObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse implements Parcelable{

    @SerializedName("results")
    private List<MovieObject> mResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    protected MoviesResponse(Parcel in) {
        mResults = in.createTypedArrayList(MovieObject.CREATOR);
        mTotalPages = in.readInt();
    }

    public static final Creator<MoviesResponse> CREATOR = new Creator<MoviesResponse>() {
        @Override
        public MoviesResponse createFromParcel(Parcel in) {
            return new MoviesResponse(in);
        }

        @Override
        public MoviesResponse[] newArray(int size) {
            return new MoviesResponse[size];
        }
    };

    public List<MovieObject> getResults() {
        return mResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mResults);
        dest.writeInt(mTotalPages);
    }
}
