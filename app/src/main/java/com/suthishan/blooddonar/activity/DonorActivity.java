package com.suthishan.blooddonar.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.utils.PreferenceData;

public class DonorActivity extends AppCompatActivity implements View.OnClickListener {

    PreferenceData preferenceData;
    LinearLayout blood_transfer,admin_hide,donor_list;

    TextView username,txt_mobile,txt_age,last_blood_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceData = new PreferenceData(this);
        donor_list = (LinearLayout) findViewById(R.id.donor_list);
        username = (TextView) findViewById(R.id.username);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        last_blood_date = (TextView) findViewById(R.id.last_blood_date);
        txt_age = (TextView) findViewById(R.id.txt_age);

        onClicklistiner();

        if (AppVariables.isMainActivityOpen) {
            if (preferenceData.getMainScreenOpen().equalsIgnoreCase("0")) {
                preferenceData.setMainScreenOpen(1);
            }
        }
        if (preferenceData.getLogin()){
            username.setText("Welcome "+preferenceData.getDonorName());
            txt_mobile.setText(preferenceData.getDonorEmail());
            txt_age.setText(preferenceData.getDonorBloodGroup());
            if(preferenceData.getLastDonateDate().equalsIgnoreCase("")){
                last_blood_date.setText("No Yet Donated");
            }else if(preferenceData.getLastDonateDate().equalsIgnoreCase("null")){
                last_blood_date.setText("No Yet Donated");
            }else {
                last_blood_date.setText(preferenceData.getLastDonateDate());
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            preferenceData.setLogin(false);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClicklistiner() {
        donor_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donor_list:
                startActivity(new Intent(getApplicationContext(), SeekerListActivity.class));
                break;
        }
    }
}
