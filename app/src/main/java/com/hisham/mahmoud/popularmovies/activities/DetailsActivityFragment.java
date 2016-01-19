package com.hisham.mahmoud.popularmovies.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.adapters.MovieAdapter;
import com.hisham.mahmoud.popularmovies.adapters.ReviewAdapter;
import com.hisham.mahmoud.popularmovies.adapters.TrailerAdapter;
import com.hisham.mahmoud.popularmovies.database.CURD;
import com.hisham.mahmoud.popularmovies.model.Movie;
import com.hisham.mahmoud.popularmovies.model.Review;
import com.hisham.mahmoud.popularmovies.model.Trailer;
import com.hisham.mahmoud.popularmovies.util.ListUtil;
import com.hisham.mahmoud.popularmovies.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class DetailsActivityFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Toolbar toolbar;
    private Movie movie;
    private ImageView posterBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView posterText;
    private TextView voteText;
    private TextView dateText;
    private TextView durationText;
    private TextView overviewText;
    private static final String MOVIE_URL ="http://api.themoviedb.org/3/movie/";
    private static final String TRAILER_URL = "https://www.youtube.com/watch";

    private ListView trailerListView;
    private ListView reviewListView;
    private ToggleButton toggleBtn;
    private CURD curd;
    private  String poster_path;
    private LinearLayout trailerLinear,reviewLinear;
    private int isFav;
    private String moviesReviewsURL;


    public DetailsActivityFragment() {
    }


    public static DetailsActivityFragment newInstance(Movie movie) {
        DetailsActivityFragment detailsFragment = new DetailsActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);

        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        posterText = (TextView) rootView.findViewById(R.id.posterTitle);
        posterBar = (ImageView) rootView.findViewById(R.id.posterImage);
        dateText = (TextView) rootView.findViewById(R.id.release_date);
        durationText = (TextView) rootView.findViewById(R.id.duration);
        voteText = (TextView) rootView.findViewById(R.id.average_vote);
        overviewText = (TextView) rootView.findViewById(R.id.overview);
        trailerListView = (ListView) rootView.findViewById(R.id.trailerListView);
        trailerListView.setOnItemClickListener(this);
        reviewListView = (ListView) rootView.findViewById(R.id.reviewListView);
        trailerLinear =(LinearLayout)rootView.findViewById(R.id.trailer_linear);
        reviewLinear =(LinearLayout)rootView.findViewById(R.id.review_linear);
        toggleBtn = (ToggleButton)rootView.findViewById(R.id.toggle);
        toggleBtn.setOnClickListener(this);
        Log.i("isNull", getArguments() == null ? "ok" : "no");

        if (toolbar!= null) {
            //onePane Mode
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            try {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        curd = new CURD(getActivity(), movie);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


        movie = getArguments().getParcelable("movie");
        Log.i("movie", movie == null ? "ok" : movie.getTitle());
        overviewText.setText(movie.getOverview());
        dateText.setText(movie.getReleaseDate());
        voteText.setText(movie.getVoteAverage());
        durationText.setText("");
        posterText.setText(movie.getTitle());

       if(curd.isInFavList(movie)){
              toggleBtn.setChecked(true);
              movie.setIsFave(1);


            }else{
                movie.setIsFave(0);
                toggleBtn.setChecked(false);


      }
        Log.i("isfav", "" + isFav);
        if(movie.getIsFave() ==0){
            poster_path =  MovieAdapter.POSTER_URL + movie.getPosterPath();
            Picasso.with(getActivity()).load(poster_path).fit().into(posterBar);
            toggleBtn.setChecked(false);
            }else{

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/popularMoviesImage");
            Picasso.with(getActivity())
                    .load(new File(file.getAbsolutePath() + File.separator + movie.getPosterPath()))
                    .into(posterBar);
                toggleBtn.setChecked(true);
            }



        Log.i("mm", movie.getMovieId() + "/videos");

        Uri trailersURI = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(movie.getMovieId()).appendPath("videos")
                .appendQueryParameter(MainActivityFragment.API_KEY_NAME, MainActivityFragment.API_KEY_VALUE)
                .build();
        String moviesTrailerURL = trailersURI.toString();


        Uri reviewssURI = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(movie.getMovieId()).appendPath("reviews")
                .appendQueryParameter(MainActivityFragment.API_KEY_NAME, MainActivityFragment.API_KEY_VALUE)
                .build();
         moviesReviewsURL = reviewssURI.toString();

        Log.i("trailers_url", moviesTrailerURL);
        Log.i("reviews_url", moviesReviewsURL);
//
        if(NetworkUtil.isNetworkAvailable(getActivity())) {



            new FetchMoviesTrailers().execute(moviesTrailerURL);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        setUpActionBar();
        setUpNavigationBar();

    }


    private void setUpNavigationBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.primaryColorDark));
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_details, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpActionBar() {
        Log.i("toolbar1", toolbar == null ? "ok" : "no");
        if (toolbar != null) {

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(7);


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Trailer trailer = (Trailer) parent.getItemAtPosition(position);
        Uri video = Uri.parse(TRAILER_URL).buildUpon()
                .appendQueryParameter("v", trailer.getTrailerKey())
                .build();
        Intent i = new Intent(Intent.ACTION_VIEW,video);
        startActivity(i);

    }

    @Override
    public void onClick(View v) {

        if(((ToggleButton) v).isChecked()) {
            // handle toggle on

            curd.addToFavoriateList();

        } else {
            // handle toggle off

            curd.removeFromFavoriateList();

        }


    }


    public class FetchMoviesTrailers extends AsyncTask<String, Void, List<Trailer>> {

        private final String LOG_TAG = FetchMoviesTrailers.class.getSimpleName();
        private String trailersStr;



            private List<Trailer> getTrailersfromJson(String trailersStr) {

            List<Trailer> trailers = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(trailersStr);
                JSONArray moviesArray = object.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject jsonMovie = (JSONObject) moviesArray.get(i);
                    Log.i("trailerKey",jsonMovie.getString("key")+": "+ jsonMovie.getString("name"));
                        //if(jsonMovie.getString("type").equals("Trailer")) {
                            trailers.add(new Trailer(jsonMovie.getString("key"), jsonMovie.getString("name")));
                            movie.setTrailers(trailers);
                        //}

                }
            } catch (JSONException js) {

            }
            return trailers;

        }

        @Override
        protected List<Trailer> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri uri = Uri.parse(params[0]).buildUpon().build();
            URL url = null;
            try {
                url = new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // No data in stream
                    return null;
                }
                trailersStr = buffer.toString();

            } catch (IOException e1) {
                e1.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            Log.i("trailersStr",trailersStr);
            return getTrailersfromJson(trailersStr);



        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);

            TrailerAdapter adapter = new TrailerAdapter(getActivity(),trailers);


                if (trailers != null) {

                    trailerLinear.setVisibility(View.VISIBLE);
                    if (adapter.isEmpty()) {
                        trailerListView.setAdapter(adapter);
                        ListUtil.setListViewHeightBasedOnItems(trailerListView);
                        adapter.notifyDataSetChanged();

                    } else {
                        trailerListView.setAdapter(adapter);
                        ListUtil.setListViewHeightBasedOnItems(trailerListView);
                        adapter.notifyDataSetChanged();
                    }
                }

            new FetchMoviesReviews().execute(moviesReviewsURL);

        }
    }


    public class FetchMoviesReviews extends AsyncTask<String, Void, List<Review>> {


        private final String LOG_TAG = FetchMoviesReviews.class.getSimpleName();
        private String reviewsStr;


        private List<Review> getReviewfromJson(String reviewsStr) {

            List<Review> reviews = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(reviewsStr);
                JSONArray moviesArray = object.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject jsonMovie = (JSONObject) moviesArray.get(i);
                        reviews.add(new Review(jsonMovie.getString("author"), jsonMovie.getString("content")));
                        movie.setReviews(reviews);                    }


            } catch (JSONException js) {

            }
            return reviews;

        }

        @Override
        protected List<Review> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri uri = Uri.parse(params[0]).buildUpon().build();
            URL url = null;
            try {
                url = new URL(uri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // No data in stream
                    return null;
                }
                reviewsStr = buffer.toString();

            } catch (IOException e1) {
                e1.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            Log.i("reviewsStr",reviewsStr);
            return getReviewfromJson(reviewsStr);



        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);



                    ReviewAdapter adapter = new ReviewAdapter(getActivity(), reviews);

                    if (reviews != null && reviews.size() != 0) {
                        reviewLinear.setVisibility(View.VISIBLE);

                        if (adapter.isEmpty()) {
                            reviewListView.setAdapter(adapter);
                            ListUtil.setListViewHeightBasedOnItems(reviewListView);
                            adapter.notifyDataSetChanged();

                        } else {
                            reviewListView.setAdapter(adapter);
                            ListUtil.setListViewHeightBasedOnItems(reviewListView);
                            adapter.notifyDataSetChanged();
                        }


                    }

        }

    }
}
