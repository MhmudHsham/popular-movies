package com.hisham.mahmoud.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


import com.hisham.mahmoud.popularmovies.activities.DetailsActivityFragment;
import com.hisham.mahmoud.popularmovies.activities.MainActivityFragment;
import com.hisham.mahmoud.popularmovies.adapters.MovieAdapter;
import com.hisham.mahmoud.popularmovies.model.Favoriate_movieTable;
import com.hisham.mahmoud.popularmovies.model.Movie;
import com.hisham.mahmoud.popularmovies.util.PosterImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;




public class CURD {
    MovieLite helper;
    private  SQLiteDatabase db;
    private Movie movie;
    private Context context;

    public CURD(Context context, Movie movie) {
        helper = new MovieLite(context);
        this.movie = movie;
        this.context = context;
        try {
            db = helper.getWritableDatabase();
        } catch (Exception e) {
            helper.getReadableDatabase();
        }
    }

    public void addToFavoriateList() {
        PosterImage posterImg = new PosterImage(movie.getPosterPath());
        Picasso.with(context)
                .load(MovieAdapter.POSTER_URL + movie.getPosterPath())
                .into(posterImg.traget);
       context.getContentResolver().insert(Favoriate_movieTable.CONTENT_URI, Favoriate_movieTable.getContentValues(movie, false));
        Toast.makeText(context, movie.getTitle() + " is Added to favoriate list", Toast.LENGTH_SHORT).show();


    }

    public boolean isInFavList(Movie movie) {

        List<Movie> off = fetechMovieFromFavoriateList();
        for(Movie offline :off){
            if(movie.getMovieId().equals(offline.getMovieId())){

                return true;
            }
        }


        return false;
    }

    public void removeFromFavoriateList() {
        if(isInFavList(movie)) {

            int cnxt = context.getContentResolver().delete(Favoriate_movieTable.CONTENT_URI,Favoriate_movieTable.FIELD_MOVIE_ID+"=?",new String []{String.valueOf(movie.getMovieId())});
            if (cnxt > 0)
                Toast.makeText(context, movie.getTitle() + " is removed from favoriate list", Toast.LENGTH_SHORT).show();
        }



    }


    public  List<Movie> fetechMovieFromFavoriateList() {

        Cursor c = context.getContentResolver().query(Favoriate_movieTable.CONTENT_URI, null, null, null, null);
        List<Movie>  offlineMovie = Favoriate_movieTable.getRows(c, false);




        return offlineMovie;
    }
}
