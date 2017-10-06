package com.example.android.movieapp.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;

public class Utility {
    public static final String API_KEY = "";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
