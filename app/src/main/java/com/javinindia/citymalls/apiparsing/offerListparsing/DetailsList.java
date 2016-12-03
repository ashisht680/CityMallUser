package com.javinindia.citymalls.apiparsing.offerListparsing;

/**
 * Created by Ashish on 22-10-2016.
 */
public class DetailsList {
    private int favStatus;
    private OfferDetails offerDetails;
    private OfferMallDetails offerMallDetails;
    private OfferShopDetails offerShopDetails;
    private OfferBrandDetails offerBrandDetails;

    public int getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(int favStatus) {
        this.favStatus = favStatus;
    }

    public OfferShopDetails getOfferShopDetails() {
        return offerShopDetails;
    }

    public void setOfferShopDetails(OfferShopDetails offerShopDetails) {
        this.offerShopDetails = offerShopDetails;
    }

    public OfferMallDetails getOfferMallDetails() {
        return offerMallDetails;
    }

    public void setOfferMallDetails(OfferMallDetails offerMallDetails) {
        this.offerMallDetails = offerMallDetails;
    }

    public OfferDetails getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(OfferDetails offerDetails) {
        this.offerDetails = offerDetails;
    }

    public OfferBrandDetails getOfferBrandDetails() {
        return offerBrandDetails;
    }

    public void setOfferBrandDetails(OfferBrandDetails offerBrandDetails) {
        this.offerBrandDetails = offerBrandDetails;
    }
}
