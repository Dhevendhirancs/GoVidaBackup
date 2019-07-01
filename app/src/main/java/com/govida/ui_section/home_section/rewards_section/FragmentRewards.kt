/**
 * @Class : FragmentRewards
 * @Usage : This activity is used to manage the Reward list page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseFragment
import com.govida.ui_section.home_section.ActivityHome
import com.govida.ui_section.home_section.challenges_section.rv_touch_listeners.RecyclerviewTouchListener
import com.govida.ui_section.home_section.rewards_section.adapters.RewardListAdapter
import com.govida.ui_section.home_section.rewards_section.models.AllRewardEntity
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsEntity
import com.govida.ui_section.home_section.rewards_section.mvp.RewardMVP
import com.govida.ui_section.home_section.rewards_section.mvp.RewardPresenterImplementer

class FragmentRewards :  BaseFragment(),View.OnClickListener, RewardMVP.RewardView, RewardPresenterImplementer.InsertAllRewards.OnDownloadListener,
    RewardPresenterImplementer.InsertMyRewards.OnDownloadListener, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener{

    var mAppDb: AppDatabase? = null
    var mRootView:View?=null
    lateinit var mBtnMine: Button
    lateinit var mBtnAllRewards: Button
    lateinit var mTvEmptyMsg: TextView
    lateinit var rvRewardList: androidx.recyclerview.widget.RecyclerView
    var rewardList: MutableList<AllRewardEntity> = mutableListOf<AllRewardEntity>()
    var myRewardsList: MutableList<MyRewardsEntity> = mutableListOf<MyRewardsEntity>()
    lateinit var mRewardAdapter:RewardListAdapter
    lateinit var mMyRewardsAdapter: RewardListAdapter
    lateinit var mRewardPresenter: RewardMVP.RewardPresenter
    private lateinit var mAppPreference: AppPreference
    lateinit var mRewardSwipe:androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    var isAllRewardButtonPressed=true
    lateinit var activityToolbar:Toolbar
    lateinit var goVps:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView= inflater.inflate(R.layout.fragment_rewards, container, false)
        setupUI(mRootView)
        return mRootView
    }

    override fun onResume() {
        super.onResume()
        mRewardPresenter.attachView(this)
        if(isAllRewardButtonPressed){
            mBtnAllRewards.performClick()
        }
        else{
            mBtnMine.performClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRewardPresenter.destroyView()
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
            activityToolbar = activity!!.findViewById(R.id.toolbar)
            goVps = activity!!.findViewById(R.id.rewards_info)

            mAppDb= AppDatabase.getDatabase(activity!!.applicationContext)
            mBtnMine=rootView.findViewById(R.id.rewards_btn_mine)
            mBtnAllRewards=rootView.findViewById(R.id.rewards_btn_availble)
            rvRewardList=rootView.findViewById(R.id.rewards_rv)
            mTvEmptyMsg=rootView.findViewById(R.id.tv_empty_msg)
            mRewardPresenter = RewardPresenterImplementer(this)
            mBtnMine.setOnClickListener(this)
            mBtnAllRewards.setOnClickListener(this)

            mRewardAdapter=RewardListAdapter(rewardList, null, activity!!, mAppDb)
            mMyRewardsAdapter= RewardListAdapter(null, myRewardsList, activity!!, mAppDb)

            mRewardSwipe = rootView.findViewById(R.id.rewards_swipe)
            mRewardSwipe.setOnRefreshListener(this)

            val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            rvRewardList.layoutManager = mLayoutManager
            rvRewardList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            rvRewardList.adapter = mRewardAdapter
            rvRewardList.addOnItemTouchListener(
                RecyclerviewTouchListener(context, rvRewardList,
                    object : RecyclerviewTouchListener.ClickListener {
                        override fun onLongClick(view: View?, position: Int) {
                        }
                        override fun onClick(view: View, position: Int) {
                        }
                    })
            )

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
                R.id.rewards_btn_mine -> {
                    isAllRewardButtonPressed=false
                    rvRewardList.adapter = mMyRewardsAdapter
                    mRewardPresenter.onMyRewardsListRequested(mAppPreference.authorizationToken, mAppDb!!, this, true)
                    mBtnMine.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnMine.setBackgroundResource(R.drawable.home_page_left_button_color_change)
                    mBtnAllRewards.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnAllRewards.setBackgroundResource(R.drawable.home_page_right_button)
                    mMyRewardsAdapter.notifyDataSetChanged()
                }
                R.id.rewards_btn_availble -> {
                    isAllRewardButtonPressed=true
                    rvRewardList.adapter = mRewardAdapter
                    mAppPreference = AppPreference(context!!)



                    mRewardPresenter.onAllRewardsListRequested(mAppPreference.authorizationToken, mAppDb!!, this, true)
                    mBtnAllRewards.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnAllRewards.setBackgroundResource(R.drawable.home_page_right_button_color_change)
                    mBtnMine.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnMine.setBackgroundResource(R.drawable.home_page_left_button)
                    mRewardAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     *  @Function : onAllRewardsReceivedSuccessfully()
     *  @params   : appRewards: MutableList<AllRewardsEntity>
     *  @Return   : void
     * 	@Usage	  : Execute after the successful All Rewards API call
     *  @Author   : 1769
     */
    override fun onAllRewardsReceivedSuccessfully(appRewards: MutableList<AllRewardEntity>, myGoVps: Int) {
        rewardList.clear()
        hideLoading()
        mRewardSwipe.isRefreshing = false
        mAppPreference.myTotalGoVps=myGoVps
        if(goVps.visibility==View.VISIBLE){
            goVps.text="My GoVPs:"+myGoVps
        }
        if (appRewards!!.size != 0) {
            mTvEmptyMsg.visibility = View.GONE
            rvRewardList.visibility = View.VISIBLE
            rewardList.addAll(appRewards)
        } else {
            mTvEmptyMsg.visibility = View.VISIBLE
            rvRewardList.visibility = View.GONE
            mTvEmptyMsg.text = getString(R.string.empty_msg_all_rewards)
        }
        //AllRewards(this).execute()
        mRewardAdapter.notifyDataSetChanged()
    }

    /**
     *  @Function : onAllRewardsReceivedFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : Execute if All Rewards API call fails
     *  @Author   : 1769
     */
    override fun onAllRewardsReceivedFailed(warnings: String) {
        mRewardSwipe.isRefreshing = false
        hideLoading()
    }

    override fun onDownloadFinished() {
        mRewardAdapter.notifyDataSetChanged()
    }

    override fun onMyRewardsDownloadFinished() {
        mRewardSwipe.isRefreshing = false
        mMyRewardsAdapter.notifyDataSetChanged()
    }

    /**
     *  @Function : onMyRewardsReceivedSuccessfully()
     *  @params   : myRewards: MutableList<MyRewardsEntity>
     *  @Return   : void
     * 	@Usage	  : Execute after the successful My Rewards API call
     *  @Author   : 1769
     */
    override fun onMyRewardsReceivedSuccessfully(myRewards: MutableList<MyRewardsEntity>, myGoVps: Int) {
        mRewardSwipe.isRefreshing = false
        myRewardsList.clear()
        mAppPreference.myTotalGoVps=myGoVps
        if(goVps.visibility==View.VISIBLE){
            goVps.text="My GoVPs:"+myGoVps
        }
        if (myRewards!!.size != 0) {
            mTvEmptyMsg.visibility = View.GONE
            rvRewardList.visibility = View.VISIBLE
            myRewardsList.addAll(myRewards)
        } else {
            mTvEmptyMsg.visibility = View.VISIBLE
            rvRewardList.visibility = View.GONE
            mTvEmptyMsg.text = getString(R.string.empty_msg_my_rewards)
        }
        mMyRewardsAdapter.notifyDataSetChanged()
        //MyRewards(this).execute()
        hideLoading()
    }

    /**
     *  @Function : onAllRewardsReceivedFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : Execute if My Rewards API call fails
     *  @Author   : 1769
     */
    override fun onMyRewardsReceivedFailed(warnings: String) {
        mRewardSwipe.isRefreshing = false
        hideLoading()
    }

    /**
     *  @Function : onRedeemRewardSuccessfully()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : Execute if Redeem Reward API call success
     *  @Author   : 1769
     */
    override fun onRedeemRewardSuccessfully() {
        hideLoading()
    }

    /**
     *  @Function : onRedeemRewardFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : Execute if Redeem Reward API call fails
     *  @Author   : 1769
     */
    override fun onRedeemRewardFailed(warnings: String) {
        hideLoading()
    }

    /**
     *  @Function : onRefresh()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : For swipe to refresh functionality
     *  @Author   : 1769
     */
    override fun onRefresh() {
        mRewardSwipe.isRefreshing = true
        if (isAllRewardButtonPressed) {
            mRewardPresenter.onAllRewardsListRequested(mAppPreference.authorizationToken, mAppDb!!, this, false)
        } else {
            mRewardPresenter.onMyRewardsListRequested(mAppPreference.authorizationToken, mAppDb!!, this, false)
        }
//        mRewardPresenter.onAllRewardsListRequestedPullToRefresh(mAppPreference.authorizationToken, mAppDb!!, isAllRewardButtonPressed)
    }
/*  //database operations for rewards
    private class AllRewards(var context: FragmentRewards) : AsyncTask<Void, Void, MutableList<AllRewardEntity>>() {
        override fun doInBackground(vararg params: Void?): MutableList<AllRewardEntity> {
            var tempList: MutableList<AllRewardEntity> = mutableListOf<AllRewardEntity>()
            if(context.mAppDb?.rewardDao()?.allRewardsCount!! >0)
            {
                tempList=context.mAppDb?.rewardDao()?.getAllRewardsList!!
                return tempList
            }
            return tempList
        }
        override fun onPostExecute(tempList:  MutableList<AllRewardEntity> ?) {
            context.rewardList.clear()
            if (tempList!!.size != 0) {
                context.mTvEmptyMsg.visibility = View.GONE
                context.rvRewardList.visibility = View.VISIBLE
                context.rewardList.addAll(tempList!!)
            } else {
                context.mTvEmptyMsg.visibility = View.VISIBLE
                context.rvRewardList.visibility = View.GONE
                context.mTvEmptyMsg.text = context.getString(R.string.empty_msg_all_rewards)
            }
            context.mRewardAdapter.notifyDataSetChanged()
        }
    }

    /**
     *  @class : MyRewards()
     * 	@Usage : Get my rewards list from database and send it to adapter
     *  @Author : 1769
     */
    private class MyRewards(var context: FragmentRewards) : AsyncTask<Void, Void, MutableList<MyRewardsEntity>>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: Void?): MutableList<MyRewardsEntity> {
            var tempList: MutableList<MyRewardsEntity> = mutableListOf<MyRewardsEntity>()
            if(context.mAppDb?.myRewardsDao()?.myRewardsCount!! >0)
            {
                tempList=context.mAppDb?.myRewardsDao()?.getMyRewardsList!!
                return tempList
            }
            return tempList
        }
        override fun onPostExecute(tempList:  MutableList<MyRewardsEntity> ?) {
            context.myRewardsList.clear()
            if (tempList!!.size != 0) {
                context.mTvEmptyMsg.visibility = View.GONE
                context.rvRewardList.visibility = View.VISIBLE
                context.myRewardsList.addAll(tempList!!)
            } else {
                context.mTvEmptyMsg.visibility = View.VISIBLE
                context.rvRewardList.visibility = View.GONE
                context.mTvEmptyMsg.text = context.getString(R.string.empty_msg_my_rewards)
            }
            context.mMyRewardsAdapter.notifyDataSetChanged()
        }
    }
    */
}
