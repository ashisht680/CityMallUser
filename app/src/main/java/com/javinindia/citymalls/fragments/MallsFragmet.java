package com.javinindia.citymalls.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.javinindia.citymalls.activity.DirectionMapActivity;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallListResponseParsing;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.location.GPSTracker;
import com.javinindia.citymalls.location.GetCurrentGPSLocation;
import com.javinindia.citymalls.location.LocationService;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.MallAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Ashish on 08-09-2016.
 */
public class MallsFragmet extends BaseFragment implements View.OnClickListener, MallAdapter.MyClickListener, MallDetailTabBarFragment.OnCallBackMallFavListener {
    private MallAdapter adapter;
    private RecyclerView recyclerview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    private RequestQueue requestQueue;
    ArrayList arrayList;
    //GPSTracker gps;
    GetCurrentGPSLocation currentGPSLocation;

    double latitude = 0.0;
    double longitude = 0.0;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    AppCompatTextView txtDataNotFound;

    LocationService locationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.e("onCreateView", "onCreateView");
        initialize(view);
        methodLocation();
        return view;
    }

    private void methodLocation() {
        if (!hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(activity)) {
            Toast.makeText(activity, "Gps not enabled", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLocationMethod();
            }
        } else {
            getLocationMethod();
        }
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(activity)
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
                                status.startResolutionForResult(activity, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    private void getLocationMethod() {
        currentGPSLocation = new GetCurrentGPSLocation(activity);
        longitude = currentGPSLocation.getLatitude();
        longitude = currentGPSLocation.getLongitude();
        sendRequestOnMallListFeed(0, 10, latitude, longitude);
     /*   locationService = new LocationService(activity);
        latitude  =locationService.getLatitude();
        longitude= locationService.getLongitude();
        sendRequestOnMallListFeed(0, 10, latitude, longitude);*/
     /*   gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
             latitude = gps.getLatitude();
             longitude = gps.getLongitude();
            Log.e("gps mall", latitude + "---" + longitude);
            sendRequestOnMallListFeed(0, 10, latitude, longitude);
        } else {
            sendRequestOnMallListFeed(0, 10, latitude, longitude);
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        getLocationMethod();
                        // Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_LONG).show();
                        // methodNext();
                        // The user was asked to change settings, but chose not to
                        // finish();
                        break;
                    }
                    case Activity.RESULT_OK: {
                        //  Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_LONG).show();
                       getLocationMethod();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause", "onPause");
    }

    private void sendRequestOnMallListFeed(final int AstartLimit, final int AcountLimit, final double Alatitude, final double Alongitude) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MALL_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("limits mall", AstartLimit + "" + AcountLimit + " lat " + latitude + " long " + longitude);
                        MallListResponseParsing responseparsing = new MallListResponseParsing();
                        responseparsing.responseParseMethod(response);
                        Log.e("request mall", response);

                        int status = responseparsing.getStatus();
                        if (status == 1) {
                            arrayList = responseparsing.getMallDetailsArrayList();
                            if (arrayList.size() > 0) {
                                txtDataNotFound.setVisibility(View.GONE);
                                if (adapter.getData() != null && adapter.getData().size() > 0) {
                                    adapter.getData().addAll(arrayList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter.setData(arrayList);
                                    adapter.notifyDataSetChanged();

                                }
                                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {
                                        arrayList.removeAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                        adapter.setData(arrayList);
                                        if (arrayList.size() > 0) {
                                        } else {
                                            getLocationMethod();
                                        }
                                    }
                                });
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                txtDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noInternetToast(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("startlimit", String.valueOf(AstartLimit));
                params.put("countlimit", String.valueOf(AcountLimit));
                params.put("lat", String.valueOf(Alatitude));
                params.put("long", String.valueOf(Alongitude));
                params.put("search", SharedPreferencesManager.getCity(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void initialize(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.corner_swipe_refresh_layout);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewMall);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        adapter = new MallAdapter(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.addOnScrollListener(new mallScrollListener());
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(MallsFragmet.this);
        txtDataNotFound = (AppCompatTextView) view.findViewById(R.id.txtDataNotFound);
        txtDataNotFound.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.mall_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    @Override
    public void onItemClick(int position, MallDetail model) {
        int pos = position;
        String mallId = model.getId().trim();
        String mallName = model.getMallName().trim();
        String mallRating = model.getRating().trim();
        double distance = model.getDistance();
        String mallPic = model.getMallPic().trim();
        int favStatus = model.getFavStatus();
        int totalOffer = model.getOfferCount();

        MallDetailTabBarFragment fragment1 = new MallDetailTabBarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        bundle.putString("mallId", mallId);
        bundle.putString("mallName", mallName);
        bundle.putString("mallRating", mallRating);
        bundle.putDouble("distance", distance);
        bundle.putString("mallPic", mallPic);
        bundle.putInt("favStatus", favStatus);
        bundle.putInt("totalOffer", totalOffer);
        fragment1.setArguments(bundle);
        SharedPreferencesManager.setMAllId(activity, mallId);
        fragment1.setMyCallBackMallFavListener(this);
        Constants.VIEW_PAGER_MALL_CURRENT_POSITION = 1;
        callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.navigationContainer);
    }

    @Override
    public void onFavourite(int position, MallDetail modal) {
        int fav = modal.getFavStatus();
        String mallId = modal.getId().trim();
        String uId = SharedPreferencesManager.getUserID(activity);
        if (fav == 0) {
            String Yes = "1";
            favHitOnApi(uId, mallId, Yes, position);
        } else {
            String No = "0";
            favHitOnApi(uId, mallId, No, position);
        }
    }

    @Override
    public void onMallNameClick(int position, MallDetail modal) {
        int pos = position;
        String mallId = modal.getId().trim();
        String mallName = modal.getMallName().trim();
        String mallRating = modal.getRating().trim();
        double distance = modal.getDistance();
        String mallPic = modal.getMallPic().trim();
        int favStatus = modal.getFavStatus();
        int totalOffer = modal.getOfferCount();

        MallDetailTabBarFragment fragment1 = new MallDetailTabBarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        bundle.putString("mallId", mallId);
        bundle.putString("mallName", mallName);
        bundle.putString("mallRating", mallRating);
        bundle.putDouble("distance", distance);
        bundle.putString("mallPic", mallPic);
        bundle.putInt("favStatus", favStatus);
        bundle.putInt("totalOffer", totalOffer);
        fragment1.setArguments(bundle);
        SharedPreferencesManager.setMAllId(activity, mallId);
        fragment1.setMyCallBackMallFavListener(this);
        Constants.VIEW_PAGER_MALL_CURRENT_POSITION = 0;
        callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.navigationContainer);
    }

    @Override
    public void onDirectionClick(int position, MallDetail modal) {
        String mallLat = modal.getMallLat().trim();
        String mallLong = modal.getMallLong().trim();
        String mallName = modal.getMallName().trim();
        Intent refresh = new Intent(activity, DirectionMapActivity.class);
        Log.e("mall",mallLat+"\t"+mallLong+"\t"+mallName);
        refresh.putExtra("mallLat", mallLat);
        refresh.putExtra("mallLong", mallLong);
        refresh.putExtra("mallName", mallName);
        startActivity(refresh);
    }

    @Override
    public void onAddressClick(int position, MallDetail mallDetail) {
        String mallName = mallDetail.getMallName().trim();
        String mallAddress = mallDetail.getMallAddress().trim();
        String mallLandmark = mallDetail.getMallLandmark().trim();
        String state = mallDetail.getState().trim();
        String city = mallDetail.getCity().trim();
        String pinCode = mallDetail.getPinCode().trim();
        double distance = mallDetail.getDistance();
        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(mallAddress)) {
            data.add(mallAddress);
        }
        if (!TextUtils.isEmpty(mallLandmark)) {
            data.add(mallLandmark);
        }
        if (!TextUtils.isEmpty(city)) {
            data.add(city);
        }
        if (!TextUtils.isEmpty(state)) {
            data.add(state);
        }
        if (!TextUtils.isEmpty(pinCode)) {
            data.add(pinCode);
        }

        if (data.size() > 0) {
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            showNewDialog(mallName, test);
        } else {
            // viewHolder.txtAddress.setText("Address: Not found");
        }
    }

    private void favHitOnApi(final String uId, final String mallId, final String no, final int position) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_FAVORITE_MALL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("fav", response);
                        JSONObject jsonObject = null;
                        String userid = null, msg = null, username = null, password = null, mallid = null, otp = null;
                        int status = 0, action = 0;
                        try {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.has("status"))
                                status = jsonObject.optInt("status");
                            if (jsonObject.has("msg"))
                                msg = jsonObject.optString("msg");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status == 1) {
                            if (jsonObject.has("userid"))
                                userid = jsonObject.optString("userid");
                            if (jsonObject.has("mallid"))
                                mallid = jsonObject.optString("mallid");
                            if (jsonObject.has("action"))
                                action = jsonObject.optInt("action");
                            List list = adapter.getData();
                            MallDetail wd = (MallDetail) list.get(position);
                            wd.setFavStatus(action);
                            adapter.notifyItemChanged(position);

                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod(msg);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noInternetToast(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", uId);
                params.put("mallid", mallId);
                params.put("status", no);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void OnCallBackMallfav(int pos, int action) {
        List list = adapter.getData();
        MallDetail wd = (MallDetail) list.get(pos);
        wd.setFavStatus(action);
        adapter.notifyItemChanged(pos);
    }


    public class mallScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerLayoutManager.getItemCount();

            int visibleThreshold = ((totalItemCount / 2) < 20) ? totalItemCount / 2 : 20;
            int firstVisibleItem = recyclerLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > startLimit) {
                    loading = false;
                    startLimit = totalItemCount;
                }
            } else {
                int nonVisibleItemCounts = totalItemCount - visibleItemCount;
                int effectiveVisibleThreshold = firstVisibleItem + visibleThreshold;

                if (nonVisibleItemCounts <= effectiveVisibleThreshold) {
                    startLimit = startLimit + 1;
                    countLimit = 10;

                    showLoader();

                    sendRequestOnMallListFeed(startLimit, countLimit, latitude, longitude);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
