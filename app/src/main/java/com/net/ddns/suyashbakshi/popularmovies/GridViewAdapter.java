package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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
            convertView = layoutInflater.inflate(R.layout.grid_item_movie,null);
        }

        String splitter[] = valueString.split("/");
        String imageURL = splitter[1];

        Log.v("IMAGE_URL",imageURL);
        String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w154/";
        String posterURI = BASE_POSTER_URI + imageURL;


        ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_textView);
//        imageView.setImageResource(R.mipmap.ic_launcher);


        Log.v("ADAPTER VALUE STRING", valueString);

        Picasso.with(getContext()).load(posterURI).into(imageView);
        return convertView;
    }

    public GridViewAdapter(Context context, List<String> movieString) {

        super(context, 0, movieString);
        //localArrayList = movieString;

    }



}