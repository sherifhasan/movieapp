
package com.example.android.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sherif on 3/25/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    public List<Movie_Details> imgitems;

    public ImageAdapter(Context context, List<Movie_Details> imgitems) {
        this.context = context;
        this.imgitems = imgitems;
    }

    @Override
    public int getCount() {
        return imgitems.size();


    }

    @Override
    public Movie_Details getItem(int position) {
        return imgitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {

        ImageView imageview;
    }

    // convert item = grid item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);

            holder.imageview = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);


        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + getItem(position).poster_path).into(holder.imageview);
        return convertView;
    }


}


