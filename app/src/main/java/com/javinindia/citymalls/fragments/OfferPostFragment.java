package com.javinindia.citymalls.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
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
import com.javinindia.citymalls.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OfferPostFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    ImageView imgBrand, imgOffer;
    RatingBar ratingBar;
    AppCompatTextView txtOfferBrandNamePost, txtRating, txtMallNamePost, txtOfferPercentage, txtSubcategory,
            txtOfferDate, txtShopTiming, txtOfferDiscription, txtOfferActualPrice, txtOfferTitle,txtAddress,txtOfferDiscountPrice;
    AppCompatButton btnRate;
    CheckBox chkImageMall;
    ProgressBar progressBar;

    String brandName, brandPic, shopName, mallName, offerRating, offerPic, offerTitle, offerCategory, offerSubCategory, offerPercentType,
            offerPercentage, offerActualPrice, offerDiscountPr, offerStartDate, offerCloseDate, offerDescription, shopOpenTime, shopCloseTime
            ,offerId,shopId,shopNewAddress,shopPic;
    int favStatus=0,position;



    private OnCallBackOfferDetailFavListener onCallBackOfferDetailFavListener;

    public interface OnCallBackOfferDetailFavListener {
        void OnCallBackOfferDetailFav(int pos, int action);
    }

    public void setMyCallBackOfferDetailFavListener(OnCallBackOfferDetailFavListener callback) {
        this.onCallBackOfferDetailFavListener = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        shopPic = getArguments().getString("shopPic");
        brandName = getArguments().getString("brandName");
        brandPic = getArguments().getString("brandPic");
        shopName = getArguments().getString("shopName");
        mallName = getArguments().getString("mallName");
        offerRating = getArguments().getString("offerRating");
        offerPic = getArguments().getString("offerPic");
        offerTitle = getArguments().getString("offerTitle");
        offerCategory = getArguments().getString("offerCategory");
        offerSubCategory = getArguments().getString("offerSubCategory");
        offerPercentType = getArguments().getString("offerPercentType");
        offerPercentage = getArguments().getString("offerPercentage");
        offerActualPrice = getArguments().getString("offerActualPrice");
        offerDiscountPr = getArguments().getString("offerDiscountPrice");
        offerStartDate = getArguments().getString("offerStartDate");
        offerCloseDate = getArguments().getString("offerCloseDate");
        offerDescription = getArguments().getString("offerDescription");
        shopOpenTime = getArguments().getString("shopOpenTime");
        shopCloseTime = getArguments().getString("shopCloseTime");
        offerId = getArguments().getString("offerId");
        shopId = getArguments().getString("shopId");
        favStatus= getArguments().getInt("favStatus");
        position =getArguments().getInt("position");
        shopNewAddress = getArguments().getString("shopNewAddress");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        setDataOnView();
        String uid = SharedPreferencesManager.getUserID(activity);
        hitViewApi(uid,offerId,shopId);
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
        }else if (!TextUtils.isEmpty(brandName)){
            textView.setText(brandName);
        }else {
            textView.setText("Offer");
        }
        textView.setTextColor(activity.getResources().getColor(android.R.color.white));
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void hitViewApi(final String uid, final String offerId, final String shopId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OFFERS_VIEW_HIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoader();
                        Log.e("response offer View","view");
                        //{"status":1,"msg":"success","userid":"15","shopid":"9","offerid":"34"}
                        Log.e("fav", response);
                        JSONObject jsonObject = null;
                        String userid = null, msg = null, username = null, password = null, shopid = null, offerid = null;
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
                                offerid = jsonObject.optString("offerid");
                            if (jsonObject.has("shopid"))
                                shopid = jsonObject.optString("shopid");
                            Log.e("user ", userid+"off "+offerid+"shop "+shopid);

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
                        hideLoader();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", uid);
                params.put("shopid", shopId);
                params.put("offerid", offerId);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void setDataOnView() {
        if (!TextUtils.isEmpty(brandName))
            txtOfferBrandNamePost.setText(Html.fromHtml(brandName));

        if (!TextUtils.isEmpty(offerRating))
            txtRating.setText("Rating:" + offerRating + "/5");
            ratingBar.setRating(Float.valueOf(offerRating));

        if (!TextUtils.isEmpty(mallName))
            txtMallNamePost.setText(Html.fromHtml(mallName) + ",\t" + Html.fromHtml(shopName));

        if (!TextUtils.isEmpty(offerTitle))
            txtOfferTitle.setText(Html.fromHtml(offerTitle));

        if (!TextUtils.isEmpty(offerPercentType) && !TextUtils.isEmpty(offerPercentage)) {
            txtOfferPercentage.setText(offerPercentType + " " + offerPercentage + "% off");
        } else if (!TextUtils.isEmpty(offerPercentType) && !TextUtils.isEmpty(offerActualPrice) && !TextUtils.isEmpty(offerDiscountPr) && TextUtils.isEmpty(offerPercentage)) {
            double actual = Double.parseDouble(offerActualPrice);
            double discount = Double.parseDouble(offerDiscountPr);
            int percent = (int) (100 - (discount * 100.0f) / actual);
            txtOfferPercentage.setText(offerPercentType + "\t" + percent + "% off");
        } else if (TextUtils.isEmpty(offerPercentType) && TextUtils.isEmpty(offerPercentage)) {
            if (!TextUtils.isEmpty(offerActualPrice) && !TextUtils.isEmpty(offerDiscountPr)) {
                double actual = Double.parseDouble(offerActualPrice);
                double discount = Double.parseDouble(offerDiscountPr);
                int percent = (int) (100 - (discount * 100.0f) / actual);
                txtOfferPercentage.setText(percent + "% off");
            }
        } else if (TextUtils.isEmpty(offerPercentType) && !TextUtils.isEmpty(offerPercentage)) {
            txtOfferPercentage.setText(offerPercentage + "% off");
        }

        if (!TextUtils.isEmpty(offerActualPrice) && !TextUtils.isEmpty(offerDiscountPr)) {
            txtOfferActualPrice.setText(Html.fromHtml("\u20B9" + offerActualPrice + "/-"));
            txtOfferActualPrice.setPaintFlags(txtOfferActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtOfferDiscountPrice.setText(Html.fromHtml("\u20B9" + offerDiscountPr + "/-"));
        }

        if (!TextUtils.isEmpty(offerCategory))
            txtSubcategory.setText("on " + Html.fromHtml(offerCategory) + "(" + Html.fromHtml(offerSubCategory) + ")");

        if (!TextUtils.isEmpty(offerStartDate) && !TextUtils.isEmpty(offerCloseDate))
            txtOfferDate.setText(offerStartDate + " till " + offerCloseDate);

        if (!TextUtils.isEmpty(shopOpenTime) && !TextUtils.isEmpty(shopCloseTime))
            txtShopTiming.setText(shopOpenTime + " to " + shopCloseTime);

        if (!TextUtils.isEmpty(offerDescription))
            txtOfferDiscription.setText(Html.fromHtml("Description: " + offerDescription));

        if (!TextUtils.isEmpty(offerPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgOffer, offerPic);
        } else if (!TextUtils.isEmpty(brandPic)){
            Utility.imageLoadGlideLibrary(activity, progressBar, imgOffer, brandPic);
        }else {
            imgOffer.setImageResource(R.drawable.no_image_icon);
        }

        if (!TextUtils.isEmpty(shopPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgBrand, shopPic);
        } else if (!TextUtils.isEmpty(brandPic)){
            Utility.imageLoadGlideLibrary(activity, progressBar, imgBrand, brandPic);
        }else {
            imgBrand.setImageResource(R.drawable.no_image_icon);
        }

        if (!TextUtils.isEmpty(shopNewAddress)){
            txtAddress.setText(shopNewAddress);
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
                    favHitOnApi(uId, offerId, Yes, position);
                } else {
                    String No = "0";
                    favHitOnApi(uId, offerId, No, position);
                }
            }
        });


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

                            onCallBackOfferDetailFavListener.OnCallBackOfferDetailFav(position,action);
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

    private void initialize(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        chkImageMall = (CheckBox) view.findViewById(R.id.chkImageMall);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        imgBrand = (ImageView) view.findViewById(R.id.imgBrand);
        imgOffer = (ImageView) view.findViewById(R.id.imgOffer);
        txtOfferBrandNamePost = (AppCompatTextView) view.findViewById(R.id.txtOfferBrandNamePost);
        txtOfferBrandNamePost.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtRating = (AppCompatTextView) view.findViewById(R.id.txtRating);
        txtRating.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMallNamePost = (AppCompatTextView) view.findViewById(R.id.txtMallNamePost);
        txtMallNamePost.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddress = (AppCompatTextView) view.findViewById(R.id.txtAddress);
        txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferPercentage = (AppCompatTextView) view.findViewById(R.id.txtOfferPercentage);
        txtOfferPercentage.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtSubcategory = (AppCompatTextView) view.findViewById(R.id.txtSubcategory);
        txtSubcategory.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferDate = (AppCompatTextView) view.findViewById(R.id.txtOfferDate);
        txtOfferDate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtShopTiming = (AppCompatTextView) view.findViewById(R.id.txtShopTiming);
        txtShopTiming.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferDiscription = (AppCompatTextView) view.findViewById(R.id.txtOfferDiscription);
        txtOfferDiscription.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnRate = (AppCompatButton) view.findViewById(R.id.btnRate);
        btnRate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferActualPrice = (AppCompatTextView) view.findViewById(R.id.txtOfferActualPrice);
        txtOfferActualPrice.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferDiscountPrice = (AppCompatTextView) view.findViewById(R.id.txtOfferDiscountPrice);
        txtOfferDiscountPrice.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtOfferTitle = (AppCompatTextView) view.findViewById(R.id.txtOfferTitle);
        txtOfferTitle.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        btnRate.setOnClickListener(this);
        imgBrand.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.offer_post_layout;
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
            case R.id.btnRate:
                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.imgBrand:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}
