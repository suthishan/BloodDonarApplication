package com.suthishan.blooddonar.utils;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.suthishan.blooddonar.constants.AppVariables;

import static com.suthishan.blooddonar.constants.AppVariables.FASTEST_LOCATION_INTERVAL;
import static com.suthishan.blooddonar.constants.AppVariables.LATITUDE;
import static com.suthishan.blooddonar.constants.AppVariables.LOCATION_INTERVAL;
import static com.suthishan.blooddonar.constants.AppVariables.LONGITUDE;


/**
 * Created by Suthishan on 20/1/2018.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = LocationService.class.getSimpleName();
    GoogleApiClient mLocationClient;

    LocationRequest mLocationRequest = new LocationRequest();


    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            return;
        }
        if (mLocationClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

            Log.d(TAG, "Connected to Google API");
        }else{
            Log.d(TAG, "Not Connected to Google API");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, "Location changed");

        if (location != null) {
            Log.d(TAG, "== location != null");
            Log.d(TAG, "== location != null");
            Log.d(TAG, "Latitude"+String.valueOf(location.getLatitude()));
            Log.d(TAG, "Longitude"+String.valueOf(location.getLongitude()));

            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }

    }

    private void sendMessageToUI(String lat, String longi) {

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(LATITUDE, lat);
        intent.putExtra(LONGITUDE, longi);
        Log.d("Latitude",lat);
        Log.d("Longitude",longi);

        AppVariables.LATITUDE=lat;
        AppVariables.LONGITUDE=longi;

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
