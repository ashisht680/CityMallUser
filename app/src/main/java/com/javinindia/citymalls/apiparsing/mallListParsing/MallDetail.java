package com.javinindia.citymalls.apiparsing.mallListParsing;

/**
 * Created by Ashish on 07-11-2016.
 */
public class MallDetail {
   private String id;
   private String mallName;
   private String description;
   private String mallAddress;
   private String mallLandmark;
   private String country;
   private String state;
   private String city;
   private String pinCode;
   private String rating;
   private String openTime;
   private String closeTime;
   private double distance;
   private String mallLat;
   private String mallLong;
   private String mallPic;
   private int favStatus;
   private int offerCount;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(int favStatus) {
        this.favStatus = favStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMallAddress() {
        return mallAddress;
    }

    public void setMallAddress(String mallAddress) {
        this.mallAddress = mallAddress;
    }

    public String getMallLandmark() {
        return mallLandmark;
    }

    public void setMallLandmark(String mallLandmark) {
        this.mallLandmark = mallLandmark;
    }

    public String getMallLat() {
        return mallLat;
    }

    public void setMallLat(String mallLat) {
        this.mallLat = mallLat;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public String getMallLong() {
        return mallLong;
    }

    public void setMallLong(String mallLong) {
        this.mallLong = mallLong;
    }

    public String getMallPic() {
        return mallPic;
    }

    public void setMallPic(String mallPic) {
        this.mallPic = mallPic;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }
}
