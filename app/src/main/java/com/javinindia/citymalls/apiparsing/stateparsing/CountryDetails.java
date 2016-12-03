package com.javinindia.citymalls.apiparsing.stateparsing;

import java.util.ArrayList;

public class CountryDetails {

    private String country;
    private ArrayList<StateDetails> stateDetailsArrayList;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ArrayList<StateDetails> getStateDetailsArrayList() {
        return stateDetailsArrayList;
    }

    public void setStateDetailsArrayList(ArrayList<StateDetails> stateDetailsArrayList) {
        this.stateDetailsArrayList = stateDetailsArrayList;
    }
}
