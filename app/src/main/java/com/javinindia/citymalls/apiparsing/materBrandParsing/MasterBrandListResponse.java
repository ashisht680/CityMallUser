package com.javinindia.citymalls.apiparsing.materBrandParsing;

import android.util.Log;

import com.javinindia.citymalls.apiparsing.base.ApiBaseData;
import com.javinindia.citymalls.apiparsing.masterCategoryParsing.CategoryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 15-11-2016.
 */
public class MasterBrandListResponse extends ApiBaseData{
    private ArrayList<BrandList> brandListArrayList;

    public ArrayList<BrandList> getBrandListArrayList() {
        return brandListArrayList;
    }

    public void setBrandListArrayList(ArrayList<BrandList> brandListArrayList) {
        this.brandListArrayList = brandListArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setStatus(jsonObject.optInt("status"));
            setMsg(jsonObject.optString("msg"));
            if (jsonObject.has("BrandList") && jsonObject.optJSONArray("BrandList")!=null)
                setBrandListArrayList(getBrandListMethod(jsonObject.optJSONArray("BrandList")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<BrandList> getBrandListMethod(JSONArray brandList) {
        ArrayList<BrandList> brandLists = new ArrayList<>();
        for (int i = 0; i < brandList.length(); i++) {
            BrandList details = new BrandList();
            JSONObject jsonObject = brandList.optJSONObject(i);
            if (jsonObject.has("id"))
                details.setId(jsonObject.optString("id"));
            if (jsonObject.has("name"))
                details.setName(jsonObject.optString("name"));
            if (jsonObject.has("logo"))
                details.setLogo(jsonObject.optString("logo"));

            brandLists.add(details);
        }
        return brandLists;
    }
}
