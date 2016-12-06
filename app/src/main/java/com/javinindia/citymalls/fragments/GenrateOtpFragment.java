package com.javinindia.citymalls.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.activity.NavigationActivity;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.preference.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 08-09-2016.
 */
public class GenrateOtpFragment extends BaseFragment implements View.OnClickListener {

    private EditText et_mobileNum, et_otp;
    AppCompatTextView txtResendOtp;
    private RequestQueue requestQueue;
    String mobile;
    String otp,email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = getArguments().getString("mobile");
        otp = getArguments().getString("otp");
        email = getArguments().getString("email");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        ImageView imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        AppCompatButton btn_regester = (AppCompatButton) view.findViewById(R.id.btn_regester);
        btn_regester.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_otp = (EditText) view.findViewById(R.id.et_otp);
        et_otp.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_mobileNum = (EditText) view.findViewById(R.id.et_Mobile);
        et_mobileNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtResendOtp = (AppCompatTextView) view.findViewById(R.id.txtResendOtp);
        txtResendOtp.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtResendOtp.setText(Html.fromHtml("<font color=#0d7bbf>" + "Resend OTP" + "</font>"));
        txtResendOtp.setOnClickListener(this);
        btn_regester.setOnClickListener(this);

        et_mobileNum.setText(mobile);
       // et_otp.setText(otp);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.generate_otp_layout;
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
            case R.id.btn_regester:
                registrationMethod();
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtTermCondition:
               /* BaseFragment termFragment = new TermsFragment();
                callFragmentMethodDead(termFragment, Constants.OTHER_USER_FEED, R.id.container);*/
                break;
        }
    }

    private void registrationMethod() {
        String mobile = et_mobileNum.getText().toString().trim();
        String  otp = et_otp.getText().toString().trim();

        if (registerValidation(mobile,otp)) {
            showLoader();
            sendDataOnRegistrationApi(mobile, email,otp);
        }

    }

    private void sendDataOnRegistrationApi(final String number,final String email, final String otp) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.VERIFY_OTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoader();
                        responseImplement(response);
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
                params.put("email", email);
                params.put("phone", number);
                params.put("otp", otp);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String msg = null;
        int status = 0;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.has("status"))
                status = jsonObject.optInt("status");
            if (jsonObject.has("msg"))
                msg = jsonObject.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status==1) {
            Intent refresh = new Intent(activity, NavigationActivity.class);
            startActivity(refresh);//Start the same Activity
            activity.finish();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }

    private boolean registerValidation(final String mobile,final String otp) {
        if (mobile.length() != 10) {
            et_mobileNum.setError("Please enter your valid mobile number");
            et_mobileNum.requestFocus();
            return false;
        }
        else if (TextUtils.isEmpty(otp)) {
            et_otp.setError("Please enter your valid OTP");
            et_otp.requestFocus();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(this.getClass().getSimpleName());
        }
    }
}