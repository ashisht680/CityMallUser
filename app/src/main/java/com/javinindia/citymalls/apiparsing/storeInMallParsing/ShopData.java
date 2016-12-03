package com.javinindia.citymalls.apiparsing.storeInMallParsing;

/**
 * Created by Ashish on 09-11-2016.
 */
public class ShopData {
    String id;
    String storeName;
    String ownerName;
    String mobile;
    String state;
    String city;
    String address;
    String pinCode;
    String mall;
    String MallName;
    String shopNo;
    String floor;
   // String updateDate;
    String openTime;
    String closeTime;
    String description;
    String profilepic;
    String banner;
    String shopCategory;
    String shopSubCategory;
    String rating;
    int shopOfferCount;
    int favStatus;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(int favStatus) {
        this.favStatus = favStatus;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMall() {
        return mall;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public String getMallName() {
        return MallName;
    }

    public void setMallName(String mallName) {
        MallName = mallName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public int getShopOfferCount() {
        return shopOfferCount;
    }

    public void setShopOfferCount(int shopOfferCount) {
        this.shopOfferCount = shopOfferCount;
    }

    public String getShopSubCategory() {
        return shopSubCategory;
    }

    public void setShopSubCategory(String shopSubCategory) {
        this.shopSubCategory = shopSubCategory;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
