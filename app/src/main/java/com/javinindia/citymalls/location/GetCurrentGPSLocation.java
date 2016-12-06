package com.javinindia.citymalls.location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 01-12-2016.
 */
public class GetCurrentGPSLocation extends Service implements LocationListener {
    protected LocationManager locationManager;
    String mprovider;
    Location location;
    double latitude;
    double longitude;
    private final Context context;

    Geocoder geocoder;


    public GetCurrentGPSLocation(Context mContext) {
        this.context = mContext;
        if (context != null) {
            methodLocation();
        } else {
            Toast.makeText(getApplicationContext(), "context null", Toast.LENGTH_LONG).show();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void methodLocation() {

        //  GeoCoder and Location Manager

        geocoder = new Geocoder(context, Locale.getDefault());

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        // check GPS is enable or not

        boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfGPS) {

            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   // return;
                    // apply for get current address
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                    try {

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                        setLatitude(location.getLatitude());
                        setLongitude(location.getLongitude());

                    } catch (Exception ex) {

                    }

                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }

        }else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // return;
                // apply for get current address
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                try {

                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                    setLatitude(location.getLatitude());
                    setLongitude(location.getLongitude());

                } catch (Exception ex) {

                }

            }
        }


    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);

            setLatitude(location.getLatitude());
            setLongitude(location.getLongitude());

        } catch (Exception ex) {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
