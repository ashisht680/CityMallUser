package com.javinindia.citymalls.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.javinindia.citymalls.constant.Constants;

/**
 * Created by Ajit Gupta on 5/11/2016.
 */
public class SharedPreferencesManager {

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getUsername(Context context) {
        return getSharedPreferences(context).getString(Constants.USERNAME, null);
    }

    public static void setUsername(Context context, String username) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.USERNAME, username);
        editor.commit();
    }

    public static String getAmigoCount(Context context) {
        return getSharedPreferences(context).getString(Constants.AMIGO_COUNT, null);
    }

    public static void setAmigoCount(Context context, String amigocount) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.AMIGO_COUNT, amigocount);
        editor.commit();
    }

    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString(Constants.EMAIL, null);
    }

    public static void setEmail(Context context, String email) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.EMAIL, email);
        editor.commit();
    }

    public static String getGender(Context context) {
        return getSharedPreferences(context).getString(Constants.GENDER, null);
    }

    public static void setGender(Context context, String gender) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.GENDER, gender);
        editor.commit();
    }

    public static String getPhone(Context context) {
        return getSharedPreferences(context).getString(Constants.PHONE, null);
    }

    public static void setPhone(Context context, String phone) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.PHONE, phone);
        editor.commit();
    }

    public static String getSocialId(Context context) {
        return getSharedPreferences(context).getString(Constants.SOCIAL_ID, null);
    }

    public static void setSocialID(Context context, String socialId) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.SOCIAL_ID, socialId);
        editor.commit();
    }

    public static String getID(Context context) {
        return getSharedPreferences(context).getString(Constants.ID, null);
    }

    public static void setID(Context context, String id) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.ID, id);
        editor.commit();
    }

    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(Constants.USER_ID, null);
    }

    public static void setUserID(Context context, String userId) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.USER_ID, userId);
        editor.commit();
    }

    public static String getPassword(Context context) {
        return getSharedPreferences(context).getString("Password", null);
    }

    public static void setPassword(Context context, String userId) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("Password", userId);
        editor.commit();
    }

    public static String getFriend(Context context) {
        return getSharedPreferences(context).getString("Friend", null);
    }

    public static void setFriend(Context context, String friend) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("Friend", friend);
        editor.commit();
    }

    public static String getFriendID(Context context) {
        return getSharedPreferences(context).getString("FriendID", null);
    }

    public static void setFriendID(Context context, String friendID) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FriendID", friendID);
        editor.commit();
    }

    public static String getDeviceToken(Context context) {
        return getSharedPreferences(context).getString("DeviceToken", null);
    }

    public static void setDeviceToken(Context context, String friendID) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("DeviceToken", friendID);
        editor.commit();
    }

    public static String getotherUid(Context context) {
        return getSharedPreferences(context).getString(Constants.OTHER_USER_ID, null);
    }

    public static void setotherUid(Context context, String otherUid) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.OTHER_USER_ID, otherUid);
        editor.commit();
    }

    public static String getImageResource(Context context) {
        return getSharedPreferences(context).getString("ProfileImage", null);
    }

    public static void setImageResource(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ProfileImage", profileImage);
        editor.commit();
    }

    public static String getFeaturedImage1(Context context) {
        return getSharedPreferences(context).getString("FeatureImage1", null);
    }
    public static void setFeaturedImage1(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FeatureImage1", profileImage);
        editor.commit();
    }

    public static String getFeaturedImage2(Context context) {
        return getSharedPreferences(context).getString("FeatureImage2", null);
    }
    public static void setFeaturedImage2(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FeatureImage2", profileImage);
        editor.commit();
    }

    public static String getFeaturedImage3(Context context) {
        return getSharedPreferences(context).getString("FeatureImage3", null);
    }
    public static void setFeaturedImage3(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FeatureImage3", profileImage);
        editor.commit();
    }

    public static String getFeaturedImage4(Context context) {
        return getSharedPreferences(context).getString("FeatureImage4", null);
    }
    public static void setFeaturedImage4(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FeatureImage4", profileImage);
        editor.commit();
    }

    public static String getFeaturedImage5(Context context) {
        return getSharedPreferences(context).getString("FeatureImage5", null);
    }
    public static void setFeaturedImage5(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FeatureImage5", profileImage);
        editor.commit();
    }

    public static String getAnonymousID(Context context) {
        return getSharedPreferences(context).getString("AnonymousID", null);
    }
    public static void setAnonymousID(Context context, String anonymousID) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("AnonymousID", anonymousID);
        editor.commit();
    }

    public static String getLocation(Context context) {
        return getSharedPreferences(context).getString("Location", null);
    }
    public static void setLocation(Context context, String anonymousID) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("Location", anonymousID);
        editor.commit();
    }


}
