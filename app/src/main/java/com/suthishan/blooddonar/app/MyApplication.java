package com.suthishan.blooddonar.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        super.attachBaseContext(LocaleHelper.setLocale(base,"en"));
        MultiDex.install(this);
    }
}
