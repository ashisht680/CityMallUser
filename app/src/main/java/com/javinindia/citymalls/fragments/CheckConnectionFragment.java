package com.javinindia.citymalls.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.activity.LoginActivity;
import com.javinindia.citymalls.activity.NavigationActivity;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;

/**
 * Created by Ashish on 16-12-2016.
 */

public class CheckConnectionFragment extends BaseFragment {

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AppCompatTextView txtAppName,txtNoInt,txtGotIt;
        txtAppName = (AppCompatTextView)view.findViewById(R.id.txtAppName);
        txtAppName.setTypeface(FontAsapBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtNoInt = (AppCompatTextView)view.findViewById(R.id.txtNoInt);
        txtNoInt.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGotIt = (AppCompatTextView)view.findViewById(R.id.txtGotIt);
        txtGotIt.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetworkConnection()) {
                    Intent refresh = new Intent(activity, LoginActivity.class);
                    startActivity(refresh);//Start the same Activity
                    activity.finish();

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle("No Internet Connection");
                    alertDialogBuilder.setMessage("You are offline please check your internet connection");
                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });
        return view;
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.internet_check_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}