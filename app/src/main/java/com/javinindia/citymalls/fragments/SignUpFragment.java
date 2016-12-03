package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private AppCompatEditText et_Name, et_email, et_phoneNum, et_password;
    RadioButton radioButton;
    TextView txtTermCondition;
    private RequestQueue requestQueue;
    private BaseFragment fragment;
    private CheckBox checkShowPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   activity.getSupportActionBar().hide();
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
        AppCompatButton buttonSignUp = (AppCompatButton) view.findViewById(R.id.btn_sign_up);
        buttonSignUp.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        buttonSignUp.setOnClickListener(this);
        et_phoneNum = (AppCompatEditText) view.findViewById(R.id.et_phoneNum);
        et_phoneNum.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_email = (AppCompatEditText) view.findViewById(R.id.et_email);
        et_email.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_Name = (AppCompatEditText) view.findViewById(R.id.et_Name);
        et_Name.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        et_password = (AppCompatEditText) view.findViewById(R.id.et_password);
        et_password.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        txtTermCondition = (TextView) view.findViewById(R.id.txtTermCondition);
        txtTermCondition.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTermCondition.setText(Html.fromHtml("<font color=#000000>" + "I accept the" + "</font>" + "\t" + "<font color=#0d7bbf>" + "terms and conditions." + "</font>"));
        checkShowPassword = (CheckBox)view.findViewById(R.id.checkShowPassword);
        checkShowPassword.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        checkShowPassword.setOnClickListener(this);
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
                   registrationMethod();
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtTermCondition:
               /* BaseFragment termFragment = new TermsFragment();
                callFragmentMethodDead(termFragment, Constants.OTHER_USER_FEED, R.id.container);*/
                break;
            case R.id.checkShowPassword:
                if (checkShowPassword.isChecked()){
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
    }

    private void registrationMethod() {
        String number = et_phoneNum.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String name = et_Name.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (registerValidation(number, email, name, password)) {
            showLoader();
            sendDataOnRegistrationApi(number, email, name, password);
        }

    }

    private void sendDataOnRegistrationApi(final String number, final String email, final String name, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SIGN_UP_URL,
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
                params.put("name", name);
                params.put("password", password);
                params.put("token", "hello");
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
        String userid = null, msg = null, username = null, password = null, mobile = null, email = null, otp = null,pic=null;
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

        if (status == 1) {
            if (jsonObject.has("userid"))
                userid = jsonObject.optString("userid");
            if (jsonObject.has("name"))
                username = jsonObject.optString("name");
            if (jsonObject.has("email"))
                email = jsonObject.optString("email");
            if (jsonObject.has("password"))
                password = jsonObject.optString("password");
            if (jsonObject.has("mobile"))
                mobile = jsonObject.optString("mobile");
            if (jsonObject.has("otp"))
                otp = jsonObject.optString("otp");
            if (jsonObject.has("pic"))
                pic = jsonObject.optString("pic");
            saveDataOnPreference(username, mobile, email, userid,pic);
            GenrateOtpFragment baseFragment = new GenrateOtpFragment();

            Bundle bundle = new Bundle();
            bundle.putString("mobile", mobile);
            bundle.putString("otp",otp);
            bundle.putString("email",email);
            baseFragment.setArguments(bundle);

            callFragmentMethod(baseFragment, this.getClass().getSimpleName(), R.id.container);
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }

    private void saveDataOnPreference(String username, String phone, String email, String userId,String pic) {
        SharedPreferencesManager.setUsername(activity, username);
        SharedPreferencesManager.setMobile(activity, phone);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setUserID(activity, userId);
        SharedPreferencesManager.setProfileImage(activity,pic);
        SharedPreferencesManager.setCity(activity,"New Delhi");
    }

    private boolean registerValidation(final String number, final String email, final String name, final String password) {
        if (TextUtils.isEmpty(name)) {
            et_Name.setError("Please enter your name");
            et_Name.requestFocus();
            return false;
        } else if (number.length() != 10) {
            et_phoneNum.setError("Mobile number entered is invalid");
            et_phoneNum.requestFocus();
            return false;
        } else if (!Utility.isEmailValid(email)) {
            et_email.setError("Email id entered is invalid");
            et_email.requestFocus();
            return false;
        } else if (password.length() < 6) {
            et_password.setError("Password should be more than 6 characters");
            et_password.requestFocus();
            return false;
        }else if (!radioButton.isChecked()){
            Toast.makeText(activity,"You have not accepted the terms and conditions.",Toast.LENGTH_LONG).show();
            return  false;
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
