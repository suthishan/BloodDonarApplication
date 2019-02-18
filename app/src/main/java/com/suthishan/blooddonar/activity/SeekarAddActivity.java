package com.suthishan.blooddonar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppUrl;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.model.SeekerModel;
import com.suthishan.blooddonar.presenter.RegisterPresenter;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.GpsLocationReceiver;
import com.suthishan.blooddonar.utils.LocationMonitoringService;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.RegisterViews;

import net.alexandroid.gps.GpsStatusDetector;

import org.json.JSONException;
import org.json.JSONObject;

public class SeekarAddActivity extends AppCompatActivity implements
        GpsStatusDetector.GpsStatusDetectorCallBack, View.OnClickListener,
        AdapterView.OnItemSelectedListener, RegisterViews {

    private boolean mAlreadyStartedService = false;
    GpsLocationReceiver gpsReceiver;
    private GpsStatusDetector mGpsStatusDetector;
    boolean mISGpsStatusDetector;
    PreferenceData preferenceData;
    RegisterPresenter registerPresenter;
    ProgressDialog pDialog;
    CheckNetwork checkNetwork;
    TextInputLayout til_s_name, til_age, til_email, til_mobile;
    EditText edt_sname, edt_age, edt_email, edt_mobile;
    Spinner gender_list;
    Button sign_up;
    String seekerName, seekerAge, seekerEmail, seekerMobile, genderList;
    SeekerModel seekerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekar_add);
        ActionBar();
        userInterface();
        onClicklistner();
        OnItemSelectedListener();
    }

    private void userInterface() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        registerPresenter = new RegisterPresenter(this,this);
        preferenceData = new PreferenceData(this);
        gpsReceiver = new GpsLocationReceiver();
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        if (mAlreadyStartedService) {
            startService(new Intent(this, LocationMonitoringService.class));
        }
        til_s_name = (TextInputLayout) findViewById(R.id.til_s_name);
        til_age = (TextInputLayout) findViewById(R.id.til_age);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_mobile = (TextInputLayout) findViewById(R.id.til_mobile);
        edt_sname = (EditText) findViewById(R.id.edt_sname);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        gender_list = (Spinner) findViewById(R.id.gender_list);
        sign_up = (Button) findViewById(R.id.sign_up);
    }
    private void ActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void OnItemSelectedListener() {
        gender_list.setOnItemSelectedListener(this);
    }

    private void onClicklistner() {
        sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                registerValuesSend();
                break;
        }
    }

    private void registerValuesSend() {
        edtTextValues();
        if(seekerName.equalsIgnoreCase("")){
            showAlert("Seeker Name is Empty");
        }else if(seekerAge.equalsIgnoreCase("")){
            showAlert("Seeker Age is Empty");
        }else if(seekerEmail.equalsIgnoreCase("")){
            showAlert("Seeker Email is Empty");
        }else if(seekerMobile.equalsIgnoreCase("")){
            showAlert("Seeker Mobile is Empty");
        }else if(genderList.equalsIgnoreCase("--Select--")){
            showAlert("Gender is Empty");
        }else {
            seekerModel = new SeekerModel();
            if(checkNetwork.isNetworkAvailable()) {
                seekerModel.setLatitude(AppVariables.NEAR_LATITUDE);
                seekerModel.setLongitude(AppVariables.NEAR_LONGITUDE);
                seekerModel.setAge(seekerAge);
                seekerModel.setEmail(seekerEmail);
                seekerModel.setMobile(seekerMobile);
                seekerModel.setGender(genderList);
                seekerModel.setSname(seekerName);
                registerPresenter.seekerValue(AppUrl.ADD_SEEKER, seekerModel);
            }else{
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void edtTextValues() {
        seekerName = edt_sname.getText().toString();
        seekerAge = edt_age.getText().toString();
        seekerEmail = edt_email.getText().toString();
        seekerMobile = edt_mobile.getText().toString();
    }

    private void showAlert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gender_list:
                genderList = parent.getSelectedItem().toString();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
    public void onDestroy(){
        super.onDestroy();
        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.cancel();
        }
    }
    @Override
    public void registerSuccess(String response) {

    }

    @Override
    public void regsiterError(String string) {

    }

    @Override
    public void seekerSuccess(String response) {
        Log.d("Response success",response);
        JSONObject jsonObject = null;

        try{
            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            Log.d("status-->",status);
            Log.d("message-->",message);


            if (status.equalsIgnoreCase("true")){
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
//                Toast.makeText(getApplicationContext(),message+"\n"+username+"\n"+password,Toast.LENGTH_LONG).show();
                final Toast toast = Toast.makeText(getApplicationContext(), message+"\n"+username+"\n"+password, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 19000);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }else {
                Log.d("message---->", message);
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void seekarError(String string) {
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
