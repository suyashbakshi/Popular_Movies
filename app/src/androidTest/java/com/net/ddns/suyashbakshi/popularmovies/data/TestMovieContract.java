package com.net.ddns.suyashbakshi.popularmovies.data;

/**
 * Created by Suyash on 2/29/2016.
 */
import android.net.Uri;
import android.test.AndroidTestCase;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;

/*
    Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestMovieContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_WEATHER_LOCATION = "/Popularity";
    private static final long TEST_WEATHER_DATE = 1419033600L;  // December 20th, 2014

    /*
        Students: Uncomment this out to test your weather location function.
     */
    public void testBuildMovieSort() {
        Uri SortUri = MoviesContract.MoviesEntry.buildMovieSort(TEST_WEATHER_LOCATION);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildMovieSort " +
                        "MovieContract.",
                SortUri);
        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                TEST_WEATHER_LOCATION, SortUri.getLastPathSegment());
        assertEquals("Error: Weather location Uri doesn't match our expected result",
                SortUri.toString(),
                "content://com.net.ddns.suyashbakshi.popularmovies/movies/%2FPopularity");
    }
}
