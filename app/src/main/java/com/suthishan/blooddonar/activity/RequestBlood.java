package com.suthishan.blooddonar.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.adpater.DonorAdpater;
import com.suthishan.blooddonar.adpater.DonorReqAdpater;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.model.DonorModel;
import com.suthishan.blooddonar.presenter.SeekerPresenter;
import com.suthishan.blooddonar.utils.CallInterface;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.GpsLocationReceiver;
import com.suthishan.blooddonar.utils.LocationMonitoringService;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.utils.REquestBloodInterface;
import com.suthishan.blooddonar.views.SeekerViews;

import net.alexandroid.gps.GpsStatusDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestBlood extends AppCompatActivity implements
        GpsStatusDetector.GpsStatusDetectorCallBack, View.OnClickListener,
        AdapterView.OnItemSelectedListener, SeekerViews, CallInterface, REquestBloodInterface {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private boolean mAlreadyStartedService = false;
    GpsLocationReceiver gpsReceiver;
    private GpsStatusDetector mGpsStatusDetector;
    boolean mISGpsStatusDetector;
    PreferenceData preferenceData;
    Spinner blood_group;
    String bloodGroup;
    ProgressDialog pDialog;
    CheckNetwork checkNetwork;
    DonorModel.Donor_data donorData;
    private List<DonorModel.Donor_data> donor_data;
    Button submit;
    SeekerPresenter seekerPresenter;
    RecyclerView rec_donotlist;
    TextView txt_no_records_found;
    LinearLayout donor_list,request_blood;
    private DonorReqAdpater donorAdpater;
    boolean isDataUpdate=true;
//    ImageButton request_donor_blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);
        ui();
        ActionBar();
        onclickListernier();
        OnItemSelectedListener();

    }

    private void OnItemSelectedListener() {
        blood_group.setOnItemSelectedListener(this);
    }

    private void onclickListernier() {
        submit.setOnClickListener(this);
    }

    private void ui() {
        checkNetwork = new CheckNetwork(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        seekerPresenter = new SeekerPresenter(this,this);
        gpsReceiver = new GpsLocationReceiver();
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();
        if (mAlreadyStartedService) {
            startService(new Intent(this, LocationMonitoringService.class));
        }
        blood_group = (Spinner) findViewById(R.id.blood_group);
        submit = (Button) findViewById(R.id.submit);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        donor_list = (LinearLayout) findViewById(R.id.donor_list);
        donor_list.setVisibility(View.GONE);
        request_blood = (LinearLayout) findViewById(R.id.request_blood);
        donor_data = new ArrayList<>();

        donor_list.setVisibility(View.GONE);
        rec_donotlist = (RecyclerView) findViewById(R.id.rec_donotlist);
        rec_donotlist.setVisibility(View.GONE);
        rec_donotlist.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RequestBlood.this);
        rec_donotlist.setLayoutManager(mLayoutManager);
        rec_donotlist.setItemAnimator(new DefaultItemAnimator());
        if(donorAdpater != null){
            donorAdpater.notifyDataSetChanged();
        }else {
            donorAdpater = new DonorReqAdpater(donor_data, RequestBlood.this, this,this);
            rec_donotlist.setAdapter(donorAdpater);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                requestBlood();
                break;

        }
    }

    private void ActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Request Blood");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void requestBlood() {
        if (bloodGroup.equalsIgnoreCase("--Select--")){
            showAlert("Blood Group is Empty");
        }else{
            seekerPresenter.requestBlood(AppVariables.NEAR_LATITUDE, AppVariables.NEAR_LONGITUDE,
                    preferenceData.getseekerID(),bloodGroup);
        }
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (pDialog!=null && pDialog.isShowing() ){
            pDialog.cancel();
        }
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
    public void showProgress() {
    pDialog.show();
    }

    @Override
    public void hideProgress() {
    pDialog.hide();
    }

    @Override
    public void seekerListSuccess(String response) {

    }

    @Override
    public void seekerListError(String string) {

    }

    @Override
    public void requestbloodSuccess(String response) {
        Log.d("Response success",response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            if (status.equalsIgnoreCase("true")) {
                request_blood.setVisibility(View.GONE);
                txt_no_records_found.setVisibility(View.GONE);
                donor_list.setVisibility(View.VISIBLE);
                rec_donotlist.setVisibility(View.VISIBLE);
//                request_donor_blood.setVisibility(View.VISIBLE);

                JSONArray jsonArray = mJsnobject.getJSONArray("active_donor_list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    donorData = new DonorModel.Donor_data();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    donorData.setDname(jsonObject.getString("dname"));
                    donorData.setBloodgroup(jsonObject.getString("bloodgroup"));
                    donorData.setMobile(jsonObject.getString("mobile"));
                    donorData.setStatus(jsonObject.getString("status"));
                    donorData.setAge(jsonObject.getString("age"));
                    donorData.setD_id(jsonObject.getInt("d_id"));
                    donorData.setLatitude(jsonObject.getString("latitude"));
                    donorData.setLongitude(jsonObject.getString("longitude"));
                    donorData.setEmail(jsonObject.getString("email"));
                    donorData.setGender(jsonObject.getString("gender"));
//                  mNearbyList.clear();
                    donor_data.add(donorData);
                    donorAdpater.notifyDataSetChanged();
                    pDialog.hide();
                }


            }
            else{
                txt_no_records_found.setVisibility(View.VISIBLE);
                request_blood.setVisibility(View.VISIBLE);
                donor_list.setVisibility(View.GONE);
                rec_donotlist.setVisibility(View.GONE);

            }
        }catch (JSONException e) {
            e.printStackTrace();
            pDialog.hide();
        }



    }

    @Override
    public void requestbloodError(String string) {
        Log.d("Request Response error",string);
    }

    @Override
    public void sendRequestSuccess(String response) {
        Log.d("send Response success",response);
        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            String message = mJsnobject.getString("message");
            if (status.equalsIgnoreCase("true")) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            pDialog.hide();
        }

    }

    @Override
    public void sendReqeustError(String error) {
        Log.d("send Response error",error);
    }

    @Override
    public void makeCall(String response) {
        isDataUpdate=false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            requestCallPermission();

        } else {
//            Log.i(NearHospitalActivity.class.getSimpleName(),"CALL permission has already been granted. Displaying camera preview.");
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+"+response)));
        }
    }

    private void requestCallPermission() {
        Log.i(DonorListActivity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
//            Log.i(NearHospitalActivity.class.getSimpleName(),            "Displaying camera permission rationale to provide additional context.");
            Toast.makeText(getApplicationContext(),"Displaying Call permission rationale to provide additional context.",Toast.LENGTH_SHORT).show();

        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
// END_INCLUDE(camera_permission_request)
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    dial.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void requestBlood(View v, int position) {

        seekerPresenter.sendRequest(String.valueOf(donorData.getD_id()),preferenceData.getseekerID());
        donor_data.remove(position);
        rec_donotlist.getAdapter().notifyDataSetChanged();
        /*request_donor_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekerPresenter.sendRequest(d_id,preferenceData.getseekerID());
            }
        });*/
    }
}
