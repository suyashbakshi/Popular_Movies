package com.net.ddns.suyashbakshi.popularmovies.data;

/**
 * Created by Suyash on 2/18/2016.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;

import com.net.ddns.suyashbakshi.popularmovies.DataBase.MovieDbHelper;
import com.net.ddns.suyashbakshi.popularmovies.DataBase.MoviesContract;

import junit.framework.Test;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.
        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MoviesContract.SortEntry.TABLE_NAME);
        tableNameHashSet.add(MoviesContract.MoviesEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the sort entry and movie entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MoviesContract.SortEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MoviesContract.SortEntry._ID);
        locationColumnHashSet.add(MoviesContract.SortEntry.COLUMN_SORT_VALUE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public long testSortTable() {
        // First step: Get reference to writable database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createSortValues if you wish)
        ContentValues cv = TestUtilities.createSortValues();

        // Insert ContentValues into database and get a row ID back
        long sortRowId = db.insert(MoviesContract.SortEntry.TABLE_NAME,null,cv);

        assertTrue(sortRowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MoviesContract.SortEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: No row returned from sort Query", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location query validation failed",cursor,cv);

        assertFalse("Error: more than one record returned for location query", cursor.moveToNext());


        // Finally, close the cursor and database
        cursor.close();
        dbHelper.close();

        return sortRowId;
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testMovieTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.
        // Instead of rewriting all of the code we've already written in testLocationTable
        // we can move this code to insertLocation and then call insertLocation from both
        // tests. Why move it? We need the code to return the ID of the inserted location
        // and our testLocationTable can only return void because it's a test.

        // First step: Get reference to writable database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        long sortRowId = insertSort();
        assertFalse("Error: Location Not Inserted Correctly", sortRowId == -1L);

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)
        ContentValues movieContentValues = TestUtilities.createMovieValues(sortRowId);

        // Insert ContentValues into database and get a row ID back
        long movieRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME,null,movieContentValues);
        assertTrue(movieRowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MoviesContract.MoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);


        // Move the cursor to a valid database row
        assertTrue("Error: No row returned to cursor for movies table", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Test insert DB failed to validate",cursor,movieContentValues);

        assertFalse("Error : More than one record returned for movie query", cursor.moveToNext());

        // Finally, close the cursor and database
        cursor.close();
        dbHelper.close();
    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */
    public long insertSort() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createSortValues if you wish)
        ContentValues cv = TestUtilities.createSortValues();

        // Insert ContentValues into database and get a row ID back
        long sortRowId = db.insert(MoviesContract.SortEntry.TABLE_NAME,null,cv);

        assertTrue(sortRowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(MoviesContract.SortEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        // Move the cursor to a valid database row
        assertTrue("Error: No row returned from sort Query", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location query validation failed",cursor,cv);

        assertFalse("Error: more than one record returned for location query",cursor.moveToNext());


        // Finally, close the cursor and database
        cursor.close();
        db.close();

        return sortRowId;
    }
}
