package com.suthishan.blooddonar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
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
import com.suthishan.blooddonar.utils.PreferenceData;
import com.suthishan.blooddonar.views.LoginViews;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginViews {

    TextView sign_up;
    TextInputEditText username, password;
    ProgressDialog pDialog;
    PreferenceData preferenceData;
    LoginPresenter loginPresenter;
    Button sign_in;
    String userName, passWord;
    CheckNetwork checkNetwork;


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
    }

    private void userInterface() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Please Wait ...");
        checkNetwork = new CheckNetwork(this);
        loginPresenter = new LoginPresenter(this, this);
        preferenceData = new PreferenceData(this);
        sign_in = (Button) findViewById(R.id.sign_in);
        sign_up = (TextView) findViewById(R.id.sign_up);
        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
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
                preferenceData.setDonorLogin(message);
            }else if (message.equalsIgnoreCase("admin login")){
                preferenceData.setAdminLogin(message);
            }
            Log.d("status-->",status);
            Log.d("message-->",message);


            /*if (status.equalsIgnoreCase("true")){
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                preferenceData.setLogin(true);
                preferenceData.setMainScreenOpen(0);
                    if (message.equalsIgnoreCase("admin login")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else if (message.equalsIgnoreCase("head")) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else if (message.equalsIgnoreCase("donor login")) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    preferenceData.setLogin(true);
                    preferenceData.setMainScreenOpen(0);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                            jsonObject1.getString("password"));


                }else{
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
    public void loginError(String string) {
        Log.d("Response success",string);
    }
}
