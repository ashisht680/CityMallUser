package com.javinindia.citymalls.apiparsing.masterCategoryParsing;

import android.util.Log;

import com.javinindia.citymalls.apiparsing.base.ApiBaseData;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 15-11-2016.
 */
public class MasterCategoryListResponse extends ApiBaseData {
    private ArrayList<CategoryList> categoryListArrayList;

    public ArrayList<CategoryList> getCategoryListArrayList() {
        return categoryListArrayList;
    }

    public void setCategoryListArrayList(ArrayList<CategoryList> categoryListArrayList) {
        this.categoryListArrayList = categoryListArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setStatus(jsonObject.optInt("status"));
            setMsg(jsonObject.optString("msg"));
            if (jsonObject.has("CategoryList") && jsonObject.optJSONArray("CategoryList")!=null)
                setCategoryListArrayList(getCategoryListMethod(jsonObject.optJSONArray("CategoryList")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<CategoryList> getCategoryListMethod(JSONArray categoryList) {
        ArrayList<CategoryList> mallDetailses = new ArrayList<>();
        for (int i = 0; i < categoryList.length(); i++) {
            CategoryList details = new CategoryList();
            JSONObject jsonObject = categoryList.optJSONObject(i);
            if (jsonObject.has("id"))
                details.setId(jsonObject.optString("id"));
            if (jsonObject.has("category"))
                details.setCategory(jsonObject.optString("category"));
            if (jsonObject.has("cat_pic"))
                details.setCat_pic(jsonObject.optString("cat_pic"));

            mallDetailses.add(details);
        }
        return mallDetailses;
    }
}
