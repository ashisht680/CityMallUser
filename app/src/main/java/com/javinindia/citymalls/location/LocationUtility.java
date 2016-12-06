package com.javinindia.citymalls.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Ashish on 05-12-2016.
 */

public class LocationUtility {
    private Context context;
    private LocationManager manager;
    private LocationListener listener;
    private Location myCurrentLocation;
    double latitude=0.0;
    double longitude=0.0;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LocationUtility(Context context) {
        this.context = context;
        this.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        boolean enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            // SEZIONE DEDICATA al location
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
                this.myCurrentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                Log.e("Loc", " CURRENT LOCATION " + myCurrentLocation);
            }

            if (myCurrentLocation!=null){
                setLatitude(myCurrentLocation.getLatitude());
                setLongitude(myCurrentLocation.getLongitude());
            }

        }
    }
}
