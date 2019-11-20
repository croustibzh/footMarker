package com.georgebrown.comp2074.footmarker;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SaveModal extends BottomSheetDialogFragment{
    private GoogleMap mMap;
    private Chronometer chronometer;
    private DataBaseHelper dbHelper;
    private Button btnSave;
    private TextView txtDistance, txtTime;
    private EditText editTxtRouteName;
    private RouteDetails routeDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.save_modal, container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = this.getArguments();
        double distance = bundle.getDouble("dist")/1000;
        long time = bundle.getLong("time");

        //Gets
        View v = getView();
        txtDistance = v.findViewById(R.id.txtDistance);
        txtTime = v.findViewById(R.id.txtTime);
        btnSave = v.findViewById(R.id.btnSaveRoute);
        editTxtRouteName = v.findViewById(R.id.editTxtRouteName);

        //Time
        long ms = time;
        long hr = ms / 3600000;
        long min = (ms - (hr*3600000)) / 60000;
        long sec =  (ms - (hr*3600000) - (min*60000)) / 1000;
        String formattedTime = hr+"h " + min+"m " + sec+"s";
        dbHelper = new DataBaseHelper(getContext());
        final int id = dbHelper.getLastId();

        //Sets
        if (R.bool.distanceUnit==0) {
            txtDistance.setText(String.format("%.2f", distance) + " km");
        }
        else {
            txtDistance.setText(String.format("%.2f", distance*0.621371) + " miles");
        }
        txtTime.setText(formattedTime);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set
                String routeName = editTxtRouteName.getText().toString();

                dbHelper.updateName(routeName, id+1);

                Intent i = new Intent(getContext() ,MainActivity.class);
                i.putExtra("saved",true);
                startActivity(i);
            }
        });
    }
}
