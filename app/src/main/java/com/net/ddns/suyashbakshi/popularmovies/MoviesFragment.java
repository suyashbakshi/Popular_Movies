package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIES_LOADER = 0;
    private GridViewAdapter mAdapter;

    private static final String[] MOVIE_COLUMNS = {
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


    public MoviesFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public void onStart() {
//        if (!Utility.isOnline(getContext())) {
//            Snackbar.make(getView(), R.string.no_internet_view, Snackbar.LENGTH_INDEFINITE)
//                    .setCallback(new Snackbar.Callback() {
//                        @Override
//                        public void onDismissed(Snackbar snackbar, int event) {
//                            super.onDismissed(snackbar, event);
//                            if (event == DISMISS_EVENT_MANUAL) {
//                                snackbar.show();
//                            }
//                        }
//                    }).show();
//        }
//        else {
//            updateMoviesList();
////            getLoaderManager().initLoader(MOVIES_LOADER,null,this);
//        }
//        super.onStart();
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v("PROBLEM_OnCreateView","RUN");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        String sortValue = Utility.getPreferredSort(getActivity());


//        TODO: A check will be given here when the favorite table is added. If sortValue is Favorite, we will generate the URI for favorite table
//        TODO: and the cursor from favorite table will be used to initialize the adapter.
//        Uri movieForSort = MoviesContract.MoviesEntry.buildMovieSort(sortValue);
//        Cursor cur = getActivity().getContentResolver().query(movieForSort,null,null,null,null);

        mAdapter = new GridViewAdapter(getActivity(), null, 0);


        GridView gridView = (GridView) rootView.findViewById(R.id.movieGridView);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {

                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                            .setData(MoviesContract.MoviesEntry.buildMovieSortWithId(Utility.getPreferredSort(getActivity()),
                                    cursor.getLong(COL_MOVIE_ID)));
                    startActivity(detailIntent);
                }
            }
        });
        return rootView;
    }


    void onSortChanged(){
        Log.v("PROBLEM_OnSortChanged","RUN");
        updateMoviesList();
        getLoaderManager().restartLoader(MOVIES_LOADER,null,this);
    }

    private void updateMoviesList() {

        String mSortOrder = Utility.getPreferredSort(getActivity());

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getContext(), mAdapter);
        fetchMoviesTask.execute(mSortOrder);
        Log.v("PROBLEM_FetchTaskEnd", "RUN");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


//        TODO: A check will be given here when the favorite table is added. If sortValue is Favorite, we will generate the URI for favorite table
//        TODO: and the cursor from favorite table will be used to initialize the adapter.

        String sortValue = Utility.getPreferredSort(getContext());

        Uri movieForSortUri = MoviesContract.MoviesEntry.buildMovieSort(sortValue);

        return new CursorLoader(getActivity(),
                movieForSortUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
