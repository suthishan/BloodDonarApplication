package com.suthishan.blooddonar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.utils.PreferenceData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    PreferenceData preferenceData;
    LinearLayout blood_transfer,admin_hide,donor_list;

    TextView username,txt_mobile,txt_age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceData = new PreferenceData(this);
        blood_transfer = (LinearLayout) findViewById(R.id.blood_transfer);
        admin_hide = (LinearLayout) findViewById(R.id.admin_hide);
        donor_list = (LinearLayout) findViewById(R.id.donor_list);
        username = (TextView) findViewById(R.id.username);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_age = (TextView) findViewById(R.id.txt_age);
        onClicklistiner();
        if (AppVariables.isMainActivityOpen) {
            if (preferenceData.getMainScreenOpen().equalsIgnoreCase("0")) {
                preferenceData.setMainScreenOpen(1);
            }
        }
        if(preferenceData.getAdminLoginDetails()){
            blood_transfer.setVisibility(View.VISIBLE);
            admin_hide.setVisibility(View.GONE);
            username.setText("Welcome Admin");

        }else if (preferenceData.getLogin()){
            admin_hide.setVisibility(View.VISIBLE);
            username.setText("Welcome "+preferenceData.getDonorName());
            txt_mobile.setText(preferenceData.getDonorMobile());
            txt_age.setText(preferenceData.getDonorIAGE());
            blood_transfer.setVisibility(View.GONE);
        }
    }

    private void onClicklistiner() {
        donor_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donor_list:
                startActivity(new Intent(getApplicationContext(), DonorListActivity.class));
                break;
        }
    }
}
