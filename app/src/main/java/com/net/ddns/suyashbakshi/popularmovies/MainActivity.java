package com.net.ddns.suyashbakshi.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

public class MainActivity extends ActionBarActivity implements MoviesFragment.Callback {

    private final String MOVIESFRAGMENT_TAG = "MTAG";
    private final String DETAILFRAGMENT_TAG = "DTAG";

    public static boolean mTwoPane;
    private String mSort = "initial";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.v("PROBLEM_OnCreateMain","RUN");
//        mSort = Utility.getPreferredSort(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if(savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(new MoviesFragment(), MOVIESFRAGMENT_TAG)
//                    .commit();
//        }
        if(findViewById(R.id.movie_detail_container) != null){

            mTwoPane = true;

            if(mTwoPane)
                Log.v("PROBLEM_TWOPANE","TRUE");

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .add(new DetailFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else {
            mTwoPane = false;
        }






//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sort = Utility.getPreferredSort(getApplicationContext());
        if(sort != null && !sort.equals(mSort)){
            MoviesFragment mf = (MoviesFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movies);

            if(null!= mf){
                Log.v("PROBLEM_OnResumeMain","RUN");
                mf.onSortChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);

            if(null!=df){
                df.onSortChanged(mSort);
            }
            mSort = sort;
        }
        Log.v("PROBLEM_OnResumeMain","END");
    }

    @Override
    public void onItemSelected(Uri idUri) {

        if(mTwoPane){

            Bundle args  = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI,idUri);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,detailFragment,DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this,DetailActivity.class)
                    .setData(idUri);
            startActivity(intent);
        }

    }
}
