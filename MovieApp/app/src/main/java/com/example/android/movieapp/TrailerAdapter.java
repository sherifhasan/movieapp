package com.example.android.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;


public class TrailerAdapter extends ArrayAdapter<String> {

    public TrailerAdapter(final Context context, final String[] trailers) {
        super(context, 0, Arrays.asList(trailers));
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.trailer_list_item, parent, false);
        }

        final TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_textview);

        trailerText.setText("Trailer " + (position + 1));

        return convertView;
    }
}



