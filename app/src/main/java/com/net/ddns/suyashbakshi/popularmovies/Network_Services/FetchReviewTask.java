package com.net.ddns.suyashbakshi.popularmovies.Network_Services;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.net.ddns.suyashbakshi.popularmovies.Adapters.ReviewViewAdapter;
import com.net.ddns.suyashbakshi.popularmovies.Adapters.TrailerViewAdapter;

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

/**
 * Created by Suyash on 3/29/2016.
 */
public class FetchReviewTask extends AsyncTask<String, Void, String[]> {

    private ReviewViewAdapter mReviewAdapter;
    private Context mContext;

    public FetchReviewTask(ReviewViewAdapter reviewAdapter, Context context) {

        mReviewAdapter = reviewAdapter;
        mContext = context;
    }

    @Override
    protected String[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJsonString = null;

        try {

            String BASE_TRAILER_URI = "https://api.themoviedb.org/3/";
            String API_KEY_PARAM = "api_key";
            String MOVIE_PATH = "movie";
            String MOVIE_ID_PATH = params[0];
            String REVIEW_PATH = "reviews";

            Uri trailerUri = Uri.parse(BASE_TRAILER_URI)
                    .buildUpon()
                    .appendPath(MOVIE_PATH)
                    .appendPath(MOVIE_ID_PATH)
                    .appendPath(REVIEW_PATH)
                    .appendQueryParameter(API_KEY_PARAM, "28b3bcbe51accd00a86cceaf70a0c2f0")
                    .build();

            URL builtUrl = new URL(trailerUri.toString());

            urlConnection = (HttpURLConnection) builtUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                return null;
            }

            reviewJsonString = buffer.toString();

        } catch (MalformedURLException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            return getReviewDataFromJson(reviewJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String[] getReviewDataFromJson(String trailerJsonString) throws JSONException {

        JSONObject trailerJson = new JSONObject(trailerJsonString);
        JSONArray resultsArray = trailerJson.getJSONArray("results");

        String noOfResults = trailerJson.getString("total_results");

        if(noOfResults.equalsIgnoreCase("0")){

            return null;
        }

        String results[] = new String[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {


            JSONObject atrailer = resultsArray.getJSONObject(i);
            String author = atrailer.getString("author");
            String content = atrailer.getString("content");

            results[i] = author + "/" + content;
            Log.v("REVIEW_RESULT", results[i]);
        }
        return results;
    }

    @Override
    protected void onPostExecute(String result[]) {

        super.onPostExecute(result);

        if (result != null) {
            mReviewAdapter.clear();
            for (String s : result) {
                mReviewAdapter.add(s);
                Log.v("REVIEW_ADAPTER_ITEM",s);
            }
            mReviewAdapter.notifyDataSetChanged();
            Toast.makeText(mContext,"Reviews Shown",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext,"No Reviews Available Currently...",Toast.LENGTH_SHORT).show();
        }

    }
}