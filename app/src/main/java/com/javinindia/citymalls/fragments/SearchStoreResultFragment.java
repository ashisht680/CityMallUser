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
import com.javinindia.citymalls.apiparsing.storeInMallParsing.ShopData;
import com.javinindia.citymalls.apiparsing.storeInMallParsing.StoreInMallResponse;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.MallStoreAdaptar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ashish on 15-11-2016.
 */
public class SearchStoreResultFragment extends BaseFragment implements View.OnClickListener, MallStoreAdaptar.MyClickListener,StoreTabsFragment.OnCallBackShopFavListener,TextWatcher {
    private MallStoreAdaptar adapter;
    private RecyclerView recyclerview;
    private int startLimit = 0;
    private int countLimit = 5000;
    private boolean loading = true;
    private RequestQueue requestQueue;
    ArrayList<ShopData> arrayList=new ArrayList<ShopData>();
    String catName;
    AppCompatTextView txtDataNotFound,txtTitleStore;
    LinearLayout llSearch;
    AppCompatEditText etSearch;
    ImageView imgSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        catName = getArguments().getString("catName");
        getArguments().remove("catName");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        sendRequestOnStoreInMallListFeed(catName);
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
        textView.setText("Search Store");
        textView.setTextColor(activity.getResources().getColor(android.R.color.white));
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }
    private void sendRequestOnStoreInMallListFeed(final String title) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEARCH_SHOP_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("title ", title);
                        StoreInMallResponse responseparsing = new StoreInMallResponse();
                        responseparsing.responseParseMethod(response);
                        Log.e("request ", response);
                        if (response.length() != 0) {
                            int status = responseparsing.getStatus();
                            if (status == 1) {
                                arrayList = responseparsing.getShopDataArrayList();
                                if (arrayList.size() > 0) {
                                    txtDataNotFound.setVisibility(View.GONE);
                                   // llSearch.setVisibility(View.VISIBLE);
                                    if (adapter.getData() != null && adapter.getData().size() > 0) {
                                        adapter.getData().addAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.setData(arrayList);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            } else {
                                txtDataNotFound.setVisibility(View.VISIBLE);
                              //  llSearch.setVisibility(View.GONE);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("sname",title);
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
        txtTitleStore = (AppCompatTextView)view.findViewById(R.id.txtTitleStore);
        txtTitleStore.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        imgSearch  =(ImageView)view.findViewById(R.id.imgSearch);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewStoresSearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        adapter = new MallStoreAdaptar(activity);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutMangerDestination);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(SearchStoreResultFragment.this);

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
        return R.layout.search_store_result_layout;
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
                    sendRequestOnStoreInMallListFeed(data);
                } else {
                    String data = etSearch.getText().toString().trim();
                    sendRequestOnStoreInMallListFeed(data);
                }
                break;
        }
    }

    @Override
    public void onItemClick(int position, ShopData model) {
        String shopId = model.getId().trim();
        SharedPreferencesManager.setShopId(activity, shopId);
        String shopName = model.getStoreName().trim();
        String shopPic = model.getBanner().trim();
        String shopRating = model.getRating().trim();
        String mallName = model.getMallName().trim();
        int totalOffers = model.getShopOfferCount();
        int favStatus = model.getFavStatus();
        StoreTabsFragment fragment = new StoreTabsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("shopId", shopId);
        bundle.putString("shopName", shopName);
        bundle.putString("shopPic", shopPic);
        bundle.putString("mallName", mallName);
        bundle.putString("shopRating", shopRating);
        bundle.putInt("totalOffers", totalOffers);
        bundle.putInt("favStatus", favStatus);
        fragment.setArguments(bundle);
        fragment.setMyCallBackShopFavListener(this);
        callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.navigationContainer);
    }

    @Override
    public void onShopFavClick(int position, ShopData model) {
        int fav = model.getFavStatus();
        String shopId = model.getId().trim();
        String uId = SharedPreferencesManager.getUserID(activity);
        if (fav == 0) {
            String Yes = "1";
            favHitOnApi(uId, shopId, Yes, position);
        } else {
            String No = "0";
            favHitOnApi(uId, shopId, No, position);
        }
    }

    private void favHitOnApi(final String uId, final String shopId, final String yes, final int position) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_FAVORITE_SHOP_URL,
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
                            if (jsonObject.has("shopid"))
                                mallid = jsonObject.optString("shopid");
                            if (jsonObject.has("action"))
                                action = jsonObject.optInt("action");
                            List list = adapter.getData();
                            ShopData wd = (ShopData) list.get(position);
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
                params.put("shopid", shopId);
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
    public void OnCallBackShopFav(int pos, int action) {
        List list = adapter.getData();
        ShopData wd = (ShopData) list.get(pos);
        wd.setFavStatus(action);
        adapter.notifyItemChanged(pos);
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
            sendRequestOnStoreInMallListFeed(text);
        } else {
            String data = etSearch.getText().toString().trim();
            sendRequestOnStoreInMallListFeed(text);
        }
       // adapter.filter(text);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}
