/**
 * @Class : ActivityWelcome
 * @Usage : This activity is used for providing tutorial screen to user
 * @Author : 1276
 */


package com.govida.ui_section.welcome_slider_section

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.notification_section.database.NotificationEntity
import com.govida.ui_section.terms_and_condition_section.ActivityTermsAndConditions
import com.govida.utility_section.AppConstants
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class ActivityWelcome : AppCompatActivity(),View.OnClickListener {
    private var mAppDb: AppDatabase? = null
    private var mDemoNotificationList: MutableList<NotificationEntity> = mutableListOf()
    private lateinit var mViewPager: androidx.viewpager.widget.ViewPager
    private lateinit var mSliderAdapter:SliderAdapter
    private lateinit var mLayouts: IntArray
    private lateinit var mBtnSkip:Button
    private lateinit var mBtnNext:Button
    private lateinit var mBtnGetStarted:Button
    private lateinit var mViewPagerPageChangeListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener
    private lateinit var mDotsIndicator:DotsIndicator
    private lateinit var mLlGetStarted:LinearLayout
    private lateinit var mLlSkipNext:LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContentView(R.layout.activity_welcome)
        changeStatusBarColor()
        setupUI()
        dataSetup()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */
    private fun setupUI(){
        mViewPager=findViewById(R.id.view_pager)
        mBtnNext=findViewById(R.id.btn_next)
        mBtnSkip=findViewById(R.id.btn_skip)
        mDotsIndicator = findViewById(R.id.layoutDots)
        mBtnGetStarted = findViewById(R.id.btn_get_started)
        mLlGetStarted = findViewById(R.id.ll_get_started)
        mLlSkipNext = findViewById(R.id.ll_skip_next)
        mLlGetStarted.visibility = View.GONE
        mLayouts = intArrayOf(
            R.layout.welcome_slide1,
            R.layout.welcome_slide2,
            R.layout.welcome_slide3
        )

        mBtnNext.setOnClickListener(this)
        mBtnSkip.setOnClickListener(this)
        mBtnGetStarted.setOnClickListener(this)
        mViewPagerPageChangeListener= object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == mLayouts.size - 1) {
                    // last page. make button text to GOT IT
                    mDotsIndicator.visibility=View.VISIBLE
                    mLlGetStarted.visibility=View.VISIBLE
                    mLlSkipNext.visibility=View.GONE
                } else {
                    // still pages are left
                    mDotsIndicator.visibility=View.VISIBLE
                    mLlGetStarted.visibility=View.GONE
                    mLlSkipNext.visibility=View.VISIBLE
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }

        mSliderAdapter=SliderAdapter()
        mViewPager.adapter=mSliderAdapter
        mViewPager.addOnPageChangeListener(mViewPagerPageChangeListener)
        mDotsIndicator.setViewPager(mViewPager)
    }


    /**
     *  @Function : changeStatusBarColor()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Make Status bar transparent
     *  @Author   : 1276
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     *  @Function : onClick()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : listener definition
     *  @Author   : 1276
     */
    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_next ->{
                    val currantPage = getItem(+1)
                    if(currantPage<mLayouts.size){
                        mViewPager.currentItem=currantPage
                    }else{
                        moveToNextSection()
                    }
                }
                R.id.btn_skip->{
                    moveToNextSection()
                }
                R.id.btn_get_started -> {
                    moveToNextSection()
                }
            }
        }
    }

    /**
     *  @Function : getItem()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : get pager page number
     *  @Author   : 1276
     */
    private fun getItem(itemNumber:Int):Int{
        return mViewPager.currentItem+1
    }

    /**
     *  @Function : moveToNextSection()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : open next screen as the functionality of this screen complete
     *  @Author   : 1276
     */
    private fun moveToNextSection(){
        val appPref = AppPreference(this)
        appPref.welcomeSetupDone(true)

        val mainActIntent = Intent(this, ActivityTermsAndConditions::class.java)
        mainActIntent.putExtra(AppConstants.FROM_SCREEN,"welcome_page")
        startActivity(mainActIntent)
        finish()
    }

    /**
     *  @class : SliderAdapter
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Adapter definition
     *  @Author   : 1276
     */
    inner class SliderAdapter : androidx.viewpager.widget.PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(mLayouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return mLayouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }


    private fun dataSetup(){
        sampleNotificationList()
        mAppDb= AppDatabase.getDatabase(this)
        InsertTask(this,mDemoNotificationList).execute()
    }

    private fun sampleNotificationList() {
        mDemoNotificationList.clear()

        val eg1= NotificationEntity()
        eg1.notificationTitle="Time Up!"
        eg1.notificationInfo="Your \"GoVida 24 Hour Step Challenge\" has finished."
        eg1.notificationReceivedAt="2 days ago"
        eg1.notificationRead=false
        mDemoNotificationList.add(eg1)

        val eg2= NotificationEntity()
        eg2.notificationTitle="Complete Your Profile"
        eg2.notificationInfo="Complete your Profile and earn a Bonus 10 GoVPs"
        eg2.notificationReceivedAt="1 days ago"
        eg2.notificationRead=false
        mDemoNotificationList.add(eg2)

        val eg3= NotificationEntity()
        eg3.notificationTitle="Lets Go!"
        eg3.notificationInfo="Your \"GoVida 24 Hour Step Challenge\" has started."
        eg3.notificationReceivedAt="3 days ago"
        eg3.notificationRead=true
        mDemoNotificationList.add(eg3)

        val eg4= NotificationEntity()
        eg4.notificationTitle="New Challenge added"
        eg4.notificationInfo="Two line description of this notification came before 5 days ago"
        eg4.notificationReceivedAt="2 days ago"
        eg4.notificationRead=true
        mDemoNotificationList.add(eg4)
    }

    private class InsertTask(var context: ActivityWelcome, var notificationList: MutableList<NotificationEntity>) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            if(context.mAppDb?.notificationDao()?.allNotificationCount==0)
            {
                context.mAppDb?.notificationDao()?.insertMultipleListRecord(notificationList)

            }

            return true
        }

    }




}
