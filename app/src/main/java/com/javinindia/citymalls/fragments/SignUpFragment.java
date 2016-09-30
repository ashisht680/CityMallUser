package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.javinindia.citymalls.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatEditText et_Name, et_email, et_phoneNum;
    RadioButton radioButton;
    TextView txtTermCondition;
    private RequestQueue requestQueue;
    private BaseFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   activity.getSupportActionBar().hide();
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
        AppCompatButton buttonSignUp = (AppCompatButton) view.findViewById(R.id.btn_sign_up);
        buttonSignUp.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        buttonSignUp.setOnClickListener(this);
        et_phoneNum = (AppCompatEditText) view.findViewById(R.id.et_phoneNum);
        et_phoneNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_email = (AppCompatEditText) view.findViewById(R.id.et_email);
        et_email.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_Name = (AppCompatEditText) view.findViewById(R.id.et_Name);
        et_Name.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        txtTermCondition = (TextView) view.findViewById(R.id.txtTermCondition);
        txtTermCondition.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTermCondition.setText(Html.fromHtml("<font color=#000000>" + "I Accept the" + "</font>" + "\t" + "<font color=#0d7bbf>" + "Terms and conditions" + "</font>"));
        txtTermCondition.setOnClickListener(this);

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.sign_up_layout;
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
            case R.id.btn_sign_up:
             //   registrationMethod();
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
        String number = et_phoneNum.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String name = et_Name.getText().toString().trim();

        if (registerValidation(number, email, name)) {
            showLoader();
          //  sendDataOnRegistrationApi(number, email, name);
        }

    }

   /* private void sendDataOnRegistrationApi(final String number, final String otp, final String password) {
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
                params.put("otp", otp);
                params.put("password", password);
                return params;
            }

        };
        stringRequest.setTag(Constants.REGISTRATION_TAG);
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
       // SharedPreferencesManager.setGender(activity, gender);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setUserID(activity, userId);
    }

    private boolean registerValidation(final String number, final String email, final String name) {
        if (TextUtils.isEmpty(name)) {
            et_Name.setError("Please enter your name");
            et_Name.requestFocus();
            return false;
        } else if (number.length() != 10) {
            et_phoneNum.setError("Please enter valid mobile number");
            et_phoneNum.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            et_email.setError("Please enter valid email");
            et_email.requestFocus();
            return false;
        }  else {
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
