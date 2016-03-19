package com.net.ddns.suyashbakshi.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MovieDbHelper;
import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;
import com.net.ddns.suyashbakshi.popularmovies.Utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by Suyash on 2/21/2016.
 */
public class TestUtilities extends AndroidTestCase {

    static final String TEST_SORT_VALUE = "Popularity";
    static final long TEST_MOVIE_ID = 293660L;


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static ContentValues createMovieValues(long sortRowId) {
        ContentValues cv = new ContentValues();

        cv.put(MoviesContract.MoviesEntry.COLUMN_SORT_KEY,sortRowId);
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,293660);
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW,"Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life.");
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,20160209);
        cv.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,"Deadpool");
        cv.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,7.27);
        cv.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,"inVq3FRqcYIRl2la8iZikYYxFNR.jpg");
        cv.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,"n1y094tVDFATSzkTnFxoGZ1qNsG.jpg");

        return cv;
    }

    static ContentValues createSortValues(){
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.SortEntry.COLUMN_SORT_VALUE,TEST_SORT_VALUE);
        return cv;
    }


    static long insertSortValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createSortValues();

        long SortRowId;
        SortRowId = db.insert(MoviesContract.SortEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Location Values", SortRowId != -1);

        return SortRowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
