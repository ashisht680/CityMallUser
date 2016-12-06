package com.javinindia.citymalls.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.fragments.BaseFragment;
import com.javinindia.citymalls.fragments.BrandsFragment;
import com.javinindia.citymalls.fragments.ChangePasswordFragment;
import com.javinindia.citymalls.fragments.FavoriteTabBarFragment;
import com.javinindia.citymalls.fragments.HomeFragment;
import com.javinindia.citymalls.fragments.LocationSearchFragment;
import com.javinindia.citymalls.fragments.LoginFragment;
import com.javinindia.citymalls.fragments.NavigationAboutFragment;
import com.javinindia.citymalls.fragments.Test;
import com.javinindia.citymalls.location.GPSTracker;
import com.javinindia.citymalls.picasso.CircleTransform;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.CustomSpinnerAdater;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ashish on 12-09-2016.
 */
public class NavigationActivity extends BaseActivity implements LocationSearchFragment.OnCallBackListener, NavigationAboutFragment.OnCallBackRefreshListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static String AVATAR_URL;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    TextView txtLocation;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    double lat = 0, log = 0;

    private GoogleApiClient googleApiClient;

    final static int REQUEST_LOCATION = 199;

    private Spinner spinner_nav;
    int iCurrentSelection = 0;
    TextView txtCityName;

    double latitude = 0.0;
    double longitude = 0.0;
    GPSTracker gps;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        initToolbar();
        setupDrawerLayout();

        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        final TextView txtOwnerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtOwnerName);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(getApplicationContext()))) {
            Picasso.with(NavigationActivity.this).load(SharedPreferencesManager.getProfileImage(getApplicationContext())).transform(new CircleTransform()).into(avatar);
        } else {
            AVATAR_URL = "http://lorempixel.com/200/200/people/1/";
            Picasso.with(NavigationActivity.this).load(R.drawable.no_image_icon).transform(new CircleTransform()).into(avatar);
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(getApplicationContext()))) {
            txtOwnerName.setText(SharedPreferencesManager.getUsername(getApplicationContext()));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(getApplicationContext()))) {
            email.setText(SharedPreferencesManager.getEmail(getApplicationContext()));
        }

        methodLocation();

        String username = SharedPreferencesManager.getUsername(getApplicationContext());

        Log.e("username", username);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction().setCustomAnimations(0, 0, 0, 0);
        mFragmentTransaction.replace(R.id.navigationContainer, new HomeFragment()).commit();


    }

    private void methodLocation() {
        if (!hasGPSDevice(NavigationActivity.this)) {
            Toast.makeText(NavigationActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(NavigationActivity.this)) {
            Toast.makeText(NavigationActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            getLocationMethod();
            // Toast.makeText(NavigationActivity.this, "Gps already enabled", Toast.LENGTH_SHORT).show();

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

    private void getLocationMethod() {

        gps = new GPSTracker(getApplicationContext());
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("gps mall", latitude + "---" + longitude);
            Toast.makeText(getApplicationContext(),latitude+"\t"+longitude+"",Toast.LENGTH_LONG).show();
        }
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            getLocationMethod();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
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
                                status.startResolutionForResult(NavigationActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_LONG).show();
                        // methodNext();
                        // The user was asked to change settings, but chose not to
                        // finish();
                        break;
                    }
                    case Activity.RESULT_OK: {
                        //  Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_LONG).show();
                        methodNext();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

    }*/

    public void methodNext() {
        Intent refresh = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle(null);

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txtCityName = (TextView) findViewById(R.id.txtCityName);
        txtCityName.setTypeface(FontAsapRegularSingleTonClass.getInstance(getApplicationContext()).getTypeFace());
        if (!TextUtils.isEmpty(SharedPreferencesManager.getCity(getApplicationContext())) && !SharedPreferencesManager.getCity(getApplicationContext()).equals(null)) {
            txtCityName.setText(SharedPreferencesManager.getCity(getApplicationContext()));
        }
        txtCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCityName.setVisibility(View.GONE);
                spinner_nav.setVisibility(View.VISIBLE);
                spinner_nav.performClick();
                addItemsToSpinner();
            }
        });
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);
    }

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("City");
        list.add("Delhi NCR");
        list.add("Mumbai");
        list.add("Kolkata");
        list.add("Bengaluru");
        list.add("Chennai");
        list.add("Hyderabad");

        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdater spinAdapter = new CustomSpinnerAdater(
                getApplicationContext(), list);


        spinner_nav.setAdapter(spinAdapter);
        // iCurrentSelection = spinner_nav.getSelectedItemPosition();
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {

                if (position != 0) {
                    // On selecting a spinner item
                    String item = adapter.getItemAtPosition(position).toString();
                    if (!item.equals(SharedPreferencesManager.getCity(getApplicationContext()))) {
                        // Showing selected spinner item
                        Toast.makeText(getApplicationContext(), "Selected  : " + item,
                                Toast.LENGTH_LONG).show();
                        SharedPreferencesManager.setCity(getApplicationContext(), item);
                        Intent refresh = new Intent(getApplicationContext(), NavigationActivity.class);
                        startActivity(refresh);//Start the same Activity
                        finish();
                        txtCityName.setText(item);
                        spinner_nav.setVisibility(View.GONE);
                        txtCityName.setVisibility(View.VISIBLE);
                    }else {
                        spinner_nav.setVisibility(View.GONE);
                        txtCityName.setVisibility(View.VISIBLE);
                    }
                } else {
                  //  spinner_nav.setVisibility(View.GONE);
                  //  txtCityName.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        //  spinner_nav.setSelection(1);

    }


    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                displayView(menuItem.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void displayView(CharSequence title) {
        if (title.equals("Home")) {
            Intent refresh = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(refresh);//Start the same Activity
            finish();
            drawerLayout.closeDrawers();
        } else if (title.equals("Profile")) {
            NavigationAboutFragment fragment = new NavigationAboutFragment();
            fragment.setMyCallUpdateProfileListener(this);
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.navigationContainer, fragment).addToBackStack(this.getClass().getSimpleName()).commit();
            drawerLayout.closeDrawers();
        } else if (title.equals("Favorite")) {
            BaseFragment fragment = new FavoriteTabBarFragment();
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.navigationContainer, fragment).addToBackStack(this.getClass().getSimpleName()).commit();
            drawerLayout.closeDrawers();
        } else if (title.equals("More")) {
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            /*BaseFragment fragment = new SignUpAddressFragment();
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.navigationContainer, fragment).addToBackStack(Constants.NAVIGATION_DETAILS).commit();*/
            drawerLayout.closeDrawers();
        } else if (title.equals("About App")) {
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            /*BaseFragment fragment = new BrandsFragment();
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.navigationContainer, fragment).addToBackStack(Constants.NAVIGATION_DETAILS).commit();*/
            drawerLayout.closeDrawers();
        } else if (title.equals("Settings")) {
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            /*BaseFragment fragment = new BrandsFragment();
            mFragmentManager = getSupportFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.navigationContainer, fragment).addToBackStack(Constants.NAVIGATION_DETAILS).commit();*/
            drawerLayout.closeDrawers();
        } else if (title.equals("Logout")) {
            Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
            dialogBox();
        }

    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Thanks for visiting City mall! Be back soon!");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();
                        SharedPreferencesManager.setUserID(getApplicationContext(), null);
                        SharedPreferencesManager.setUsername(getApplicationContext(), null);
                        SharedPreferencesManager.setPassword(getApplicationContext(), null);
                        SharedPreferencesManager.setEmail(getApplicationContext(), null);
                        SharedPreferencesManager.setLocation(getApplicationContext(), null);
                        SharedPreferencesManager.setProfileImage(getApplicationContext(),null);
                        Intent refresh = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(refresh);//Start the same Activity
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Take me back",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_change_password:
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                mFragmentManager = getSupportFragmentManager();
                mFragmentManager.beginTransaction()
                        .replace(R.id.navigationContainer, fragment).addToBackStack(this.getClass().getSimpleName()).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;

    }

    @Override
    public void onCallBack(String a) {
        Toast.makeText(getApplication(), a, Toast.LENGTH_LONG).show();
        SharedPreferencesManager.setLocation(getApplicationContext(), a);
        Intent refresh = new Intent(this, NavigationActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("NavigationActivity", "resume");
    }


    @Override
    public void OnCallBackRefresh(String name, String email, String pic) {
        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final TextView txtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        final TextView txtOwnerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtOwnerName);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(getApplicationContext()))) {
            Picasso.with(NavigationActivity.this).load(SharedPreferencesManager.getProfileImage(getApplicationContext())).transform(new CircleTransform()).into(avatar);
        } else {
            AVATAR_URL = "http://lorempixel.com/200/200/people/1/";
            Picasso.with(NavigationActivity.this).load(R.drawable.no_image_icon).transform(new CircleTransform()).into(avatar);
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(getApplicationContext()))) {
            txtOwnerName.setText(SharedPreferencesManager.getUsername(getApplicationContext()));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(getApplicationContext()))) {
            txtEmail.setText(SharedPreferencesManager.getEmail(getApplicationContext()));
        }
    }
}