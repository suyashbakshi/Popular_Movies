package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;
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
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 1;

    private Uri mUri;
    static final String DETAIL_URI = "URI";

    private static final String[] DETAIL_COLUMNS = {
            MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
            MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID
    };

    static final int COL_MOVIE_TABLE_ID = 0;
    static final int COL_ORIGINAL_TITLE = 1;
    static final int COL_RELEASE_DATE = 2;
    static final int COL_VOTE_AVERAGE = 3;
    static final int COL_POSTER_PATH = 4;
    static final int COL_OVERVIEW = 5;
    static final int COL_BACKDROP_PATH = 6;
    static final int COL_MOVIE_ID = 7;

    private ImageView backdrop;
    private TextView description;
    private TextView releasedate;
    private TextView rating;
    private CollapsingToolbarLayout collapsingToolbar;
    private String BASE_BACKDROP_URI = "http://image.tmdb.org/t/p/w780/";

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
//        Intent intent = getActivity().getIntent();
//        if (intent!=null){
//
//            String mDetails = intent.getDataString();
//            String split[] = mDetails.split("/");

        backdrop = (ImageView) rootView.findViewById(R.id.backdrop_imageview);
        description = (TextView) rootView.findViewById(R.id.detail_description_textview);
        releasedate = (TextView) rootView.findViewById(R.id.detail_date_textview);
        rating = (TextView) rootView.findViewById(R.id.detail_rating_textview);
        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
//
//            collapsingToolbar.setTitle(split[0].toUpperCase());
////            String imageURL = split[3];
//            String backdropURL = split[5];
//
////            String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w342/";
//            String BASE_BACKDROP_URI = "http://image.tmdb.org/t/p/w780/";
//
////            String posterURI = BASE_POSTER_URI + backdropURL;
//            String backdropURI = BASE_BACKDROP_URI + backdropURL;
//            Log.v("POSTER URI :", backdropURI);
//
//            Picasso.with(getActivity()).load(backdropURI).into(backdrop);
//            releasedate.setText(getFriendlyDateformat(split[1]));
//            rating.setText(split[2]+" /10");
//            description.setText(split[4]);
//        }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        Intent intent = getActivity().getIntent();

//        if (intent == null || intent.getData() == null)
//            return null;


//            return new CursorLoader(
//                    getContext(),
//                    intent.getData(),
//                    DETAIL_COLUMNS,
//                    null,
//                    null,
//                    null
//            );
        if (null != mUri) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null, null, null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            String title = data.getString(COL_ORIGINAL_TITLE);
            String releaseDate = getFriendlyDateformat(data.getString(COL_RELEASE_DATE));
            String backDrop_path = data.getString(COL_BACKDROP_PATH);
            String vote_average = String.valueOf(data.getInt(COL_VOTE_AVERAGE));
            String overview = data.getString(COL_OVERVIEW);

            Log.v("DATE_VALUE", releaseDate);

            collapsingToolbar.setTitle(title);
            releasedate.setText(releaseDate);
            Picasso.with(getContext()).load(BASE_BACKDROP_URI + backDrop_path).into(backdrop);
            rating.setText(vote_average + " / 10");
            description.setText(overview);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    void onSortChanged(String newSort) {
        // replace the uri, since the sorting has changed
        Uri uri = mUri;
        if (null != uri) {
            long id = MoviesContract.MoviesEntry.getIdFromUri(uri);
            Uri updatedUri = MoviesContract.MoviesEntry.buildMovieSortWithId(newSort, id);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }
}
