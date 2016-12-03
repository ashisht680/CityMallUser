package com.javinindia.citymalls.apiparsing.mallListParsing;

import android.util.Log;

import com.javinindia.citymalls.apiparsing.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 07-11-2016.
 */
public class MallListResponseParsing extends ApiBaseData {
    private ArrayList<MallDetail> mallDetailsArrayList;

    public ArrayList<MallDetail> getMallDetailsArrayList() {
        return mallDetailsArrayList;
    }

    public void setMallDetailsArrayList(ArrayList<MallDetail> mallDetailsArrayList) {
        this.mallDetailsArrayList = mallDetailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setStatus(jsonObject.optInt("status"));
            setMsg(jsonObject.optString("msg"));
            if (jsonObject.has("mallDetail") && jsonObject.optJSONArray("mallDetail")!=null)
                setMallDetailsArrayList(getMallListMethod(jsonObject.optJSONArray("mallDetail")));
            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<MallDetail> getMallListMethod(JSONArray mallDetails) {
        ArrayList<MallDetail> mallDetailses = new ArrayList<>();
        for (int i = 0; i < mallDetails.length(); i++) {
            MallDetail details = new MallDetail();
            JSONObject jsonObject = mallDetails.optJSONObject(i);
            if (jsonObject.has("id"))
                details.setId(jsonObject.optString("id"));
            if (jsonObject.has("mallName"))
                details.setMallName(jsonObject.optString("mallName"));
            if (jsonObject.has("description"))
                details.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("mallAddress"))
                details.setMallAddress(jsonObject.optString("mallAddress"));
            if (jsonObject.has("mallLandmark"))
                details.setMallLandmark(jsonObject.optString("mallLandmark"));
            if (jsonObject.has("mallLat"))
                details.setMallLat(jsonObject.optString("mallLat"));
            if (jsonObject.has("mallLong"))
                details.setMallLong(jsonObject.optString("mallLong"));
            if (jsonObject.has("country"))
                details.setCountry(jsonObject.optString("country"));
            if (jsonObject.has("state"))
                details.setState(jsonObject.optString("state"));
            if (jsonObject.has("city"))
                details.setCity(jsonObject.optString("city"));
            if (jsonObject.has("pincode"))
                details.setPinCode(jsonObject.optString("pincode"));
            if (jsonObject.has("rating"))
                details.setRating(jsonObject.optString("rating"));
            if (jsonObject.has("openTime"))
                details.setOpenTime(jsonObject.optString("openTime"));
            if (jsonObject.has("closeTime"))
                details.setCloseTime(jsonObject.optString("closeTime"));
            if (jsonObject.has("distance"))
                details.setDistance(jsonObject.optDouble("distance"));
            if (jsonObject.has("mallPic"))
                details.setMallPic(jsonObject.optString("mallPic"));
            if (jsonObject.has("favStatus"))
                details.setFavStatus(jsonObject.optInt("favStatus"));
            if (jsonObject.has("offerCount"))
                details.setOfferCount(jsonObject.optInt("offerCount"));

            mallDetailses.add(details);
        }
        return mallDetailses;
    }
}
