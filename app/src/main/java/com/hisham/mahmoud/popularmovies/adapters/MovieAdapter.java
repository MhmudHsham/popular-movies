package com.hisham.mahmoud.popularmovies.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


public class MovieAdapter extends BaseAdapter{



    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String POSTER_URL_1 = "http://image.tmdb.org/t/p/w342";

    private  List<Movie> movieList;
    private Context context;
    public MovieAdapter() {


    }

    public MovieAdapter(Context context , List<Movie> movieList) {
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_poster_item , parent ,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);


        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);
        if(movie.getIsFave() == 0) {

            String posterPath = POSTER_URL + movie.getPosterPath();

            Log.i("posterPath", movie.getPosterPath());
            Picasso.with(context).load(posterPath).into(holder.posterImageView);
        }else{

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/popularMoviesImage");
            Picasso.with(context)
                    .load(new File(file.getAbsolutePath() + movie.getPosterPath()))
                            .into(holder.posterImageView);
        }

        return convertView;
    }



    public class ViewHolder{
        TextView posterTitleView;
        ImageView posterImageView;
        FrameLayout mainLayout;




        public  ViewHolder(View convertView){
            posterImageView = (ImageView) convertView.findViewById(R.id.posterImage);
            mainLayout = (FrameLayout) convertView.findViewById(R.id.mainlayout);
            posterTitleView = (TextView) convertView.findViewById(R.id.posterTitle);
        }

    }
}
