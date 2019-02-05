package com.suthishan.blooddonar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sathish on 3/15/2018.
 */

public class CheckNetwork {
    Context context;
    public CheckNetwork(Context context) {
        this.context=context;
    }

    //If you only want to check your Internet connection available or not
    public  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

