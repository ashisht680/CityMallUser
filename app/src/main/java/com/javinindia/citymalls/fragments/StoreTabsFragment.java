package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
 * Created by Ashish on 29-09-2016.
 */
public class StoreTabsFragment extends BaseFragment {
    private RequestQueue requestQueue;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    AppCompatTextView txtStoreName, txtRating, txtStoreMall, txtOffers,txtStoreAddress;
    ImageView imgStore;
    ProgressBar progress;
    CheckBox chkImageShop;
    RatingBar ratingBar;
    String shopId;
    String shopName,address;
    String shopPic;
    String shopRating;
    String mallName;
    int totalOffers = 0;
    int favStatus = 0;
    int position;

    private OnCallBackShopFavListener onCallBackShopFavListener;

    public interface OnCallBackShopFavListener {
        void OnCallBackShopFav(int pos, int action);
    }

    public void setMyCallBackShopFavListener(OnCallBackShopFavListener callback) {
        this.onCallBackShopFavListener = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        position = getArguments().getInt("position");
        shopId = getArguments().getString("shopId");
        shopName = getArguments().getString("shopName");
        mallName = getArguments().getString("mallName");
        shopRating = getArguments().getString("shopRating");
        shopPic = getArguments().getString("shopPic");
        favStatus = getArguments().getInt("favStatus");
        totalOffers = getArguments().getInt("totalOffers");
        address = getArguments().getString("address");
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
        initToolbar(view);
        intialize(view);
        setDateMethod();
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
        if (!TextUtils.isEmpty(shopName)){
            textView.setText(shopName);
        }else {
            textView.setText("Store");
        }
        textView.setTextColor(activity.getResources().getColor(android.R.color.white));
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void setDateMethod() {
        if (!TextUtils.isEmpty(shopName)) {
            txtStoreName.setText(Html.fromHtml(shopName));
        }
        if (!TextUtils.isEmpty(shopPic)) {
            Utility.imageLoadGlideLibrary(activity, progress, imgStore, shopPic);
        } else {
            imgStore.setImageResource(R.drawable.no_image_icon);
        }
        if (!TextUtils.isEmpty(mallName)) {
            txtStoreMall.setText(Html.fromHtml(mallName));
        } else {
            txtStoreMall.setText("No Mall Detail");
        }
        if (totalOffers != 0) {
            if (totalOffers == 1) {
                txtOffers.setText(totalOffers + " offer");
            } else {
                txtOffers.setText(totalOffers + " offers");
            }
        } else {
            txtOffers.setText("No offers");
        }

        if (!TextUtils.isEmpty(shopRating)) {
            txtRating.setText("Rating: " + shopRating + "/5");
            ratingBar.setRating(Float.parseFloat(shopRating));
        } else {
            txtRating.setText("Rating: 0/5");
            ratingBar.setRating((float) 0.0);
        }

        if (!TextUtils.isEmpty(address)) {
            txtStoreAddress.setText(Html.fromHtml(address));
        }
        if (favStatus == 1) {
            chkImageShop.setChecked(true);
        } else {
            chkImageShop.setChecked(false);
        }

        chkImageShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uId = SharedPreferencesManager.getUserID(activity);
                if (favStatus == 0) {
                    String Yes = "1";
                    favHitOnApi(uId, shopId, Yes);
                } else {
                    String No = "0";
                    favHitOnApi(uId, shopId, No);
                }
            }
        });
    }

    private void intialize(View view) {
        txtStoreName = (AppCompatTextView) view.findViewById(R.id.txtStoreName);
        txtStoreName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtRating = (AppCompatTextView) view.findViewById(R.id.txtRating);
        txtRating.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtStoreMall = (AppCompatTextView) view.findViewById(R.id.txtStoreMall);
        txtStoreMall.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOffers = (AppCompatTextView) view.findViewById(R.id.txtOffers);
        txtOffers.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtStoreAddress = (AppCompatTextView) view.findViewById(R.id.txtStoreAddress);
        txtStoreAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        imgStore = (ImageView) view.findViewById(R.id.imgStore);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        chkImageShop = (CheckBox) view.findViewById(R.id.chkImageShop);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //   viewPager.setCurrentItem(Constants.VIEW_PAGER_CURRENT_POSITION);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    private void favHitOnApi(final String uId, final String shopId, final String yes) {
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
                            onCallBackShopFavListener.OnCallBackShopFav(position, action);
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
    protected int getFragmentLayout() {
        return R.layout.store_tab_layout;
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
        adapter.addFragment(new StoreOffersFragment(), "Offers");
        //  adapter.addFragment(new StoreEventsFragment(), "Events");
        viewPager.setAdapter(adapter);

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}
