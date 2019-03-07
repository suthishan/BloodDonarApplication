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
import com.suthishan.blooddonar.interactor.SeekerInteractor;
import com.suthishan.blooddonar.views.DonorViews;
import com.suthishan.blooddonar.views.SeekerViews;
import com.suthishan.blooddonar.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class SeekerPresenter implements SeekerInteractor {

    SeekerViews seekerViews;
    Activity activity;

    public SeekerPresenter(Activity activity, SeekerViews seekerViews){
        this.activity = activity;
        this.seekerViews = seekerViews;
    }
    @Override
    public void getSeekerList() {
        seekerViews.showProgress();
        String url = AppUrl.BASE_URL + AppUrl.SEEKER_LIST;

        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+AppUrl.SEEKER_LIST;
        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                seekerViews.hideProgress();
                Log.d("success",response);
                seekerViews.seekerListSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                seekerViews.hideProgress();
                Log.d(" error",error.toString());
                seekerViews.seekerListError(error.toString());
            }
        }) {
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
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);
    }

    @Override
    public void requestBlood(String latitude, String longitude, String s_id, String bloodGroup) {
        seekerViews.showProgress();

        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+
                AppUrl.SEEKER_REQUEST_BLOOD+"?lat="+latitude+"&long="+longitude+"&s_id="+s_id+"&bloodgroup="+bloodGroup;
        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                seekerViews.hideProgress();
                Log.d("success",response);
                seekerViews.requestbloodSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                seekerViews.hideProgress();
                Log.d(" error",error.toString());
                seekerViews.requestbloodError(error.toString());
            }
        }) {
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
    public void sendRequest(String d_id, String s_id) {
        seekerViews.showProgress();

        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+
                AppUrl.REQUEST_BLOOD+"?d_id="+d_id+"&s_id="+s_id;
        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                seekerViews.hideProgress();
                Log.d("success",response);
                seekerViews.sendRequestSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                seekerViews.hideProgress();
                Log.d(" error",error.toString());
                seekerViews.sendReqeustError(error.toString());
            }
        }) {
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
