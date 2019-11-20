package com.georgebrown.comp2074.footmarker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RoutesListAdapter extends ArrayAdapter<RouteDetails> {
        private Context mContext;
        int mResource;
        Bitmap bitmap;
    public RoutesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RouteDetails> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        byte[] image = getItem(position).getImage();
        String name = getItem(position).getName();
        String tag = getItem(position).getHashTag();
        long time = getItem(position).getTime();
        double distance = getItem(position).getDistance();
        float rating = getItem(position).getRating();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        //Routes routes = new Routes(imgUrl,name,tag,time,distance,rating);
        convertView = inflater.inflate(mResource,parent,false);

        bitmap = getImage(image);
        ImageView vImgView = convertView.findViewById(R.id.imgRoutes);
        TextView vTxtName = convertView.findViewById(R.id.txtName);
        TextView vTxtTag = convertView.findViewById(R.id.txtTag);
        RatingBar vRatingBar = convertView.findViewById(R.id.ratingBar);

        vImgView.setImageBitmap(bitmap);
        vTxtName.setText(name);
        vTxtTag.setText(tag);
        vRatingBar.setRating(rating);
        vRatingBar.setFocusable(false);
        vRatingBar.setIsIndicator(true);

        return convertView;


    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
