package com.suthishan.blooddonar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.suthishan.blooddonar.constants.AppVariables;

public class PreferenceData {
    SharedPreferences sharedPreferences;
    public PreferenceData(Context context) {
        sharedPreferences = context.getSharedPreferences(AppVariables.PREF_NAME, Context.MODE_PRIVATE);
    }
    public SharedPreferences getPreference() {
        return sharedPreferences;
    }

    @SuppressLint("ApplySharedPref")
    public void setLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(AppVariables.USER_LOGIN, isLogin).apply();
    }
    public boolean getLogin() {
        return  sharedPreferences.getBoolean(AppVariables.USER_LOGIN, Boolean.parseBoolean(""));
    }

    public void setAdminLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean(AppVariables.ADMIN_LOGIN_DETAIL, isLogin).apply();
    }
    public boolean getAdminLoginDetails() {
        return  sharedPreferences.getBoolean(AppVariables.ADMIN_LOGIN_DETAIL, Boolean.parseBoolean(""));
    }


    public void setCurentAdress(String strAddress) {
        sharedPreferences.edit().putString(AppVariables.CURENT_ADDRESS, strAddress).apply();
    }

    public void setCurentlatitude(String latitude) {
        sharedPreferences.edit().putString(AppVariables.CURENT_LAT, latitude).apply();
    }

    @SuppressLint("ApplySharedPref")
    public void setCurentlongitude(String longitude) {
        sharedPreferences.edit().putString(AppVariables.CURENT_LON, longitude).apply();
    }

    public String gettCurentAdress() {
        return  sharedPreferences.getString(AppVariables.CURENT_ADDRESS, "");
    }
    public String gettCurentlatitude() {
        return  sharedPreferences.getString(AppVariables.CURENT_LAT, "");
    }
    public String gettCurentlongitude() {
        return  sharedPreferences.getString(AppVariables.CURENT_LON, "");
    }

    public void setMainScreenOpen(int count) {
        sharedPreferences.edit().putString(AppVariables.isMainActivityOpen_Count, String.valueOf(count)).apply();
    }

    public String getMainScreenOpen() {
        return sharedPreferences.getString(AppVariables.isMainActivityOpen_Count,"");
    }

    public void setDonorLogin(String message) {
        sharedPreferences.edit().putString(AppVariables.DONOR_LOGIN, message).apply();
    }

    public String getDonorLogin(){
        return  sharedPreferences.getString(AppVariables.DONOR_LOGIN, "");
    }

    public void setAdminLogin(String message) {
        sharedPreferences.edit().putString(AppVariables.ADMIN_LOGIN, message).apply();
    }

    public String getAdminLogin(){
        return  sharedPreferences.getString(AppVariables.ADMIN_LOGIN, "");
    }

    public void userData(String d_id, String dname, String age,
                         String bloodgroup, String gender, String latitude,
                         String longitude, String email, String mobile,
                         String status, String password) {

        sharedPreferences.edit().putString(AppVariables.DONOR_ID, d_id).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_NAME, dname).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_AGE, age).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_BLOODGROUP, bloodgroup).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_GENDER, gender).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_LATITUDE, latitude).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_LONGITUDE, longitude).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_EMAIL, email).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_MOBILE, mobile).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_STATUS, status).commit();
        sharedPreferences.edit().putString(AppVariables.DONOR_PASSWORD, password).commit();

    }
    public String getDonorID(){
        return sharedPreferences.getString(AppVariables.DONOR_ID,"");
    }
    public String getDonorName(){
        return sharedPreferences.getString(AppVariables.DONOR_NAME,"");
    }
    public String getDonorIAGE(){
        return sharedPreferences.getString(AppVariables.DONOR_AGE,"");
    }
    public String getDonorBloodGroup(){
        return sharedPreferences.getString(AppVariables.DONOR_BLOODGROUP,"");
    }
    public String getDonorGender(){
        return sharedPreferences.getString(AppVariables.DONOR_GENDER,"");
    }
    public String getDonorEmail(){
        return sharedPreferences.getString(AppVariables.DONOR_EMAIL,"");
    }
    public String getDonorStatus(){
        return sharedPreferences.getString(AppVariables.DONOR_STATUS,"");
    }
    public String getDonorMobile(){
        return sharedPreferences.getString(AppVariables.DONOR_MOBILE,"");
    }
}
