package com.javinindia.citymalls.fragments;

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

    private EditText et_mobileNum, et_otp,et_password;
    AppCompatTextView txtResendOtp;
    private RequestQueue requestQueue;
    private BaseFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  activity.getSupportActionBar().hide();
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
        btn_regester.setOnClickListener(this);
        et_otp = (EditText) view.findViewById(R.id.et_Name);
        et_otp.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_mobileNum = (EditText) view.findViewById(R.id.et_Mobile);
        et_mobileNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtResendOtp = (AppCompatTextView) view.findViewById(R.id.txtResendOtp);
        txtResendOtp.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtResendOtp.setText(Html.fromHtml("<font color=#0d7bbf>" + "Resend OTP" + "</font>"));
        txtResendOtp.setOnClickListener(this);
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
              //  registrationMethod();
                BaseFragment signUpFragment = new SignUpFragment();
                callFragmentMethod(signUpFragment, this.getClass().getSimpleName(), R.id.container);
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
        String password = et_password.getText().toString().trim();
        String mobile = et_mobileNum.getText().toString().trim();
        String  otp = et_otp.getText().toString().trim();

        if (registerValidation(mobile, password,otp)) {
            showLoader();
          //  sendDataOnRegistrationApi(mobile, password,otp);
        }

    }

    /*private void sendDataOnRegistrationApi(final String number, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTRATION_URL,
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
                params.put("number", number);
                params.put("password", password);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }*/

    private void responseImplement(String response) {
        JSONObject jsonObject = null;
        String status = null, userid = null, msg = null, username = null, phone = null, gender = null, email = null;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.has("status"))
                status = jsonObject.optString("status");
            if (jsonObject.has("userid"))
                userid = jsonObject.optString("userid");
            if (jsonObject.has("msg"))
                msg = jsonObject.optString("msg");
            if (jsonObject.has("username"))
                username = jsonObject.optString("username");
            if (jsonObject.has("email"))
                email = jsonObject.optString("email");
            if (jsonObject.has("phone"))
                phone = jsonObject.optString("phone");
            if (jsonObject.has("gender"))
                gender = jsonObject.optString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase("true") && !status.isEmpty()) {
            // fragment = new HomeLauncherFragment();
            //   fragment = new LoginFragment();
            saveDataOnPreference(username, phone, gender, email, userid);
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            callFragmentMethodDead(fragment, this.getClass().getSimpleName());
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }

    private void saveDataOnPreference(String username, String phone, String gender, String email, String userId) {
        SharedPreferencesManager.setUsername(activity, username);
        SharedPreferencesManager.setMobile(activity, phone);
        SharedPreferencesManager.setEmail(activity, email);
        //  SharedPreferencesManager.setSocialID(activity, socialId);
        SharedPreferencesManager.setUserID(activity, userId);
    }

    private boolean registerValidation(final String mobile, final String password,final String otp) {
        if (mobile.length() != 10) {
            et_mobileNum.setError("Please enter your valid mobile number");
            et_mobileNum.requestFocus();
            return false;
        } else if (password.length() < 8) {
            et_mobileNum.setError("Please enter password more than 8 character");
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