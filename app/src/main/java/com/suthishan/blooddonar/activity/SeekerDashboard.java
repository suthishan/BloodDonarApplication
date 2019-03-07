package com.suthishan.blooddonar.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.utils.PreferenceData;

public class SeekerDashboard extends AppCompatActivity implements View.OnClickListener {

    TextView username,txt_email;
    PreferenceData preferenceData;
    Button request_blood;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_dashboard);
        ui();
        onClicklistiner();
    }

    private void onClicklistiner() {
        request_blood.setOnClickListener(this);
    }

    private void ui() {
        preferenceData = new PreferenceData(this);
        txt_email = (TextView) findViewById(R.id.txt_email);
        username = (TextView) findViewById(R.id.username);
        request_blood = (Button) findViewById(R.id.request_blood);
        txt_email.setText(preferenceData.getseekerEmail());
        username.setText("Welcome "+preferenceData.getseekerName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request_blood:
            startActivity(new Intent(getApplicationContext(), RequestBlood.class));
            finish();
            break;
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
            preferenceData.setSeekerLogin(false);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}

