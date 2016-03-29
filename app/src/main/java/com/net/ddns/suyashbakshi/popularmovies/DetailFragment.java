package com.net.ddns.suyashbakshi.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.net.ddns.suyashbakshi.popularmovies.Adapters.TrailerViewAdapter;
import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;
import com.net.ddns.suyashbakshi.popularmovies.Network_Services.FetchTrailerTask;
import com.net.ddns.suyashbakshi.popularmovies.Utility.Utility;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_FAVORITED,
            MoviesContract.MoviesEntry.COLUMN_SORT_KEY
    };

    static final int COL_MOVIE_TABLE_ID = 0;
    static final int COL_ORIGINAL_TITLE = 1;
    static final int COL_RELEASE_DATE = 2;
    static final int COL_VOTE_AVERAGE = 3;
    static final int COL_POSTER_PATH = 4;
    static final int COL_OVERVIEW = 5;
    static final int COL_BACKDROP_PATH = 6;
    static final int COL_MOVIE_ID = 7;
    static final int COL_FAVORITED = 8;
    static final int COL_SORT_KEY = 9;

    private ImageView backdrop;
    private TextView description;
    private TextView releasedate;
    private TextView rating;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fav_fab;
    private FloatingActionButton trailerPlay;
    private ListView trailerView;
    private String BASE_BACKDROP_URI = "http://image.tmdb.org/t/p/w780/";

    private TrailerViewAdapter trailerViewAdapter;

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
        fav_fab = (FloatingActionButton)rootView.findViewById(R.id.fav_action_button);
        trailerPlay = (FloatingActionButton)rootView.findViewById(R.id.trailer_play);
        trailerView = (ListView)rootView.findViewById(R.id.trailer_list_view);

        trailerViewAdapter = new TrailerViewAdapter(getContext(),new ArrayList<String>());

        trailerView.setAdapter(trailerViewAdapter);



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

            final String title = data.getString(COL_ORIGINAL_TITLE);
            final String releaseDate = data.getString(COL_RELEASE_DATE);
            final String backDrop_path = data.getString(COL_BACKDROP_PATH);
            final String poster_path = data.getString(COL_POSTER_PATH);
            final String vote_average = String.valueOf(data.getInt(COL_VOTE_AVERAGE));
            final String overview = data.getString(COL_OVERVIEW);
            final String favorited = data.getString(COL_FAVORITED);
            final String movie_id = data.getString(COL_MOVIE_ID);
            final String sort_id = data.getString(COL_SORT_KEY);

            Log.v("DATE_VALUE", releaseDate);

            collapsingToolbar.setTitle(title);
            releasedate.setText(getFriendlyDateformat(releaseDate));
            Picasso.with(getContext()).load(BASE_BACKDROP_URI + backDrop_path).into(backdrop);
            rating.setText(vote_average + " / 10");
            description.setText(overview);

            if(favorited.equals("1"))
                fav_fab.setImageResource(R.mipmap.enabled_fav);
            else if(favorited.equals("0"))
                fav_fab.setImageResource(R.mipmap.disabled_fav);


            fav_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri movieUri = MoviesContract.MoviesEntry.CONTENT_URI;
                    Long final_favorited = Long.parseLong(favorited);

                    //These two separate selections are created because in case ONE movie exists in various categories, then we add the movie row that
                    //belongs to current sorting value. But if someone wants to do something fishy, he'll add that one movie from both categories, which
                    //will cause duplication in favorite settings. For this, we'll create combination of movie_id and favorited column as UNIQUE. Also,
                    //while removing movie from favorite category, we'll overwrite favorite column of all occurrences of that movie in table.
                    String favMovieAddSelection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ? AND " + MoviesContract.MoviesEntry.COLUMN_SORT_KEY + " = ?";
                    String favMovieDeleteSelection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?";

                       if (final_favorited == 1) {

                           getActivity().getContentResolver().update(movieUri,
                                   createFavValues(movie_id, overview, releaseDate, title, vote_average, poster_path, backDrop_path, favorited),
                                   favMovieDeleteSelection,
                                   new String[]{movie_id});
                           fav_fab.setImageResource(R.mipmap.disabled_fav);
                           Toast.makeText(getContext(),getString(R.string.remove_fav),Toast.LENGTH_SHORT).show();
                       }
                       else if (final_favorited == 0) {

                           //check if movie already exists as a favorite. I.E. if movie was added as favorite from another category, we must not
                           //make it favorite from the current sorting category, as this would create duplication while showing favorite movies.
                           Cursor checkCursor = getActivity().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                                   null,
                                   favMovieDeleteSelection,
                                   new String[]{movie_id},null);

                           int favColIndex = checkCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_FAVORITED);
                           while (checkCursor.moveToNext()){
                               if( checkCursor.getString(favColIndex).equals("1")){
                                   Toast.makeText(getContext(),getString(R.string.already_fav),Toast.LENGTH_SHORT).show();
                                   return;
                               }
                           }

                           getActivity().getContentResolver().update(movieUri,
                                   createFavValues(movie_id, overview, releaseDate, title, vote_average, poster_path, backDrop_path, favorited),
                                   favMovieAddSelection,
                                   new String[]{movie_id, sort_id});
                           fav_fab.setImageResource(R.mipmap.enabled_fav);
                           Toast.makeText(getContext(), getString(R.string.add_fav), Toast.LENGTH_SHORT).show();
                       }
                   }
            });

            trailerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FetchTrailerTask fetchTrailerTask = new FetchTrailerTask(trailerViewAdapter);
                    fetchTrailerTask.execute(movie_id);
                }
            });

            trailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String itemString[] = trailerViewAdapter.getItem(position).split("/");

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + itemString[0]));
                    startActivity(intent);

                }
            });
        }

    }

    private ContentValues createFavValues(String movie_id, String overview, String releaseDate, String title, String vote_average, String poster_path, String backDrop_path, String favorited) {

        String invertFav = null;

        if(Integer.parseInt(favorited) == 0)
            invertFav = "1";
        else if(Integer.parseInt(favorited) == 1)
            invertFav = "0";

        ContentValues contentValues = new ContentValues();

//        contentValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_KEY,sort_id);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,movie_id);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW,overview);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,releaseDate);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,title);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,vote_average);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,poster_path);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,backDrop_path);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_FAVORITED,invertFav);

        return contentValues;
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    void onSortChanged(String newSort) {
        // replace the uri, since the sorting has changed
        Uri uri = mUri;
        if (null != uri) {
            long id = MoviesContract.MoviesEntry.getIdFromUri(uri);
            Uri updatedUri = MoviesContract.MoviesEntry.buildMovieSortWithMovieId(newSort, id);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }
}
