package com.net.ddns.suyashbakshi.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Suyash on 3/13/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

    private Context mContext;
    private GridViewAdapter mAdapter;

    public FetchMoviesTask(Context context, GridViewAdapter moviesAdapter) {
        mContext = context;
        mAdapter = moviesAdapter;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonString = null;
        int numOfMovies = 20;
        String apikey = "28b3bcbe51accd00a86cceaf70a0c2f0";

        try {
            final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String API_PARAM = "api_key";
            final String SORT_PARAM = "sort_by";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(API_PARAM, apikey)
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v("BUILT URI : ", builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null)
                movieJsonString = null;
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0) {
                return null;
            }

            movieJsonString = stringBuffer.toString();
            Log.v("JSON STRING : ", movieJsonString);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            movieJsonString = null;
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.v("Buffered Reader", "Error in Closing");
                    e.printStackTrace();
                }
            }
        }

        try {
            return getMovieDataFromJson(movieJsonString, numOfMovies, params[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String[] getMovieDataFromJson(String movieJsonString, int numOfMovies, String sortValue) throws JSONException {

        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";
        final String IMAGE_URI = "poster_path";
        final String OVERVIEW = "overview";
        final String BACKDROP_PATH = "backdrop_path";
        final String MOVIE_ID = "id";

        JSONObject movieJson = new JSONObject(movieJsonString);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        long sortId = addSortValue(sortValue);

        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

//        String[] results = new String[movieArray.length()];
        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String release_date;
            String vote_average;
            String image_url;
            String overview;
            String backdrop_path;
            String movie_id;

            JSONObject aMovie = movieArray.getJSONObject(i);

            title = aMovie.getString(ORIGINAL_TITLE);
            release_date = aMovie.getString(RELEASE_DATE);
            vote_average = aMovie.getString(VOTE_AVERAGE);
            image_url = aMovie.getString(IMAGE_URI);
            overview = aMovie.getString(OVERVIEW);
            backdrop_path = aMovie.getString(BACKDROP_PATH);
            movie_id = aMovie.getString(MOVIE_ID);

            ContentValues movieValues = new ContentValues();

            movieValues.put(MoviesContract.MoviesEntry.COLUMN_SORT_KEY, sortId);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movie_id);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, title);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, release_date);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, vote_average);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, image_url);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, backdrop_path);

            cVVector.add(movieValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
            Log.v("NO_OF_ROWS_INSERTED", String.valueOf(rowsInserted));
        }


//        The code below retrieves the entries that we just made in the database using the bulkInsert() method
//        to ensure that correct entries have been made. We use the contentResolver QUERY function to retrieve data
//        inside a cursor and then reinitialize the cVVector to hold them and convert it into a String Array.
//        convertContentValuesToUXFormat() is a helper method to get a READABLE STRING that can be passed to the adapter for updating the UI.
//        Uri movieForSortUri = MoviesContract.MoviesEntry.buildMovieSort(sortValue);
//
//        Cursor cur = mContext.getContentResolver().query(movieForSortUri, null, null, null, null);
//
//        cVVector = new Vector<ContentValues>(cur.getCount());
//        if (cur.moveToFirst()) {
//            do {
//                ContentValues cv = new ContentValues();
//                DatabaseUtils.cursorRowToContentValues(cur, cv);
//                cVVector.add(cv);
//            } while (cur.moveToNext());
//        }
//
//        String results[] = convertContentValuesToUXFormat(cVVector);
//
////        results[i] = movieString;
//
////    }
//        for (String s : results) {
//            Log.v("RESULTS : ", s);
//        }
//        return results;
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        mAdapter.notifyDataSetChanged();
    }


    public long addSortValue(String sortValue) {

        long sortId;

        Cursor sortCursor = mContext.getContentResolver().query(MoviesContract.SortEntry.CONTENT_URI,
                new String[]{MoviesContract.SortEntry._ID},
                MoviesContract.SortEntry.COLUMN_SORT_VALUE + " = ? ",
                new String[]{sortValue}, null);

        if (sortCursor.moveToFirst()) {
            int sortColumnIndex = sortCursor.getColumnIndex(MoviesContract.SortEntry._ID);
            sortId = sortCursor.getLong(sortColumnIndex);
//            Log.v("INSIDE_IF","");
        } else {
            ContentValues sortContentValues = new ContentValues();

            sortContentValues.put(MoviesContract.SortEntry.COLUMN_SORT_VALUE, sortValue);

            Uri insertedUri = mContext.getContentResolver().insert(MoviesContract.SortEntry.CONTENT_URI, sortContentValues);

            sortId = ContentUris.parseId(insertedUri);

//            Log.v("INSIDE_ELSE", sortId + " " + sortValue + insertedUri);
        }
        sortCursor.close();
        return sortId;
    }


    String[] convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
        // return strings to keep UI functional for now
        String[] resultStrs = new String[cvv.size()];
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues weatherValues = cvv.elementAt(i);
            resultStrs[i] = weatherValues.getAsString(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE) + "/" +
                    weatherValues.getAsString(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE) + "/" +
                    weatherValues.getAsString(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE) +
                    weatherValues.getAsString(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH) + "/" +
                    weatherValues.getAsString(MoviesContract.MoviesEntry.COLUMN_OVERVIEW) +
                    weatherValues.getAsString(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH);
//            title + "/" + release_date + "/" + vote_average + "" + image_url + "/" + overview + "" + backdrop_path

        }
        return resultStrs;
    }
}
