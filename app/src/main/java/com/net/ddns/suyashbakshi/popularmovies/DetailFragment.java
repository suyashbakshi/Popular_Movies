package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){

            String mDetails = intent.getStringExtra(Intent.EXTRA_TEXT);

            String split[] = mDetails.split("/");

            ImageView moviePoster = (ImageView)rootView.findViewById(R.id.detail_imageview);
            TextView movieTitle = (TextView)rootView.findViewById(R.id.detail_movie_title_textview);
            TextView description = (TextView)rootView.findViewById(R.id.detail_description_textview);
            TextView releasedate = (TextView)rootView.findViewById(R.id.detail_date_textview);
            TextView rating = (TextView)rootView.findViewById(R.id.detail_rating_textview);

            String imageURL = split[3];
            String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w342/";
            String posterURI = BASE_POSTER_URI + imageURL;
            Log.v("POSTER URI :", posterURI);
            Picasso.with(getActivity()).load(posterURI).into(moviePoster);

            movieTitle.setText(split[0]);
            releasedate.setText(getFriendlyDateformat(split[1]));
            rating.setText(split[2]+" /10");
            description.setText(split[4]);
        }
        return rootView;
    }

    private String getFriendlyDateformat(String s) {
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = currentDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return newDateFormat.format(date);
    }
}
