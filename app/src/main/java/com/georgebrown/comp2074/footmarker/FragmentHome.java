package com.georgebrown.comp2074.footmarker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        Button btnStart = v.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),FullScreenMapsActivity.class);
                startActivity(i);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        final GoogleMap mMap = googleMap;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        final Task location = fusedLocationProviderClient.getLastLocation();
        if (location == null) {
            return;
        } else {
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Location latLng = (Location) location.getResult();
                    LatLng currLoc = new LatLng(latLng.getLatitude(), latLng.getLongitude());
                    mMap.setMyLocationEnabled(true);
                    mMap.addMarker(new MarkerOptions().position(currLoc).title("You are here!"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 15));

                }
            });
            //mMap.setMyLocationEnabled(true);
            //mMap.addMarker(new MarkerOptions().position(currLoc).title("PlaceHolder"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc,15));

        }
    }
}
