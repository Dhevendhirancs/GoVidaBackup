package com.govida.ui_section.home_section.activity_section

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseFragment
import com.govida.ui_section.home_section.ActivityHome
import com.govida.ui_section.home_section.activity_section.mvp.ActivityFragmentMVP
import com.govida.ui_section.home_section.activity_section.mvp.ActivityFragmentPresenterImplementer
import com.govida.ui_section.home_section.challenges_section.adapters.ChallengeListAdapter
import com.govida.ui_section.home_section.challenges_section.rv_touch_listeners.RecyclerviewTouchListener
import com.govida.ui_section.home_section.checkin_section.ActivityCheckin
import com.govida.ui_section.home_section.checkin_section.ActivitySelectVenue
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.ui_section.home_section.profile_section.ActivityProfileEdit
import com.govida.ui_section.setting_section.ActivitySettings
import com.govida.utility_section.AppLogger
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class FragmentActivities : BaseFragment(), View.OnClickListener, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener,ActivityFragmentMVP.ActivityFragmentView {

    private lateinit var btnDay:Button
    private lateinit var btnWeek:Button
    private lateinit var btnMonth:Button
    private lateinit var btnYear:Button
    private lateinit var btnToday:Button
    private lateinit var btnTotalGoVps:Button
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var mSliderAdapter: SliderAdapter
    private lateinit var mTvBonusPoints: TextView

    private lateinit var mTvActivityPoints:TextView
    private lateinit var mTvStepPoints:TextView
    private lateinit var mTvCheckinPoints:TextView
    private lateinit var mTvTotalGovpsPoints:TextView
    private lateinit var mTvCharityPoints:TextView
    private lateinit var mTvTotleGovpsRedeemPoints:TextView
    private lateinit var mTvChallengePoints:TextView

    private var mPointsButtonOnToday=true
    private var mActivityDurationSelector=1   // 1->day 2-> week 3-> month 4 -> year
    private lateinit var mActivitiesTvCheckIn:TextView
    private lateinit var mActivitiesTvGovps:TextView
    private lateinit var appPreference: AppPreference
    private lateinit var mIvPhotoOne: ImageView
    private lateinit var mIvPhotoTwo: ImageView
    private lateinit var mIvPhotoThree: ImageView
    private lateinit var mFragmentActPresenter:ActivityFragmentMVP.ActivityFragmentPresenter
    var layouts: MutableList<Int> = mutableListOf()
    private lateinit var viewPagerPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var mTvToday:TextView
    private lateinit var mTvDayLimit:TextView
    lateinit var tvDistance:TextView
    private lateinit var mllFromTo:LinearLayout
    lateinit var tvSteps:TextView
    lateinit var startDate:String
    lateinit var endDate:String
    var steps:Int = 0
    var distance:Double = 0.0
    private lateinit var mSwipePageView: androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    lateinit var mLlSwipeCardManager:LinearLayout
    var mAppDb: AppDatabase? = null
    var challengeList: MutableList<ChallengesEntity> = mutableListOf()
    private lateinit var rvChallengeList: androidx.recyclerview.widget.RecyclerView
    lateinit var mChallengeAdapter: ChallengeListAdapter
    lateinit var mTvOngoingChallengesCount: TextView
    lateinit var mTvOngoingChallengesTitle: TextView
    var isResumeCalled:Boolean=false
    private var permissionCardPosition: Int = 5
    private var profileCardPosition: Int = 5
    private var checkinCardPosition:Int = 5
    private var challengeCardPosition:Int = 5
    private var isSyncCalledTrigger=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLogger.d("auth in progress")
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view: View = inflater.inflate(R.layout.fragment_activities, container, false)
        setupUI(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        mFragmentActPresenter.attachView(this)
        isResumeCalled=true
        todayDataFullView()
        OngoingChallenges(this).execute()
        if(mPointsButtonOnToday){
            btnToday.performClick()
        }
        else{
            btnTotalGoVps.performClick()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            if(isResumeCalled){
                OngoingChallenges(this).execute()
                setupDotsIndicatorLayouts()
            }
        }
    }

    /**
     *  @Function : setupUI()
     *  @params   : view: View
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI(view: View) {
        appPreference = AppPreference(context!!)
        val toolbar:Toolbar= activity!!.findViewById(R.id.toolbar)
        val toolbarTitle:TextView=toolbar.findViewById(R.id.title)
        toolbarTitle.text=getString(R.string.title_activity)

        btnDay = view.findViewById(R.id.btn_day)
        btnWeek = view.findViewById(R.id.btn_week)
        btnMonth = view.findViewById(R.id.btn_month)
        btnYear = view.findViewById(R.id.btn_year)
        mTvToday=view.findViewById(R.id.activity_tv_today)
        btnToday = view.findViewById(R.id.btn_today)
        btnTotalGoVps = view.findViewById(R.id.btn_total_govps)
        mllFromTo=view.findViewById(R.id.acitvity_ll_from_to)
        mTvDayLimit=view.findViewById(R.id.activity_tv_day_limit)
        tvSteps=view.findViewById(R.id.tv_steps)
        tvDistance=view.findViewById(R.id.tv_distance)
        mSwipePageView=view.findViewById(R.id.activity_swipe)
        mTvOngoingChallengesTitle = view.findViewById(R.id.tv_ongoing_challenges_title)
        mTvBonusPoints = view.findViewById(R.id.tv_bonus_points)
        mLlSwipeCardManager=view.findViewById(R.id.ll_swipe_card_manager)
        mTvActivityPoints=view.findViewById(R.id.tv_activity_points)
        mTvStepPoints=view.findViewById(R.id.tv_step_points)
        mTvCheckinPoints=view.findViewById(R.id.tv_checkin_points)
        mTvTotalGovpsPoints=view.findViewById(R.id.tv_totel_govps_points)
        mTvCharityPoints=view.findViewById(R.id.tv_charity_points)
        mTvChallengePoints=view.findViewById(R.id.tv_challenge_points)
        mTvTotleGovpsRedeemPoints=view.findViewById(R.id.tv_totle_govps_redeem_points)

        mActivitiesTvCheckIn=view.findViewById(R.id.activities_tv_check_in)
        mActivitiesTvGovps=view.findViewById(R.id.activities_tv_govps)


        mFragmentActPresenter=ActivityFragmentPresenterImplementer(this)
        mTvOngoingChallengesTitle.visibility = View.GONE
        mllFromTo.visibility=View.GONE
        btnDay.setOnClickListener(this)
        btnWeek.setOnClickListener(this)
        btnMonth.setOnClickListener(this)
        btnYear.setOnClickListener(this)
        btnToday.setOnClickListener(this)
        btnTotalGoVps.setOnClickListener(this)
        mSwipePageView.setOnRefreshListener(this)

        mViewPager = view.findViewById(R.id.view_pager)

        permissionCardPosition = 5
        profileCardPosition = 5
        dotsIndicator = view.findViewById(R.id.layoutDots)
        setupDotsIndicatorLayouts()
        mAppDb= AppDatabase.getDatabase(activity!!.applicationContext)
        rvChallengeList=view.findViewById(R.id.challenges_rv)
        mChallengeAdapter=ChallengeListAdapter(challengeList, activity!!,mAppDb)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        rvChallengeList.layoutManager = mLayoutManager
        rvChallengeList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        rvChallengeList.adapter = mChallengeAdapter
        rvChallengeList.addOnItemTouchListener(
            RecyclerviewTouchListener(context, rvChallengeList,
                object : RecyclerviewTouchListener.ClickListener {
                    override fun onLongClick(view: View?, position: Int) {
                    }
                    override fun onClick(view: View, position: Int) {
                    }
                })
        )
        mTvOngoingChallengesCount=view.findViewById(R.id.tv_ongoing_challenges_count)
        if (appPreference.isGoogleFitPermissionGiven && appPreference.isGPSPermissionGiven) {
            if(!isSyncCalledTrigger){
                isSyncCalledTrigger=true
                SendActivityStateToServer().execute()
            }

        }
        setBonusPoints()
        setPointsData()
    }

    /**
     *  @Function : setupDotsIndicatorLayouts()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup card layouts for dots indicator based on user permissions
     *  @Author   : 1769
     */
    private fun setupDotsIndicatorLayouts () {
        layouts.clear()

        if (!appPreference.isGPSPermissionGiven || !appPreference.isGoogleFitPermissionGiven) {
            permissionCardPosition = layouts.size
            layouts.add(layouts.size, R.layout.swipe_card4)
        }
        if (!appPreference.isProfileCompletionBonusReceived) {
            profileCardPosition = layouts.size
            layouts.add(layouts.size, R.layout.swipe_card1)
        }
        if (!appPreference.isCheckInBonusReceived) {
            checkinCardPosition = layouts.size
            layouts.add(layouts.size, R.layout.swipe_card2)
        }
        if (!appPreference.isFirstChallengeCardShow) {
            challengeCardPosition = layouts.size
            layouts.add(layouts.size, R.layout.swipe_card3)
        }
        viewPagerPageChangeListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts.size - 1) {
                    // last page. make button text to GOT IT
//                    dotsIndicator.visibility=View.INVISIBLE
                } else {
                    // still pages are left
//                    dotsIndicator.visibility=View.VISIBLE
                }
            }
            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            }

            override fun onPageScrollStateChanged(arg0: Int) {
            }
        }

        mSliderAdapter = SliderAdapter()
        mViewPager.adapter = mSliderAdapter
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        dotsIndicator.setViewPager(mViewPager)
        if (layouts.isEmpty()) {
            mLlSwipeCardManager.visibility = GONE
        }
    }


    override fun onDestroy() {
        mFragmentActPresenter.destroyView()
        super.onDestroy()
    }

    inner class SliderAdapter : androidx.viewpager.widget.PagerAdapter()
    {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts[position], container, false)
            setProfilePicture(view)
            when (position) {
                permissionCardPosition -> {
                    if (!appPreference.isGPSPointAlert && !appPreference.isDeviceIntegrationPointAlert) {
                        view!!.findViewById<TextView>(R.id.tv_swipe_card_permission_desc).text = getString(R.string.swipe_card4_desc_both)
                    } else if (!appPreference.isGPSPointAlert) {
                        view!!.findViewById<TextView>(R.id.tv_swipe_card_permission_desc).text = getString(R.string.swipe_card4_desc_gps)
                    } else if (!appPreference.isDeviceIntegrationPointAlert){
                        view!!.findViewById<TextView>(R.id.tv_swipe_card_permission_desc).text = getString(R.string.swipe_card4_desc_googlefit)
                    } else if (!appPreference.isGPSPermissionGiven && !appPreference.isGoogleFitPermissionGiven) {
                        view!!.findViewById<TextView>(R.id.tv_swipe_card_permission_desc).text = getString(R.string.both_alert)
                    } else if (!appPreference.isGPSPermissionGiven) {
                        view!!.findViewById<TextView>(R.id.tv_swipe_card_permission_desc).text = getString(R.string.gps_alert)
                    } else if (!appPreference.isGoogleFitPermissionGiven){
                        view!!.findViewById<TextView>(R.id.tv_swipe_card_permission_desc).text = getString(R.string.google_fit_alert)
                    }
                } profileCardPosition -> {
                    view!!.findViewById<TextView>(R.id.tv_firstname).text=getString(R.string.demo_hey_user, appPreference.userFirstName)
                    view!!.findViewById<TextView>(R.id.tv_profile_completion_percent).text = appPreference.profileCompletionStatus.toString() + "%"
                    view!!.findViewById<ProgressBar>(R.id.pb_profile_completion_percent).progress = appPreference.profileCompletionStatus
                }
            }

            view.setOnClickListener {
                when(position){
                    profileCardPosition -> {
                        // for profile
                        val mainActIntent = Intent(activity, ActivityProfileEdit::class.java)
                        startActivity(mainActIntent)
                    } checkinCardPosition -> {
                        // for Check-in
                        if (appPreference.checkinTime.equals("")) {
                            val mainActIntent = Intent(activity, ActivitySelectVenue::class.java)
                            startActivity(mainActIntent)
                        } else {
                            val mainActIntent = Intent(activity, ActivityCheckin::class.java)
                            startActivity(mainActIntent)
                        }
                    } challengeCardPosition -> {
                        // for Challenges
                        (activity as ActivityHome).moveToRequiredPageinHomeSection(1)
                    } permissionCardPosition -> {
                        // for permission card
                        val mainActIntent = Intent(activity, ActivitySettings::class.java)
                        startActivity(mainActIntent)
                    }
                }
            }
            container.addView(view)
            return view
        }

        /**
         *  @Function : setProfilePicture()
         *  @params   : view: View?
         *  @Return   : void
         * 	@Usage	  : Setup profile picture in profile completion card
         *  @Author   : 1769
         */
        private fun setProfilePicture(view: View?) {
            if (appPreference.profilePicturePath.isNotEmpty()) {
                Glide.with(activity!!).load(appPreference.profilePicturePath).into(view!!.findViewById<ImageView>(R.id.iv_profile_picture))
            } else {
                view!!.findViewById<ImageView>(R.id.iv_profile_picture).setImageResource(R.drawable.profile_pic_3)
            }
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }


    }

    /**
     *  @Function : setBonusPoints()
     *  @params   : view: View?
     *  @Return   : void
     * 	@Usage	  : Setup initial bonus points for onboarding, gps and google fit permission access
     *  @Author   : 1769
     */
    private fun setBonusPoints() {
        appPreference = AppPreference(activity!!)
        var bonusPoints = 0
        if (appPreference.isOnboardingPointAlert) {
            bonusPoints += 10
        }
        if (appPreference.isDeviceIntegrationPointAlert) {
            bonusPoints +=10
        }
        if (appPreference.isGPSPointAlert) {
            bonusPoints += 10
        }
        if (appPreference.isFirstChallengeCardShow) {
            bonusPoints += 10
        }
        mTvBonusPoints.text = bonusPoints.toString()
    }

    /**
     *  @Class : GetStepCount
     * 	@Usage	  : Used for Google fit request execution in background thread
     *  @Author   : 1769
     */
    private inner class GetStepCount : AsyncTask<Void, Void, Void>() {

        /**
         *  @Function : doInBackground()
         *  @params   : vararg params: Void
         *  @Return   : Void
         * 	@Usage	  : Initiate the background thread for Google fit requests
         *  @Author   : 1769
         */
        override fun doInBackground(vararg params: Void): Void? {
            try{
                (activity as ActivityHome).connectApiClient()
                steps = (activity as ActivityHome).getStepCount(getDate(startDate),  getDate(endDate))
                distance = (activity as ActivityHome).getDistance(getDate(startDate),  getDate(endDate))
            }catch(ex:Exception)
            {
                Crashlytics.logException(ex)
                steps=0
                distance=0.0
            }
            return null
        }

        /**
         *  @Function : onPostExecute()
         *  @params   : result: Void
         *  @Return   : void
         * 	@Usage	  : Used to do actions immediately after the background thread execution
         *  @Author   : 1769
         */
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            hideLoading()
            tvSteps.text = displayFormat(steps)
            if (distance > 99) {
                tvDistance.text = String.format("%dKm", distance.toInt())
            } else {
                tvDistance.text = String.format("%.1fKm", distance)
            }
        }
    }


    /**
     *  @Class : SendActivityStateToServer
     * 	@Usage	  : Used for Google fit request execution and upload data to server
     *  @Author   : 1276
     */
    private inner class SendActivityStateToServer : AsyncTask<Void, Void, ActivitySyncRequest>() {
        override fun doInBackground(vararg params: Void): ActivitySyncRequest? {
            try{
                (activity as ActivityHome).connectApiClient()
                return (activity as ActivityHome).SyncDataToServer()
            }catch(ex:Exception)
            {
                Crashlytics.logException(ex)
            }
            return null
        }

        /**
         *  @Function : onPostExecute()
         *  @params   : result: Void
         *  @Return   : void
         * 	@Usage	  : Used to do actions immediately after the background thread execution
         *  @Author   : 1769
         */
        override fun onPostExecute(result: ActivitySyncRequest?) {
            super.onPostExecute(result)
            hideLoading()
            if(result!=null){
                mFragmentActPresenter.onActivitySyncPostRequested(appPreference.authorizationToken,result)
                /*if(result.stepsHistory!=null && result.stepsHistory!!.count()>0){
                    mFragmentActPresenter.onActivitySyncPostRequested(appPreference.authorizationToken,result)
                }*/
            }

        }
    }

    /**
     *  @Function : displayFormat()
     *  @params   : steps: Int
     *  @Return   : CharSequence
     * 	@Usage	  : Format the data which are provided by Google fit
     *  @Author   : 1769
     */
    private fun displayFormat(steps: Int): CharSequence? {
        val step:Double = steps.toDouble()
        return when {
            step < 999 -> step.toString()
            step < 999999 -> String.format("%.1fK", step/1000)
            else -> String.format("%.1fM", step/10000)
        }
    }

    /**
     *  @Function : getDate()
     *  @params   : _date: String
     *  @Return   : Date
     * 	@Usage	  : Format the date based on the format which required to do Google fit requests
     *  @Author   : 1769
     */
    private fun getDate(_date: String): Date? {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return try {
            fmt.parse(_date)
        } catch (pe: ParseException) {
            null
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
                R.id.btn_day -> {
                    setPointsData()
                    mActivityDurationSelector=1
                    todayDataFullView()
                }R.id.btn_week -> {
                    if (appPreference.isGoogleFitPermissionGiven && appPreference.isGPSPermissionGiven) {
                        mActivityDurationSelector=2
                        setPointsData()
                        getWeek()
                        GetStepCount().execute()
                    }
                    btnDay.setTextColor(resources.getColor(R.color.default_text_color))
                    btnDay.setBackgroundResource(R.drawable.home_page_left_button)
                    btnWeek.setTextColor(resources.getColor(R.color.colorWhite))
                    btnWeek.setBackgroundResource(R.drawable.home_page_middle_button_color_change)
                    btnMonth.setTextColor(resources.getColor(R.color.default_text_color))
                    btnMonth.setBackgroundResource(R.drawable.home_page_middle_button)
                    btnYear.setTextColor(resources.getColor(R.color.default_text_color))
                    btnYear.setBackgroundResource(R.drawable.home_page_right_button)
                } R.id.btn_month -> {
                    if (appPreference.isGoogleFitPermissionGiven && appPreference.isGPSPermissionGiven) {
                        mActivityDurationSelector=3
                        setPointsData()
                        getMonth()
                        GetStepCount().execute()
                    }
                    btnDay.setTextColor(resources.getColor(R.color.default_text_color))
                    btnDay.setBackgroundResource(R.drawable.home_page_left_button)
                    btnWeek.setTextColor(resources.getColor(R.color.default_text_color))
                    btnWeek.setBackgroundResource(R.drawable.home_page_middle_button)
                    btnMonth.setTextColor(resources.getColor(R.color.colorWhite))
                    btnMonth.setBackgroundResource(R.drawable.home_page_middle_button_color_change)
                    btnYear.setTextColor(resources.getColor(R.color.default_text_color))
                    btnYear.setBackgroundResource(R.drawable.home_page_right_button)
                } R.id.btn_year -> {
                    if (appPreference.isGoogleFitPermissionGiven && appPreference.isGPSPermissionGiven) {
                        mActivityDurationSelector=4
                        setPointsData()
                        getYear()
                        GetStepCount().execute()
                    }
                    btnDay.setTextColor(resources.getColor(R.color.default_text_color))
                    btnDay.setBackgroundResource(R.drawable.home_page_left_button)
                    btnWeek.setTextColor(resources.getColor(R.color.default_text_color))
                    btnWeek.setBackgroundResource(R.drawable.home_page_middle_button)
                    btnMonth.setTextColor(resources.getColor(R.color.default_text_color))
                    btnMonth.setBackgroundResource(R.drawable.home_page_middle_button)
                    btnYear.setTextColor(resources.getColor(R.color.colorWhite))
                    btnYear.setBackgroundResource(R.drawable.home_page_right_button_color_change)
                } R.id.btn_today -> {
                    mPointsButtonOnToday=true
                    setPointsData()
                    btnToday.setTextColor(resources.getColor(R.color.colorWhite))
                    btnToday.setBackgroundResource(R.drawable.home_page_left_button_color_change)
                    btnTotalGoVps.setTextColor(resources.getColor(R.color.default_text_color))
                    btnTotalGoVps.setBackgroundResource(R.drawable.home_page_right_button)
                } R.id.btn_total_govps -> {
                    mPointsButtonOnToday=false
                    setPointsData()
                    btnToday.setTextColor(resources.getColor(R.color.default_text_color))
                    btnToday.setBackgroundResource(R.drawable.home_page_left_button)
                    btnTotalGoVps.setTextColor(resources.getColor(R.color.colorWhite))
                    btnTotalGoVps.setBackgroundResource(R.drawable.home_page_right_button_color_change)
                }
            }
        }
    }

    /**
     *  @Function : todayDataFullView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup today data from google fit
     *  @Author   : 1769
     */
    private fun todayDataFullView() {
        if (appPreference.isGoogleFitPermissionGiven && appPreference.isGPSPermissionGiven) {
            setTodayDate()
            getToday()
            GetStepCount().execute()
        }
        btnDay.setTextColor(resources.getColor(R.color.colorWhite))
        btnDay.setBackgroundResource(R.drawable.home_page_left_button_color_change)
        btnWeek.setTextColor(resources.getColor(R.color.default_text_color))
        btnWeek.setBackgroundResource(R.drawable.home_page_middle_button)
        btnMonth.setTextColor(resources.getColor(R.color.default_text_color))
        btnMonth.setBackgroundResource(R.drawable.home_page_middle_button)
        btnYear.setTextColor(resources.getColor(R.color.default_text_color))
        btnYear.setBackgroundResource(R.drawable.home_page_right_button)
    }

    /**
     *  @Function : setYearDate()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Format the date for a year and display
     *  @Author   : 1769
     */
    private fun setYearDate() {
        mllFromTo.visibility=View.VISIBLE
        val format = SimpleDateFormat("EEE, dd MMM yyy")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        mTvToday.text =format.format(calendar.time)
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyy")
        mTvDayLimit.text =getString(R.string.demo_date, df.format(c))
    }

    /**
     *  @Function : setMonthDate()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Format the date for a month and display
     *  @Author   : 1769
     */
    private fun setMonthDate() {
        mllFromTo.visibility=View.VISIBLE
        val format = SimpleDateFormat("EEE, dd MMM yyy")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        mTvToday.text =format.format(calendar.time)
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyy")
        mTvDayLimit.text =getString(R.string.demo_date, df.format(c))
    }

    /**
     *  @Function : setWeekDate()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Format the date for a week and display
     *  @Author   : 1769
     */
    private fun setWeekDate() {
        mllFromTo.visibility=View.VISIBLE
        val format = SimpleDateFormat("EEE, dd MMM yyy")
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
        mTvToday.text =format.format(calendar.time)

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyy")
        mTvDayLimit.text =getString(R.string.demo_date, df.format(c))
    }

    /**
     *  @Function : setTodayDate()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Format the date for today and display
     *  @Author   : 1769
     */
    private fun setTodayDate() {
        mllFromTo.visibility=View.GONE
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd MMM yyy")
        mTvToday.text =getString(R.string.demo_date, df.format(c))
    }

    /**
     *  @Function : getToday()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Get the today date and set it into startDate and endDate for Google fit requests
     *  @Author   : 1769
     */
    private fun getToday() {
        val now = Calendar.getInstance().time
        val endFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        endDate = endFormat.format(now)
        val startFormat = SimpleDateFormat("yyyy-MM-dd")
        startDate = startFormat.format(now) + " 00:00:00"
    }

    /**
     *  @Function : getWeek()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Get the week date and set it into startDate and endDate for Google fit requests
     *  @Author   : 1769
     */
    private fun getWeek() {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
        val startFormat = SimpleDateFormat("yyyy-MM-dd")
        startDate = startFormat.format(calendar.time) + " 00:00:00"
        // if start date is older than login date then use login date as starting point
        // else start date is younger than login date , means lot of time is passed after login then
        // use start date as default
        val now = Calendar.getInstance().time
        val endFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        endDate = endFormat.format(now)
    }

    /**
     *  @Function : getMonth()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Get the month date and set it into startDate and endDate for Google fit requests
     *  @Author   : 1769
     */
    private fun getMonth() {
        val date = Calendar.getInstance().time
        val calender = Calendar.getInstance()
        calender.time = date
        val tempMonth: Int =calender.get(Calendar.MONTH)
        val tempYear:Int = calender.get(Calendar.YEAR)
        var month:String = tempMonth.toString()
        val year:String = tempYear.toString()
        if (month.length < 2)
            month = "0$month"
        startDate = "$year-$month-01 00:00:00"

        val now = Calendar.getInstance().time
        val endFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        endDate = endFormat.format(now)
    }

    /**
     *  @Function : getYear()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Get the year date and set it into startDate and endDate for Google fit requests
     *  @Author   : 1769
     */
    private fun getYear() {
        val date = Calendar.getInstance().time
        val calender = Calendar.getInstance()
        calender.time = date
        val tempYear:Int = calender.get(Calendar.YEAR)
        val year:String = tempYear.toString()
        startDate = "$year-01-01 00:00:00"

        val now = Calendar.getInstance().time
        val endFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        endDate = endFormat.format(now)
    }

    /**
     *  @Function : onRefresh()
     *  @params   : v: View
     *  @Return   : void
     * 	@Usage	  : listener function for swipe or pull to refresh
     *  @Author   : 1769
     */
    override fun onRefresh() {
        mSwipePageView.isRefreshing = true
        if (appPreference.isGoogleFitPermissionGiven && appPreference.isGPSPermissionGiven) {
            SendActivityStateToServer().execute()
        }
    }

    override fun onActivitySyncReceivedSuccessfully(receivedChallengesEntities: ActivitySyncResponse) {
        mSwipePageView.isRefreshing = false
        val receivedData=receivedChallengesEntities.responseBody?.data
        appPreference.dayCheckInCount=""+receivedData?.day?.checkinCount
        appPreference.daySteps=""+receivedData?.day?.dayActivity?.steps
        appPreference.dayCheckIn=""+receivedData?.day?.dayActivity?.checkIn
        appPreference.dayBonus=""+receivedData?.day?.bonus
        appPreference.dayCharityEarning=""+receivedData?.day?.charityEarnings
        appPreference.dayChallenge=""+receivedData?.day?.challengePoint
        appPreference.dayRedeem=""+receivedData?.day?.redeemed


        appPreference.weekCheckInCount=""+receivedData?.week?.checkinCount
        appPreference.weekSteps=""+receivedData?.week?.weekActivity?.steps
        appPreference.weekCheckIn=""+receivedData?.week?.weekActivity?.checkIn
        appPreference.weekBonus=""+receivedData?.week?.bonus
        appPreference.weekCharityEarning=""+receivedData?.week?.charityEarnings
        appPreference.weekChallenge=""+receivedData?.week?.challengePoint
        appPreference.weekRedeem=""+receivedData?.week?.redeemed

        appPreference.monthCheckInCount=""+receivedData?.month?.checkinCount
        appPreference.monthSteps=""+receivedData?.month?.monthActivity?.steps
        appPreference.monthCheckIn=""+receivedData?.month?.monthActivity?.checkIn
        appPreference.monthBonus=""+receivedData?.month?.bonus
        appPreference.monthCharityEarning=""+receivedData?.month?.charityEarnings
        appPreference.monthChallenge=""+receivedData?.month?.challengePoint
        appPreference.monthRedeem=""+receivedData?.month?.redeemed

        appPreference.yearCheckInCount=""+receivedData?.year?.checkinCount
        appPreference.yearSteps=""+receivedData?.year?.yearActivity?.steps
        appPreference.yearCheckIn=""+receivedData?.year?.yearActivity?.checkIn
        appPreference.yearBonus=""+receivedData?.year?.bonus
        appPreference.yearCharityEarning=""+receivedData?.year?.charityEarnings
        appPreference.yearChallenge=""+receivedData?.year?.challengePoint
        appPreference.yearRedeem=""+receivedData?.year?.redeemed

        appPreference.totalCheckInCount=""+receivedData?.totalGoVp?.checkinCount
        appPreference.totalSteps=""+receivedData?.totalGoVp?.totalGoVpsActivity?.steps
        appPreference.totalCheckIn=""+receivedData?.totalGoVp?.totalGoVpsActivity?.checkIn
        appPreference.totalBonus=""+receivedData?.totalGoVp?.bonus
        appPreference.totalCharityEarning=""+receivedData?.totalGoVp?.charityEarnings
        appPreference.totalChallenge=""+receivedData?.totalGoVp?.challengePoint
        appPreference.totalRedeem=""+receivedData?.totalGoVp?.redeemed
        setPointsData()

        //val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //val dateInMili=fmt.parse(receivedChallengesEntities.lastSyncTime).time

        val fmt =SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        fmt.timeZone = TimeZone.getTimeZone("UTC");
        val dateFromServer=fmt.parse(receivedChallengesEntities.lastSyncTime)
        fmt.timeZone = TimeZone.getDefault()
        val convertedTimeToDevice=fmt.format(dateFromServer)
        val dateInMili=fmt.parse(convertedTimeToDevice).time
        appPreference.userLastSyncActivityDate=dateInMili

    }

    private fun setPointsData(){
        if(mPointsButtonOnToday){
            mTvBonusPoints.text=appPreference.dayBonus
            mTvActivityPoints.text=""+((appPreference.daySteps).toInt()+(appPreference.dayCheckIn).toInt())
            mTvStepPoints.text=appPreference.daySteps
            mTvCheckinPoints.text=appPreference.dayCheckIn
            mTvChallengePoints.text=appPreference.dayChallenge
            mTvTotleGovpsRedeemPoints.text=appPreference.dayRedeem
            mTvTotalGovpsPoints.text=""+((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt()+(appPreference.totalBonus).toInt()+(appPreference.totalChallenge).toInt()-(appPreference.totalRedeem).toInt())
            mTvCharityPoints.text=appPreference.totalCharityEarning
        }else{
            mTvBonusPoints.text=appPreference.totalBonus
            mTvActivityPoints.text=""+((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt())
            mTvStepPoints.text=appPreference.totalSteps
            mTvCheckinPoints.text=appPreference.totalCheckIn
            mTvChallengePoints.text=appPreference.totalChallenge
            mTvTotleGovpsRedeemPoints.text=appPreference.totalRedeem
            mTvTotalGovpsPoints.text=""+(((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt()+(appPreference.totalBonus).toInt()+(appPreference.totalChallenge).toInt()-(appPreference.totalRedeem).toInt()))
            mTvCharityPoints.text=appPreference.totalCharityEarning
        }

        when(mActivityDurationSelector){
            1->{
                mActivitiesTvCheckIn.text=appPreference.dayCheckInCount
                mActivitiesTvGovps.text=""+(((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt()+(appPreference.totalBonus).toInt()+(appPreference.totalChallenge).toInt()))
            }
            2->{
                mActivitiesTvCheckIn.text=appPreference.weekCheckInCount
                mActivitiesTvGovps.text=""+(((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt()+(appPreference.totalBonus).toInt()+(appPreference.totalChallenge).toInt()))
            }
            3->{
                mActivitiesTvCheckIn.text=appPreference.monthCheckInCount
                mActivitiesTvGovps.text=""+(((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt()+(appPreference.totalBonus).toInt()+(appPreference.totalChallenge).toInt()))
            }
            4->{
                mActivitiesTvCheckIn.text=appPreference.yearCheckInCount
                mActivitiesTvGovps.text=""+(((appPreference.totalSteps).toInt()+(appPreference.totalCheckIn).toInt()+(appPreference.totalBonus).toInt()+(appPreference.totalChallenge).toInt()))
            }

        }
    }
    override fun onActivitySyncReceivedFailed(warning: String) {
        mSwipePageView.isRefreshing = false
        isSyncCalledTrigger=false
    }

    /**
     *  @Class : OngoingChallenges
     * 	@Usage  : render ongoing challenges in activity page
     *  @Author   : 1769
     */
    private class OngoingChallenges(var context: FragmentActivities) : AsyncTask<Void, Void, MutableList<ChallengesEntity>>() {

        override fun doInBackground(vararg params: Void?): MutableList<ChallengesEntity> {
            var tempList: MutableList<ChallengesEntity> = mutableListOf()
            if(context.mAppDb?.challengeDao()?.allChallengeCount!! >0)
            {
                tempList=context.mAppDb?.challengeDao()?.ongoingChallenges!!
                return tempList
            }
            return tempList
        }
        override fun onPostExecute(tempList:  MutableList<ChallengesEntity> ?) {
            if (tempList != null) {
                if (tempList.size > 0) {
                    context.mTvOngoingChallengesTitle.visibility = View.VISIBLE
                    context.mTvOngoingChallengesCount.visibility = View.VISIBLE
                    context.mTvOngoingChallengesCount.text = " (" + tempList!!.size.toString() + ")"
                    context.challengeList.clear()
                    context.challengeList.addAll(tempList!!)
                    context.rvChallengeList.visibility = View.VISIBLE
                    context.mChallengeAdapter.notifyDataSetChanged()
                } else {
                    context.mTvOngoingChallengesTitle.visibility = View.GONE
                    context.mTvOngoingChallengesCount.visibility = View.GONE
                    context.challengeList.clear()
                    context.mChallengeAdapter.notifyDataSetChanged()
                    context.rvChallengeList.visibility = View.GONE
                }
            }
        }
    }
}
