package com.suthishan.blooddonar.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppUrl;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.model.RegisterModel;
import com.suthishan.blooddonar.presenter.RegisterPresenter;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.GpsLocationReceiver;
import com.suthishan.blooddonar.utils.LocationMonitoringService;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.RegisterViews;

import net.alexandroid.gps.GpsStatusDetector;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements
        GpsStatusDetector.GpsStatusDetectorCallBack, View.OnClickListener,
        AdapterView.OnItemSelectedListener, RegisterViews {

    private boolean mAlreadyStartedService = false;
    GpsLocationReceiver gpsReceiver;
    private GpsStatusDetector mGpsStatusDetector;
    boolean mISGpsStatusDetector;
    int deviceApi = Build.VERSION.SDK_INT;
    PreferenceData preferenceData;
    RegisterPresenter registerPresenter;
    ProgressDialog pDialog;
    CheckNetwork checkNetwork;
    TextInputLayout til_d_name, til_age, til_email, til_mobile;
    EditText edt_dname, edt_age, edt_email, edt_mobile;
    Spinner blood_group, gender_list;
    Button sign_up;
    String donarName, donarAge, donarEmail, donarMobile, bloodGroup, genderList;
    RegisterModel registerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar();
        userInterface();
        onClicklistner();
        OnItemSelectedListener();
    }

    private void OnItemSelectedListener() {
        blood_group.setOnItemSelectedListener(this);
        gender_list.setOnItemSelectedListener(this);
    }

    private void onClicklistner() {
        sign_up.setOnClickListener(this);
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
        til_d_name = (TextInputLayout) findViewById(R.id.til_d_name);
        til_age = (TextInputLayout) findViewById(R.id.til_age);
        til_email = (TextInputLayout) findViewById(R.id.til_email);
        til_mobile = (TextInputLayout) findViewById(R.id.til_mobile);
        edt_dname = (EditText) findViewById(R.id.edt_dname);
        edt_age = (EditText) findViewById(R.id.edt_age);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        blood_group = (Spinner) findViewById(R.id.blood_group);
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
        if (donarName.equalsIgnoreCase("")){
            showAlert("Donar Name is Empty");
        }else if(donarAge.equalsIgnoreCase("")){
            showAlert("Donar Age is Empty");
        }else if (donarEmail.equalsIgnoreCase("")){
            showAlert("Donar Email is Empty");
        }else if(donarMobile.equalsIgnoreCase("")){
            showAlert("Donar Mobile is Empty");
        }else if(bloodGroup.equalsIgnoreCase("--Select--")){
            showAlert("Donar Blood Group is Empty");
        }else if(genderList.equalsIgnoreCase("--Select--")){
            showAlert("Gender is Empty");
        }else{
            registerModel = new RegisterModel();
            if(checkNetwork.isNetworkAvailable()) {
                registerModel.setLatitude(AppVariables.NEAR_LATITUDE);
                registerModel.setLongitude(AppVariables.NEAR_LONGITUDE);
                registerModel.setDname(donarName);
                registerModel.setBloodgroup(bloodGroup);
                registerModel.setAge(donarAge);
                registerModel.setGender(genderList);
                registerModel.setEmail(donarEmail);
                registerModel.setMobile(donarMobile);
                registerPresenter.registerValue(AppUrl.REGISTER,registerModel);

            }else{
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void edtTextValues() {
        donarName = edt_dname.getText().toString();
        donarAge = edt_age.getText().toString();
        donarEmail = edt_email.getText().toString();
        donarMobile = edt_mobile.getText().toString();
    }

    private void showAlert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.blood_group:
                bloodGroup = parent.getSelectedItem().toString();
                break;

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
        Log.d("Response success",response);
        JSONObject jsonObject = null;

        try{
            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            Log.d("status-->",status);
            Log.d("message-->",message);


            if (status.equalsIgnoreCase("true")){
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
    public void regsiterError(String string) {
        Log.d("Response success",string);
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
    }
}
