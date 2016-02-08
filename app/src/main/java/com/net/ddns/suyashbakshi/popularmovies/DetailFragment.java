package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
            String imageURL = intent.getStringExtra(Intent.EXTRA_TEXT);
            ImageView moviePoster = (ImageView)rootView.findViewById(R.id.detail_imageview);

            String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w780/";
            String posterURI = BASE_POSTER_URI + imageURL;
            Log.v("POSTER URI :",posterURI);

            Picasso.with(getActivity()).load(posterURI).into(moviePoster);
        }
        return rootView;
    }
}
