package com.suthishan.blooddonar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.presenter.DonorPresenter;
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.DonorViews;

import org.json.JSONException;
import org.json.JSONObject;

public class DonorActivity extends AppCompatActivity implements View.OnClickListener, DonorViews {

    PreferenceData preferenceData;
    LinearLayout blood_transfer,admin_hide,donor_list,request_block;
    Button accept,decline;

    TextView username,txt_mobile,txt_age,last_blood_date;
    ProgressDialog pDialog;
    DonorPresenter donorPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        preferenceData = new PreferenceData(this);
        donor_list = (LinearLayout) findViewById(R.id.donor_list);
        request_block = (LinearLayout) findViewById(R.id.request_block);
        accept = (Button) findViewById(R.id.accept);
        decline = (Button) findViewById(R.id.decline);
        request_block.setVisibility(View.GONE);
        username = (TextView) findViewById(R.id.username);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        last_blood_date = (TextView) findViewById(R.id.last_blood_date);
        txt_age = (TextView) findViewById(R.id.txt_age);
        donorPresenter = new DonorPresenter(this,this);

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
        if(preferenceData.getDonorRequest().equalsIgnoreCase("1")){
            request_block.setVisibility(View.VISIBLE);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String accept = "accept";
                    donorPresenter.donorAcceptDecline(preferenceData.getDonorID(), accept);
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String decline = "decline";
                    donorPresenter.donorAcceptDecline(preferenceData.getDonorID(),decline);
                }
            });
        }else{
            request_block.setVisibility(View.GONE);
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

    @Override
    public void showProgress() {
        pDialog.show();
    }

    @Override
    public void hideProgress() {
        pDialog.hide();
    }

    @Override
    public void donorListSuccess(String response) {
        Log.d("Response success",response);
        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            Log.d("status-->",status);
            Log.d("message-->",message);

           if (status.equalsIgnoreCase("true")) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                JSONObject jsonObject1 = jsonObject.getJSONObject("donor_data");
                String blood_request = jsonObject1.getString("blood_request");
                if(blood_request.equalsIgnoreCase("0")){
                    request_block.setVisibility(View.GONE);
                }else{
                    request_block.setVisibility(View.VISIBLE);
                }
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
                        jsonObject1.getString("last_blood_donated_date"),
                        jsonObject1.getString("blood_request"),
                        jsonObject1.getString("req_from"));


            }else{
                Log.d("message---->", message);
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void donorListError(String string) {
        Log.d("Response error",string);

    }
}
