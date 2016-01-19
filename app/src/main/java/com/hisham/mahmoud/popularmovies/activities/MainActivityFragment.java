package com.hisham.mahmoud.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.adapters.MovieAdapter;
import com.hisham.mahmoud.popularmovies.database.CURD;
import com.hisham.mahmoud.popularmovies.model.Movie;
import com.hisham.mahmoud.popularmovies.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {


    private Toolbar toolbar;
    private static final String MOVIES_URL = "http://api.themoviedb.org/3/movie/";
    private static final String SORTED_BY = "sort_by";
    private static final String POPULARITY_CRITERIA = "popular";
    private static final String HIGHRATE_CRITERIA = "top_rated";
    public static final String API_KEY_NAME = "api_key";
    public static final String API_KEY_VALUE = "837aa67b269303622a476bbe24283a57";
    private String popularMoviesURL;
    private String highMoviesRateURL;
    private GridView gridView;
    private String sortMode;
    boolean isFav;

    private SharedPreferences.Editor editor;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.posterView);
        gridView.setOnItemClickListener(this);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        // setUpActionBar();


        String url = null;

        Uri popularURI = Uri.parse(MOVIES_URL).buildUpon()
                .appendPath(POPULARITY_CRITERIA)
                .appendQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                .build();
        popularMoviesURL = popularURI.toString();


        Uri highRatedURI = Uri.parse(MOVIES_URL).buildUpon()
                .appendPath(HIGHRATE_CRITERIA)
                .appendQueryParameter(API_KEY_NAME, API_KEY_VALUE)
                .build();
        highMoviesRateURL = highRatedURI.toString();


        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sortMode = sharedPreferences.getString("sortMode", POPULARITY_CRITERIA);
        switch (sortMode) {
            case POPULARITY_CRITERIA:
                url = popularMoviesURL.toString();
                break;
            case HIGHRATE_CRITERIA:
                url = highMoviesRateURL.toString();
                break;
        }
             Log.i("currentURL", "" + NetworkUtil.isNetworkAvailable(getActivity()));

            if(NetworkUtil.isNetworkAvailable(getActivity())) {
                new FetchMovies().execute(url);
            }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Movie movie = (Movie) parent.getItemAtPosition(position);
        ((Callback) getActivity()).onItemSelected(movie);

    }

    public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {


        private final String LOG_TAG = FetchMovies.class.getSimpleName();
        private String moviesStr;


        private List<Movie> getMoviesfromJson(String moviesStr) {

            List<Movie> movies = new ArrayList<>();
            if(moviesStr!= null) {
                try {
                    JSONObject object = new JSONObject(moviesStr);
                    JSONArray moviesArray = object.getJSONArray("results");

                    for (int i = 0; i < moviesArray.length(); i++) {
                        JSONObject jsonMovie = (JSONObject) moviesArray.get(i);
                        if (jsonMovie.getString("poster_path") != null) {
                            movies.add(new Movie(jsonMovie.getString("id"), jsonMovie.getString("original_title"), jsonMovie.getString("release_date"), jsonMovie.getString("vote_average"), jsonMovie.getString("overview"), jsonMovie.getString("poster_path"), 0));
                        }

                    }
                } catch (JSONException js) {

                }

            }
            return movies;

        }

        @Override
        protected List<Movie> doInBackground(String... params) {
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
                moviesStr = buffer.toString();

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

           // Log.i("moviesStr", moviesStr);
            return getMoviesfromJson(moviesStr);


        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            MovieAdapter adapter = new MovieAdapter(getActivity(), movies);

            if (movies != null) {

                if (adapter.isEmpty()) {
                    gridView.setAdapter(adapter);
                } else {
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {


            case R.id.popularity_sort:

                sortMode = POPULARITY_CRITERIA;
                editor.putString("sortMode", sortMode);
                editor.commit();
                if(NetworkUtil.isNetworkAvailable(getActivity())) {
                    new FetchMovies().execute(popularMoviesURL);
                }
                break;

            case R.id.highRated_sort:

                sortMode = HIGHRATE_CRITERIA;
                editor.putString("sortMode", sortMode);
                editor.commit();
                if(NetworkUtil.isNetworkAvailable(getActivity())) {
                    new FetchMovies().execute(highMoviesRateURL);
                }
                break;
            case R.id.favoriate:

                updateFav();

                break;
        }

        return true;
    }

    public void updateFav() {
        CURD curd = new CURD(getActivity(), null);
        List<Movie> favoriateList = curd.fetechMovieFromFavoriateList();
        MovieAdapter adapter = new MovieAdapter(getActivity(), favoriateList);

        if (favoriateList != null) {
            if (adapter.isEmpty()) {
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

    }

    public interface Callback {

        public void onItemSelected(Movie movie);
    }


}
