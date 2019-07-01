/**
 * @Class : ActivitySettings
 * @Usage : This activity is used for providing functionality of setting page such as google fit, gps and logout management
 * @Author : 1276
 */
package com.govida.ui_section.setting_section

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.be_connected.mvp.BeConnectedMVP
import com.govida.ui_section.be_connected.mvp.BeConnectedPresenterImplementer
import com.govida.ui_section.login_section.ActivityLogin
import com.govida.utility_section.AppLogger
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class ActivitySettings : BaseActivity(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, BeConnectedMVP.BeConnectedView{
    private var mAppDb: AppDatabase? = null
    private val AUTH_PENDING = "auth_state_pending"
    private var mAuthInProgress = false
    private val REQUEST_OAUTH = 1
    private lateinit var mTvGoogleFitAccess: TextView
    private lateinit var mTvLocationAccess: TextView
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mBtnLogout:Button
    private lateinit var mAppPreference: AppPreference
    private lateinit var mBeConnectedPresenter: BeConnectedMVP.BeConnectedPresenter
    private lateinit var mTvGooglefitSeparator: TextView
    private lateinit var mLlGooglefit: LinearLayout
    private lateinit var mLlChangePassword: LinearLayout
    private lateinit var mSwitchNotification: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState != null) {
            mAuthInProgress = savedInstanceState.getBoolean(AUTH_PENDING)
        }
        setupUI()
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
        val toolbar: Toolbar = findViewById(R.id.toolbar_settings)
        val toolbarTitle:TextView=toolbar.findViewById(R.id.title)
        toolbarTitle.text=getString(R.string.settings)
        mAppDb= AppDatabase.getDatabase(this)
        mTvGoogleFitAccess = findViewById(R.id.tv_google_fit_access)
        mTvLocationAccess = findViewById(R.id.tv_location_access)
        mBtnLogout=findViewById(R.id.setting_btn_logout)
        mBtnLogout.setOnClickListener(this)
        mTvGoogleFitAccess.setOnClickListener(this)
        mTvLocationAccess.setOnClickListener(this)
        mTvGooglefitSeparator = findViewById(R.id.tv_googlefit_separator)
        mLlGooglefit = findViewById(R.id.ll_googlefit)
        mBeConnectedPresenter= BeConnectedPresenterImplementer(this)
        mLlChangePassword = findViewById(R.id.ll_change_password)
        mSwitchNotification = findViewById(R.id.switch_notification)
        mLlChangePassword.setOnClickListener(this)
        if (mAppPreference.isGoogleFitPermissionGiven) {
            mTvGooglefitSeparator.visibility = View.GONE
            mLlGooglefit.visibility = View.GONE
        } else {
            mTvGoogleFitAccess.text = getString(R.string.turn_on)
            mTvGooglefitSeparator.visibility = View.VISIBLE
            mLlGooglefit.visibility = View.VISIBLE
        }
        if (mAppPreference.isGPSPermissionGiven) {
            mTvLocationAccess.text = getString(R.string.turn_off)
        } else {
            mTvLocationAccess.text = getString(R.string.turn_on)
        }
    }

    override fun onResume() {
        super.onResume()
        mBeConnectedPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBeConnectedPresenter.destroyView()
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
            when (v.id)
            {
                R.id.tv_google_fit_access -> {
                    if (mAppPreference.isGoogleFitPermissionGiven) {
                        mAppPreference.setGoogleFitPermission(false)
                        mTvGoogleFitAccess.text = getString(R.string.turn_on)
                    } else {
                        if (mAppPreference.isGPSPermissionGiven) {
                            connectApiClient()
                        } else {
                            showAlert(getString(R.string.enable_gps_to_access_fit))
                        }
                    }
                } R.id.tv_location_access -> {
                    if (mAppPreference.isGPSPermissionGiven) {
                        mAppPreference.setGPSPermission(false)
                        mTvLocationAccess.text = getString(R.string.turn_on)
                    } else {
                        requestAllPermission()
                    }
                } R.id.setting_btn_logout->{
                    ResetDataBase().execute()
                } R.id.ll_change_password -> {
                    val mainActIntent = Intent(this, ActivityChangePassword::class.java)
                    startActivity(mainActIntent)
                }
            }
        }
    }

    /**
     *  @Function : revokeGoogleFitPermission()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : To Disable the Google fit permissions
     *  @Author   : 1769
     */
    private fun revokeGoogleFitPermission() {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_WRITE)
            .build()
        val signInOptions = GoogleSignInOptions.Builder().addExtension(fitnessOptions).build()
        val client = GoogleSignIn.getClient(this, signInOptions)
        client.revokeAccess()
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
            mGoogleApiClient.connect()
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
        AppLogger.d("Google fit connected")
        subscribe()
        if(!mAppPreference.isDeviceIntegrationPointAlert){
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
        AppLogger.d("Connection Suspended")
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
        } else {
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
                if (!mGoogleApiClient.isConnecting && !mGoogleApiClient.isConnected) {
                    mTvGoogleFitAccess.text = getString(R.string.turn_off)
                    mGoogleApiClient.connect()

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mTvGoogleFitAccess.text = getString(R.string.turn_on)
                AppLogger.d("RESULT CANCELED")
            }
        } else {
            AppLogger.d("requestCode NOT request_oauth")
        }
    }

    /**
     *  @Function : requestAllPermission()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : to requesting permission for location access
     *  @Author   : 1276
     */
    private fun requestAllPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                /*
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                */
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        mAppPreference.setGPSPermission(true)
                        mTvLocationAccess.text = getString(R.string.turn_off)
                        if(!mAppPreference.isGPSPointAlert){
                            mBeConnectedPresenter.onGPSBonusPointClicked(true,true,mAppPreference.authorizationToken)
                        }
                    } else {
                        mAppPreference.setGPSPermission(false)
                        mTvLocationAccess.text = getString(R.string.turn_on)
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
     *  @Function : showAlert()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : to display simple alert message
     *  @Author   : 1769
     */
    private fun showAlert(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
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

    /**
     * @Class : ResetDataBase
     * @Usage : This class is used for performing reset operation on database
     * @Author : 1276
     */
    inner class ResetDataBase : AsyncTask<Void, Void, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            hideKeyboard()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): Boolean? {
            mAppDb?.challengeDao()?.onResetChallenge()
            mAppDb?.rewardDao()?.deleteAllRewards()
            mAppDb?.myRewardsDao()?.deleteMyRewards()
            return true
        }
        override fun onPostExecute(receivedData:  Boolean?) {
            hideLoading()
            mAppPreference.clearAllPreferences()
            val mainActIntent = Intent(applicationContext, ActivityLogin::class.java)
            mainActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            mainActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainActIntent)
            finish()
        }
    }

    /**
     *  @Function : onGoogleFitBonusPointPostSuccessful()
     *  @params   : receivedData: BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : Execute if the bonus api response for google fit bonus update successful
     *  @Author   : 1769
     */
    override fun onGoogleFitBonusPointPostSuccessful(receivedData: BonusPointResponseObject.Data) {

        val appPreference = AppPreference(this)
        appPreference.setGoogleFitPermission(true)
        mAppPreference.setGoogleFitPermission(true)
        mTvGoogleFitAccess.text = getString(R.string.turn_off)
        mTvGooglefitSeparator.visibility = View.GONE
        mLlGooglefit.visibility = View.GONE

        showAlert(getString(R.string.googlefit_bonus_alert))
        mAppPreference.showOnboardingPointsAlert(receivedData.onboarding!!)
        mAppPreference.showDeviceIntegrationPointsAlert(receivedData.deviceIntegration!!)
        mAppPreference.showGPSPointsAlert(receivedData.gps!!)
        mAppPreference.isProfileCompletionBonusReceived=receivedData.profileCompletion!!
        mAppPreference.isCheckInBonusReceived=receivedData.firstCheckIn!!
        mAppPreference.isFirstChallengeCardShow=receivedData.firstChallenge!!
    }

    /**
     *  @Function : onGoogleFitBonusPointPostSuccessful()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Execute if the bonus api response for google fit bonus update failed
     *  @Author   : 1769
     */
    override fun onGooglefitBonusPointPostFailed() {
        showAlert(getString(R.string.checkout_api_failed))
    }

    /**
     *  @Function : onGpsPointsPostSuccessful()
     *  @params   : receivedData: BonusPointResponseObject.Data
     *  @Return   : void
     * 	@Usage	  : Execute if the bonus api response for gps bonus update successful
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
    }

    /**
     *  @Function : onGPSPointsPostFailed()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Execute if the bonus api response for gps bonus update failed
     *  @Author   : 1769
     */
    override fun onGPSPointsPostFailed() {
        showAlert(getString(R.string.checkout_api_failed))
    }

}
