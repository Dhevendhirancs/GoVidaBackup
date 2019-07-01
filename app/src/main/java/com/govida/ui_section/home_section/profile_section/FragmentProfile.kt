package com.govida.ui_section.home_section.profile_section


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.about_section.ActivityAbout
import com.govida.ui_section.base_class_section.BaseFragment
import com.govida.ui_section.help_and_support_section.ActivityHelpSupport
import com.govida.ui_section.leaderboard_section.ActivityLeaderboard
import com.govida.ui_section.notification_section.ActivityNotification
import com.govida.ui_section.setting_section.ActivitySettings

class FragmentProfile : BaseFragment(), View.OnClickListener {

    var mRootView:View?=null
    lateinit var llSettings:LinearLayout
    lateinit var llNotification:LinearLayout
    lateinit var btnProfileView: Button
    lateinit var mTvUsername:TextView
    lateinit var llLeaderbord:LinearLayout
    lateinit var mIvProfilePicture: ImageView
    lateinit var appPreference: AppPreference
    lateinit var mLlHelpAndSupport: LinearLayout
    lateinit var mLlAbout: LinearLayout
    lateinit var mPbProfileProgress: ProgressBar
    lateinit var mTvBonusMessage: TextView
    lateinit var mAppPreference: AppPreference
    lateinit var mTvProfileStatus: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView= inflater.inflate(R.layout.fragment_profile, container, false)
        setupUI(mRootView)
        return mRootView
    }

    override fun onResume() {
        super.onResume()
        setProfilePicture()
        checkProfileCompletionStatus()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to set user profile picture from URL
     *  @Author   : 1769
     */
    private fun setProfilePicture() {
        appPreference = AppPreference(context!!)
        if (appPreference.profilePicturePath.isNotEmpty()) {
            Glide.with(this).load(appPreference.profilePicturePath).into(mIvProfilePicture)
        } else {
            mIvProfilePicture.setImageResource(R.drawable.profile_pic_3)
        }
        mTvUsername.text=appPreference.userFirstName
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI(rootView: View?) {
        if(rootView!=null){
            mTvUsername=rootView.findViewById(R.id.profile_tv_username)
            llSettings=rootView.findViewById(R.id.ll_settings)
            llNotification=rootView.findViewById(R.id.ll_notification)
            llLeaderbord=rootView.findViewById(R.id.ll_leaderboard)
            llLeaderbord.setOnClickListener(this)
            llSettings.setOnClickListener(this)
            llNotification.setOnClickListener(this)
            btnProfileView = rootView.findViewById(R.id.profile_btn_view)
            btnProfileView.setOnClickListener(this)
            mIvProfilePicture = rootView.findViewById(R.id.iv_photo)
            mLlHelpAndSupport = rootView.findViewById(R.id.ll_help_and_support)
            mLlHelpAndSupport.setOnClickListener(this)
            mLlAbout = rootView.findViewById(R.id.ll_about)
            mLlAbout.setOnClickListener(this)
            mPbProfileProgress = rootView.findViewById(R.id.profile_progress)
            mTvBonusMessage = rootView.findViewById(R.id.tv_bonus_message)
            mTvProfileStatus = rootView.findViewById(R.id.profile_status)
        }
    }

    private fun checkProfileCompletionStatus() {
        mAppPreference = AppPreference(context!!)
        mPbProfileProgress.progress = mAppPreference.profileCompletionStatus
        mTvProfileStatus.text = mAppPreference.profileCompletionStatus.toString() + "%"
        if (mAppPreference.profileCompletionStatus == 100) {
            mTvBonusMessage.visibility = View.GONE
        } else {
            mTvBonusMessage.visibility = View.VISIBLE
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
                R.id.ll_settings -> {
                    val mainActIntent = Intent(context, ActivitySettings::class.java)
                    startActivity(mainActIntent)
                } R.id.profile_btn_view -> {
                    val mainActIntent = Intent(context, ActivityProfileView::class.java)
                    startActivity(mainActIntent)
                } R.id.ll_notification -> {
                    val mainActIntent = Intent(context, ActivityNotification::class.java)
                    startActivity(mainActIntent)
                } R.id.ll_leaderboard -> {
                    val mainActIntent = Intent(context, ActivityLeaderboard::class.java)
                    startActivity(mainActIntent)
                } R.id.ll_help_and_support -> {
                    val mainActIntent = Intent(context, ActivityHelpSupport::class.java)
                    startActivity(mainActIntent)
                } R.id.ll_about -> {
                    val mainActIntent = Intent(context, ActivityAbout::class.java)
                    startActivity(mainActIntent)
                }
            }
        }
    }
}

