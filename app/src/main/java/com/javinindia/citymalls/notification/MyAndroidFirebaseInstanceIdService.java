package com.javinindia.citymalls.notification;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javinindia.citymalls.preference.SharedPreferencesManager;


public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyAndroidFCMIIDService";
    Context context;


   /* public MyAndroidFirebaseInstanceIdService() {
    }

    public MyAndroidFirebaseInstanceIdService(Context context) {
        this.context = context;
    }*/

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(refreshedToken)) {
            SharedPreferencesManager.setDeviceToken(getApplicationContext(), refreshedToken);
        } else {

        }


    }

  /*  public void sendRegistrationToServer() {
        //Implement this method if you want to store the token on your server
        onTokenRefresh();
    }*/

}