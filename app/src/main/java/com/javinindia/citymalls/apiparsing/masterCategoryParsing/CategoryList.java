package com.javinindia.citymalls.apiparsing.masterCategoryParsing;

/**
 * Created by Ashish on 15-11-2016.
 */
public class CategoryList {
   private String id;
   private String category;
   private String cat_pic;

    public String getCat_pic() {
        return cat_pic;
    }

    public void setCat_pic(String cat_pic) {
        this.cat_pic = cat_pic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
