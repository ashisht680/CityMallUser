package com.javinindia.citymalls.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;

/**
 * Created by Ashish on 18-11-2016.
 */
public class LocationActivity extends Activity implements LocationListener {
   //private final Context mContext;
    boolean checkGPS = false;

    boolean checkNetwork = false;

    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    Location loc;
    double latitude;
    double longitude;

    private GoogleApiClient googleApiClient;

    final static int REQUEST_LOCATION = 199;

    protected LocationManager locationManager;


    public LocationActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("location activity","on create");
        methodOhGPS();
    }


    public void methodOhGPS() {
        if(!hasGPSDevice(LocationActivity.this)){
            Toast.makeText(LocationActivity.this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(LocationActivity.this)) {
            Toast.makeText(LocationActivity.this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }else{
            Toast.makeText(LocationActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(LocationActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(LocationActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        Toast.makeText(getApplicationContext(),"off",Toast.LENGTH_LONG).show();
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    case Activity.RESULT_OK:{
                        Toast.makeText(getApplicationContext(),"on",Toast.LENGTH_LONG).show();
                        getLocation();
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(getApplicationContext(), "No Service Provider Available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (checkNetwork) {
                    //    Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }

                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    } catch (SecurityException e) {

                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPS) {
                //  Toast.makeText(mContext, "GPS", Toast.LENGTH_SHORT).show();
                if (loc == null) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    @Override
    public void onLocationChanged(Location location) {

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
}
