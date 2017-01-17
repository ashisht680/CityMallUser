package com.javinindia.citymalls.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ashish on 14-12-2016.
 */

public class FeedbackFragment extends BaseFragment {
    AppCompatEditText etFeedback;
    AppCompatButton btnFeedback;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        AppCompatTextView txtFeedback, txtTitle, txtPoints;
        txtFeedback = (AppCompatTextView) view.findViewById(R.id.txtFeedback);
        txtFeedback.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtFeedback.setPaintFlags(txtFeedback.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtTitle = (AppCompatTextView) view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etFeedback = (AppCompatEditText) view.findViewById(R.id.etFeedback);
        etFeedback.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnFeedback = (AppCompatButton) view.findViewById(R.id.btnFeedback);
        btnFeedback.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFeedback.getText().toString().trim().equals("")) {
                    String feed = etFeedback.getText().toString().trim();
                    methodHit(feed);
                } else {
                    Toast.makeText(activity, "You are not entered Feedback", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
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
        AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.tittle);
        textView.setText("Feedback");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void methodHit(final String feed) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FEEDBACK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        responseImplement(response);
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
                params.put("id", SharedPreferencesManager.getUserID(activity));
                params.put("feed", feed);
                params.put("type", "user");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
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
        if (status == 1) {
            Toast.makeText(activity, "Congrats! Your feedback has been successfully submitted. We'll get back to you shortly!", Toast.LENGTH_LONG).show();
            activity.onBackPressed();
        } else {
            if (!TextUtils.isEmpty(msg)) {
                showDialogMethod(msg);
            }
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.feedback_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu != null)
            menu.clear();
    }
}
