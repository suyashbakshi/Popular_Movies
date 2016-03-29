package com.net.ddns.suyashbakshi.popularmovies.Network_Services;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.net.ddns.suyashbakshi.popularmovies.Adapters.TrailerViewAdapter;
import com.net.ddns.suyashbakshi.popularmovies.BuildConfig;
import com.net.ddns.suyashbakshi.popularmovies.Utility.Utility;

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
 * Created by Suyash on 3/27/2016.
 */
public class FetchTrailerTask extends AsyncTask<String, Void, String[]> {

    private TrailerViewAdapter mTrailerAdapter;

    public FetchTrailerTask(TrailerViewAdapter trailerAdapter) {

        mTrailerAdapter = trailerAdapter;
    }

    @Override
    protected String[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailerJsonString = null;
        ;

        try {

            String BASE_TRAILER_URI = "https://api.themoviedb.org/3/";
            String API_KEY_PARAM = "api_key";
            String MOVIE_PATH = "movie";
            String MOVIE_ID_PATH = params[0];
            String TRAILER_PATH = "trailers";

            Uri trailerUri = Uri.parse(BASE_TRAILER_URI)
                    .buildUpon()
                    .appendPath(MOVIE_PATH)
                    .appendPath(MOVIE_ID_PATH)
                    .appendPath(TRAILER_PATH)
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

            trailerJsonString = buffer.toString();

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
            return getTrailerDataFromJson(trailerJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String[] getTrailerDataFromJson(String trailerJsonString) throws JSONException {

        JSONObject trailerJson = new JSONObject(trailerJsonString);
        JSONArray resultsArray = trailerJson.getJSONArray("youtube");

        String results[] = new String[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {


            JSONObject atrailer = resultsArray.getJSONObject(i);
            String key = atrailer.getString("source");
            String name = atrailer.getString("name");

            results[i] = key + "/" + name;
            Log.v("TRAILER_RESULT", results[i]);
        }
        return results;
    }

    @Override
    protected void onPostExecute(String result[]) {

//        GetTrailerJsonValue results = new GetTrailerJsonValue();
//        Gson gson = new Gson();
//
//        results = gson.fromJson(result,GetTrailerJsonValue.class);
//
//        trailerKey = results.key;
        super.onPostExecute(result);

        if (result != null) {
            mTrailerAdapter.clear();
            for (String s : result) {
                mTrailerAdapter.add(s);
                Log.v("ADAPTER_ITEM",s);
            }
            mTrailerAdapter.notifyDataSetChanged();
        }

    }
}

//class GetTrailerJsonValue{
//
//    public String key;
//
//    GetTrailerJsonValue(){}
//}
