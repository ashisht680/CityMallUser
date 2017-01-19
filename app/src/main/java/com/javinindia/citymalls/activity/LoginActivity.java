package com.javinindia.citymalls.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.fragments.BaseFragment;
import com.javinindia.citymalls.fragments.LoginFragment;
import com.javinindia.citymalls.notification.MyAndroidFirebaseInstanceIdService;
import com.javinindia.citymalls.preference.SharedPreferencesManager;


/**
 * Created by Ashish on 26-09-2016.
 */
public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        new MyAndroidFirebaseInstanceIdService();
        String username = SharedPreferencesManager.getUsername(getApplicationContext());
        if (TextUtils.isEmpty(username)) {
            BaseFragment baseFragment = new LoginFragment();
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
            fragmentTransaction.add(R.id.container, baseFragment);
            fragmentTransaction.commit();
        } else {
            Intent splashIntent = new Intent(LoginActivity.this, NavigationActivity.class);
            startActivity(splashIntent);
            finish();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

        private static final String TAG = "MyAndroidFCMIIDService";
        @Override
        public void onTokenRefresh() {
            //Get hold of the registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (!TextUtils.isEmpty(refreshedToken)) {
                SharedPreferencesManager.setDeviceToken(getApplicationContext(), refreshedToken);
            } else {

            }

        }

    }
}
