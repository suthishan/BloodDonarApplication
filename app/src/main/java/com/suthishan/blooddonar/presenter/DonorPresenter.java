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
import com.suthishan.blooddonar.interactor.DonorInteractor;
import com.suthishan.blooddonar.views.DonorViews;
import com.suthishan.blooddonar.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class DonorPresenter implements DonorInteractor {

    private DonorViews donorViews;
    private Activity activity;

    public DonorPresenter(Activity activity, DonorViews donorViews){
        this.activity = activity;
        this.donorViews = donorViews;
    }

    @Override
    public void getDonorList() {
        donorViews.showProgress();

        String url = AppUrl.BASE_URL + AppUrl.DONOR_LIST;


        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+AppUrl.DONOR_LIST;

        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                donorViews.hideProgress();
                Log.d("success",response);
                donorViews.donorListSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                donorViews.hideProgress();
                Log.d(" error",error.toString());
                donorViews.donorListError(error.toString());
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
    public void donorAcceptDecline(String d_id, String message) {
        donorViews.showProgress();

        String url = AppUrl.BASE_URL + AppUrl.DONOR_LIST;


        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+AppUrl.ACCEPT_DECLINE+"?d_id="+d_id+"&decision="+message;

        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                donorViews.hideProgress();
                Log.d("success",response);
                donorViews.donorListSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                donorViews.hideProgress();
                Log.d(" error",error.toString());
                donorViews.donorListError(error.toString());
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
}
