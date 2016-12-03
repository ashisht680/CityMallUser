package com.javinindia.citymalls.apiparsing.stateparsing;

import java.util.ArrayList;

public class StateDetails {

    private String state;
    private ArrayList<CityDetails> cityDetailsArrayList;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<CityDetails> getCityDetailsArrayList() {
        return cityDetailsArrayList;
    }

    public void setCityDetailsArrayList(ArrayList<CityDetails> cityDetailsArrayList) {
        this.cityDetailsArrayList = cityDetailsArrayList;
    }
}
