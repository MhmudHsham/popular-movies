package com.hisham.mahmoud.popularmovies.activities;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.model.Movie;


public class DetailsActivity extends AppCompatActivity {
    DetailsActivityFragment detailsActivityFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setUpNavigationBar();

        Movie movie = getIntent().getParcelableExtra("movie");
        DetailsActivityFragment detailsActivityFragment = DetailsActivityFragment.newInstance(movie);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, detailsActivityFragment).commit();

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
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
