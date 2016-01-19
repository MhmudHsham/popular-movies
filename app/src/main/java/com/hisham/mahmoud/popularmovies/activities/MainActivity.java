package com.hisham.mahmoud.popularmovies.activities;


import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.model.Movie;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwoPane;
    private DetailsActivityFragment detailsActivityFragment;
    private FrameLayout container;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
       toolbar = (Toolbar) findViewById(R.id.toolbar);
            setUpActionBar();
            setUpNavigationBar();
            container = (FrameLayout) findViewById(R.id.fragment1);

        }


    @Override
    protected void onStart() {
        super.onStart();
        if(container !=null){
            mTwoPane = true;
        }else{
            mTwoPane = false;
        }
    }
    private void setUpActionBar() {

        Log.i("toolbar", toolbar == null ? "ok" : "no");
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setElevation(7);


        }
    }
    private void setUpNavigationBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.primaryColorDark));
        }    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onItemSelected(Movie movie) {
        Log.i("mPane", ""+mTwoPane);
        if(mTwoPane){
            DetailsActivityFragment detailsActivityFragment = DetailsActivityFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, detailsActivityFragment).commit();

        }else{
            Intent i = new Intent(MainActivity.this,DetailsActivity.class);
            i.putExtra("movie",movie);
            startActivity(i);

        }

    }
}
