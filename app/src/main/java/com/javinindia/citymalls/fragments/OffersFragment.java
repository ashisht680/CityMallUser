package com.javinindia.citymalls.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;
import com.javinindia.citymalls.apiparsing.offerListparsing.DetailsList;
import com.javinindia.citymalls.apiparsing.offerListparsing.OfferDetails;
import com.javinindia.citymalls.apiparsing.offerListparsing.OfferListResponseparsing;
import com.javinindia.citymalls.apiparsing.storeInMallParsing.ShopData;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.location.GPSTracker;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
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
 * Created by Ashish on 08-09-2016.
 */
public class OffersFragment extends BaseFragment implements OfferAdaptar.MyClickListener, OfferPostFragment.OnCallBackOfferDetailFavListener {

    private RecyclerView recyclerview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private OfferAdaptar adapter;
    ArrayList arrayList;
    AppCompatTextView txtDataNotFound;
   /* GPSTracker gps;
    double latitude = 0.0;
    double longitude = 0.0;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.getSupportActionBar().show();
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
        initialize(view);
        sendRequestOnReplyFeed(0, 10);
        return view;
    }

    private void sendRequestOnReplyFeed(final int AstartLimit, final int AcountLimit) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OFFER_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("request offer", response);
                      //  Log.e("limits offer", AstartLimit + "" + AcountLimit);
                        OfferListResponseparsing responseparsing = new OfferListResponseparsing();
                        responseparsing.responseParseMethod(response);
                        int status = responseparsing.getStatus();
                        if (status == 1) {
                            arrayList = responseparsing.getDetailsListArrayList();
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
                                            // getLocationMethod();
                                            sendRequestOnReplyFeed(0, 5);
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
                String uid = SharedPreferencesManager.getUserID(activity);
                String search = SharedPreferencesManager.getCity(activity);
                Log.e("ofer search",uid+"\t"+search);
                //uid=1&startlimit=0&countlimit=10&search=New%20Delhi
                params.put("uid", uid);
                params.put("startlimit", String.valueOf(AstartLimit));
                params.put("countlimit", String.valueOf(AcountLimit));
                params.put("search",search);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    private void initialize(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.offer_swipe_refresh_layout);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewOffer);
        adapter = new OfferAdaptar(activity,0);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.addOnScrollListener(new replyScrollListener());
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(OffersFragment.this);
        txtDataNotFound = (AppCompatTextView) view.findViewById(R.id.txtDataNotFound);
        txtDataNotFound.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.offer_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }


    @Override
    public void onOfferItemClick(int position, DetailsList detailsList) {
        String shopNewAddress = "";
        String brandName = detailsList.getOfferBrandDetails().getBrandName().trim();
        String brandPic = detailsList.getOfferBrandDetails().getBrandLogo().trim();
        String shopName = detailsList.getOfferShopDetails().getShopName().trim();
        String shopPic = detailsList.getOfferShopDetails().getShopProfilePic().trim();
        String mallName = detailsList.getOfferMallDetails().getMallName().trim();
        String offerId = detailsList.getOfferDetails().getOfferId().trim();
        String shopId = detailsList.getOfferShopDetails().getShopId().trim();
        String offerRating = "4";
        String offerPic = detailsList.getOfferDetails().getOfferBanner().trim();
        String offerTitle = detailsList.getOfferDetails().getOfferTitle().trim();
        String offerCategory = detailsList.getOfferDetails().getOfferCategory();
        String offerSubCategory = detailsList.getOfferDetails().getOfferSubcategory().trim();
        String offerPercentType = detailsList.getOfferDetails().getOfferPercentageType().trim();
        String offerPercentage = detailsList.getOfferDetails().getOfferPercentage().trim();
        String offerActualPrice = detailsList.getOfferDetails().getOfferActualPrice().trim();
        String offerDiscountPrice = detailsList.getOfferDetails().getOfferDiscountedPrice().trim();
        String offerStartDate = detailsList.getOfferDetails().getOfferOpenDate().trim();
        String offerCloseDate = detailsList.getOfferDetails().getOfferCloseDate().trim();
        String offerDescription = detailsList.getOfferDetails().getOfferDescription().trim();
        String shopOpenTime = detailsList.getOfferShopDetails().getShopOpenTime().trim();
        String shopCloseTime = detailsList.getOfferShopDetails().getShopCloseTime().trim();
        String shopNo = detailsList.getOfferShopDetails().getShopNo().trim();
        String floor = detailsList.getOfferShopDetails().getShopFloorNo().trim();
        int favStatus = detailsList.getFavStatus();
        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(shopNo)){
            data.add(shopNo);
        }
        if (!TextUtils.isEmpty(floor)){
            data.add(floor);
        }

        if (data.size()>0){
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            shopNewAddress=test;
        }

        OfferPostFragment fragment1 = new OfferPostFragment();

        Bundle bundle = new Bundle();
        bundle.putString("shopPic",shopPic);
        bundle.putString("shopNewAddress", shopNewAddress);
        bundle.putString("brandName", brandName);
        bundle.putString("brandPic", brandPic);
        bundle.putString("shopName", shopName);
        bundle.putString("mallName", mallName);
        bundle.putString("offerRating", offerRating);
        bundle.putString("offerPic", offerPic);
        bundle.putString("offerTitle", offerTitle);
        bundle.putString("offerCategory", offerCategory);
        bundle.putString("offerSubCategory", offerSubCategory);
        bundle.putString("offerPercentType", offerPercentType);
        bundle.putString("offerPercentage", offerPercentage);
        bundle.putString("offerActualPrice", offerActualPrice);
        bundle.putString("offerDiscountPrice", offerDiscountPrice);
        bundle.putString("offerStartDate", offerStartDate);
        bundle.putString("offerCloseDate", offerCloseDate);
        bundle.putString("offerDescription", offerDescription);
        bundle.putString("shopOpenTime", shopOpenTime);
        bundle.putString("shopCloseTime", shopCloseTime);
        bundle.putString("offerId", offerId);
        bundle.putString("shopId", shopId);
        bundle.putInt("favStatus", favStatus);
        bundle.putInt("position", position);
        fragment1.setArguments(bundle);
        fragment1.setMyCallBackOfferDetailFavListener(this);
        callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.navigationContainer);
    }

    @Override
    public void onFavoriteClick(int position, DetailsList detailsList) {
        int fav = detailsList.getFavStatus();
        String offerId = detailsList.getOfferDetails().getOfferId().trim();
        String uId = SharedPreferencesManager.getUserID(activity);
        if (fav == 0) {
            String Yes = "1";
            favHitOnApi(uId, offerId, Yes, position);
        } else {
            String No = "0";
            favHitOnApi(uId, offerId, No, position);
        }
    }

    private void favHitOnApi(final String uId, final String offerId, final String yes, final int position) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_FAVORITE_OFFER_URL,
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
                            if (jsonObject.has("offerid"))
                                mallid = jsonObject.optString("offerid");
                            if (jsonObject.has("action"))
                                action = jsonObject.optInt("action");
                            List list = adapter.getData();
                            DetailsList wd = (DetailsList) list.get(position);
                            wd.setFavStatus(action);
                            adapter.notifyItemChanged(position);

                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                               // showDialogMethod(msg);
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
                params.put("offerid", offerId);
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
    public void OnCallBackOfferDetailFav(int pos, int action) {
        List list = adapter.getData();
        DetailsList wd = (DetailsList) list.get(pos);
        wd.setFavStatus(action);
        adapter.notifyItemChanged(pos);
    }

    public class replyScrollListener extends RecyclerView.OnScrollListener {
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

                    sendRequestOnReplyFeed(startLimit, countLimit);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
