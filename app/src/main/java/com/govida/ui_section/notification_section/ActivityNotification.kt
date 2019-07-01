package com.govida.ui_section.notification_section

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.govida.R
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.notification_section.adapters.NotificationListAdapter
import com.govida.ui_section.notification_section.database.NotificationEntity
import com.govida.ui_section.notification_section.item_decorator_for_list.SimpleDividerItemDecoration
import com.govida.ui_section.setting_section.ActivitySettings


class ActivityNotification : BaseActivity(), View.OnClickListener {

    private var mAppDb: AppDatabase? = null
    var mNotificationList: MutableList<NotificationEntity> = mutableListOf()
    lateinit var mNotificationAdapter:NotificationListAdapter
    lateinit var mRvNotificationList: androidx.recyclerview.widget.RecyclerView
    lateinit var mIvSettings: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setupUI()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */
    private fun setupUI() {
        mAppDb= AppDatabase.getDatabase(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar_settings)
        val toolbarTitle: TextView =toolbar.findViewById(R.id.title)
        toolbarTitle.text=getString(R.string.notifications)
        mRvNotificationList=findViewById(R.id.notification_rv)

        mNotificationAdapter=NotificationListAdapter(mNotificationList, this,mAppDb)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mRvNotificationList.layoutManager = mLayoutManager
        mRvNotificationList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        mRvNotificationList.adapter = mNotificationAdapter
        mRvNotificationList.addItemDecoration(SimpleDividerItemDecoration(this))
        mIvSettings = findViewById(R.id.iv_settings)
        mIvSettings.visibility = View.VISIBLE
        mIvSettings.setOnClickListener(this)
        AllNotifications(this).execute()
    }

    private class AllNotifications(var context: ActivityNotification) : AsyncTask<Void, Void, MutableList<NotificationEntity>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            context.showLoading()
        }
        override fun doInBackground(vararg params: Void?): MutableList<NotificationEntity> {
            var tempList: MutableList<NotificationEntity> = mutableListOf()
            if(context.mAppDb?.notificationDao()?.allNotificationCount!! >0)
            {
                tempList=context.mAppDb?.notificationDao()?.allNotifications!!
                return tempList
            }
            return tempList
        }
        override fun onPostExecute(tempList:  MutableList<NotificationEntity> ?) {
            context.hideLoading()
            context.mNotificationList.clear()
            context.mNotificationList.addAll(tempList!!)
            context.mNotificationAdapter.notifyDataSetChanged()
        }
    }

    /**
     *  @Function : onClick()
     *  @params   : View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_settings -> {
                val mainActIntent = Intent(this, ActivitySettings::class.java)
                startActivity(mainActIntent)
            }
        }
    }
    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String?) {
    }

}
