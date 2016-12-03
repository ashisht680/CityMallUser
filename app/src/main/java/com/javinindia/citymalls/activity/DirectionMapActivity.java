

package com.javinindia.citymalls.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.utility.PermissionUtils;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectionMapActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {

    private static final String MAP_FRAGMENT_TAG = "map";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;

    String mallLat;
    String mallLong;
    String mallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);

        if (getIntent() != null) {
            mallLat = getIntent().getStringExtra("mallLat");
            mallLong = getIntent().getStringExtra("mallLong");
            mallName = getIntent().getStringExtra("mallName");
        }

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(android.R.id.content, mapFragment, MAP_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (locationManager != null) {

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                onLocationChanged(location);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.e("lalal",latitude+"\t"+ longitude+"");
                //28.6980° N, 77.1405° E
                //28.7034° N, 77.1321° E
                LatLng latLng = new LatLng(28.6980, 77.1405);
                mMap.addMarker(new MarkerOptions().position(latLng).title("you"));
                final LatLng DWARKA12 = new LatLng(latitude, longitude);
                final LatLng PITAMPURA = new LatLng(Double.parseDouble(mallLat), Double.parseDouble(mallLong));
               // final LatLng PITAMPURA = new LatLng(28.7034, 77.1321);
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(DWARKA12)
                        .include(PITAMPURA)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                getDirection(latitude, longitude, Double.parseDouble(mallLat), Double.parseDouble(mallLong));
                //getDirection(28.6980, 77.1405, 28.7034, 77.1321);
            }
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        } else {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getApplicationContext());
            alertDialogBuilder.setTitle("Internet connection slow or GPS disable");
            alertDialogBuilder
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        enableMyLocation();

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {

            mMap.setMyLocationEnabled(true);
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mallLat), Double.parseDouble(mallLong))).title(mallName)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.mall_colored));
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "On your GPS location please", Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {
        //AIzaSyCqkWk_fyCg6NPimD45uRsI1lHQgJ2aSyM
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

    private void getDirection(final double a, final double b, final double c, final double d) {
        //Getting the URL
        String url = makeURL(a, b, c, d);
        Log.e("getDirection", a + "\t" + b + "\t" + c + "\t" + d + "");
        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);
        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        drawPath(response, a, b, c, d);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String makeURL(double sourcelat, double sourcelog, double destlat, double destlog) {
        //https://maps.googleapis.com/maps/api/directions/json?origin=27.6325&destination=85.1453=end&sensor=false
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        //   urlString.append("&key=AIzaSyB1Xx-SgK7ck1B6pNLIywUFbXlo2923BWQ");
        return urlString.toString();
    }

    //The parameter is the server response
    public void drawPath(String result, double a, double b, double c, double d) {
        Log.e("drawPath", result + "");
        //Getting both the coordinates
        LatLng from = new LatLng(a, b);
        LatLng to = new LatLng(c, d);
        //Calculating the distance in meters
        Double distance = SphericalUtil.computeDistanceBetween(from, to) * 1000;
        double number2 = (int) Math.round(distance * 100) / (double) 100;
        //Displaying the distance
        Toast.makeText(this, String.valueOf(number2 + " km"), Toast.LENGTH_SHORT).show();
        try {
            Log.e("try draw", distance + "");
            //Parsing json
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(10)
                    .color(Color.BLUE)
                    .geodesic(true)
            );
        } catch (JSONException e) {
            Log.e("catch draw", distance + "");
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
