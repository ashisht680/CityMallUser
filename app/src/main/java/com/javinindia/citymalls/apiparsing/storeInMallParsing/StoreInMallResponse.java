package com.javinindia.citymalls.apiparsing.storeInMallParsing;

import android.util.Log;

import com.javinindia.citymalls.apiparsing.base.ApiBaseData;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 09-11-2016.
 */
public class StoreInMallResponse extends ApiBaseData{
    private ArrayList<ShopData> shopDataArrayList;

    public ArrayList<ShopData> getShopDataArrayList() {
        return shopDataArrayList;
    }

    public void setShopDataArrayList(ArrayList<ShopData> shopDataArrayList) {
        this.shopDataArrayList = shopDataArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setStatus(jsonObject.optInt("status"));
            setMsg(jsonObject.optString("msg"));
            if (jsonObject.has("ShopData") && jsonObject.optJSONArray("ShopData")!=null)
                setShopDataArrayList(getStoreInMallListMethod(jsonObject.optJSONArray("ShopData")));
            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ShopData> getStoreInMallListMethod(JSONArray shopData) {
        ArrayList<ShopData> shopDatas = new ArrayList<>();
        for (int i = 0; i < shopData.length(); i++) {
            ShopData details = new ShopData();
            JSONObject jsonObject = shopData.optJSONObject(i);
            if (jsonObject.has("id"))
                details.setId(jsonObject.optString("id"));
            if (jsonObject.has("storeName"))
                details.setStoreName(jsonObject.optString("storeName"));
            if (jsonObject.has("ownerName"))
                details.setOwnerName(jsonObject.optString("ownerName"));
            if (jsonObject.has("mobile"))
                details.setMobile(jsonObject.optString("mobile"));
            if (jsonObject.has("state"))
                details.setState(jsonObject.optString("state"));
            if (jsonObject.has("city"))
                details.setCity(jsonObject.optString("city"));
            if (jsonObject.has("address"))
                details.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("pincode"))
                details.setPinCode(jsonObject.optString("pincode"));
            if (jsonObject.has("mall"))
                details.setMall(jsonObject.optString("mall"));
            if (jsonObject.has("MallName"))
                details.setMallName(jsonObject.optString("MallName"));
            if (jsonObject.has("shopNo"))
                details.setShopNo(jsonObject.optString("shopNo"));
            if (jsonObject.has("floor"))
                details.setFloor(jsonObject.optString("floor"));
            if (jsonObject.has("openTime"))
                details.setOpenTime(jsonObject.optString("openTime"));
            if (jsonObject.has("closeTime"))
                details.setCloseTime(jsonObject.optString("closeTime"));
            if (jsonObject.has("description"))
                details.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("profilepic"))
                details.setProfilepic(jsonObject.optString("profilepic"));
            if (jsonObject.has("banner"))
                details.setBanner(jsonObject.optString("banner"));
            if (jsonObject.has("shopCategory"))
                details.setShopCategory(jsonObject.optString("shopCategory"));
            if (jsonObject.has("shopSubCategory"))
                details.setShopSubCategory(jsonObject.optString("shopSubCategory"));
            if (jsonObject.has("rating"))
                details.setRating(jsonObject.optString("rating"));
            if (jsonObject.has("shopOfferCount"))
                details.setShopOfferCount(jsonObject.optInt("shopOfferCount"));
            if (jsonObject.has("favStatus"))
                details.setFavStatus(jsonObject.optInt("favStatus"));





            shopDatas.add(details);
        }
        return shopDatas;
    }
}
