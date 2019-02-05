package com.suthishan.blooddonar.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.suthishan.blooddonar.model.DonorModel;
import com.suthishan.blooddonar.presenter.DonorPresenter;
import com.suthishan.blooddonar.utils.CallInterface;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.DonorViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DonorListActivity extends AppCompatActivity implements DonorViews, CallInterface {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    SwipeRefreshLayout swifeRefresh;
    RecyclerView rec_donor_list;
    TextView txt_no_records_found;
    ProgressDialog pDialog;
    PreferenceData preferenceData;
    private List<DonorModel.Donor_data> donor_data;
    DonorModel.Donor_data donorData;
    boolean isDataUpdate=true;
    CheckNetwork checkNetwork;
    DonorPresenter donorPresenter;
    private DonorAdpater donorAdpater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);
        userInterface();
        onClicklistner();
        showActionBar();
    }

    private void onClicklistner() {

    }

    private void userInterface() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        checkNetwork = new CheckNetwork(this);
        preferenceData = new PreferenceData(this);
        donorPresenter = new DonorPresenter(this,this);
        swifeRefresh = (SwipeRefreshLayout) findViewById(R.id.swifeRefresh);
        rec_donor_list = (RecyclerView) findViewById(R.id.rec_donor_list);
        txt_no_records_found = (TextView) findViewById(R.id.txt_no_records_found);
        if(checkNetwork.isNetworkAvailable()){
            donorPresenter.getDonorList();
        }else {
            Toast.makeText(getApplicationContext(), "No internet Connection." + checkNetwork.isNetworkAvailable(), Toast.LENGTH_LONG).show();
        }

        donor_data = new ArrayList<>();
        txt_no_records_found.setVisibility(View.GONE);
        rec_donor_list.setVisibility(View.GONE);
        rec_donor_list.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DonorListActivity.this);
        rec_donor_list.setLayoutManager(mLayoutManager);
        rec_donor_list.setItemAnimator(new DefaultItemAnimator());
        if(donorAdpater != null){
            donorAdpater.notifyDataSetChanged();
        }else {
            donorAdpater = new DonorAdpater(donor_data, DonorListActivity.this, this);
            rec_donor_list.setAdapter(donorAdpater);
        }

    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Donor List");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        if (!DonorListActivity.this.isDataUpdate) {
            pDialog.show();
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
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void donorListSuccess(String response) {
        Log.d("Response success",response);

        try {
            JSONObject mJsnobject = new JSONObject(response);
            String status = mJsnobject.getString("status");
            if (status.equalsIgnoreCase("true")) {
                txt_no_records_found.setVisibility(View.GONE);
                rec_donor_list.setVisibility(View.VISIBLE);
                JSONArray jsonArray = mJsnobject.getJSONArray("donor_data");
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
//                mNearbyList.clear();
                    donor_data.add(donorData);
                    donorAdpater.notifyDataSetChanged();
                    pDialog.hide();
                }
            }
            else{
                txt_no_records_found.setVisibility(View.VISIBLE);
                rec_donor_list.setVisibility(View.GONE);

            }
        }catch (JSONException e) {
            e.printStackTrace();
            pDialog.hide();
        }

    }

    @Override
    public void donorListError(String string) {
        Log.d("Response success",string);
    }

    @Override
    public void makeCall(String response) {

    }
}
