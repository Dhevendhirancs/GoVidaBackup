/**
 * @Class : ActivityCheckin
 * @Usage : This activity is used for check in timer functionality.
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.home_section.checkin_section.mvp.VenueMVP
import com.govida.ui_section.home_section.checkin_section.mvp.VenuePresenterImplementer
import com.govida.ui_section.home_section.checkin_section.service.AlarmBroadcastReceiver
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class ActivityCheckin : BaseActivity(), View.OnClickListener, VenueMVP.CheckoutView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener{

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val handler = Handler()
    private lateinit var pbProgress: ProgressBar
    private lateinit var tvMin: TextView
    private lateinit var tvSec: TextView
    private lateinit var mTvCheckinTitle:TextView
    private lateinit var mTvCheckinSubTitle:TextView
    private var min = 0
    private var sec = 0
    private var value = 0
    private lateinit var mFabButton: FloatingActionButton
    private lateinit var btnCheckin:Button
    private var progressCount:Int = 0
    private val checkinDurationInMin = 2
    private val checkinDuration = checkinDurationInMin*60
    private lateinit var mAppPreference: AppPreference
    private lateinit var mCheckoutPresenter: VenueMVP.CheckoutPresenter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    private lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager
    private var isGoogleApiClientConnected: Boolean = false
    private var REQUEST_CHECK_SETTINGS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)
        setupUI()
        startTimer()
//        checkInStatusVerify()
    }

    /**
     *  @Function : checkInStatusVerify()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To verify whether the user already checked in or not (or) to check whether the timer is already running or not
     *  @Author   : 1769
     */
    private fun checkInStatusVerify() {
        val appPreference= AppPreference(this)
        if (appPreference.checkinHour.equals(25)) {
            btnCheckin.text = getString(com.govida.R.string.checkin)
        } else {
            btnCheckin.text = getString(com.govida.R.string.checkout)
            startTimer()
        }
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        mCheckoutPresenter = VenuePresenterImplementer(null, this)
        mAppPreference = AppPreference(this)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        pbProgress = findViewById(com.govida.R.id.pb_progress);
        tvMin = findViewById(com.govida.R.id.tv_min)
        tvSec = findViewById(com.govida.R.id.tv_sec)
        mFabButton=findViewById(R.id.home_center_button)
        btnCheckin=findViewById(R.id.btn_checkin)
        mTvCheckinTitle=findViewById(R.id.tv_checkin_title)
        mTvCheckinSubTitle=findViewById(R.id.tv_checkin_sub_title)
        val extraDetails = intent.extras
        if (extraDetails != null) {
            if (extraDetails.containsKey(AppConstants.VENUE_NAME)) {
                mAppPreference.currentVenueName = extraDetails.getString(AppConstants.VENUE_NAME)
            }
        }
        mTvCheckinSubTitle.text = mAppPreference.currentVenueName
        btnCheckin.setOnClickListener(this)
        mFabButton.setOnClickListener(this)
        pbProgress.max = checkinDuration
        pbProgress.secondaryProgress = checkinDuration
        pbProgress.progressDrawable = getDrawable(R.drawable.checkin_circular)
        pbProgress.progress = 0
        tvMin.text = getString(R.string.initial_time)
        tvSec.text = getString(R.string.initial_time)
        btnCheckin.text = getString(R.string.checkout)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    /**
     *  @Function : startAlarmBroadcastReceiver()
     *  @params   : context: Context, delay: Long
     *  @Return   : void
     * 	@Usage	  : Set alarm manager to trigger a particular action after a particular time
     *  @Author   : 1769
     */
    fun startAlarmBroadcastReceiver(context: Context, delay: Long) {
        var intent: Intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.putExtra(AppConstants.NOTIFICATION_FLAG, AppConstants.FROM_CHECKIN)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Remove any previous pending intent.
        alarmManager.cancel(pendingIntent)
        var SDK_INT: Int = Build.VERSION.SDK_INT
        if (SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
        else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
        else if (SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
        }
    }

    /**
     *  @Function : startTimer()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Start the timer and manage to start new timer and continue with the last timer
     *  @Author   : 1769
     */
    private fun startTimer() {
        btnCheckin.text = getText(R.string.checkout)
        if(mAppPreference.checkinHour == 25){
            startAlarmBroadcastReceiver (this, 120000)
        }
        val appPreference= AppPreference(this)
        lateinit var time:String
        min = 0
        sec = 0
        value = 0
        if (appPreference.checkinHour == 25) {
            val startTime = Calendar.getInstance().getTime()
            val startCalender = Calendar.getInstance()
            startCalender.time = startTime
            appPreference.checkinHour = startCalender.get(Calendar.HOUR)
            appPreference.checkinMin = startCalender.get(Calendar.MINUTE)
            appPreference.checkinSec = startCalender.get(Calendar.SECOND)
            progressCount = 0
        } else {
            val currentDate = Calendar.getInstance().getTime()
            val calenderForCurrent = Calendar.getInstance()
            calenderForCurrent.setTime(currentDate)

            if (appPreference.checkinHour == calenderForCurrent.get(Calendar.HOUR)) {
                if (appPreference.checkinSec < calenderForCurrent.get(Calendar.SECOND)) {
                    min = calenderForCurrent.get(Calendar.MINUTE) - appPreference.checkinMin
                    sec = calenderForCurrent.get(Calendar.SECOND) - appPreference.checkinSec
                } else {
                    min = (calenderForCurrent.get(Calendar.MINUTE) - appPreference.checkinMin) - 1
                    sec = (60 - appPreference.checkinSec) + calenderForCurrent.get(Calendar.SECOND)
                }
            } else {
                min = (59 - appPreference.checkinMin) + calenderForCurrent.get(Calendar.MINUTE)
                if (appPreference.checkinSec < calenderForCurrent.get(Calendar.SECOND)) {
                    sec = calenderForCurrent.get(Calendar.SECOND) - appPreference.checkinSec
                } else {
                    min -= 1
                    sec = (60 - appPreference.checkinSec) + calenderForCurrent.get(Calendar.SECOND)
                }
            }
            progressCount = ((min * 60) + sec) % checkinDuration
        }
        timer = Timer()
        initializeTimerTask()
        timer!!.schedule(timerTask, 0, 1000)
    }

    /**
     *  @Function : initializeTimerTask()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To change the timer and progress bar every second
     *  @Author   : 1769
     */
    private fun initializeTimerTask() {
        val appPreference= AppPreference(this)
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    sec++
                    if (sec > 59) {
                        min++
                        sec = 0
                    }
                    tvMin.text = (if (min < 10) "0" + min else Integer.toString(min))
                    tvSec.text = (if (sec < 10) "0" + sec else Integer.toString(sec))
                    progressCount++
//                    if (min % 1 == 0 && sec == 0) {
                    if ((min+1) % checkinDurationInMin == 1 && sec == 0)
                        progressCount = 0
                    pbProgress.progress = progressCount
                    if (min >= checkinDurationInMin) {
                        pbProgress.progressDrawable = getDrawable(R.drawable.checkin_circular_timeout)
                        mTvCheckinTitle.text = "Great " + appPreference.userFirstName + "!"
                        mTvCheckinSubTitle.text = getString(R.string.demo_complete_message) + mAppPreference.currentVenueName
                    }
                }
            }
        }
    }

    /**
     *  @Function : stopTimer()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To stop the timer
     *  @Author   : 1769
     */
    private fun stopTimer() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    /**
     *  @Function : stopTimer()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To reset the timer and check in button
     *  @Author   : 1769
     */
    private fun resetTimer() {
        val appPreference= AppPreference(this)
        appPreference.checkinHour = 25
        tvMin.text = getString(R.string.initial_time)
        tvSec.text = getString(R.string.initial_time)
        pbProgress.progress = 0
        btnCheckin.text = getText(R.string.checkin)
    }

    /**
     *  @Function : onClick()
     *  @params   : v: View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.home_center_button -> {
                    stopTimer()
                    finish()
                } R.id.btn_checkin -> {
                    showAlert(getString(R.string.checkout_alert))
                }
            }
        }
    }

    /**
     *  @Function : checkLocation()
     *  @params   : void
     *  @Return   : Boolean
     * 	@Usage	  : To check the location permission status
     *  @Author   : 1769
     */
    private fun checkLocation(): Boolean {
        if(!isLocationEnabled()) {
            displayLocationSettingsRequest(this)
        }
        return isLocationEnabled()
    }

    /**
     *  @Function : displayLocationSettingsRequest()
     *  @params   : context: Context
     *  @Return   : void
     * 	@Usage	  : To display the permission pop up to the user
     *  @Author   : 1769
     */
    private fun displayLocationSettingsRequest(context: Context) {
        var googleApiClient: GoogleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()

        var locationRequest: LocationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        var builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        var result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback(object: ResultCallback<LocationSettingsResult> {
            override fun onResult(result: LocationSettingsResult) {
                var status: Status = result.getStatus();
                when (status.getStatusCode()) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        //All location settings are satisfied
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        //Location settings are not satisfied. Show the user a dialog to upgrade location settings
                        try {
                            status.startResolutionForResult(this@ActivityCheckin, REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                            //PendingIntent unable to execute request
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        //Location settings are inadequate, and cannot be fixed here
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //user has given permission
//                        requestPreferredVenue()
                    } Activity.RESULT_CANCELED -> {
                    //user denied the location permission
                }
                }
            }
        }
    }

    /**
     *  @Function : isLocationEnabled()
     *  @params   : void
     *  @Return   : Boolean
     * 	@Usage	  : To check the system location permission status
     *  @Author   : 1769
     */
    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect()
            isGoogleApiClientConnected = false
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        isGoogleApiClientConnected = false
        mGoogleApiClient.connect();
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        isGoogleApiClientConnected = false
    }

    override fun onConnected(p0: Bundle?) {
        isGoogleApiClientConnected = true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        startLocationUpdates()
    }

    /**
     *  @Function : startLocationUpdates()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To initiate the location service updates
     *  @Author   : 1769
     */
    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
            mLocationRequest, this)
        getLocation()
    }

    /**
     *  @Function : getLocation()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To get the user's current latitude and longitude
     *  @Author   : 1769
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient .getLastLocation()
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    mLocation = location
                    latitude = location.latitude
                    longitude = location.longitude
                    mCheckoutPresenter.onCheckoutRequested(mAppPreference.authorizationToken, mAppPreference.checkinId, latitude, longitude)
                } else {
//                    showAlert(getString(R.string.checkout_api_failed))
                }
            })
    }

    /**
     *  @Function : showAlert()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : to show default system alert
     *  @Author   : 1769
     */
    fun showAlert(alertMsg:String) {
        val builder = AlertDialog.Builder(this,  R.style.AlertDialogCustom)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Checkout") { dialog, which ->
            if (!mAppPreference.isCheckoutDone) {
                if (isGoogleApiClientConnected) {
                    startLocationUpdates()
                } else {
                    if (mGoogleApiClient != null) {
                        mGoogleApiClient.connect()
                    }
                }
            } else {
                stopTimer()
                resetTimer()
                finish()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     *  @Function : getLatLong()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : to get the current latitude and longitude
     *  @Author   : 1769
     */
    @SuppressLint("MissingPermission")
    private fun getLatLong() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                latitude = location?.latitude!!
                longitude = location?.longitude
                mCheckoutPresenter.onCheckoutRequested(mAppPreference.authorizationToken, mAppPreference.checkinId, latitude, longitude)
            }
    }

    /**
     *  @Function : requestAllPermission()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : requesting dynamic permissions for accessing location
     *  @Author   : 1769
     */
    private fun requestAllPermission() {
        mAppPreference = AppPreference(this)
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        getLatLong()
                    }
                    else {
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show() }
            .onSameThread()
            .check()
    }

    /**
     *  @Function : onCheckoutFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : execute if checkout api fails
     *  @Author   : 1769
     */
    override fun onCheckoutFailed(warnings: String) {
        showAlertForError(getString(R.string.checkout_api_failed))
        AppLogger.d(warnings)
    }

    /**
     *  @Function : onCheckoutSuccessfully()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : execute after successfully checkout
     *  @Author   : 1769
     */
    override fun onCheckoutSuccessfully() {
        AppLogger.d("Success")
        if (!mAppPreference.isCheckInBonusReceived) {
            mAppPreference.isCheckInBonusReceived = true
        }
        mAppPreference.isCheckoutDone = true
        stopTimer()
        resetTimer()
        finish()
    }

    /**
     *  @Function : showAlertForError()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : default system alert only with positive button
     *  @Author   : 1769
     */
    fun showAlertForError(alertMsg:String) {
        val builder = AlertDialog.Builder(this,  R.style.AlertDialogCustom)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun onFragmentDetached(tag: String?) {

    }

    override fun onFragmentAttached() {

    }

    override fun onLocationChanged(location: Location?) {
        //not used here
    }
}
