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
import com.suthishan.blooddonar.interactor.LoginInteractor;
import com.suthishan.blooddonar.views.LoginViews;
import com.suthishan.blooddonar.volley.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter implements LoginInteractor {

    private LoginViews loginViews;
    private Activity activity;

    public LoginPresenter(Activity activity, LoginViews loginViews) {
        this.loginViews = loginViews;
        this.activity = activity;
    }

    @Override
    public void loginCheck(final String username, final String password) {
        loginViews.showProgress();

        String url = AppUrl.BASE_URL + AppUrl.LOGIN;
        Log.d("username-->",username);
        Log.d("password", password);

        String urlcall = "https://bloodproject.azurewebsites.net/api/Home/"+AppUrl.LOGIN+"?username="+username+"&password="+password;

        Log.e("urlcall-->",urlcall);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlcall, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loginViews.hideProgress();
                Log.d("success",response);
                loginViews.loginSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginViews.hideProgress();
                Log.d(" error",error.toString());
                loginViews.loginError(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("password",password);
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
        VolleySingleton.getInstance(activity).getRequestQueue().getCache().remove(url);


    }
}
