package com.example.eslam_abo_el_fetouh.myimoviesudacity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends CursorAdapter {

    public ImageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView image = (ImageView)view.findViewById(R.id.item_pic);
        ImageView favImg = (ImageView)view.findViewById(R.id.imageView2);
        TextView title = (TextView)view.findViewById(R.id.item_tv);
        if (cursor.getInt(MainFragment.COL_MOVIE_FAV) == 1){
            favImg.setImageResource(R.drawable.fav);
        }else {
            favImg.setImageResource(0);
        }
        String Title = cursor.getString(MainFragment.COL_MOVIE_TITLE);
        title.setText(Title);
        String url = new Core(context).large_image_url + cursor.getString(MainFragment.COL_MOVIE_IMAGE);
        Picasso.with(context).load(url).into(image);
    }
}