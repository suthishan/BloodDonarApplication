package com.suthishan.blooddonar.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.adpater.DonorAdpater;
import com.suthishan.blooddonar.adpater.SeekerAdpater;
import com.suthishan.blooddonar.model.DonorModel;
import com.suthishan.blooddonar.model.SeekerModel;
import com.suthishan.blooddonar.presenter.DonorPresenter;
import com.suthishan.blooddonar.presenter.SeekerPresenter;
import com.suthishan.blooddonar.utils.CallInterface;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.SeekerViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeekerListActivity extends AppCompatActivity implements SeekerViews, CallInterface {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    SwipeRefreshLayout swifeRefresh;
    RecyclerView rec_seeker_list;
    TextView txt_no_records_found;
    ProgressDialog pDialog;
    PreferenceData preferenceData;
    private List<SeekerModel.Seeker_data> seeker_data;
    SeekerModel.Seeker_data  seekerData;
    boolean isDataUpdate=true;
    CheckNetwork checkNetwork;
    SeekerPresenter seekerPresenter;
    private SeekerAdpater seekerAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_list);
        userInterface();
        onClicklistner();
        showActionBar();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Seeker List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void onClicklistner() {

    }

    private void userInterface() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        checkNetwork = new CheckNetwork(this);
        preferenceData = new PreferenceData(this);
        seekerPresenter = new SeekerPresenter(this,this);
        swifeRefresh = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
        rec_seeker_list = (RecyclerView) findViewById(R.id.rec_seeker_list);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        if(checkNetwork.isNetworkAvailable()){
            seekerPresenter.getSeekerList();
        }else {
            Toast.makeText(getApplicationContext(), "No internet Connection." + checkNetwork.isNetworkAvailable(), Toast.LENGTH_LONG).show();
        }

        seeker_data = new ArrayList<>();
        txt_no_records_found.setVisibility(View.GONE);
        rec_seeker_list.setVisibility(View.GONE);
        rec_seeker_list.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SeekerListActivity.this);
        rec_seeker_list.setLayoutManager(mLayoutManager);
        rec_seeker_list.setItemAnimator(new DefaultItemAnimator());
        if(seekerAdpater != null){
            seekerAdpater.notifyDataSetChanged();
        }else {
            seekerAdpater = new SeekerAdpater(seeker_data, SeekerListActivity.this, this);
            rec_seeker_list.setAdapter(seekerAdpater);
        }

    }

    @Override
    public void showProgress() {
        if (!SeekerListActivity.this.isDataUpdate) {
            pDialog.show();
        }
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
    public void seekerListSuccess(String response) {
        Log.d("Response success",response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            if (status.equalsIgnoreCase("true")) {
                txt_no_records_found.setVisibility(View.GONE);
                rec_seeker_list.setVisibility(View.VISIBLE);
                JSONArray jsonArray = mJsnobject.getJSONArray("seeker_data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    seekerData = new SeekerModel.Seeker_data();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    seekerData.setSname(jsonObject.getString("sname"));
                    seekerData.setS_id(Integer.parseInt(jsonObject.getString("s_id")));
                    seekerData.setAge(jsonObject.getString("age"));
                    seekerData.setEmail(jsonObject.getString("email"));
                    seekerData.setGender(jsonObject.getString("gender"));
                    seekerData.setMobile(jsonObject.getString("mobile"));
                    seekerData.setLatitude(jsonObject.getString("latitude"));
                    seekerData.setLongitude(jsonObject.getString("longitude"));
                    seekerData.setStatus(jsonObject.getString("status"));
                    seekerData.setPassword(jsonObject.getString("password"));

//                mNearbyList.clear();
                    seeker_data.add(seekerData);
                    seekerAdpater.notifyDataSetChanged();
                    pDialog.hide();
                }
            }
            else{
                txt_no_records_found.setVisibility(View.VISIBLE);
                rec_seeker_list.setVisibility(View.GONE);

            }
        }catch (JSONException e) {
            e.printStackTrace();
            pDialog.hide();
        }

    }

    @Override
    public void seekerListError(String string) {
        Log.d("Response success",string);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
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
        Log.i(SeekerListActivity.class.getSimpleName(), "CALL permission has NOT been granted. Requesting permission.");

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
}
