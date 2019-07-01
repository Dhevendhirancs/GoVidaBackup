/**
 * @Class : ActivitySelectVenue
 * @Usage : This activity is used to manage selecting the venues
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.home_section.challenges_section.rv_touch_listeners.RecyclerviewTouchListener
import com.govida.ui_section.home_section.checkin_section.adapters.VenueListAdapter
import com.govida.ui_section.home_section.checkin_section.item_decorator_for_list.SimpleDividerItemDecoration
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse
import com.govida.ui_section.home_section.checkin_section.mvp.VenueMVP
import com.govida.ui_section.home_section.checkin_section.mvp.VenuePresenterImplementer
import android.location.Location
import android.location.LocationListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.multidex.MultiDex
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.OnSuccessListener
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.be_connected.mvp.BeConnectedMVP
import com.govida.ui_section.be_connected.mvp.BeConnectedPresenterImplementer
import com.govida.utility_section.AppLogger.d

class ActivitySelectVenue : BaseActivity (), VenueMVP.VenueView, View.OnClickListener, LocationListener, BeConnectedMVP.BeConnectedView,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private lateinit var mVenuePresenter: VenueMVP.VenuePresenter
    private lateinit var mAppPreference: AppPreference
    private lateinit var mBtnStartSession: Button
    private lateinit var mRvVenueList: androidx.recyclerview.widget.RecyclerView
    private var mVenueList: MutableList<VenueResponse.Data> = mutableListOf()
    private lateinit var mVenueListAdapter:VenueListAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var mSearchView: SearchView
    private lateinit var mLlLocation: LinearLayout
    private lateinit var mTvEmtpyListMsg: TextView
    private lateinit var mTvVenueTitle: TextView
    private var mVerifiedVenueFlag: Boolean = false
    private lateinit var mSearchString: String
    private lateinit var mBtnClose: FloatingActionButton
    private lateinit var mTvSearchHint: TextView
    private lateinit var mTvTitle: TextView
    private lateinit var mIvLocation: ImageView
    private lateinit var mBeConnectedPresenter: BeConnectedMVP.BeConnectedPresenter
    private var mTempVenueList: MutableList<VenueResponse.Data> = mutableListOf()
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private var mLocationRequest: LocationRequest? = null
    private val listener: com.google.android.gms.location.LocationListener? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager
    private var isGoogleApiClientConnected: Boolean = false
    private var REQUEST_CHECK_SETTINGS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_venue)
        setupUI()
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

        var builder:LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
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
                                status.startResolutionForResult(this@ActivitySelectVenue, REQUEST_CHECK_SETTINGS)
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
                        requestPreferredVenue()
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
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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
                    if (mTempVenueList.isNullOrEmpty()) {
                        if (mVerifiedVenueFlag) {
                            mVenuePresenter.onVerifiedVenueRequested(mAppPreference.authorizationToken, location.latitude, location.longitude)
                        } else {
                            mVenuePresenter.onVenueRequested(mAppPreference.authorizationToken, mSearchString, location.latitude, location.longitude)
                        }
                    } else {
                        for (venue in mTempVenueList!!) {
                            venue.userLatitude = location.latitude.toString()
                            venue.userLongitude = location.longitude.toString()
                        }
                        mTvEmtpyListMsg.visibility = View.GONE
                        mRvVenueList.visibility = View.VISIBLE
                        mVenueList.clear()
                        mVenueList.addAll(mTempVenueList!!)
                        mVenueListAdapter.notifyDataSetChanged()
                    }
                } else {
//                    showAlert(getString(R.string.checkout_api_failed))
                }
            })
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        mAppPreference = AppPreference(this)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        mTvSearchHint = findViewById(R.id.tv_search_hint)
        mVenuePresenter = VenuePresenterImplementer(this, null)
        mBtnStartSession = findViewById(R.id.btn_start_session)
        mBtnStartSession.setOnClickListener(this)
        mRvVenueList = findViewById(R.id.rv_venue_list)
        mVenueListAdapter= VenueListAdapter(mVenueList, this)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mRvVenueList.layoutManager = mLayoutManager
        mRvVenueList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        mRvVenueList.adapter = mVenueListAdapter
        mRvVenueList.addItemDecoration(SimpleDividerItemDecoration(this))
        mRvVenueList.addOnItemTouchListener(
            RecyclerviewTouchListener(this, mRvVenueList,
                object : RecyclerviewTouchListener.ClickListener {
                    override fun onLongClick(view: View?, position: Int) {
                    }
                    override fun onClick(view: View, position: Int) {
                    }
                })
        )
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mSearchView = findViewById(R.id.searchView)
        mLlLocation = findViewById(R.id.ll_location)
        mLlLocation.setOnClickListener(this)
        mTvEmtpyListMsg = findViewById(R.id.tv_empty_list_msg)
        mTvVenueTitle = findViewById(R.id.tv_venue_title)
        mTvVenueTitle.text = getString (R.string.current_location)
        mTvTitle = findViewById(R.id.tv_title)
        mTvTitle.text = getString(R.string.preferred_venue)
        mIvLocation = findViewById(R.id.iv_location)
        mIvLocation.visibility = View.VISIBLE
        mBeConnectedPresenter= BeConnectedPresenterImplementer(this)

        mSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mTempVenueList.clear()
                mSearchString = mSearchView.query.toString()
                mVerifiedVenueFlag = false
                mTvSearchHint.visibility = View.GONE
                mLlLocation.visibility = View.GONE
                if (mAppPreference.isGPSPermissionGiven) {
                    if (isGoogleApiClientConnected) {
                        startLocationUpdates()
                    } else {
                        if (mGoogleApiClient != null) {
                            mGoogleApiClient.connect()
                        }
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isEmpty()) {
                    mTvSearchHint.visibility = View.VISIBLE
                    mLlLocation.visibility = View.VISIBLE
                } else {
                    mTvSearchHint.visibility = View.GONE
                }
                return true
            }
        })
        mBtnClose = findViewById(R.id.btn_close)
        mBtnClose.setOnClickListener(this)
        MultiDex.install(this)
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!mAppPreference.isGPSPermissionGiven) {
            showAlert (getString(R.string.alert_enable_gps))
        } else {
            if (checkLocation()) {
                requestPreferredVenue()
                mTvVenueTitle.text = getString (R.string.current_location)
                mTvTitle.text = getString(R.string.preferred_venue)
            }
        }
    }

    /**
     *  @Function : showAlert()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : default system alert box
     *  @Author   : 1769
     */
    private fun showAlert (alertMsg:String) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
        builder.setTitle(getString (R.string.app_name))
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton(getString (R.string.material_calendar_positive_button)) { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     *  @Function : requestPreferredVenue()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : preferred venue list api call
     *  @Author   : 1769
     */
    private fun requestPreferredVenue() {
        mAppPreference = AppPreference(this)
        mVenuePresenter.onPreferredVenueRequested(mAppPreference.authorizationToken)
    }

    /**
     *  @Function : onClick()
     *  @params   : v: View
     *  @Return   : void
     * 	@Usage	  : onclick listener function definition
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_start_session -> {
//                val mainActIntent = Intent(this, ActivityCheckin::class.java)
//                startActivity(mainActIntent)
            } R.id.ll_location -> {
                mTempVenueList.clear()
                if (mVerifiedVenueFlag) {
                    mVerifiedVenueFlag = false
                    mTvVenueTitle.text = getString (R.string.current_location)
                    mTvTitle.text = getString(R.string.preferred_venue)
                    mIvLocation.visibility = View.VISIBLE
                    if (mAppPreference.isGPSPermissionGiven) {
                        requestPreferredVenue()
                    } else {
//                        showAlert(getString(R.string.alert_enable_gps))
                    }
                } else {
                    mVerifiedVenueFlag = true
                    mTvVenueTitle.text = getString (R.string.preferred_location)
                    mTvTitle.text = getString(R.string.verified_venue)
                    mIvLocation.visibility = View.GONE
                    if (mAppPreference.isGPSPermissionGiven) {
                        if (isGoogleApiClientConnected) {
                            startLocationUpdates()
                        } else {
                            if (mGoogleApiClient != null) {
                                mGoogleApiClient.connect()
                            }
                        }
                    } else {
//                        showAlert(getString(R.string.alert_enable_gps))
                    }
                }
            } R.id.btn_close -> {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mVenuePresenter.attachView(this)
        mBeConnectedPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mVenuePresenter.destroyView()
        mBeConnectedPresenter.destroyView()
    }

    /**
     *  @Function : onVenueReceivedSuccesfully()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : execute if search venue api successful
     *  @Author   : 1769
     */
    override fun onVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?) {
        mLlLocation.visibility = View.GONE
        mTvTitle.visibility = View.GONE
        if (venueList.isNullOrEmpty()) {
            mTvEmtpyListMsg.text = getString(R.string.venue_not_available_msg)
            mTvEmtpyListMsg.visibility = View.VISIBLE
            mRvVenueList.visibility = View.GONE
        } else {
            for (venue in venueList!!) {
                venue.userLatitude = latitude.toString()
                venue.userLongitude = longitude.toString()
            }
            mTvEmtpyListMsg.visibility = View.GONE
            mRvVenueList.visibility = View.VISIBLE
            mVenueList.clear()
            mVenueList.addAll(venueList!!)
            mVenueListAdapter.notifyDataSetChanged()
        }
    }

    /**
     *  @Function : onVenueReceivedFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : execute if search venue api fails
     *  @Author   : 1769
     */
    override fun onVenueReceivedFailed(warnings: String) {
        d(warnings)
    }

    /**
     *  @Function : onPreferredVenueReceivedSuccesfully()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : execute if preferred venue api successful
     *  @Author   : 1769
     */
    override fun onPreferredVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?) {
        mLlLocation.visibility = View.VISIBLE
        mTvTitle.visibility = View.VISIBLE
        if (venueList.isNullOrEmpty()) {
            mTvEmtpyListMsg.text = getString(R.string.venue_text)
            mTvEmtpyListMsg.visibility = View.VISIBLE
            mRvVenueList.visibility = View.GONE
        } else {
            mTempVenueList.clear()
            mTempVenueList.addAll(venueList!!)
            if (isGoogleApiClientConnected) {
                startLocationUpdates()
            } else {
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect()
                }
            }
        }
    }

    /**
     *  @Function : onPreferredVenueReceivedFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : execute if preferred venue api fails
     *  @Author   : 1769
     */
    override fun onPreferredVenueReceivedFailed(warnings: String) {
        d(warnings)
    }

    /**
     *  @Function : onVerifiedVenueReceivedSuccesfully()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : execute if verified venue api successful
     *  @Author   : 1769
     */
    override fun onVerifiedVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?) {
        mLlLocation.visibility = View.VISIBLE
        mTvTitle.visibility = View.VISIBLE
        if (venueList.isNullOrEmpty()) {
            mTvEmtpyListMsg.text = getString(R.string.verified_venue_not_available_msg)
            mTvEmtpyListMsg.visibility = View.VISIBLE
            mRvVenueList.visibility = View.GONE
        } else {
            for (venue in venueList!!) {
                venue.userLatitude = latitude.toString()
                venue.userLongitude = longitude.toString()
            }
            mTvEmtpyListMsg.visibility = View.GONE
            mRvVenueList.visibility = View.VISIBLE
            mVenueList.clear()
            mVenueList.addAll(venueList!!)
            mVenueListAdapter.notifyDataSetChanged()
        }
    }

    /**
     *  @Function : onVerifiedVenueReceivedFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : execute if verified venue api fails
     *  @Author   : 1769
     */
    override fun onVerifiedVenueReceivedFailed(warnings: String) {
        d(warnings)
    }

    /**
     *  @Function : onGpsPointsPostSuccessful()
     *  @params   : receivedData: BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : execute if bonus point api for GPS succeeds
     *  @Author   : 1769
     */
    override fun onGpsPointsPostSuccessful(receivedData: BonusPointResponseObject.Data) {
        showAlert(getString(R.string.gps_bonus_alert))
        mAppPreference.showOnboardingPointsAlert(receivedData.onboarding!!)
        mAppPreference.showDeviceIntegrationPointsAlert(receivedData.deviceIntegration!!)
        mAppPreference.showGPSPointsAlert(receivedData.gps!!)
        mAppPreference.isProfileCompletionBonusReceived=receivedData.profileCompletion!!
        mAppPreference.isCheckInBonusReceived=receivedData.firstCheckIn!!
        mAppPreference.isFirstChallengeCardShow=receivedData.firstChallenge!!
        d(getString(R.string.gps_api_success))
        if (isGoogleApiClientConnected) {
            startLocationUpdates()
        } else {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect()
            }
        }
    }

    /**
     *  @Function : onGPSPointsPostFailed()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : execute if bonus point api for GPS fails
     *  @Author   : 1769
     */
    override fun onGPSPointsPostFailed() {
        d(getString(R.string.gps_api_fail))
//        showAlert(getString(R.string.checkout_api_failed))
    }

    override fun onGoogleFitBonusPointPostSuccessful(receivedData: BonusPointResponseObject.Data) {
        //not used here
    }

    override fun onGooglefitBonusPointPostFailed() {
        //not used here
    }

    override fun onFragmentAttached() {
        //not used here
    }

    override fun onFragmentDetached(tag: String?) {
        //not used here
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //not used here
    }

    override fun onProviderEnabled(provider: String?) {
        //not used here
    }

    override fun onProviderDisabled(provider: String?) {
        //not used here
    }

    override fun onLocationChanged(location: Location?) {
        //not used here
    }
}