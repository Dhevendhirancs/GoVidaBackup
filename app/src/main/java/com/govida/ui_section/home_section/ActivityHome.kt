/**
 * @Class : ActivityHome
 * @Usage : This activity is used for displaying Home page when user logged in
 * @Author : 1276
 */

package com.govida.ui_section.home_section

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.home_section.activity_section.FragmentActivities
import com.govida.ui_section.home_section.challenges_section.FragmentChallenges
import com.govida.ui_section.home_section.checkin_section.ActivityCheckin
import com.govida.ui_section.home_section.checkin_section.ActivitySelectVenue
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.ui_section.home_section.mvp.ActivityHomeMVP
import com.govida.ui_section.home_section.mvp.ActivityHomePresenterImplementer
import com.govida.ui_section.home_section.profile_section.FragmentProfile
import com.govida.ui_section.home_section.rewards_section.FragmentRewards
import com.govida.ui_section.leaderboard_section.ActivityLeaderboard
import com.govida.ui_section.notification_section.ActivityNotification
import com.govida.utility_section.AppLogger
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger.d
import com.govida.utility_section.AppLogger.e
import com.govida.utility_section.CommonUtils
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import java.lang.Long
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityHome : BaseActivity(), androidx.viewpager.widget.ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,ActivityHomeMVP.ActivityHomeView,
    ActivityHomePresenterImplementer.InsertChallengeTask.OnDownloadListener
{
    private var mAppDb: AppDatabase? = null
    lateinit var mBtmNav: BottomNavigationViewEx
    lateinit var mPagertabs: androidx.viewpager.widget.ViewPager
    lateinit var mFabButton: FloatingActionButton
    lateinit var mFabButtonSec: FloatingActionButton
    lateinit var mHomeAdapter:HomeTabAdapter
    lateinit var rlCheckinTimer:RelativeLayout
    private var fragments: ArrayList<androidx.fragment.app.Fragment>? = null// used for ViewPager adapter
    var previousPosition=-1
    var checkInStatus:Boolean = false
    var checkInStatusToken = "checkInToken"
    private lateinit var mHomePresenter:ActivityHomeMVP.ActivityHomePresenter
    private var mGoogleApiClient:GoogleApiClient? = null
    private var authInProgress = false
    private val authPending = "auth_state_pending"
    private val requestOAuth = 1
    private lateinit var readRequest: DataReadRequest
    var stepCount:Int = 0
    var distance:Double = 0.0

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val handler = Handler()
    private var min = 0
    private var sec = 0
    private lateinit var tvMin: TextView
    private lateinit var tvSec: TextView
    private lateinit var mIvLeaderboard: ImageView
    private lateinit var mIvNotification: ImageView
    private lateinit var mAppPreference:AppPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(authPending)
        }
        setupUI()
        val extraDetails=intent.extras
        if(extraDetails!=null && extraDetails.containsKey(AppConstants.INTENT_NOTIFICATION_DATA)){
            val notificationBundle=extraDetails.getBundle(AppConstants.INTENT_NOTIFICATION_DATA)
            handleNotificationIntent(notificationBundle)
        }

    }

    private fun handleNotificationIntent(notificationBundle:Bundle){
        if (isNetworkConnected) {
            if(notificationBundle.get("challenge").equals("challenge")){
                if(mPagertabs!=null){
                    mPagertabs.setCurrentItem(1,false)
                }
            }
            if(notificationBundle.get("checkin_bonus").equals("Checkin_Bonus")){
                if(mPagertabs!=null){
                    mPagertabs.setCurrentItem(3,false)
                }
            }
            if(notificationBundle.get("reward").equals("reward")){
                if(mPagertabs!=null){
                    mPagertabs.setCurrentItem(2,false)
                }
            }
            if(notificationBundle.get("leaderboard").equals("leaderboard")){

            }
        }else{
            error(R.string.not_connected_to_internet)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        mHomePresenter.attachView(this)
//        if (mAppPreference.checkinTime == "") {
        if (mAppPreference.checkinHour == 25) {
//            mFabButton.setBackgroundResource(R.drawable.round_button_color_bg)
            mFabButton.visibility = View.VISIBLE
            mFabButtonSec.visibility = View.GONE
            rlCheckinTimer.visibility = View.GONE
        } else {
            mFabButton.visibility = View.GONE
            mFabButtonSec.visibility = View.VISIBLE
//            mFabButton.setBackgroundResource(R.drawable.round_button_color_bg_sec)
            rlCheckinTimer.visibility = View.VISIBLE
            stopTimer()
            startTimer()

        }
    }

    override fun onDestroy() {
        mHomePresenter.destroyView()
        super.onDestroy()
    }
    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */
    @SuppressLint("RestrictedApi")
    private fun setupUI() {
        mBtmNav=findViewById(R.id.home_bnve)
        mPagertabs=findViewById(R.id.home_pager_tabs)
        mFabButton=findViewById(R.id.home_center_button)

        mFabButtonSec=findViewById(R.id.btn_checkout)
        rlCheckinTimer=findViewById(R.id.rl_checkin_timer)
        mFabButton.setOnClickListener(this)
        mFabButtonSec.setOnClickListener(this)
        rlCheckinTimer.setOnClickListener(this)

        mIvLeaderboard = findViewById(R.id.home_iv_leaderboard)
        mIvLeaderboard.setOnClickListener(this)
        mIvNotification = findViewById(R.id.home_iv_notification)
        mIvNotification.setOnClickListener(this)
        mAppPreference= AppPreference(this)
        mHomePresenter= ActivityHomePresenterImplementer(this)
        mAppDb= AppDatabase.getDatabase(this)
        mBtmNav.enableItemShiftingMode(false);
        mBtmNav.enableShiftingMode(false);
        mBtmNav.enableAnimation(false);

        fragments = ArrayList(4)

        tvMin = findViewById(R.id.tv_min)
        tvSec = findViewById(R.id.tv_sec)

        val fragAct=FragmentActivities()
        val fragChallenges=FragmentChallenges()
        val fragRewards=FragmentRewards()
        val fragProfile=FragmentProfile()

        fragments!!.add(0,fragAct)
        fragments!!.add(1,fragChallenges)
        fragments!!.add(2,fragRewards)
        fragments!!.add(3,fragProfile)

        mHomeAdapter= HomeTabAdapter(supportFragmentManager, fragments!!)
        mPagertabs.adapter=mHomeAdapter
        mBtmNav.onNavigationItemSelectedListener = this
        mPagertabs.addOnPageChangeListener(this)

        mHomePresenter.onChallengeListRequested(mAppPreference.authorizationToken,mAppDb!!,this)


        /*val appPreference= AppPreference(this)
        if(appPreference.isGoogleFitPermissionGiven){
          var syncRequest= ActivitySyncRequest()
         syncRequest.lastSyncTime="02/06/2019 10:50:11"
         var stepList: MutableList<ActivitySyncRequest.StepsHistory> =mutableListOf()
         var a=syncRequest.StepsHistory()
         a.count=13000
         a.datetime="03/06/2019 11:50:11"
         stepList.add(a)

         var b=syncRequest.StepsHistory()
         b.count=13000
         b.datetime="03/06/2019 12:50:11"
         stepList.add(b)


         var c=syncRequest.StepsHistory()
         c.count=13000
         c.datetime="03/06/2019 13:50:11"
         stepList.add(c)
         syncRequest.stepsHistory= stepList as ArrayList<ActivitySyncRequest.StepsHistory>?
         mHomePresenter.onActivitySyncPostRequested(mAppPreference.authorizationToken,syncRequest)
        }else{

        }*/
        if (mAppPreference.checkinHour == 25) {
            mFabButton.setBackgroundResource(R.drawable.round_button_color_bg)
            mFabButton.visibility = View.VISIBLE
            mFabButtonSec.visibility = View.GONE
            rlCheckinTimer.visibility = View.GONE
        } else {
            mFabButton.setBackgroundResource(R.drawable.round_button_color_bg_sec)
            mFabButton.visibility = View.GONE
            mFabButtonSec.visibility = View.VISIBLE
            rlCheckinTimer.visibility = View.VISIBLE
        }


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
//                    val mainActIntent = Intent(this, ActivityCheckin::class.java)
//                    startActivity(mainActIntent)
                    val mainActIntent = Intent(this, ActivitySelectVenue::class.java)
                    startActivity(mainActIntent)
                }
                R.id.btn_checkout -> {
                    val mainActIntent = Intent(this, ActivityCheckin::class.java)
                    startActivity(mainActIntent)
                } R.id.home_iv_leaderboard -> {
                    val mainActIntent = Intent(this, ActivityLeaderboard::class.java)
                    startActivity(mainActIntent)
                } R.id.home_iv_notification -> {
                    val mainActIntent = Intent(this, ActivityNotification::class.java)
                    startActivity(mainActIntent)
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var position=0
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val toolbarTitle: TextView =toolbar.findViewById(R.id.title)
        val toolbarRewards: TextView  =toolbar.findViewById(R.id.rewards_info)
        when(item.itemId){
            R.id.i_home->{
                toolbar.visibility=View.VISIBLE
                position=0
                toolbarTitle.text=getString(R.string.title_activity)
                toolbarRewards.visibility=View.GONE
            }
            R.id.i_challenges->{
                position=1
                toolbar.visibility=View.VISIBLE
                toolbarTitle.text=getString(R.string.title_challenges)
                toolbarRewards.visibility=View.GONE
            }
            R.id.i_rewards->{
                position=2
                toolbar.visibility=View.VISIBLE
                toolbarTitle.text=getString(R.string.title_rewards)
                toolbarRewards.visibility=View.VISIBLE
                toolbarRewards.text="My GoVPs:"+mAppPreference.myTotalGoVps
            }
            R.id.i_profile->{
                position=3
                toolbar.visibility=View.GONE
                toolbarTitle.text=getString(R.string.title_profile)
                toolbarRewards.visibility=View.GONE
            }
            R.id.i_empty->{
                toolbarRewards.visibility=View.GONE
                return false
            }
        }
        if(previousPosition!=position){
            mPagertabs.setCurrentItem(position,false)
            previousPosition=position
        }
        return true
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        var positionItem=position
        if(position>=2){
            positionItem=position+1
        }
        mBtmNav.currentItem=positionItem
    }

    public fun moveToRequiredPageinHomeSection(position: Int){
        var positionItem=position
        if(position>=2){
            positionItem=position+1
        }
        mBtmNav.currentItem=positionItem
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }

    /**
     *  @Function : connectApiClient()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Google fit API integration permission
     *  @Author   : 1769
     */
    fun connectApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, 0, this)
                .build()
        }
    }

    /**
     *  @Function : getStepCount()
     *  @params   : startDate: Date, endDate: Date
     *  @Return   : Int
     * 	@Usage	  : Define the Read Request to read the user's step count from Google Fit
     *  @Author   : 1769
     */
    fun getStepCount(startDate: Date?, endDate: Date?): Int{
        val startDate = startDate
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        val startTime = cal1.timeInMillis

        val endDate = endDate
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        val endTime = cal2.timeInMillis

        readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
        return readStepData()
    }

    /**
     *  @Function : readStepData()
     *  @params   : void
     *  @Return   : Int
     * 	@Usage	  : Execute Google fit read request and get the Step count data
     *  @Author   : 1769
     */
    private fun readStepData(): Int {
        stepCount = 0
        val dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES)
        if (dataReadResult.buckets.size > 0) {
            for (bucket in dataReadResult.buckets) {
                val dataSets = bucket.dataSets
                for (dataSet in dataSets) {
                    if(dataSet.dataPoints.size>0){
                        showStepDataSet(dataSet)
                    }
                }
            }
        } else if (dataReadResult.dataSets.size > 0) {
            for (dataSet in dataReadResult.dataSets) {
                if(dataSet.dataPoints.size>0){
                    showStepDataSet(dataSet)
                }

            }
        }
        return stepCount
    }

    /**
     *  @Function : showStepDataSet()
     *  @params   : dataSet: DataSet
     *  @Return   : void
     * 	@Usage	  : Extract the step count value from Data set
     *  @Author   : 1769
     */
    private fun showStepDataSet(dataSet: DataSet) {
        for (dp in dataSet.dataPoints)
        {
            for (field in dp.dataType.fields) {
                var temp: Int = Integer.parseInt(dp.getValue(field).toString())
                e("History", "\tStart: " + android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", Long.parseLong(dp.getStartTime(TimeUnit.MILLISECONDS).toString())));
                e("History", "\tEnd: " + android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", Long.parseLong(dp.getEndTime(TimeUnit.MILLISECONDS).toString())));
                stepCount += temp
            }
        }
    }


    /**
     *  @Function : getStepCountForSyncWithServer()
     *  @params   : startDate: Date, endDate: Date
     *  @Return   : Int
     * 	@Usage	  : Define the Read Request to read the user's step count from Google Fit
     *  @Author   : 1769
     */
    fun getStepCountForSyncWithServer(startDate: Date?, endDate: Date?,syncRequest:ActivitySyncRequest): ActivitySyncRequest{
        val startDate = startDate
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        val startTime = cal1.timeInMillis

        val endDate = endDate
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        val endTime = cal2.timeInMillis

        readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.HOURS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
        return readStepDataForSyncWithServer(syncRequest)!!
    }

    /**
     *  @Function : readStepDataForSyncWithServer()
     *  @params   : void
     *  @Return   : Int
     * 	@Usage	  : Execute Google fit read request and get the Step count data
     *  @Author   : 1769
     */
    private fun readStepDataForSyncWithServer(syncRequest:ActivitySyncRequest): ActivitySyncRequest? {
        val dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES)

        var stepList: MutableList<ActivitySyncRequest.StepsHistory> =mutableListOf()

        if (dataReadResult.buckets.size > 0) {
            for (bucket in dataReadResult.buckets) {
                val dataSets = bucket.dataSets
                for (dataSet in dataSets) {
                    if(dataSet.dataPoints.size>0){
                       stepList.add(showStepDataSetForSyncWithServer(dataSet,syncRequest))
                    }
                }
            }
        } else if (dataReadResult.dataSets.size > 0) {
            for (dataSet in dataReadResult.dataSets) {
                if(dataSet.dataPoints.size>0){
                   stepList.add(showStepDataSetForSyncWithServer(dataSet,syncRequest))
                }
            }
        }
        syncRequest.stepsHistory=stepList
        return syncRequest
    }

    /**
     *  @Function : showStepDataSetForSyncWithServer()
     *  @params   : dataSet: DataSet
     *  @Return   : void
     * 	@Usage	  : Extract the step count value from Data set
     *  @Author   : 1769
     */
    private fun showStepDataSetForSyncWithServer(dataSet: DataSet,syncRequest:ActivitySyncRequest): ActivitySyncRequest.StepsHistory {
        val tempData=syncRequest.StepsHistory()
        for (dp in dataSet.dataPoints)
        {
            for (field in dp.dataType.fields) {
                val temp: Int = Integer.parseInt(dp.getValue(field).toString())
                e("History", "\tStart: " + android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", Long.parseLong(dp.getStartTime(TimeUnit.MILLISECONDS).toString())));
                e("History", "\tEnd: " + android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", Long.parseLong(dp.getEndTime(TimeUnit.MILLISECONDS).toString())));


                tempData.count=temp
                tempData.datetime=CommonUtils.localToGMT(Long.parseLong(dp.getEndTime(TimeUnit.MILLISECONDS).toString()))
                //tempData.datetime=""+android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", Long.parseLong(dp.getEndTime(TimeUnit.MILLISECONDS).toString()))
            }
        }
//        syncRequest.stepsHistory=stepList as ArrayList<ActivitySyncRequest.StepsHistory>?
        return tempData
    }
    /**
     *  @Function : getDate()
     *  @params   : _date: String
     *  @Return   : Date
     * 	@Usage	  : Format the date based on the format which required to do Google fit requests
     *  @Author   : 1769
     */
    private fun getDateForGoogleFitRequest(_date: String): Date? {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return try {
            fmt.parse(_date)
        } catch (pe: ParseException) {
            null
        }
    }
    fun SyncDataToServer(): ActivitySyncRequest{
        val syncRequest= ActivitySyncRequest()

        // sync start date time is previous last time sync
        val userLastSyncDate:Date =Date(mAppPreference.userLastSyncActivityDate)
        val startSyncRequest=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startSyncTime = startSyncRequest.format(userLastSyncDate)

        // sync end date time is current datetime
        val nowTime = Calendar.getInstance().time
        val endTimeTillRequest = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val syncEndTime = endTimeTillRequest.format(nowTime)

        // last sync date
        val userUploadSyncFormat=SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val userUploadSyncTime=userUploadSyncFormat.format(userLastSyncDate)
        syncRequest.lastSyncTime=userUploadSyncTime

        return getStepCountForSyncWithServer(getDateForGoogleFitRequest(startSyncTime),getDateForGoogleFitRequest(syncEndTime),syncRequest)
    }


    /**
     *  @Function : getDistance()
     *  @params   : startDate: Date, endDate: Date
     *  @Return   : Double
     * 	@Usage	  : Define the Read Request to read the user's distance moved data from Google Fit
     *  @Author   : 1769
     */
    fun getDistance(startDate: Date?, endDate: Date?): Double{
        val startDate = startDate
        val cal1 = Calendar.getInstance()
        cal1.time = startDate
        val startTime = cal1.timeInMillis

        val endDate = endDate
        val cal2 = Calendar.getInstance()
        cal2.time = endDate
        val endTime = cal2.timeInMillis

        readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
        return readDistanceData()
    }

    /**
     *  @Function : readDistanceData()
     *  @params   : void
     *  @Return   : Int
     * 	@Usage	  : Execute Google fit read request and get the distance data
     *  @Author   : 1769
     */
    private fun readDistanceData(): Double {
        distance = 0.0
        val dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES)
        if (dataReadResult.buckets.size > 0) {
            for (bucket in dataReadResult.buckets) {
                val dataSets = bucket.dataSets
                for (dataSet in dataSets) {
                    showDistanceDataSet(dataSet)
                }
            }
        } else if (dataReadResult.dataSets.size > 0) {
            for (dataSet in dataReadResult.dataSets) {
                showDistanceDataSet(dataSet)
            }
        }
        return distance
    }

    /**
     *  @Function : showDistanceDataSet()
     *  @params   : dataSet: DataSet
     *  @Return   : void
     * 	@Usage	  : Extract the distance value in Km from Data set
     *  @Author   : 1769
     */
    private fun showDistanceDataSet(dataSet: DataSet) {
        for (dp in dataSet.dataPoints) {
            for (field in dp.dataType.fields) {
                var temp: Double = (dp.getValue(field).toString()).toDouble()
                distance += (temp/1000)
            }
        }
    }

    /**
     *  @Function : onConnectionSuspended()
     *  @params   : p0: Int
     *  @Return   : void
     * 	@Usage	  : Used to get status If Google API client connection suspended
     *  @Author   : 1769
     */
    override fun onConnectionSuspended(p0: Int) {
        d("Connection Suspended")
    }

    /**
     *  @Function : onConnectionFailed()
     *  @params   : connectionResult: ConnectionResult
     *  @Return   : void
     * 	@Usage	  : Used to get status and reconnect If Google API client connection failed
     *  @Author   : 1769
     */
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true
                connectionResult.startResolutionForResult(this, requestOAuth)
            } catch (e: IntentSender.SendIntentException) {

            }
        } else {
            d("auth in progress")
        }
        d("Connection failed")
    }

    /**
     *  @Function : onConnected()
     *  @params   : p0: Bundle
     *  @Return   : void
     * 	@Usage	  : Used to get status If Google API client successfully connected
     *  @Author   : 1769
     */
    override fun onConnected(p0: Bundle?) {
        d("Connected")
//        requestAllPermission()
    }

    /**
     *  @Function : startTimer()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Start the timer and manage to start new timer and continue with the last timer
     *  @Author   : 1769
     */
    private fun startTimer() {
        lateinit var time:String
        min = 0
        sec = 0
        if (mAppPreference.checkinHour == 25) {  // if timer is not running
            val startTime = Calendar.getInstance().getTime()
            val startCalender = Calendar.getInstance()
            startCalender.time = startTime
            mAppPreference.checkinHour = startCalender.get(Calendar.HOUR)
            mAppPreference.checkinMin = startCalender.get(Calendar.MINUTE)
            mAppPreference.checkinSec = startCalender.get(Calendar.SECOND)
        } else { // if timer is already running
            val currentDate = Calendar.getInstance().time
            val calenderForCurrent = Calendar.getInstance()
            calenderForCurrent.time = currentDate
            if (mAppPreference.checkinHour == calenderForCurrent.get(Calendar.HOUR)) {
                if (mAppPreference.checkinSec < calenderForCurrent.get(Calendar.SECOND)) {
                    min = calenderForCurrent.get(Calendar.MINUTE) - mAppPreference.checkinMin
                    sec = calenderForCurrent.get(Calendar.SECOND) - mAppPreference.checkinSec
                } else {
                    min = (calenderForCurrent.get(Calendar.MINUTE) - mAppPreference.checkinMin) - 1
                    sec = (60 - mAppPreference.checkinSec) + calenderForCurrent.get(Calendar.SECOND)
                }
            } else {
                min = (59 - mAppPreference.checkinMin) + calenderForCurrent.get(Calendar.MINUTE)
                if (mAppPreference.checkinSec < calenderForCurrent.get(Calendar.SECOND)) {
                    sec = calenderForCurrent.get(Calendar.SECOND) - mAppPreference.checkinSec
                } else {
                    min -= 1
                    sec = (60 - mAppPreference.checkinSec) + calenderForCurrent.get(Calendar.SECOND)
                }
            }
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
                }
            }
        }
    }

    /**
     *
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

    override fun onChallengeReceivedSuccessfully(receivedChallengesEntities: MutableList<ChallengesEntity>) {
        d("Call Done")

    }

    override fun onDownloadFinished() {
        hideLoading()
        mPagertabs.adapter!!.notifyDataSetChanged()
    }

    override fun onActivitySyncReceivedSuccessfully(receivedActivityEntities: ActivitySyncResponse.Data) {
        hideLoading()
        d("Call Done")

    }
}
