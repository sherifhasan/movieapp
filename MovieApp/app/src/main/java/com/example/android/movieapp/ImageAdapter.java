
package com.example.android.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movieapp.Models.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sherif on 3/25/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    public List<MovieObject> imgitems;

    public ImageAdapter(Context context, List<MovieObject> imgitems) {
        this.context = context;
        this.imgitems = imgitems;
    }

    @Override
    public int getCount() {
        return imgitems.size();


    }

    @Override
    public MovieObject getItem(int position) {
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
        Picasso.with(context).load(context.getString(R.string.imagePath) + getItem(position).getPoster_path()).into(holder.imageview);
        return convertView;
    }


}


