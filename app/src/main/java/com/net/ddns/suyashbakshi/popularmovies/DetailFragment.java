package com.net.ddns.suyashbakshi.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
    private static final int FAVORITE_LOADER = 2;

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

    private static final String[] FAV_COLUMNS = {
            MoviesContract.FavoriteEntry.TABLE_NAME + "." + MoviesContract.FavoriteEntry._ID,
            MoviesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE,
            MoviesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.FavoriteEntry.COLUMN_POSTER_PATH,
            MoviesContract.FavoriteEntry.COLUMN_OVERVIEW,
            MoviesContract.FavoriteEntry.COLUMN_BACKDROP_PATH,
            MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID
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
    private FloatingActionButton fab;
    private String BASE_BACKDROP_URI = "http://image.tmdb.org/t/p/w780/";

    String title;
    String releaseDate;
    String backDrop_path;
    String vote_average;
    String overview;
    String movie_id;
    String poster_path;
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
        fab = (FloatingActionButton) rootView.findViewById(R.id.fav_fab);
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
        getLoaderManager().initLoader(FAVORITE_LOADER, null, this);
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
        switch (id) {
            case DETAIL_LOADER:
                if (null != mUri) {
                    Log.v("DETAIL_LOADER", "FRAGMENT");
                    return new CursorLoader(
                            getActivity(),
                            mUri,
                            DETAIL_COLUMNS,
                            null, null, null
                    );
                }
                break;
            case FAVORITE_LOADER:
                if (null != mUri) {
                    Log.v("FAVORITE_LOADER", "FRAGMENT");
                    return new CursorLoader(
                            getActivity(),
                            mUri,
                            FAV_COLUMNS,
                            null,
                            null,
                            null
                    );
                }
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            switch (loader.getId()) {

                case DETAIL_LOADER: {

                    title = data.getString(COL_ORIGINAL_TITLE);
                    releaseDate = getFriendlyDateformat(data.getString(COL_RELEASE_DATE));
                    backDrop_path = data.getString(COL_BACKDROP_PATH);
                    vote_average = String.valueOf(data.getInt(COL_VOTE_AVERAGE));
                    overview = data.getString(COL_OVERVIEW);
                    movie_id = data.getString(COL_MOVIE_ID);
                    poster_path = data.getString(COL_POSTER_PATH);

//            Log.v("DATE_VALUE", releaseDate);

                    collapsingToolbar.setTitle(title);
                    releasedate.setText(releaseDate);
                    Picasso.with(getContext()).load(BASE_BACKDROP_URI + backDrop_path).into(backdrop);
                    rating.setText(vote_average + " / 10");
                    description.setText(overview);
                }

                case FAVORITE_LOADER: {

                    boolean favorited = false;
                    if(data.moveToFirst()){

                        do {
                            if(data.getString(COL_MOVIE_ID).equalsIgnoreCase(movie_id)){
                                favorited = true;
                            }
                        }while (data.moveToNext());
                    }

                    final boolean mFavorited = favorited;

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID, movie_id);
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_OVERVIEW, overview);
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE, releaseDate);
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE, title);
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, vote_average);
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_POSTER_PATH, poster_path);
                            contentValues.put(MoviesContract.FavoriteEntry.COLUMN_BACKDROP_PATH, backDrop_path);

                            if(mFavorited){
                                getActivity().getContentResolver().delete(
                                        MoviesContract.FavoriteEntry.buildFavWithId(Long.parseLong(movie_id)),null,null);
                                Toast.makeText(getContext(),"REMOVED FROM FAV",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                getActivity().getContentResolver().insert(
                                        MoviesContract.FavoriteEntry.CONTENT_URI.buildUpon().build(),
                                        contentValues);
                                Toast.makeText(getContext(),"ADDED TO FAV",Toast.LENGTH_SHORT);
                            }

                        }
                    });
                }

            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    void onSortChanged(String newSort) {
        // replace the uri, since the sorting has changed
        Uri uri = mUri;
        Log.v("mUri", String.valueOf(mUri));
        if (null != uri) {
            long id = MoviesContract.MoviesEntry.getIdFromUri(uri);
            Log.v("ID", String.valueOf(id));
            Uri updatedUri = MoviesContract.MoviesEntry.buildMovieSortWithId(newSort, id);
            mUri = updatedUri;
            Log.v("UPDATED_URI", String.valueOf(updatedUri));
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }
}
