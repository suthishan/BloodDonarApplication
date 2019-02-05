package com.suthishan.blooddonar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.PreferenceData;

public class NoInternetConnection extends AppCompatActivity {

    CheckNetwork checkNetwork;
    Button btn_refresh;
    PreferenceData preferenceData;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_no_internet_connection);
    }
}
