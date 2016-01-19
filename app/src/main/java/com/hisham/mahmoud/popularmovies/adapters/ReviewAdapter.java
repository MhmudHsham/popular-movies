package com.hisham.mahmoud.popularmovies.adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReviewAdapter extends BaseAdapter{

    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String POSTER_URL_1 = "http://image.tmdb.org/t/p/w342";

    private  List<Review> reviewList;
    private Context context;
    public ReviewAdapter() {
    }

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Review getItem(int position) {
        return reviewList.get(position);
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
            convertView = inflater.inflate(R.layout.review_item , parent ,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);


        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Review review = getItem(position);

        Log.i("content",review.getContent());
        holder.autherText.setText(review.getAuthor());
        holder.contentText.setText(review.getContent());


        return convertView;
    }



    public class ViewHolder{
        TextView autherText;
        TextView contentText;




        public  ViewHolder(View convertView){
            autherText = (TextView) convertView.findViewById(R.id.autherText);
            contentText = (TextView) convertView.findViewById(R.id.contextText);
        }

    }
}
