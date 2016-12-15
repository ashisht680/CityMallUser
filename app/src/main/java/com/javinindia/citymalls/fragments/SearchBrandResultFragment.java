package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.javinindia.citymalls.apiparsing.offerListparsing.DetailsList;
import com.javinindia.citymalls.apiparsing.offerListparsing.OfferListResponseparsing;
import com.javinindia.citymalls.apiparsing.storeInMallParsing.ShopData;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
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
 * Created by Ashish on 15-11-2016.
 */
public class SearchBrandResultFragment extends BaseFragment implements View.OnClickListener, OfferAdaptar.MyClickListener, TextWatcher,OfferPostFragment.OnCallBackOfferDetailFavListener {

    private RecyclerView recyclerview;
    private int startLimit = 0;
    private int countLimit = 5000;
    private boolean loading = true;
    private RequestQueue requestQueue;
    private OfferAdaptar adapter;
    ArrayList<DetailsList> arrayList=new ArrayList<DetailsList>();
    AppCompatTextView txtDataNotFound,txtTitleBrand;
    LinearLayout llSearch;
    AppCompatEditText etSearch;
    ImageView imgSearch;
    String brand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        brand = getArguments().getString("brand");
        getArguments().remove("brand");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        sendRequestOnReplyFeed(brand);
        return view;
    }

    private void initToolbar(View view) {
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(null);
        AppCompatTextView textView =(AppCompatTextView)view.findViewById(R.id.tittle) ;
        textView.setText("Search Brand");
        textView.setTextColor(activity.getResources().getColor(android.R.color.white));
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void sendRequestOnReplyFeed(final String data) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEARCH_BRAND_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        OfferListResponseparsing responseparsing = new OfferListResponseparsing();
                        Log.e("request", response);
                        responseparsing.responseParseMethod(response);

                        int status = responseparsing.getStatus();
                        if(status==1){
                            arrayList = responseparsing.getDetailsListArrayList();
                            if(arrayList.size()>0){
                                txtDataNotFound.setVisibility(View.GONE);
                             //   llSearch.setVisibility(View.VISIBLE);
                                if (adapter.getData() != null && adapter.getData().size() > 0) {
                                    adapter.getData().addAll(arrayList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter.setData(arrayList);
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        }else {
                            txtDataNotFound.setVisibility(View.VISIBLE);
                         //   llSearch.setVisibility(View.GONE);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", SharedPreferencesManager.getUserID(activity));
                params.put("bname", data);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }
    private void initialize(View view) {
        txtTitleBrand = (AppCompatTextView)view.findViewById(R.id.txtTitleBrand);
        txtTitleBrand.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        imgSearch  =(ImageView)view.findViewById(R.id.imgSearch);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewMallOffer);
        adapter = new OfferAdaptar(activity,0);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(SearchBrandResultFragment.this);

        txtDataNotFound = (AppCompatTextView) view.findViewById(R.id.txtDataNotFound);
        txtDataNotFound.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        llSearch = (LinearLayout) view.findViewById(R.id.llSearch);

        etSearch = (AppCompatEditText)view.findViewById(R.id.etSearch);
        etSearch.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etSearch.addTextChangedListener(this);

        imgSearch.setOnClickListener(this);
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.search_brand_result_layout;
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
            case R.id.imgSearch:
                if (arrayList.size() > 0) {
                    arrayList.removeAll(arrayList);
                    adapter.notifyDataSetChanged();
                    adapter.setData(arrayList);
                    String data = etSearch.getText().toString().trim();
                    sendRequestOnReplyFeed(data);
                } else {
                    String data = etSearch.getText().toString().trim();
                    sendRequestOnReplyFeed(data);
                }
                break;
        }
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString().toLowerCase(Locale.getDefault());
        Log.e("value",text);
        if (arrayList.size() > 0) {
            arrayList.removeAll(arrayList);
            adapter.notifyDataSetChanged();
            adapter.setData(arrayList);
            String data = etSearch.getText().toString().trim();
            sendRequestOnReplyFeed(text);
        } else {
            String data = etSearch.getText().toString().trim();
            sendRequestOnReplyFeed(text);
        }
    }


    @Override
    public void OnCallBackOfferDetailFav(int pos, int action) {
        List list = adapter.getData();
        DetailsList wd = (DetailsList) list.get(pos);
        wd.setFavStatus(action);
        adapter.notifyItemChanged(pos);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}

