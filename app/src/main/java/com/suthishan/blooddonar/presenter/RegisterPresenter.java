package com.suthishan.blooddonar.presenter;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.suthishan.blooddonar.constants.AppUrl;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.interactor.RegisterInteractor;
import com.suthishan.blooddonar.model.RegisterModel;
import com.suthishan.blooddonar.model.SeekerModel;
import com.suthishan.blooddonar.views.RegisterViews;
import com.suthishan.blooddonar.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class RegisterPresenter implements RegisterInteractor {

    private RegisterViews registerViews;
    private Activity activity;

    public RegisterPresenter(Activity activity, RegisterViews registerViews) {
        this.registerViews = registerViews;
        this.activity = activity;
    }

    @Override
    public void registerValue(final String registerUrl, final RegisterModel registerModel) {
        registerViews.showProgress();

//        String url = AppUrl.BASE_URL + AppUrl.LOGIN;
        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+registerUrl+"?lat="+ AppVariables.NEAR_LATITUDE +"&long="+AppVariables.NEAR_LONGITUDE+"&dname="+registerModel.getDname()+"&bloodgroup="+registerModel.getBloodgroup()+"&age="+registerModel.getAge()+"&gender="+registerModel.getGender()+"&email="+registerModel.getEmail()+"&mobile="+registerModel.getMobile();
        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                registerViews.hideProgress();
                Log.d("success",response);
                registerViews.registerSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                registerViews.hideProgress();
                Log.d(" error",error.toString());
                registerViews.regsiterError(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat",AppVariables.LATITUDE);
                params.put("long",AppVariables.LONGITUDE);
                params.put("dname",registerModel.getDname());
                params.put("bloodgroup",registerModel.getBloodgroup());
                params.put("age",registerModel.getAge());
                params.put("gender",registerModel.getGender());
                params.put("email",registerModel.getEmail());
                params.put("mobile",registerModel.getMobile());
                Log.d("params--->",params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
                Log.d("Credentials ","Basic " +base64EncodedCredentials.toString());

                return header;
            }
            public int getMethod() {
                return Method.GET;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(activity).addToRequestQueue(strReq);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(urlcall);


    }

    @Override
    public void seekerValue(String seekerUrl, final SeekerModel seekerModel) {
        registerViews.showProgress();

        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+seekerUrl+"?lat="+ AppVariables.NEAR_LATITUDE +"&long="+AppVariables.NEAR_LONGITUDE+"&sname="+seekerModel.getSname()+"&age="+seekerModel.getAge()+"&gender="+seekerModel.getGender()+"&email="+seekerModel.getEmail()+"&mobile="+seekerModel.getMobile();
        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                registerViews.hideProgress();
                Log.d("success",response);
                registerViews.seekerSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                registerViews.hideProgress();
                Log.d(" error",error.toString());
                registerViews.seekarError(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat",AppVariables.LATITUDE);
                params.put("long",AppVariables.LONGITUDE);
                params.put("sname",seekerModel.getSname());
                params.put("age",seekerModel.getAge());
                params.put("gender",seekerModel.getGender());
                params.put("email",seekerModel.getEmail());
                params.put("mobile",seekerModel.getMobile());
                Log.d("params--->",params.toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = "admin" + ":" + "1234";
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/x-www-from-urlencoded; charset=utf-8");
                header.put("Authorization", "Basic " + base64EncodedCredentials);
                Log.d("Credentials ","Basic " +base64EncodedCredentials.toString());

                return header;
            }
            public int getMethod() {
                return Method.GET;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(activity).addToRequestQueue(strReq);
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(urlcall);

    }
}
