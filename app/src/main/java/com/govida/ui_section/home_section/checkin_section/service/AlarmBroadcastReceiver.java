/**
 * @Class : AlarmBroadcastReceiver
 * @Usage : This activity is used to manage local notification and auto checkout functionality
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.govida.R;
import com.govida.app_sharedpreference.AppPreference;
import com.govida.ui_section.home_section.ActivityHome;
import com.govida.utility_section.AppConstants;

public class AlarmBroadcastReceiver extends BroadcastReceiver
        implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";
    private AppPreference mAppPreference;
    private Boolean isGoogleApiClientConnected = false;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private Long UPDATE_INTERVAL = (long) (2 * 1000);  /* 10 secs */
    private Long FASTEST_INTERVAL = (long)2000; /* 2 sec */
    private double latitude, longitude;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mAppPreference = new AppPreference(context);
        if (intent.hasExtra(AppConstants.NOTIFICATION_FLAG)) {
            if (intent.getStringExtra(AppConstants.NOTIFICATION_FLAG).equals(AppConstants.FROM_CHECKIN)) {
                if(mAppPreference.getCheckinHour() != 25) {
                    createNotification(context.getString(R.string.app_name), context.getString(R.string.session_notification_msg), context);
                    getLatLong();
                }
            } else if (intent.getStringExtra(AppConstants.NOTIFICATION_FLAG).equals(AppConstants.FROM_CHALLENGE)) {
                createNotification(context.getString(R.string.app_name), context.getString(R.string.challenge_notification), context);
            }
        }
    }

    /**
     *  @Function : getLatLong()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to initiate the Google Api Client for location services
     *  @Author   : 1769
     */
    private void getLatLong() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mGoogleApiClient.connect();
    }

    /**
     *  @Function : createNotification()
     *  @params   : String title, String message, Context mContext
     *  @Return   : void
     * 	@Usage	  : To create local notification and send it to the user
     *  @Author   : 1769
     */
    public void createNotification(String title, String message, Context mContext)
    {
        Intent resultIntent = new Intent(mContext , ActivityHome.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_ONE_SHOT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.app_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        }
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 , mBuilder.build());

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isGoogleApiClientConnected = true;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
    }

    /**
     *  @Function : startLocationUpdates()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To initiate the location service updates
     *  @Author   : 1769
     */
    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        getLocation();
    }

    /**
     *  @Function : getLocation()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To get the user's last known location
     *  @Author   : 1769
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
//                                mCheckoutPresenter.onCheckoutRequested(mAppPreference.getAuthorizationToken(), mAppPreference.getCheckinId(), latitude, longitude);
                                Intent intent = new Intent(mContext, BackgroundApiCall.class);
                                intent.putExtra(AppConstants.LATITUDE, latitude);
                                intent.putExtra(AppConstants.LONGITUDE, longitude);
                                mContext.startService(intent);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e ("Exception", e.getMessage());
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        isGoogleApiClientConnected = false;
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isGoogleApiClientConnected = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        //not used here
    }
}
