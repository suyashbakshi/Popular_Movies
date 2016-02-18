package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

//    private ArrayAdapter mAdapter;

    private GridViewAdapter mAdapter;

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

    @Override
    public void onStart() {
        if(isOnline()) {
            updateMoviesList();
        }
        else{
            Snackbar.make(getView(), R.string.no_internet_view, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(isOnline()) {
                               updateMoviesList();
                           }
                        }
                    }).show();
        }
        super.onStart();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


//        String[] movieArray = {"Inception", "Wolf Of Wall Street", "Interstellar", "Antman"};
//        List<String> movieList = new ArrayList<String>(Arrays.asList(movieArray));

//        mAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.grid_item_movie,
//                R.id.grid_item_textView,
//                new ArrayList<String>());

        mAdapter = new GridViewAdapter(getActivity(),new ArrayList<String>());


        GridView gridView = (GridView) rootView.findViewById(R.id.movieGridView);
        gridView.setAdapter(mAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mdetails = mAdapter.getItem(position).toString();
                Log.v("Details Print ", mdetails);
//                String[] splitted = mdetails.split("/");
//                String imageURL = splitted[3];
//                Log.v("URL ", imageURL);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, mdetails);
                startActivity(detailIntent);
            }
        });

        return rootView;
    }


    private void updateMoviesList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mSortOrder = sharedPreferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popular_param));
        Log.v("Current Sort Order :",mSortOrder);

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(mSortOrder);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        ProgressBar pb = (ProgressBar)getView().findViewById(R.id.movie_progress_bar);


        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }


        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString = null;
            int numOfMovies = 20;

            //String sortingOrder = "popularity.desc";
            String apikey = "28b3bcbe51accd00a86cceaf70a0c2f0";

            try{
                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
                final String API_PARAM = "api_key";
                final String SORT_PARAM = "sort_by";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(API_PARAM, apikey)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("BUILT URI : ",builtUri.toString());

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if(inputStream == null)
                    movieJsonString = null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0){
                    return null;
                }

                movieJsonString = stringBuffer.toString();
                Log.v("JSON STRING : ",movieJsonString);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                movieJsonString = null;
                e.printStackTrace();
            }
            finally {
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
                return getMovieDataFromJson(movieJsonString,numOfMovies);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }



        private String[] getMovieDataFromJson(String movieJsonString, int numOfMovies) throws JSONException{

            final String RESULTS = "results";
            final String ORIGINAL_TITLE = "original_title";
            final String RELEASE_DATE = "release_date";
            final String VOTE_AVERAGE = "vote_average";
            final String IMAGE_URI = "poster_path";
            final String OVERVIEW = "overview";

            JSONObject movieJson = new JSONObject(movieJsonString);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);

            String[] results = new String[numOfMovies];
            for (int i = 0; i< movieArray.length(); i++){
                String title;
                String release_date;
                String vote_average;
                String image_url;
                String overview;

                JSONObject aMovie = movieArray.getJSONObject(i);

                title = aMovie.getString(ORIGINAL_TITLE);
                release_date = aMovie.getString(RELEASE_DATE);
                vote_average = aMovie.getString(VOTE_AVERAGE);
                image_url = aMovie.getString(IMAGE_URI);
                overview = aMovie.getString(OVERVIEW);

                String movieString = title + "/" + release_date + "/" + vote_average + "" + image_url + "/" + overview;

                results[i] = movieString;
            }

            for(String s: results){
                Log.v("RESULTS : ",s);
            }
            return results;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if (result!=null){
                mAdapter.clear();
                for (String movies : result){
                    mAdapter.add(movies);
                }
                pb.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();

            }

        }
    }
}
