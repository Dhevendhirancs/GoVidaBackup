/**
 * @Class : ActivityBeConnected
 * @Usage : This activity is used for providing permission information
 * @Author : 1276
 */

package com.govida.ui_section.be_connected

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.job_schedulers.JobSchedulerMaster
import com.govida.push_notification.NotificationTokenRegistrationService
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.be_connected.mvp.BeConnectedMVP
import com.govida.ui_section.be_connected.mvp.BeConnectedPresenterImplementer
import com.govida.ui_section.home_section.ActivityHome
import com.govida.utility_section.AppLogger.d
import com.govida.utility_section.CommonUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class ActivityBeConnected : BaseActivity(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,BeConnectedMVP.BeConnectedView {

    private lateinit var mTvTermsInfoLink:TextView
    private lateinit var mBtnAllow:Button
    private var mGoogleApiClient: GoogleApiClient?=null
    private val REQUEST_OAUTH = 1
    private val AUTH_PENDING = "auth_state_pending"
    private var mAuthInProgress = false
    private lateinit var mIvGoogleFit:ImageView
    lateinit var mIvGPS:ImageView
    private lateinit var mIvProfile:ImageView
    private lateinit var mBeConnectedPresenter:BeConnectedMVP.BeConnectedPresenter
    lateinit var mAppPreference:AppPreference
    private lateinit var mRvGooglefit: RelativeLayout
    private lateinit var mRvGps: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            mAuthInProgress = savedInstanceState.getBoolean(AUTH_PENDING)
        }
        setContentView(R.layout.activity_be_connected)
        setupUI()
    }
    override fun onResume() {
        super.onResume()
        mBeConnectedPresenter.attachView(this)
    }
    override fun onDestroy() {
        mBeConnectedPresenter.destroyView()
        super.onDestroy()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */
    private fun setupUI() {

        // trigger job schedular
        val jobSchedulerMaster =JobSchedulerMaster(this)
        jobSchedulerMaster.initAllJobs()

        mAppPreference= AppPreference(this)
        mTvTermsInfoLink=findViewById(R.id.tv_terms_info_link)
        mBtnAllow=findViewById(R.id.btn_allow)
        mIvGoogleFit=findViewById(R.id.connect_iv_googlefit)
        mIvGPS=findViewById(R.id.connect_iv_gps)
        mIvProfile=findViewById(R.id.connect_iv_profile)
        mRvGooglefit = findViewById(R.id.rv_googlefit)
        mRvGooglefit.setOnClickListener(this)
        mRvGps = findViewById(R.id.rv_gps)
        mRvGps.setOnClickListener(this)
        mBeConnectedPresenter=BeConnectedPresenterImplementer(this)
        mBtnAllow.setOnClickListener(this)
        mTvTermsInfoLink.text = CommonUtils.fromHtml(getString(R.string.terms_info_link))
        checkPermissions()
        if(isNetworkConnected){
            val intent = Intent(this, NotificationTokenRegistrationService::class.java)
            startService(intent)
        }else{
            onError(R.string.not_connected_to_internet)
        }
    }

    /**
     *  @Function : checkPermissions()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Manage the tickmark based on permission given
     *  @Author   : 1769
     */
    private fun checkPermissions() {
        if (mAppPreference.isGoogleFitPermissionGiven) {
            mIvGoogleFit.setImageResource(R.drawable.ic_checked)
        }
        if (mAppPreference.isGPSPermissionGiven) {
            mIvGPS.setImageResource(R.drawable.ic_checked)
        }
    }

    /**
     *  @Function : onClick()
     *  @params   : View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1276
     */
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_allow->{
                    if (!mAppPreference.isGoogleFitPermissionGiven && !mAppPreference.isGPSPermissionGiven) {
                        showAlert(getString(R.string.alert_googlefit_gps))
                    } else if (!mAppPreference.isGoogleFitPermissionGiven) {
                        showAlert(getString(R.string.alert_googlefit))
                    } else if (!mAppPreference.isGPSPermissionGiven) {
                        showAlert(getString(R.string.alert_gps))
                    } else {
                        startHomeActivity()
                    }
                } R.id.rv_googlefit -> {
                    if (!mAppPreference.isGoogleFitPermissionGiven) {
                        connectApiClient()
                    }
                } R.id.rv_gps -> {
                    if (!mAppPreference.isGPSPermissionGiven) {
                        requestAllPermission()
                    }
                }
            }
        }
    }

    /**
     *  @Function : showAlert()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Display alert message for user.
     *  @Author   : 1276
     */
    private fun showAlert(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
            startHomeActivity()
        }
        builder.show()
    }


    /**
     *  @Function : showAlertForGoogleFit()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Display alert message for user.
     *  @Author   : 1276
     */
    private fun showAlertForGoogleFit(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     *  @Function : showAlertForGPS()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : show alert for GPS
     *  @Author   : 1769
     */
    private fun showAlertForGPS(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     *  @Function : connectApiClient()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Google fit API integration permission
     *  @Author   : 1769
     */
    private fun connectApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Fitness.RECORDING_API)
            .addApi(Fitness.HISTORY_API)
            .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
            .addScope(Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        mGoogleApiClient!!.connect()
    }

    /**
     *  @Function : subscribe()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : subscribe the events for google fit
     *  @Author   : 1769
     */
    private fun subscribe() {
        Fitness.RecordingApi.subscribe(mGoogleApiClient, DataType.TYPE_DISTANCE_DELTA)
        Fitness.RecordingApi.subscribe(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA)
        Fitness.RecordingApi.subscribe(mGoogleApiClient, DataType.TYPE_CALORIES_EXPENDED)
    }

    /**
     *  @Function : onConnected()
     *  @params   : Bundle
     *  @Return   : void
     * 	@Usage	  : Google fit API integration successfully committed
     *  @Author   : 1769
     */
    override fun onConnected(p0: Bundle?) {
        d("Google fit connected")
        if(mAppPreference.isDeviceIntegrationPointAlert){
            mIvGoogleFit.setImageResource(R.drawable.ic_checked)
            mAppPreference.setGoogleFitPermission(true)
        }else{
            mBeConnectedPresenter.onGoogleFitBonusPointClicked(true, true,mAppPreference.authorizationToken)
        }
    }

    /**
     *  @Function : onConnectionSuspended()
     *  @params   : Int
     *  @Return   : void
     * 	@Usage	  : Google fit API integration connection suspended
     *  @Author   : 1769
     */
    override fun onConnectionSuspended(p0: Int) {
        d("Connection Suspended")
    }

    /**
     *  @Function : onConnectionSuspended()
     *  @params   : Int
     *  @Return   : void
     * 	@Usage	  : Google fit API integration connection failed
     *  @Author   : 1769
     */
    override fun onConnectionFailed(p0: ConnectionResult) {
        if (!mAuthInProgress) {
            try {
                mAuthInProgress = true
                p0.startResolutionForResult(this, REQUEST_OAUTH)
            } catch (e: IntentSender.SendIntentException) {

            }
        }
    }

    /**
     *  @Function : onActivityResult()
     *  @params   : Int, Int, Intent
     *  @Return   : void
     * 	@Usage	  : Managing response for Google API Client permission from user
     *  @Author   : 1769
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_OAUTH) {
            mAuthInProgress = false
            if (resultCode == Activity.RESULT_OK) {
                if (!mGoogleApiClient?.isConnecting!! && !mGoogleApiClient?.isConnected!!) {
                    mGoogleApiClient?.connect()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mAppPreference.setGoogleFitPermission(false)
                d("RESULT CANCELED")
            }
        } else {
            d("requestCode NOT request_oauth")
        }
    }

    /**
     * navigate to home page
     */
    private fun startHomeActivity() {
        mAppPreference.homeSetupDone(true)
        val mainActIntent = Intent(applicationContext, ActivityHome::class.java)
        startActivity(mainActIntent)
        finish()
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private fun requestAllPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if(mAppPreference.isGPSPointAlert){
                            mIvGPS.setImageResource(R.drawable.ic_checked)
                            mAppPreference.setGPSPermission(true)
                            if(mGoogleApiClient!=null){
                                subscribe()
                            }
//                            startHomeActivity()
                        }else{
                            mBeConnectedPresenter.onGPSBonusPointClicked(true,true,mAppPreference.authorizationToken)
                        }
                    }
                    else {
                        mAppPreference.setGPSPermission(false)
                        showAlertForGPS(getString(R.string.gps_off_alert))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { Toast.makeText(applicationContext, "Error occurred! ", Toast.LENGTH_SHORT).show() }
            .onSameThread()
            .check()
    }


    /*private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()

    }
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
    */

    /**
     *  @Function : onGoogleFitBonusPointPostSuccessful()
     *  @params   : BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : Manage UI when Google fit information is synced with server.
     *  @Author   : 1276
     */
    override fun onGoogleFitBonusPointPostSuccessful(receivedData: BonusPointResponseObject.Data) {
        showAlertForGoogleFit(getString(R.string.googlefit_bonus_alert))
        mIvGoogleFit.setImageResource(R.drawable.ic_checked)
        mAppPreference.setGoogleFitPermission(true)

        mAppPreference.showOnboardingPointsAlert(receivedData.onboarding!!)
        mAppPreference.showDeviceIntegrationPointsAlert(receivedData.deviceIntegration!!)
        mAppPreference.showGPSPointsAlert(receivedData.gps!!)
        mAppPreference.isProfileCompletionBonusReceived=receivedData.profileCompletion!!
        mAppPreference.isCheckInBonusReceived=receivedData.firstCheckIn!!
        mAppPreference.isFirstChallengeCardShow=receivedData.firstChallenge!!
    }

    /**
     *  @Function : onGpsPointsPostSuccessful()
     *  @params   : BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : Manage UI when GPS information is synced with server.
     *  @Author   : 1276
     */
    override fun onGpsPointsPostSuccessful(receivedData: BonusPointResponseObject.Data) {
        showAlertForGPS(getString(R.string.gps_bonus_alert))
        mIvGPS.setImageResource(R.drawable.ic_checked)
        mAppPreference.setGPSPermission(true)

        subscribe()
        mAppPreference.showOnboardingPointsAlert(receivedData.onboarding!!)
        mAppPreference.showDeviceIntegrationPointsAlert(receivedData.deviceIntegration!!)
        mAppPreference.showGPSPointsAlert(receivedData.gps!!)
        mAppPreference.isProfileCompletionBonusReceived=receivedData.profileCompletion!!
        mAppPreference.isCheckInBonusReceived=receivedData.firstCheckIn!!
        mAppPreference.isFirstChallengeCardShow=receivedData.firstChallenge!!
    }

    /**
     *  @Function : onGPSPointsPostFailed()
     *  @params   : BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : Manage UI when GPS information is synced with server failed.
     *  @Author   : 1276
     */
    override fun onGPSPointsPostFailed() {
        showAlertForGPS(getString(R.string.checkout_api_failed))
    }

    /**
     *  @Function : onGooglefitBonusPointPostFailed()
     *  @params   : BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : Manage UI when Google Fit information is synced with server failed.
     *  @Author   : 1276
     */
    override fun onGooglefitBonusPointPostFailed() {
        showAlertForGPS(getString(R.string.checkout_api_failed))
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String?) {
    }

}

