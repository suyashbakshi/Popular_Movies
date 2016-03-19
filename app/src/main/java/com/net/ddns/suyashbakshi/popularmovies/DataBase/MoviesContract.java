package com.net.ddns.suyashbakshi.popularmovies.DataBase;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Suyash on 2/18/2016.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.net.ddns.suyashbakshi.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final String PATH_SORT = "sort";


    public static class MoviesEntry implements BaseColumns{

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_SORT_KEY = "sort_id";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =   ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =  ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static Uri buildMoviesUri(long id) {

            return ContentUris.withAppendedId(CONTENT_URI,id);

        }

//          this function is used to get the Uri for the main gridview screen which returns a DIR from the database.
//        on main screen, the movies are required on the basis of sort criteria defined by the user. So, we get URI
//        for getting movie with sort criteria.

        public static Uri buildMovieSort(String sortValue){

            return CONTENT_URI.buildUpon().appendPath(sortValue).build();
        }

//        this function is used to get the Uri for the details view which returns information of a single movie
//        based upon the MOVIE ID attribute. The parameter "Id" corresponds to the COLUMN_MOVIE_ID in the database.
//        On details view, we need to know the movie id of the movie selected to perform operations like retrieving the
//        data such as poster, overview, release date, trailers and review links of the corresponding movie. So we generate
//        the URI for movie with sort criteria and its movie_id.

        public static Uri buildMovieSortWithId(String sortValue, long Id){
            return CONTENT_URI.buildUpon().appendPath(sortValue)
                    .appendPath(Long.toString(Id)).build();
        }

        public static String getSortFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static long getIdFromUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(2));
        }


    }

    public static class SortEntry implements BaseColumns{

        public static final String TABLE_NAME = "sort";

        public static final String COLUMN_SORT_VALUE = "sort_value";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SORT).build();

        public static final String CONTENT_TYPE =   ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SORT;

        public static final String CONTENT_ITEM_TYPE =  ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SORT;


        public static Uri buildSortUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
