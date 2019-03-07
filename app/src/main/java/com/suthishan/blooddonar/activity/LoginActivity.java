package com.suthishan.blooddonar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.presenter.LoginPresenter;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.GpsLocationReceiver;
import com.suthishan.blooddonar.utils.LocationMonitoringService;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.LoginViews;

import net.alexandroid.gps.GpsStatusDetector;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginViews, GpsStatusDetector.GpsStatusDetectorCallBack {

    TextView sign_up, sign_up_seekar;
    TextInputEditText username, password;
    ProgressDialog pDialog;
    PreferenceData preferenceData;
    LoginPresenter loginPresenter;
    Button sign_in;
    String userName, passWord;
    CheckNetwork checkNetwork;
    private boolean mAlreadyStartedService = false;
    GpsLocationReceiver gpsReceiver;
    private GpsStatusDetector mGpsStatusDetector;
    boolean mISGpsStatusDetector;
    boolean doubleBackToExitPressedOnce = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInterface();
        onClicklistner();
    }

    private void onClicklistner() {
        sign_up.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        sign_up_seekar.setOnClickListener(this);
    }

    private void userInterface() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        gpsReceiver = new GpsLocationReceiver();
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        if (mAlreadyStartedService) {
            startService(new Intent(this, LocationMonitoringService.class));
        }
        checkNetwork = new CheckNetwork(this);
        loginPresenter = new LoginPresenter(this, this);
        preferenceData = new PreferenceData(this);
        sign_in = (Button) findViewById(R.id.sign_in);
        sign_up = (TextView) findViewById(R.id.sign_up);
        sign_up_seekar = (TextView) findViewById(R.id.sign_up_seekar);
        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
                break;
            case R.id.sign_up_seekar:
                startActivity(new Intent(getApplicationContext(), SeekarAddActivity.class));
                finish();
                break;
            case R.id.sign_in:
                if(checkNetwork.isNetworkAvailable()){
                    loginCheck();
                }else {
                    Toast.makeText(getApplicationContext(), "No internet Connection." + checkNetwork.isNetworkAvailable(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void loginCheck() {
        userName = username.getText().toString();
        passWord = password.getText().toString();

        if(userName.equalsIgnoreCase("")){
            username.setError("Please Enter Username");
        }else if(passWord.equalsIgnoreCase("")){
            password.setError(" Please Enter Password");
        }else {
            loginPresenter.loginCheck(userName,passWord);
        }
    }

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void loginSuccess(String response) {
        Log.d("Response success",response);
        JSONObject jsonObject = null;

        try{
            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            String mode = jsonObject.getString("mode");

            if(message.equalsIgnoreCase("donor login")){
                if(mode.equalsIgnoreCase("donor")){
                    preferenceData.setDonorLogin(message);
                }
            }else if (message.equalsIgnoreCase("admin login")){
                if(mode.equalsIgnoreCase("admin")) {
                    preferenceData.setAdminLogin(message);
                }
            }else if (message.equalsIgnoreCase("seeker login")){
                if(mode.equalsIgnoreCase("seeker")){
                    preferenceData.setSeekerLogin(message);
                }
            }
            Log.d("status-->",status);
            Log.d("message-->",message);


            /*if (status.equalsIgnoreCase("true")){
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                preferenceData.setLogin(true);
                preferenceData.setMainScreenOpen(0);
                    if (message.equalsIgnoreCase("admin login")) {
                        startActivity(new Intent(getApplicationContext(), DonorActivity.class));
                        finish();
                    }else if (message.equalsIgnoreCase("head")) {
                        startActivity(new Intent(getApplicationContext(), DonorActivity.class));
                        finish();
                    }

            }else {
                AppVariables.BACK_BUTTON_GONE = false;
                finish();
                Log.d("message---->", message);
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }*/


                if (message.equalsIgnoreCase("admin login")) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    preferenceData.setAdminLogin(true);
                    preferenceData.setMainScreenOpen(0);
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    finish();
                }else if (message.equalsIgnoreCase("donor login")) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    preferenceData.setLogin(true);
                    preferenceData.setMainScreenOpen(0);
                    startActivity(new Intent(getApplicationContext(), DonorActivity.class));
                    finish();

                    JSONObject jsonObject1 = jsonObject.getJSONObject("donor_data");
                    preferenceData.userData(
                            jsonObject1.getString("d_id"),
                            jsonObject1.getString("dname"),
                            jsonObject1.getString("age"),
                            jsonObject1.getString("bloodgroup"),
                            jsonObject1.getString("gender"),
                            jsonObject1.getString("latitude"),
                            jsonObject1.getString("longitude"),
                            jsonObject1.getString("email"),
                            jsonObject1.getString("mobile"),
                            jsonObject1.getString("status"),
                            jsonObject1.getString("password"),
                            jsonObject1.getString("last_blood_donated_date"));


                }else if(message.equalsIgnoreCase("seeker login")){
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    preferenceData.setSeekerLogin(true);
                    preferenceData.setMainScreenOpen(0);
                    startActivity(new Intent(getApplicationContext(), SeekerDashboard.class));
                    finish();
                    JSONObject jsonObject2 = jsonObject.getJSONObject("seeker_data");
                    preferenceData.seekerData(jsonObject2.getString("s_id"),
                            jsonObject2.getString("sname"),
                            jsonObject2.getString("age"),
                            jsonObject2.getString("gender"),
                            jsonObject2.getString("email"),
                            jsonObject2.getString("mobile"),
                            jsonObject2.getString("latitude"),
                            jsonObject2.getString("longitude"),
                            jsonObject2.getString("password"),
                            jsonObject2.getString("status"));

                }
                else{
                    AppVariables.BACK_BUTTON_GONE = false;
                    finish();
                    Log.d("message---->", message);
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.cancel();
        }
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

    @Override
    public void loginError(String string) {
        Log.d("Response success",string);
    }

    @Override
    public void onGpsSettingStatus(boolean enabled) {
        Log.d("TAG", "onGpsSettingStatus: " + enabled);
        mISGpsStatusDetector = enabled;
        if(!enabled){
            mGpsStatusDetector.checkGpsStatus();
        }
    }

    @Override
    public void onGpsAlertCanceledByUser() {
        Log.d("TAG", "onGpsAlertCanceledByUser");
        startActivity(new Intent(getApplicationContext(),TurnOnGpsLocation.class));
    }
}
