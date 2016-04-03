package com.net.ddns.suyashbakshi.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MovieProvider;
import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;

/**
 * Created by Suyash on 3/11/2016.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final String SORT_QUERY = "popularity";
    private static final long TEST_ID = 293660L;  // December 20th, 2014
//    private static final long TEST_LOCATION_ID = 10L;

    // content://com.example.android.sunshine.app/weather"
    private static final Uri TEST_MOVIE_DIR = MoviesContract.MoviesEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_SORT_DIR = MoviesContract.MoviesEntry.buildMovieSort(SORT_QUERY);
    private static final Uri TEST_MOVIE_WITH_SORT_AND_ID_DIR = MoviesContract.MoviesEntry.buildMovieSortWithMovieId(SORT_QUERY, TEST_ID);
    // content://com.example.android.sunshine.app/location"
    private static final Uri TEST_SORT_DIR = MoviesContract.SortEntry.CONTENT_URI;

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
        assertEquals("Error: The MOVIE WITH SORT URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_SORT_DIR), MovieProvider.MOVIE_WITH_SORT);
        assertEquals("Error: The MOVIE WITH SORT AND ID URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_SORT_AND_ID_DIR), MovieProvider.MOVIE_WITH_SORT_AND_ID);
        assertEquals("Error: The SORT URI was matched incorrectly.",
                testMatcher.match(TEST_SORT_DIR), MovieProvider.SORT);
    }
}
