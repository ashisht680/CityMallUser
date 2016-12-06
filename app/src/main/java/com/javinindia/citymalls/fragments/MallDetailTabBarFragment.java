package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.ViewPagerAdapter;
import com.javinindia.citymalls.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 30-09-2016.
 */
public class MallDetailTabBarFragment extends BaseFragment {
    private RequestQueue requestQueue;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int pos;
    String mallId;
    String mallName;
    String mallRating;
    double distance;
    String mallPic;
    int favStatus;
    int totalOffer;

    AppCompatTextView txtMallName, txtRating, txtMallDistance, txtOffers;
    ImageView imgFavourateMalls;
    CheckBox chkImageMall;
    ProgressBar progress;
    RatingBar ratingBar;

    private OnCallBackMallFavListener onCallBackRefreshListener;

    public interface OnCallBackMallFavListener {
        void OnCallBackMallfav(int pos, int action);
    }

    public void setMyCallBackMallFavListener(OnCallBackMallFavListener callback) {
        this.onCallBackRefreshListener = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("pos");
        mallId = getArguments().getString("mallId");
        mallName = getArguments().getString("mallName");
        mallRating = getArguments().getString("mallRating");
        distance = getArguments().getDouble("distance");
        mallPic = getArguments().getString("mallPic");
        favStatus = getArguments().getInt("favStatus");
        totalOffer = getArguments().getInt("totalOffer");
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
        txtMallName = (AppCompatTextView) view.findViewById(R.id.txtMallName);
        txtMallName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtRating = (AppCompatTextView) view.findViewById(R.id.txtRating);
        txtRating.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMallDistance = (AppCompatTextView) view.findViewById(R.id.txtMallDistance);
        txtMallDistance.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOffers = (AppCompatTextView) view.findViewById(R.id.txtOffers);
        txtOffers.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        chkImageMall = (CheckBox) view.findViewById(R.id.chkImageMall);
        imgFavourateMalls = (ImageView) view.findViewById(R.id.imgFavourateMalls);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        imgFavourateMalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });


        if (!TextUtils.isEmpty(mallName)) {
            txtMallName.setText(Html.fromHtml(mallName));
        }
        if (!TextUtils.isEmpty(mallRating)) {
            ratingBar.setRating(Float.parseFloat(mallRating));
            txtRating.setText("Rating: " + mallRating + "/5");
        } else {
            txtRating.setText("Rating: 0/5");
            ratingBar.setRating((float) 0.0);
        }
        if (distance != 0) {
            txtMallDistance.setText("Distance: " + (int) distance + "km");
        } else {
            txtMallDistance.setText("Distance: Near you");
        }
        if (totalOffer != 0) {
            if (totalOffer == 1) {
                txtOffers.setText(Html.fromHtml(totalOffer + " Offer"));
            } else {
                txtOffers.setText(Html.fromHtml(totalOffer + " Offers"));
            }
        } else {
            txtOffers.setText("No offers");
        }
        if (!TextUtils.isEmpty(mallPic)) {
            Utility.imageLoadGlideLibrary(activity, progress, imgFavourateMalls, mallPic);
        }else {
            imgFavourateMalls.setImageResource(R.drawable.no_image_icon);
        }
        if (favStatus == 1) {
            chkImageMall.setChecked(true);
        } else {
            chkImageMall.setChecked(false);
        }

        chkImageMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uId = SharedPreferencesManager.getUserID(activity);
                if (favStatus == 0) {
                    String Yes = "1";
                    favHitOnApi(uId, mallId, Yes);
                } else {
                    String No = "0";
                    favHitOnApi(uId, mallId, No);
                }
            }
        });


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(Constants.VIEW_PAGER_MALL_CURRENT_POSITION);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void favHitOnApi(final String uId, final String mallId, final String yes) {
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

                            onCallBackRefreshListener.OnCallBackMallfav(pos, action);

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
                params.put("status", yes);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.mall_detail_tab_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MallsStoreListFragment(), "Stores");
        adapter.addFragment(new MallsOffersListFragment(), "Offers");
        //  adapter.addFragment(new MallEventsListFragment(), "Events");
        viewPager.setAdapter(adapter);

    }
}
