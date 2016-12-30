package com.javinindia.citymalls.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.userParsing.UesrParsingResponse;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.utility.CheckConnection;
import com.javinindia.citymalls.utility.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 09-09-2016.
 */
public class NavigationAboutFragment extends BaseFragment implements View.OnClickListener,EditProfileFragment.OnCallBackUpdateProfileListener ,CheckConnectionFragment.OnCallBackInternetListener{
    private RequestQueue requestQueue;
    ImageView imgProfilePic;
    AppCompatTextView personName,txtEmailHd,txtEmailAddress,txtMobileHd,txtMobile,txtGenderHd,txtGender,txtDOBHd,txtDOB,
            txtLocationHd,txtState,txtCity,txtAddress,txtStateHd,txtCityHd,txtAddressHd;
    RelativeLayout rlEdit,rlBack;
    ProgressBar progressBar;
    int MY_PERMISSION_ACCESS_COURSE_LOCATION=0;
    String sAddress="";

    private OnCallBackRefreshListener onCallBackRefreshListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void OnCallBackInternet() {
        activity.onBackPressed();
    }

    public interface OnCallBackRefreshListener {
        void OnCallBackRefresh(String name,String email,String pic);
    }

    public void setMyCallUpdateProfileListener(OnCallBackRefreshListener callback) {
        this.onCallBackRefreshListener = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        methodForHitView();
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
        textView.setText("Profile");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void methodForHitView() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.USER_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String sID = null, msg = null, sPic = null, gender=null,dob=null;
                        String sName, sEmail, sMobileNum, sState, sCity;
                        int status =0;
                        loading.dismiss();
                        UesrParsingResponse shopViewResponse = new UesrParsingResponse();
                        shopViewResponse.responseParseMethod(response);

                        if (shopViewResponse.getStatus()==1) {
                            status = shopViewResponse.getStatus();
                            msg = shopViewResponse.getMsg().trim();
                            sID = shopViewResponse.getUserid().trim();
                            sPic = shopViewResponse.getProf_pic().trim();
                            sName = shopViewResponse.getName().trim();
                            sEmail = shopViewResponse.getEmail().trim();
                            sMobileNum = shopViewResponse.getPhone().trim();
                            sState = shopViewResponse.getState().trim();
                            sCity = shopViewResponse.getCity().trim();
                            sAddress = shopViewResponse.getAddress().trim();
                            gender = shopViewResponse.getGender().trim();
                            dob = shopViewResponse.getDob().trim();

                            methodSetDate(sPic,sName,sEmail,sMobileNum,sState,sCity,sAddress,gender,dob);

                            saveDataOnPreference(sName, sMobileNum, sEmail,sPic);
                            onCallBackRefreshListener.OnCallBackRefresh(sName,sEmail,sPic);
                        } else {
                            if (!TextUtils.isEmpty(shopViewResponse.getMsg().trim())) {
                                showDialogMethod(shopViewResponse.getMsg().trim());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodSetDate(String sPic, String sName, String sEmail, String sMobileNum, String sState, String sCity, String sAddress, String gender, String dob) {
        if(!TextUtils.isEmpty(sName) && !sName.equals("null")){
            personName.setText(Utility.fromHtml(sName));
        }else {
            personName.setText(" ");
        }
        if(!TextUtils.isEmpty(sEmail) && !sEmail.equals("null")){
            txtEmailAddress.setText(Utility.fromHtml(sEmail));
        }else {
            txtEmailAddress.setText(" ");
        }
        if(!TextUtils.isEmpty(sMobileNum) && !sMobileNum.equals("null")){
            txtMobile.setText(Utility.fromHtml(sMobileNum));
        }else {
            txtMobile.setText(" ");
        }
        if(!TextUtils.isEmpty(sState) && !sState.equals("null")){
            txtState.setText(Utility.fromHtml(sState));
        }else {
            txtState.setText(" ");
        }
        if(!TextUtils.isEmpty(sCity) && !sCity.equals("null")){
            txtCity.setText(Utility.fromHtml(sCity));
        }else {
            txtCity.setText(" ");
        }
        if(!TextUtils.isEmpty(sAddress) && !sAddress.equals("null")){
            txtAddress.setText(Utility.fromHtml(sAddress));
        }else {
            txtAddress.setText(" ");
        }
        if(!TextUtils.isEmpty(gender) && !gender.equals("null")){
            txtGender.setText(Utility.fromHtml(gender));
        }else {
            txtGender.setText(" ");
        }
        if(!TextUtils.isEmpty(dob) && !dob.equals("null")){
            txtDOB.setText(Utility.fromHtml(dob));
        }else {
            txtDOB.setText(" ");
        }
        if (!TextUtils.isEmpty(sPic)) {
            Utility.imageLoadGlideLibrary(activity, progressBar, imgProfilePic, sPic);
        }
    }

    private void saveDataOnPreference(String username, String phone, String email, String pic) {
        SharedPreferencesManager.setUsername(activity, username);
        SharedPreferencesManager.setMobile(activity, phone);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setProfileImage(activity,pic);
    }

    private void initialize(View view) {
        rlBack = (RelativeLayout)view.findViewById(R.id.rlBack);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        imgProfilePic = (ImageView)view.findViewById(R.id.imgProfilePic);
        rlEdit = (RelativeLayout)view.findViewById(R.id.rlEdit);
        personName = (AppCompatTextView)view.findViewById(R.id.personName);
        personName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHd = (AppCompatTextView)view.findViewById(R.id.txtEmailHd);
        txtEmailHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailAddress = (AppCompatTextView)view.findViewById(R.id.txtEmailAddress);
        txtEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHd = (AppCompatTextView)view.findViewById(R.id.txtMobileHd);
        txtMobileHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobile = (AppCompatTextView)view.findViewById(R.id.txtMobile);
        txtMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGenderHd = (AppCompatTextView)view.findViewById(R.id.txtGenderHd);
        txtGenderHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGender = (AppCompatTextView)view.findViewById(R.id.txtGender);
        txtGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDOBHd = (AppCompatTextView)view.findViewById(R.id.txtDOBHd);
        txtDOBHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDOB = (AppCompatTextView)view.findViewById(R.id.txtDOB);
        txtDOB.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtStateHd = (AppCompatTextView)view.findViewById(R.id.txtStateHd);
        txtStateHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtState = (AppCompatTextView)view.findViewById(R.id.txtState);
        txtState.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtCityHd = (AppCompatTextView)view.findViewById(R.id.txtCityHd);
        txtCityHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtCity = (AppCompatTextView)view.findViewById(R.id.txtCity);
        txtCity.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddressHd = (AppCompatTextView)view.findViewById(R.id.txtAddressHd);
        txtAddressHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddress = (AppCompatTextView)view.findViewById(R.id.txtAddress);
        txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        rlEdit.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        txtAddress.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.profile_layout;
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
            case R.id.rlEdit:
                if (CheckConnection.haveNetworkConnection(activity)) {
                    EditProfileFragment fragment1 = new EditProfileFragment();
                    fragment1.setMyCallUpdateProfileListener(this);
                    callFragmentMethod(fragment1, this.getClass().getSimpleName(),R.id.navigationContainer);
                }else {
                    methodCallCheckInternet();
                }
                break;
            case R.id.rlBack:
                activity.onBackPressed();
                break;
            case R.id.txtAddress:
                if(!TextUtils.isEmpty(sAddress) && !sAddress.equals("null")){
                    showNewDialog("Address",sAddress);
                }else {
                    showDialogMethod("Address not found");
                }
                break;
        }
    }

    public void methodCallCheckInternet() {
        CheckConnectionFragment fragment = new CheckConnectionFragment();
        fragment.setMyCallBackInternetListener(this);
        callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.navigationContainer);
    }

    @Override
    public void OnCallBackUpdateProfile() {
        methodForHitView();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}
