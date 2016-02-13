package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<String> {

    //List<String> localArrayList;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String valueString = getItem(position);



        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_item_movie,parent,false);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_textView);
        TextView textView = (TextView)convertView.findViewById(R.id.movie_name_textview);

        String imageURL = null;

        String splitter[] = valueString.split("/");
        if(splitter.length > 0) {
            imageURL = splitter[3];
        }
        else {
            imageView.setImageResource(R.mipmap.ic_launcher);
            return convertView;
        }

        Log.v("IMAGE_URL",imageURL);
        String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w342/";
        String posterURI = BASE_POSTER_URI + imageURL;


        Log.v("ADAPTER VALUE STRING", valueString);

        Picasso.with(getContext()).load(posterURI).into(imageView);
        textView.setText(splitter[0]);

        return convertView;
    }

    public GridViewAdapter(Context context, List<String> movieString) {

        super(context, 0, movieString);
        //localArrayList = movieString;

    }



}

