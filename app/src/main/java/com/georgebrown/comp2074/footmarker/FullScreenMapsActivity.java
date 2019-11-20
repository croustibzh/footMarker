package com.georgebrown.comp2074.footmarker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.maps.GoogleMap.*;

public class FullScreenMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private double distance;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LatLngBounds.Builder boundryBuilder = new LatLngBounds.Builder();
    private byte[] imageBytes;
    private DataBaseHelper dbHelper;
    private Bitmap imgBitmap;
    private long timeElapsed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dbHelper = new DataBaseHelper(this);
        distance = 0;
        chronometer = findViewById(R.id.chronometer);
        running = true;
        chronometer.start();

    }
    public void startChronometer(View view){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer(View view){
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            //fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            running = false;
        }
    }

    public void saveRouteImg(){
        LatLngBounds boundry = boundryBuilder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(boundry, 100);
        mMap.stopAnimation();
        mMap.animateCamera(cu);
        mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                imageBytes = baos.toByteArray();
                timeElapsed = SystemClock.elapsedRealtime()-chronometer.getBase();
                dbHelper.addRoute("Route", distance, timeElapsed,0, imageBytes,"#HashTag");
            }
        });
    }

    public void saveRoutes(View view){

        chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
    /*    Intent i = new Intent(this,MainActivity.class);
        i.putExtra("saved",true);
        startActivity(i);
    */
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        saveRouteImg();

        Bundle bundle = new Bundle();
        bundle.putDouble("dist", distance);
        bundle.putLong("time", timeElapsed);
        SaveModal bottomSheet = new SaveModal();
        bottomSheet.setArguments(bundle);

        bottomSheet.show(getSupportFragmentManager(), "bottomSheet");
    }

    public void snapshot (GoogleMap.SnapshotReadyCallback callback, Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        imageBytes = baos.toByteArray();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final TextView txtDistance = findViewById(R.id.txtDistance);
        final List<LatLng> position = new ArrayList<LatLng>();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(7);
        final Polyline polyline = mMap.addPolyline(polylineOptions);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null){
                    return;
                }
                Location location = locationResult.getLastLocation();
                    LatLng updatePosition = new LatLng(location.getLatitude(),location.getLongitude());
                    boundryBuilder.include(updatePosition);
                    position.add(updatePosition);
                    if(position.size()>1){
                        distance += SphericalUtil.computeDistanceBetween(position.get(position.size()-2),updatePosition);
                        polyline.setPoints(position);
                        position.add(updatePosition);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(updatePosition,20));
                    mMap.setMyLocationEnabled(true);
                    txtDistance.setText(String.format("%.2f",distance/1000) + " km");

                }

        };
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }
}
