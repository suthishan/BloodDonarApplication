package com.suthishan.blooddonar.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.suthishan.blooddonar.BuildConfig;
import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppUrl;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.utils.CheckNetwork;
import com.suthishan.blooddonar.utils.LocationMonitoringService;
import com.suthishan.blooddonar.utils.PreferenceData;

import net.alexandroid.gps.GpsStatusDetector;

import java.util.Calendar;

public class StartUpScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GpsStatusDetector.GpsStatusDetectorCallBack,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = StartUpScreen.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 12;
    private boolean mAlreadyStartedService = false;
    private static int SPLASH_TIME_OUT = 3000;
    protected LocationManager mLocationManager;
    private static final int GPS_ENABLE_REQUEST = 0x1001;
    private static final int WIFI_ENABLE_REQUEST = 0x1006;
    private AlertDialog mInternetDialog;
    private AlertDialog mGPSDialog;
    private GpsStatusDetector mGpsStatusDetector;
    boolean mISGpsStatusDetector;

    CheckNetwork networkCheck;
    PreferenceData preferenceData;
    Calendar mCurrentDate;
    int day, month, year, hour, minute, sec;
    TextView Create;

    String strAddress="", strTime;

    IntentFilter intentFilter;
    int deviceApi = Build.VERSION.SDK_INT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        networkCheck = new CheckNetwork(this);
        preferenceData = new PreferenceData(this);
        checkAPiVersion();
        mGpsStatusDetector = new GpsStatusDetector(this);
        mGpsStatusDetector.checkGpsStatus();

        startStep1();
        /*if (mAlreadyStartedService) {
            if (!preferenceData.getLogin()) {
                stopService(new Intent(this, LocationService.class));
                mAlreadyStartedService = false;
            }
        }*/
    }
    private void checkAPiVersion() {
        if (deviceApi <= Build.VERSION_CODES.KITKAT) {
            startActivity(new Intent(this, LowerVersionActivity.class));
            finish();
        } else {
            startStep1();
            if(networkCheck.isNetworkAvailable()) {
                startService(new Intent(this, LocationMonitoringService.class));
                LocalBroadcastManager.getInstance(this).registerReceiver(
                        new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String latitude = intent.getStringExtra(AppVariables.EXTRA_LATITUDE);
                                String longitude = intent.getStringExtra(AppVariables.EXTRA_LONGITUDE);
                                String mylocaton = latitude + "\t" + longitude;
                                Log.d(StartUpScreen.class.getSimpleName(),"my location "+mylocaton);
                                if (latitude != null && longitude != null) {
//                                        strAddress = getCompleteAddressString(latitude, longitude);
//                                        preferenceData.setCurentAdress(strAddress);
                                    Log.d(StartUpScreen.class.getSimpleName(),"my latitude "+latitude);
                                    Log.d(StartUpScreen.class.getSimpleName(),"my longitude "+longitude);

                                    preferenceData.setCurentlatitude(latitude);
                                    preferenceData.setCurentlongitude(longitude);
//                                        Log.d(RegisterActivity.class.getSimpleName(),"strAddress "+strAddress);

                                    AppVariables.NEAR_LATITUDE = latitude;
                                    AppVariables.NEAR_LONGITUDE = longitude;
//                                        Log.d(RegisterActivity.class.getSimpleName(),"call location update api ");
                                }
                            }
                        }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
                );

            }else{
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        startStep1();
    }

    private void startStep1() {
        if (isGooglePlayServicesAvailable()) {
            startStep2(null);
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }

    }

    private Boolean startStep2(DialogInterface dialog) {
        Log.e(StartUpScreen.class.getSimpleName(),"startStep2");

        if (preferenceData.getAdminLoginDetails()){
            preferenceData.setMainScreenOpen(0);
            Intent i = new Intent(StartUpScreen.this, DashboardActivity.class);
            startActivity(i);
        }else if(preferenceData.getSeekerLoggged()){
            preferenceData.setMainScreenOpen(0);
            Intent i = new Intent(StartUpScreen.this, SeekerDashboard.class);
            startActivity(i);
        } else if(preferenceData.getLogin()){
            preferenceData.setMainScreenOpen(0);
            Intent i = new Intent(StartUpScreen.this, DonorActivity.class);
            startActivity(i);

        } else{
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                if(preferenceData.getLogin()){
                    startStep3();
                    return false;
                }else{
                    checkInternetConnection();
                    return false;
                }
            }
            if (dialog != null) {
                dialog.dismiss();
            }

            if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
                startStep3();
            } else {  //No user has not granted the permissions yet. Request now.
                requestPermissions();
            }
            return true;
        }
        return true;
    }

    private void startStep3() {
        if (!mAlreadyStartedService) {
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);
            mAlreadyStartedService = true;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferenceData.getAdminLoginDetails()){
                    preferenceData.setMainScreenOpen(0);
                    Intent i = new Intent(StartUpScreen.this, DashboardActivity.class);
                    startActivity(i);
                }else if(preferenceData.getSeekerLoggged()){
                    preferenceData.setMainScreenOpen(0);
                    Intent i = new Intent(StartUpScreen.this, SeekerDashboard.class);
                    startActivity(i);
                }else if (preferenceData.getLogin()) {
                    preferenceData.setMainScreenOpen(0);
                    Intent i = new Intent(StartUpScreen.this, DonorActivity.class);
                    startActivity(i);
                } else {
                    preferenceData.setMainScreenOpen(0);
                    Intent i = new Intent(StartUpScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    private void checkInternetConnection() {
        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), NoInternetConnection.class));
        finish();
    }

    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionState3 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permissionState4 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionState5 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionState6 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionState7 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionState8 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);
        int permissionState9 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED &&
                permissionState3 == PackageManager.PERMISSION_GRANTED && permissionState4 == PackageManager.PERMISSION_GRANTED
                && permissionState5 == PackageManager.PERMISSION_GRANTED && permissionState6 == PackageManager.PERMISSION_GRANTED
                && permissionState7 == PackageManager.PERMISSION_GRANTED && permissionState8 == PackageManager.PERMISSION_GRANTED
                && permissionState9 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean shouldProvideRationale3 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.INTERNET);
        boolean shouldProvideRationale4 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);
        boolean shouldProvideRationale5 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS);
        boolean shouldProvideRationale6 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean shouldProvideRationale7 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean shouldProvideRationale8 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE);
        boolean shouldProvideRationale9 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_PHONE_STATE);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2 || shouldProvideRationale3 || shouldProvideRationale4
                || shouldProvideRationale5 || shouldProvideRationale6 || shouldProvideRationale7 || shouldProvideRationale8 || shouldProvideRationale9) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(StartUpScreen.this,
                                    new String[]
                                            {
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.INTERNET,
                                                    Manifest.permission.CAMERA,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.CALL_PHONE,
                                                    Manifest.permission.SEND_SMS,
                                                    Manifest.permission.READ_PHONE_STATE

                                            },
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]
                            {
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.SEND_SMS,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_PHONE_STATE
                            },
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }

    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();
            } else {
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    @Override
    public void onDestroy() {
        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
}
