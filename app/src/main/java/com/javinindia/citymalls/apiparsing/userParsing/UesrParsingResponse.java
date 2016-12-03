package com.javinindia.citymalls.apiparsing.userParsing;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ashish on 12-11-2016.
 */
public class UesrParsingResponse {
    private int status;
    private String msg;
    private String userid;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String address;
    private String coutnry;
    private String state;
    private String city;
    private String prof_pic;
    private String verifyOtp;
    private String emailVerify;

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

    public String getCoutnry() {
        return coutnry;
    }

    public void setCoutnry(String coutnry) {
        this.coutnry = coutnry;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(String emailVerify) {
        this.emailVerify = emailVerify;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf_pic() {
        return prof_pic;
    }

    public void setProf_pic(String prof_pic) {
        this.prof_pic = prof_pic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVerifyOtp() {
        return verifyOtp;
    }

    public void setVerifyOtp(String verifyOtp) {
        this.verifyOtp = verifyOtp;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.has("status"))
                setStatus(jsonObject.optInt("status"));
            if (jsonObject.has("msg"))
                setMsg(jsonObject.optString("msg"));
            if (jsonObject.has("userid"))
                setUserid(jsonObject.optString("userid"));
            if (jsonObject.has("name"))
                setName(jsonObject.optString("name"));
            if (jsonObject.has("email"))
                setEmail(jsonObject.optString("email"));
            if (jsonObject.has("phone"))
                setPhone(jsonObject.optString("phone"));
            if (jsonObject.has("gender"))
                setGender(jsonObject.optString("gender"));
            if (jsonObject.has("dob"))
                setDob(jsonObject.optString("dob"));
            if (jsonObject.has("address"))
                setAddress(jsonObject.optString("address"));
            if (jsonObject.has("coutnry"))
                setCoutnry(jsonObject.optString("coutnry"));
            if (jsonObject.has("state"))
                setState(jsonObject.optString("state"));
            if (jsonObject.has("city"))
                setCity(jsonObject.optString("city"));
            if (jsonObject.has("prof_pic"))
                setProf_pic(jsonObject.optString("prof_pic"));
            if (jsonObject.has("verifyOtp"))
                setVerifyOtp(jsonObject.optString("verifyOtp"));
            if (jsonObject.has("emailVerify"))
                setEmailVerify(jsonObject.optString("emailVerify"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
