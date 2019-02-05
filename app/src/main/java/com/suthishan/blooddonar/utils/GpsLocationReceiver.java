package com.suthishan.blooddonar.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

public class GpsLocationReceiver extends BroadcastReceiver {
//    private GpsStatusDetector mGpsStatusDetector;

    public static final String LOCATION_CHANGED = "LOCATION_CHANGED";
    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";

    Location location;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

           /* Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
                    Toast.LENGTH_SHORT).show();
            Intent pushIntent = new Intent(context, LocationUpdateActivity.class);
            pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pushIntent);*/
//            mGpsStatusDetector = new GpsStatusDetector((Activity) context.getApplicationContext());
//            mGpsStatusDetector.checkGpsStatus();
            Intent pushIntent = new Intent(context, LocationMonitoringService.class);
            context.startService(pushIntent);
        }

    }


    /*  @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
      }

      @Override
      public void onGpsSettingStatus(boolean enabled) {
          Log.d("TAG", "onGpsSettingStatus: " + enabled);
          if(!enabled){
              mGpsStatusDetector.checkGpsStatus();
          }
      }

      @Override
      public void onGpsAlertCanceledByUser() {
          Log.d("TAG", "onGpsAlertCanceledByUser");
      }*/
}
