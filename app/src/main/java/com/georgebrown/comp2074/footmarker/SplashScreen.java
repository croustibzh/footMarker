package com.georgebrown.comp2074.footmarker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;

import static com.georgebrown.comp2074.footmarker.Constants.ERROR_DIALOG_REQUEST;

public class SplashScreen extends AppCompatActivity {
    private static String FineLocation =Manifest.permission.ACCESS_FINE_LOCATION;

    private static int LocPermissionReqCode = 1111;
    private Handler wait = new Handler();
    private static boolean mapLocationPermissionGranted = false;
    @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getLocationPermission();

    }
    public boolean googleServiceOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
        }else {
            Toast.makeText(this,"Google service cannot be found on phone some functions in application wil be impaired",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean isGPSEnabled(){
        final LocationManager locationManager =(LocationManager)getSystemService(LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }
        return false;
    }

    private void getLocationPermission(){
        String permission =FineLocation;

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FineLocation)== PackageManager.PERMISSION_GRANTED){
            mapLocationPermissionGranted = true;
            wait.postDelayed(new Runnable() {

                                 @Override
                                 public void run() {
                                     startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                     finish();
                                 }
                             }
                    , 3000);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{permission},LocPermissionReqCode);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mapLocationPermissionGranted = false;

        switch (requestCode){
            case 1111:{
                if (grantResults.length>0 ){
                    for (int i = 0; i <grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mapLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mapLocationPermissionGranted=true;
                    wait.postDelayed(new Runnable() {

                                         @Override
                                         public void run() {
                                             startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                             finish();
                                         }
                                     }
                            , 3000);
                }
            }
        }

    }
}
