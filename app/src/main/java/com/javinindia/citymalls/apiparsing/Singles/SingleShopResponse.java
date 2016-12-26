package com.javinindia.citymalls.apiparsing.Singles;

import android.util.Log;

import com.javinindia.citymalls.apiparsing.base.ApiBaseData;
import com.javinindia.citymalls.apiparsing.offerListparsing.DetailsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 26-12-2016.
 */

public class SingleShopResponse extends ApiBaseData {

    private ArrayList<DetailsShop> detailsListArrayList;

    public ArrayList<DetailsShop> getDetailsListArrayList() {
        return detailsListArrayList;
    }

    public void setDetailsListArrayList(ArrayList<DetailsShop> detailsListArrayList) {
        this.detailsListArrayList = detailsListArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.has("Details"))
                setDetailsListArrayList(getDetailInMethod(jsonObject.optJSONArray("Details")));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DetailsShop> getDetailInMethod(JSONArray details) {
        ArrayList<DetailsShop> shopArrayList = new ArrayList<>();
        for (int i = 0; i < details.length(); i++) {
            DetailsShop shop = new DetailsShop();
            JSONObject jsonObject = details.optJSONObject(i);
            if (jsonObject.has("id"))
                shop.setId(jsonObject.optString("id"));
            if (jsonObject.has("storeName"))
                shop.setStoreName(jsonObject.optString("storeName"));
            if (jsonObject.has("ownerName"))
                shop.setOwnerName(jsonObject.optString("ownerName"));
            if (jsonObject.has("mobile"))
                shop.setMobile(jsonObject.optString("mobile"));
            if (jsonObject.has("state"))
                shop.setState(jsonObject.optString("state"));
            if (jsonObject.has("city"))
                shop.setCity(jsonObject.optString("city"));
            if (jsonObject.has("address"))
                shop.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("pinCode"))
                shop.setPinCode(jsonObject.optString("pinCode"));
            if (jsonObject.has("mall"))
                shop.setMall(jsonObject.optString("mall"));
            if (jsonObject.has("MallName"))
                shop.setMallName(jsonObject.optString("MallName"));
            if (jsonObject.has("shopNo"))
                shop.setShopNo(jsonObject.optString("shopNo"));
            if (jsonObject.has("floor"))
                shop.setFloor(jsonObject.optString("floor"));
            if (jsonObject.has("updateDate"))
                shop.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("openTime"))
                shop.setOpenTime(jsonObject.optString("openTime"));
            if (jsonObject.has("closeTime"))
                shop.setCloseTime(jsonObject.optString("closeTime"));
            if (jsonObject.has("description"))
                shop.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("profilepic"))
                shop.setProfilepic(jsonObject.optString("profilepic"));
            if (jsonObject.has("banner"))
                shop.setBanner(jsonObject.optString("banner"));
            if (jsonObject.has("shopCategory"))
                shop.setShopCategory(jsonObject.optString("shopCategory"));
            if (jsonObject.has("shopSubCategory"))
                shop.setShopSubCategory(jsonObject.optString("shopSubCategory"));
            if (jsonObject.has("rating"))
                shop.setRating(jsonObject.optString("rating"));
            if (jsonObject.has("shopOfferCount"))
                shop.setShopOfferCount(jsonObject.optInt("shopOfferCount"));
            if (jsonObject.has("favStatus"))
                shop.setFavStatus(jsonObject.optInt("favStatus"));

            shopArrayList.add(shop);
            Log.d("Response", shopArrayList.toString());
        }
        return shopArrayList;
    }
}
