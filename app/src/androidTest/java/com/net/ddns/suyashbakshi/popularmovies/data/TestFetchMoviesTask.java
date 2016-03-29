package com.net.ddns.suyashbakshi.popularmovies.data;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.test.AndroidTestCase;


import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;
import com.net.ddns.suyashbakshi.popularmovies.Network_Services.FetchMoviesTask;
import com.net.ddns.suyashbakshi.popularmovies.Adapters.GridViewAdapter;

import java.util.ArrayList;

/**
 * Created by Suyash on 3/19/2016.
 */
public class TestFetchMoviesTask extends AndroidTestCase {
    static final String ADD_SORT_VALUE = "Popularity";

    /*
        Students: uncomment testAddLocation after you have written the AddLocation function.
        This test will only run on API level 11 and higher because of a requirement in the
        content provider.
     */
    @TargetApi(11)
    public void testAddSortValue() {
        // start from a clean state
        getContext().getContentResolver().delete(MoviesContract.SortEntry.CONTENT_URI,
                MoviesContract.SortEntry.COLUMN_SORT_VALUE+ " = ?",
                new String[]{ADD_SORT_VALUE});

        FetchMoviesTask fmt = new FetchMoviesTask(getContext(),new GridViewAdapter(getContext(),new ArrayList<String>()));
        long sortId = fmt.addSortValue(ADD_SORT_VALUE);

        // does addLocation return a valid record ID?
        assertFalse("Error: addSortValue returned an invalid ID on insert",
                sortId == -1);

        // test all this twice
        for ( int i = 0; i < 2; i++ ) {

            // does the ID point to our location?
            Cursor sortCursor = getContext().getContentResolver().query(
                    MoviesContract.SortEntry.CONTENT_URI,
                    new String[]{
                            MoviesContract.SortEntry._ID,
                            MoviesContract.SortEntry.COLUMN_SORT_VALUE
                    },
                    MoviesContract.SortEntry.COLUMN_SORT_VALUE+ " = ?",
                    new String[]{ADD_SORT_VALUE},
                    null);

            // these match the indices of the projection
            if (sortCursor.moveToFirst()) {
                assertEquals("Error: the queried value of sortId does not match the returned value" +
                        "from addSortValue", sortCursor.getLong(0), sortId);
                assertEquals("Error: the queried value of sort value is incorrect",
                        sortCursor.getString(1), ADD_SORT_VALUE);
            } else {
                fail("Error: the id you used to query returned an empty cursor");
            }

            // there should be no more records
            assertFalse("Error: there should be only one record returned from a sort query",
                    sortCursor.moveToNext());

            // add the location again
            long newSortId = fmt.addSortValue(ADD_SORT_VALUE);

            assertEquals("Error: inserting a sort again should return the same ID",
                    sortId, newSortId);
        }
        // reset our state back to normal
        getContext().getContentResolver().delete(MoviesContract.SortEntry.CONTENT_URI,
                MoviesContract.SortEntry.COLUMN_SORT_VALUE+ " = ?",
                new String[]{ADD_SORT_VALUE});

        // clean up the test so that other tests can use the content provider
        getContext().getContentResolver().
                acquireContentProviderClient(MoviesContract.SortEntry.CONTENT_URI).
                getLocalContentProvider().shutdown();
    }
}
