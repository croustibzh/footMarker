
package com.georgebrown.comp2074.footmarker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



public class FragmentRouteDetails extends Fragment {

  private String newRouteName, newRouteTime, newRouteDistance, newHashTag;
  private TextView txtDistance, txtTime;
  private EditText editTextFragment, hashTag;
  private Button buttonFragment, btnShare;
  private DataBaseHelper dbHelper;
  private byte[] image;
  private ImageView imageView;
  private RouteDetails routeDetails;
  private Bitmap bitmap;
  private RatingBar ratingBar;
  private int id;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_route_details, container, false);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Bundle bundle = this.getArguments();
    id = bundle.getInt("id");
    dbHelper = new DataBaseHelper(getActivity());
    Cursor data = dbHelper.getRoute(id);
    while (data.moveToNext()) {
      routeDetails = new RouteDetails(
        data.getInt(0),
        data.getString(1), //name
        data.getDouble(2), //distance
        data.getLong(3), //time
        data.getFloat(4), //rating
        data.getBlob(5), //image
        data.getString(6) //hashtag
      );
    }

    //Gets
    View v = getView();
    bitmap = getImage( routeDetails.getImage() );
    imageView = v.findViewById(R.id.mapView); //map
    editTextFragment = v.findViewById(R.id.edTName); //name
    txtDistance = v.findViewById(R.id.txtVDist); //distance
    txtTime = v.findViewById(R.id.txtVTime); //time
    buttonFragment = v.findViewById(R.id.btnSaveChanges); //save changes
    btnShare = v.findViewById(R.id.btnShare);
    ratingBar = v.findViewById(R.id.ratingBarDetail);
    hashTag = v.findViewById(R.id.editTxtHashtag);

    //Set Map
    imageView.setImageBitmap(bitmap);

    //Set Name
    final String currentRouteName = routeDetails.getName();
    editTextFragment.setText(currentRouteName);
    editTextFragment.requestFocus();

//        //Set Distance
//        newRouteDistance = String.format("%.2f", routeDetails.getDistance() ) + " km";
//        txtDistance.setText(newRouteDistance);

    //Sets distance unit output
    if (Constants.DIST_UNIT==false) {
      newRouteDistance = String.format("%.2f", routeDetails.getDistance()) + " km";
      txtDistance.setText(newRouteDistance);
    }
    else {
      newRouteDistance = String.format("%.2f", routeDetails.getDistance()*0.621371) + " miles";
      txtDistance.setText(newRouteDistance);
    }

    //Set Time
    long ms = routeDetails.getTime();
    long hr = ms / 3600000;
    long min = (ms - (hr*3600000)) / 60000;
    long sec =  (ms - (hr*3600000) - (min*60000)) / 1000;
    newRouteTime = hr+"h " + min+"m " + sec+"s";
    txtTime.setText(newRouteTime);

    //Set Hashtag
    newHashTag = routeDetails.getHashTag();
    hashTag.setText(newHashTag);

    //Set Rating
    final Float currentRating = routeDetails.getRating();
    ratingBar.setRating(currentRating);


    //Rating Bar - WhenClicked
    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
      @Override
      public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        dbHelper.updateRating(rating, id);
      }
    });

    //Share Button - OnClick
    btnShare.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_SUBJECT, newRouteName);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Route: " + currentRouteName +
                        "\nDistance: " + newRouteDistance +
                        "\nTime: " + newRouteTime +
                        "\nHashtag: " + newHashTag);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share Route");
        startActivity(shareIntent);
      }
    });

    //Save Changes Button - OnClick
    buttonFragment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Names + update db
        newRouteName = editTextFragment.getText().toString();
        dbHelper.updateName(newRouteName, id);
        editTextFragment.setText(newRouteName);

        //Hashtag + update db
        newHashTag = hashTag.getText().toString();
        dbHelper.updateHashtag(newHashTag, id);
        hashTag.setText(newHashTag);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, new FragmentRoutes());
        fragmentTransaction.commit();
      }

    });
  }

  public static Bitmap getImage(byte[] image) {
    return BitmapFactory.decodeByteArray(image, 0, image.length);
  }
}

