package com.hisham.mahmoud.popularmovies.adapters;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.hisham.mahmoud.popularmovies.R;
import com.hisham.mahmoud.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends BaseAdapter{


    private  List<Trailer> trailerListList;
    private Context context;
    public TrailerAdapter() {
    }

    public TrailerAdapter(Context context, List<Trailer> trailerListList) {
        this.trailerListList = trailerListList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trailerListList.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailerListList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            Log.i("context",context==null?"yes":"no");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trailer_item , parent ,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);


        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Trailer trailer = getItem(position);

        holder.trailerText.setText(trailer.getTrailerName());



        return convertView;
    }



    public class ViewHolder{
        TextView trailerText;




        public  ViewHolder(View convertView){

            trailerText = (TextView) convertView.findViewById(R.id.trailer_text);
        }

    }
}
