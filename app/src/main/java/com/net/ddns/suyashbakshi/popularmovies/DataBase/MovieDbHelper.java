package com.net.ddns.suyashbakshi.popularmovies.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suyash on 2/18/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "+ MoviesContract.MoviesEntry.TABLE_NAME + " ("+
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.MoviesEntry.COLUMN_SORT_KEY + " INTEGER NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL," +

                " FOREIGN KEY (" + MoviesContract.MoviesEntry.COLUMN_SORT_KEY + ") REFERENCES "+
                MoviesContract.SortEntry.TABLE_NAME + " (" + MoviesContract.SortEntry._ID + "), " +

                "UNIQUE (" + MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + ", "+
                MoviesContract.MoviesEntry.COLUMN_SORT_KEY + "));";


        final String SQL_CREATE_SORT_TABLE = "CREATE TABLE "+ MoviesContract.SortEntry.TABLE_NAME + " ("+
                MoviesContract.SortEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.SortEntry.COLUMN_SORT_VALUE + " TEXT UNIQUE NOT NULL );";

        final String SQL_CREATE_FAV_TABLE = "CREATE TABLE "+ MoviesContract.FavoriteEntry.TABLE_NAME + " (" +
                MoviesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MoviesContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MoviesContract.FavoriteEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL," +
                MoviesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MoviesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL," +
                MoviesContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MoviesContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL );";



        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_SORT_TABLE);
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.SortEntry.TABLE_NAME);
        onCreate(db);
    }
}
