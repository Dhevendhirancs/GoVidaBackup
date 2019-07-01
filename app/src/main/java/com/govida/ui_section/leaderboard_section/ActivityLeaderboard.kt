/**
 * @Class : ActivityLeaderboard
 * @Usage : Used to render and manage leaderboard data
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.leaderboard_section.adapters.LeaderboardListAdapter
import com.govida.ui_section.leaderboard_section.database.LeaderboardEntity
import com.govida.ui_section.leaderboard_section.item_decorator_for_list.SimpleDividerItemDecoration
import com.govida.ui_section.leaderboard_section.model.AdapterObject
import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse
import com.govida.ui_section.leaderboard_section.mvp.LeaderboardMVP
import com.govida.ui_section.leaderboard_section.mvp.LeaderboardPresenterImplementer
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger

class ActivityLeaderboard : BaseActivity(), View.OnClickListener,LeaderboardMVP.LeaderboardView {
    private var mAppDb: AppDatabase? = null
    var mLeaderboardList: MutableList<AdapterObject> = mutableListOf()
    lateinit var mLeaderboardAdapter: LeaderboardListAdapter
    private lateinit var mRvLeaderboard: androidx.recyclerview.widget.RecyclerView
    private lateinit var mBtnDay: Button
    private lateinit var mBtnWeek: Button
    private lateinit var mBtnMonth: Button
    private lateinit var mBtnYear: Button
    private lateinit var mRlCurrentUser: RelativeLayout
    private lateinit var mIvCurrentUserPicture: ImageView
    private lateinit var mAppPreference: AppPreference
    private lateinit var currentTab: String
    private lateinit var mLeaderboardPresenter: LeaderboardMVP.LeaderboardPresenter
    private var mUserRank: Int = 0
    private lateinit var mTvUserRank: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
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
        currentTab = AppConstants.DAY
        mAppDb= AppDatabase.getDatabase(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar_settings)
        val toolbarTitle: TextView =toolbar.findViewById(R.id.title)
        mBtnDay = findViewById(R.id.btn_day)
        mBtnWeek = findViewById(R.id.btn_week)
        mBtnMonth = findViewById(R.id.btn_month)
        mBtnYear = findViewById(R.id.btn_year)
        mRlCurrentUser = findViewById(R.id.rl_current_user)
        mIvCurrentUserPicture = findViewById(R.id.iv_current_user)
        mTvUserRank = findViewById(R.id.tv_user_rank)
        mBtnDay.setOnClickListener(this)
        mBtnWeek.setOnClickListener(this)
        mBtnMonth.setOnClickListener(this)
        mBtnYear.setOnClickListener(this)
        mRlCurrentUser.setOnClickListener(this)
        toolbarTitle.text=getString(R.string.leaderboard)
        mRvLeaderboard=findViewById(R.id.rv_leaderboard)
        mLeaderboardAdapter= LeaderboardListAdapter(mLeaderboardList, this)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mRvLeaderboard.layoutManager = mLayoutManager
        mRvLeaderboard.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        mRvLeaderboard.adapter = mLeaderboardAdapter
        mRvLeaderboard.addItemDecoration(SimpleDividerItemDecoration(this))
        mLeaderboardPresenter = LeaderboardPresenterImplementer(this)
        mLeaderboardPresenter.onRequestLeaderboard(mAppPreference.authorizationToken)
//        AllLeaders(this).execute()
    }

    override fun onResume() {
        super.onResume()
        setProfilePicture()
    }

    /**
     *  @Function : setProfilePicture()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to user profile picture from URL
     *  @Author   : 1769
     */
    private fun setProfilePicture() {
        if (mAppPreference.profilePicturePath.isNotEmpty()) {
            Glide.with(this).load(mAppPreference.profilePicturePath).into(mIvCurrentUserPicture)
        } else {
            mIvCurrentUserPicture.setImageResource(R.drawable.profile_pic_3)
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
                    currentTab = AppConstants.DAY
                    mLeaderboardPresenter.onRequestLeaderboard(mAppPreference.authorizationToken)
                    mBtnDay.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnDay.setBackgroundResource(R.drawable.home_page_left_button_color_change)
                    mBtnWeek.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnWeek.setBackgroundResource(R.drawable.home_page_middle_button)
                    mBtnMonth.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnMonth.setBackgroundResource(R.drawable.home_page_middle_button)
                    mBtnYear.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnYear.setBackgroundResource(R.drawable.home_page_right_button)
                }
                R.id.btn_week -> {
                    currentTab = AppConstants.WEEK
                    mLeaderboardPresenter.onRequestLeaderboard(mAppPreference.authorizationToken)
                    mBtnDay.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnDay.setBackgroundResource(R.drawable.home_page_left_button)
                    mBtnWeek.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnWeek.setBackgroundResource(R.drawable.home_page_middle_button_color_change)
                    mBtnMonth.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnMonth.setBackgroundResource(R.drawable.home_page_middle_button)
                    mBtnYear.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnYear.setBackgroundResource(R.drawable.home_page_right_button)
                }
                R.id.btn_month -> {
                    currentTab = AppConstants.MONTH
                    mLeaderboardPresenter.onRequestLeaderboard(mAppPreference.authorizationToken)
                    mBtnDay.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnDay.setBackgroundResource(R.drawable.home_page_left_button)
                    mBtnWeek.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnWeek.setBackgroundResource(R.drawable.home_page_middle_button)
                    mBtnMonth.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnMonth.setBackgroundResource(R.drawable.home_page_middle_button_color_change)
                    mBtnYear.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnYear.setBackgroundResource(R.drawable.home_page_right_button)
                }
                R.id.btn_year -> {
                    currentTab = AppConstants.YEAR
                    mLeaderboardPresenter.onRequestLeaderboard(mAppPreference.authorizationToken)
                    mBtnDay.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnDay.setBackgroundResource(R.drawable.home_page_left_button)
                    mBtnWeek.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnWeek.setBackgroundResource(R.drawable.home_page_middle_button)
                    mBtnMonth.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnMonth.setBackgroundResource(R.drawable.home_page_middle_button)
                    mBtnYear.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnYear.setBackgroundResource(R.drawable.home_page_right_button_color_change)
                } R.id.rl_current_user -> {
                    mRvLeaderboard.smoothScrollToPosition(mUserRank)
                }
            }
        }
    }

    /**
     *  @Function : onLeaderboardSuccessful()
     *  @params   : result: LeaderboardResponse.Data?
     *  @Return   : void
     * 	@Usage	  : Execute if leaderboard api call successful and used to set data to adapter
     *  @Author   : 1769
     */
    override fun onLeaderboardSuccessful(result: LeaderboardResponse.Data?) {
        mLeaderboardList.clear()
        when (currentTab) {
            AppConstants.DAY -> {
                for (temp in result!!.topLeaderboard!!.daily!!) {
                    var t1=AdapterObject()
                    t1.activityType=temp.activityType
                    t1.distanceTravelled = temp.distanceTravelled
                    t1.employeeId = temp.employeeId
                    t1.employeeName = temp.employeeName
                    t1.rank = temp.rank
                    t1.stepsWalked = temp.stepsWalked
                    if (temp.employeeId == mAppPreference.employeeId) {
                        mUserRank = temp.rank!!
                        mTvUserRank.text = mUserRank.toString()
                    }
                    mLeaderboardList.add(t1)
                }
            } AppConstants.WEEK -> {
                for (temp in result!!.topLeaderboard!!.weekly!!) {
                    var t1=AdapterObject()
                    t1.activityType=temp.activityType
                    t1.distanceTravelled = temp.distanceTravelled
                    t1.employeeId = temp.employeeId
                    t1.employeeName = temp.employeeName
                    t1.rank = temp.rank
                    t1.stepsWalked = temp.stepsWalked
                    if (temp.employeeId == mAppPreference.employeeId) {
                        mUserRank = temp.rank!!
                        mTvUserRank.text = mUserRank.toString()
                    }
                    mLeaderboardList.add(t1)
                }
            } AppConstants.MONTH -> {
                for (temp in result!!.topLeaderboard!!.monthly!!) {
                    var t1=AdapterObject()
                    t1.activityType=temp.activityType
                    t1.distanceTravelled = temp.distanceTravelled
                    t1.employeeId = temp.employeeId
                    t1.employeeName = temp.employeeName
                    t1.rank = temp.rank
                    t1.stepsWalked = temp.stepsWalked
                    if (temp.employeeId == mAppPreference.employeeId) {
                        mUserRank = temp.rank!!
                        mTvUserRank.text = mUserRank.toString()
                    }
                    mLeaderboardList.add(t1)
                }
            } AppConstants.YEAR -> {
                for (temp in result!!.topLeaderboard!!.yearly!!) {
                    var t1=AdapterObject()
                    t1.activityType=temp.activityType
                    t1.distanceTravelled = temp.distanceTravelled
                    t1.employeeId = temp.employeeId
                    t1.employeeName = temp.employeeName
                    t1.rank = temp.rank
                    t1.stepsWalked = temp.stepsWalked
                    if (temp.employeeId == mAppPreference.employeeId) {
                        mUserRank = temp.rank!!
                        mTvUserRank.text = mUserRank.toString()
                    }
                    mLeaderboardList.add(t1)
                }
            }
        }
        mLeaderboardAdapter.notifyDataSetChanged()
    }

    /**
     *  @Function : onLeaderboardFailed()
     *  @params   : errorMsg: String
     *  @Return   : void
     * 	@Usage	  : Execute if leaderboard api call fails
     *  @Author   : 1769
     */
    override fun onLeaderboardFailed(errorMsg: String) {
        AppLogger.d(errorMsg)
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }

    /**
     *  @Class : AllLeaders
     * 	@Usage : Used to get the leaderboard data list
     *  @Author : 1769
     */
    /*
    private class AllLeaders(var context: ActivityLeaderboard) : AsyncTask<Void, Void, MutableList<LeaderboardEntity>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context.showLoading()
        }
        override fun doInBackground(vararg params: Void?): MutableList<LeaderboardEntity> {
            var tempList: MutableList<LeaderboardEntity> = mutableListOf()
            if(context.mAppDb?.leaderboardDao()?.allLeadersCount!! >0)
            {
                tempList=context.mAppDb?.leaderboardDao()?.allLeaders!!
                return tempList
            }
            return tempList
        }
        override fun onPostExecute(tempList:  MutableList<LeaderboardEntity> ?) {
            context.hideLoading()
            context.mLeaderboardList.clear()
//            context.mLeaderboardList.addAll(tempList!!)
            context.mLeaderboardAdapter.notifyDataSetChanged()
        }
    }
    */
}
