package com.net.ddns.suyashbakshi.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;
import com.net.ddns.suyashbakshi.popularmovies.R;
import com.squareup.picasso.Picasso;

public class GridViewAdapter extends CursorAdapter {
    public GridViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    //List<String> localArrayList;

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        String valueString = getItem(position);
//
//
//
//        if(convertView == null){
//            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.grid_item_movie,parent,false);
//        }
//
//        final ImageView imageView = (ImageView)convertView.findViewById(R.id.grid_item_textView);
////        TextView textView = (TextView)convertView.findViewById(R.id.movie_name_textview);
//
//        String imageURL = null;
//
//        String splitter[] = valueString.split("/");
//        if(splitter.length > 0) {
//            imageURL = splitter[3];
//        }
//        else {
//            imageView.setImageResource(R.mipmap.ic_launcher);
//            return convertView;
//        }
//
//        Log.v("IMAGE_URL",imageURL);
//        String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w342/";
//        String posterURI = BASE_POSTER_URI + imageURL;
//
//
//        Log.v("ADAPTER VALUE STRING", valueString);
//
//        Picasso.with(getContext()).load(posterURI).into(imageView);
////        textView.setText(splitter[0]);
//
//        return convertView;
//    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        int index_title = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE);
        int index_release_date = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE);
        int index_vote_average = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE);
        int index_poster_path = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH);
        int index_overview = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW);
        int index_backdrop_path = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH);
        int index_movie_id = cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID);

        Log.v("INT_CONST", String.valueOf(index_title) + " " +
                String.valueOf(index_release_date) + " " +
                String.valueOf(index_vote_average) + " " +
                String.valueOf(index_poster_path) + " " +
                String.valueOf(index_overview) + " " +
                String.valueOf(index_backdrop_path) + " " +
                String.valueOf(index_movie_id));

        return cursor.getString(index_title) + "/" +
                cursor.getString(index_release_date) + "/" +
                cursor.getString(index_vote_average) +
                cursor.getString(index_poster_path) + "/" +
                cursor.getString(index_overview) +
                cursor.getString(index_backdrop_path) + "/" +
                cursor.getString(index_movie_id);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movie, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String imageURL = null;

        String splitter[] = convertCursorRowToUXFormat(cursor).split("/");
        if (splitter.length > 0)
            imageURL = splitter[3];

        Log.v("IMAGE_URL", imageURL);
        String BASE_POSTER_URI = "http://image.tmdb.org/t/p/w342/";
        String posterURI = BASE_POSTER_URI + imageURL;


        Log.v("ADAPTER_VALUE_STRING", splitter[0] + " : " + splitter[1] + " : " + splitter[2] + " : " + splitter[3] + " : " + splitter[4] + " : " + splitter[5] + " : " + splitter[6]);

        Picasso.with(context).load(posterURI).into(viewHolder.imageView);

    }

    public static class ViewHolder {

        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.grid_item_textView);
        }
    }
}

