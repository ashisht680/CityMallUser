package com.javinindia.citymalls.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.activity.DirectionMapActivity;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallListResponseParsing;
import com.javinindia.citymalls.apiparsing.offerListparsing.DetailsList;
import com.javinindia.citymalls.apiparsing.offerListparsing.OfferListResponseparsing;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.location.GPSTracker;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.MallAdapter;
import com.javinindia.citymalls.recyclerview.OfferAdaptar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ashish on 15-11-2016.
 */
public class SearchMallFragments extends BaseFragment implements View.OnClickListener, TextWatcher, MallAdapter.MyClickListener, MallDetailTabBarFragment.OnCallBackMallFavListener {
    private RecyclerView recyclerview;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private MallAdapter adapter;
    ArrayList<MallDetail> arrayList = new ArrayList<MallDetail>();
    AppCompatTextView txtDataNotFound, txtTitleBrand;
    LinearLayout llSearch;
    AppCompatEditText etSearch;
    ImageView imgSearch;
    private int startLimit = 0;
    private int countLimit = 500;
    String mall = "";
    GPSTracker gps;
    double latitude = 0.0;
    double longitude = 0.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize(view);
        if (TextUtils.isEmpty(SharedPreferencesManager.getCity(activity)) && !SharedPreferencesManager.getCity(activity).equals(null)) {
            mall = SharedPreferencesManager.getCity(activity);
        } else {
            mall = "New Delhi";
        }
        getLocationMethod();
       // sendRequestOnReplyFeed(mall);
        return view;
    }

    private void getLocationMethod() {
        gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("gps mall", latitude + "---" + longitude);
            sendRequestOnReplyFeed(0, 500, latitude, longitude);
        } else {
            sendRequestOnReplyFeed(0, 500, latitude, longitude);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.search_mall_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    private void initialize(View view) {
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewMallSearch);
        adapter = new MallAdapter(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(SearchMallFragments.this);

        txtDataNotFound = (AppCompatTextView) view.findViewById(R.id.txtDataNotFound);
        txtDataNotFound.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);

        etSearch = (AppCompatEditText) view.findViewById(R.id.etSearch);
        etSearch.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etSearch.addTextChangedListener(this);
        imgSearch.setOnClickListener(this);
    }

    private void sendRequestOnReplyFeed(final int AstartLimit, final int AcountLimit, final double Alatitude, final double Alongitude) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEARCH_MALL_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MallListResponseParsing responseparsing = new MallListResponseParsing();
                        responseparsing.responseParseMethod(response);
                        Log.e("request search", response);
                        int status = responseparsing.getStatus();
                        if (status == 1) {
                            if (responseparsing.getMallDetailsArrayList().size()>0){
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

                                } else {
                                    txtDataNotFound.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //userid=1&name=p&startlimit=0&countlimit=500&lat=3.5&long=2.7
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("startlimit", String.valueOf(AstartLimit));
                params.put("countlimit", String.valueOf(AcountLimit));
                params.put("name", mall);
                params.put("lat", String.valueOf(Alatitude));
                params.put("long", String.valueOf(Alongitude));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSearch:
                //  showDialogMethod("Updating database please try after some time");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString().toLowerCase(Locale.getDefault());
        if (arrayList.size() > 0) {
            arrayList.removeAll(arrayList);
            adapter.notifyDataSetChanged();
            adapter.setData(arrayList);
            String data = etSearch.getText().toString().trim();
            sendRequestOnReplyFeed(0, 5, latitude, longitude);
        } else {
            String data = etSearch.getText().toString().trim();
            sendRequestOnReplyFeed(0, 5, latitude, longitude);
        }
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
                              //  showDialogMethod(msg);
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
    public void onItemClick(int position, MallDetail model) {
        int pos = position;
        String mallId = model.getId().trim();
        String mallName = model.getMallName().trim();
        String mallRating = model.getRating().trim();
        double distance = model.getDistance();
        String mallPic = model.getMallPic().trim();
        int favStatus = model.getFavStatus();
        int totalOffer = model.getOfferCount();
        String address="";

        String mallLandmark = model.getMallLandmark().trim();
        String city = model.getCity().trim();
        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(mallLandmark)) {
            data.add(mallLandmark);
        }
        if (!TextUtils.isEmpty(city)) {
            data.add(city);
        }

        if (data.size() > 0) {
            String str = Arrays.toString(data.toArray());
            address = str.replaceAll("[\\[\\](){}]", "");
        }

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
        bundle.putString("address", address);
        fragment1.setArguments(bundle);
        SharedPreferencesManager.setMAllId(activity, mallId);
        fragment1.setMyCallBackMallFavListener(this);
        Constants.VIEW_PAGER_MALL_CURRENT_POSITION = 1;
        callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.navigationContainer);
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
        String address="";

        String mallLandmark = modal.getMallLandmark().trim();
        String city = modal.getCity().trim();
        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(mallLandmark)) {
            data.add(mallLandmark);
        }
        if (!TextUtils.isEmpty(city)) {
            data.add(city);
        }

        if (data.size() > 0) {
            String str = Arrays.toString(data.toArray());
            address = str.replaceAll("[\\[\\](){}]", "");
        }

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
        bundle.putString("address", address);
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
        Log.e("direction",mallLat+"\t"+mallLong);
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Double.parseDouble(mallLat),  Double.parseDouble(mallLong),mallName);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        activity.startActivity(intent);
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

    @Override
    public void OnCallBackMallfav(int pos, int action) {
        List list = adapter.getData();
        MallDetail wd = (MallDetail) list.get(pos);
        wd.setFavStatus(action);
        adapter.notifyItemChanged(pos);
    }
}
