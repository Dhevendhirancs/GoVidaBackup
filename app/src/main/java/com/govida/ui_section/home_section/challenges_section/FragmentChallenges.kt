package com.govida.ui_section.home_section.challenges_section


import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseFragment
import com.govida.ui_section.home_section.challenges_section.adapters.ChallengeListAdapter
import com.govida.ui_section.home_section.challenges_section.mvp.ChallengeMVP
import com.govida.ui_section.home_section.challenges_section.mvp.ChallengePresenterImplementer
import com.govida.ui_section.home_section.challenges_section.rv_touch_listeners.RecyclerviewTouchListener
import com.govida.ui_section.home_section.model.ChallengesEntity


class FragmentChallenges : BaseFragment(),View.OnClickListener,androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener,
    ChallengeMVP.ChallengeView {
    var mAppDb: AppDatabase? = null
    var mRootView:View?=null
    lateinit var mBtnMine:Button
    lateinit var mBtnAllChallenges:Button
    lateinit var mTvEmptyMessage: TextView
    var challengeList: MutableList<ChallengesEntity> = mutableListOf<ChallengesEntity>()
    lateinit var rvChallengeList: androidx.recyclerview.widget.RecyclerView
    lateinit var mChallengeAdapter:ChallengeListAdapter
    var isAllChallengeButtonPressed=true
    lateinit var mChlgPresenter:ChallengeMVP.ChallengePresenter
    lateinit var mchallengeSwipe:androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    var mAppPreference:AppPreference?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView= inflater.inflate(R.layout.fragment_challenges, container, false)
        setupUI(mRootView)
        return mRootView
    }

    override fun onDestroy() {
        mChlgPresenter.destroyView()
        super.onDestroy()
    }
    private fun setupUI(rootView: View?) {
        if(rootView!=null){
            mAppDb= AppDatabase.getDatabase(activity!!.applicationContext)
            mBtnMine=rootView.findViewById(R.id.challenges_btn_mine)
            mBtnAllChallenges=rootView.findViewById(R.id.challenges_btn_availble)
            rvChallengeList=rootView.findViewById(R.id.challenges_rv)
            mTvEmptyMessage=rootView.findViewById(R.id.challenges_tv_no_content)
            mchallengeSwipe=rootView.findViewById(R.id.challenge_swipe)
            mAppPreference=AppPreference(this.activity!!)
            mChlgPresenter= ChallengePresenterImplementer(this)
            mBtnMine.setOnClickListener(this)
            mBtnAllChallenges.setOnClickListener(this)
            mchallengeSwipe.setOnRefreshListener(this)
            //SampleData()
            mChallengeAdapter=ChallengeListAdapter(challengeList, activity!!,mAppDb)


            val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            rvChallengeList.layoutManager = mLayoutManager
            rvChallengeList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            rvChallengeList.adapter = mChallengeAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        mChlgPresenter.attachView(this)
        if(isAllChallengeButtonPressed){
            mBtnAllChallenges.performClick()
        }
        else{
            mBtnMine.performClick()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.challenges_btn_mine -> {
                    isAllChallengeButtonPressed=false
                    mChlgPresenter.onChallengeListRequested(mAppPreference!!.authorizationToken,mAppDb!!,isAllChallengeButtonPressed)
                    mBtnMine.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnMine.setBackgroundResource(R.drawable.home_page_left_button_color_change)
                    mBtnAllChallenges.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnAllChallenges.setBackgroundResource(R.drawable.home_page_right_button)
                    mChallengeAdapter.notifyDataSetChanged()
                }
                R.id.challenges_btn_availble -> {
                    isAllChallengeButtonPressed=true
                    mChlgPresenter.onChallengeListRequested(mAppPreference!!.authorizationToken,mAppDb!!,isAllChallengeButtonPressed)
                    mBtnAllChallenges.setTextColor(resources.getColor(R.color.colorWhite))
                    mBtnAllChallenges.setBackgroundResource(R.drawable.home_page_right_button_color_change)
                    mBtnMine.setTextColor(resources.getColor(R.color.default_text_color))
                    mBtnMine.setBackgroundResource(R.drawable.home_page_left_button)
                    mChallengeAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onRefresh() {
        mchallengeSwipe.isRefreshing = true
        /*if(isAllChallengeButtonPressed){
            mBtnAllChallenges.performClick()
        }
        else{
            mBtnMine.performClick()
        }*/
        mChlgPresenter.onChallengeListRequestedPullToRefresh(mAppPreference?.authorizationToken!!,mAppDb!!,isAllChallengeButtonPressed)

    }

    override fun onChallengeReceivedSuccessfully(receivedChallengesEntities: MutableList<ChallengesEntity>) {
        challengeList.clear()
        if (receivedChallengesEntities!!.size != 0) {
            mTvEmptyMessage.visibility = View.GONE
            rvChallengeList.visibility = View.VISIBLE
            challengeList.addAll(receivedChallengesEntities!!)
        } else {
            mTvEmptyMessage.visibility = View.VISIBLE
            rvChallengeList.visibility = View.GONE
            if (isAllChallengeButtonPressed) {
                mTvEmptyMessage.text = getString(R.string.empty_msg_all_challenges)
            } else {
                mTvEmptyMessage.text = getString(R.string.empty_msg_ongoing_challenge)
            }
        }
        mChallengeAdapter.notifyDataSetChanged()
        mchallengeSwipe.isRefreshing = false
    }

    override fun onChallengeReceivedFailed() {
        challengeList.clear()
        mTvEmptyMessage.visibility = View.VISIBLE
        rvChallengeList.visibility = View.GONE
        if (isAllChallengeButtonPressed) {
            mTvEmptyMessage.text = getString(R.string.empty_msg_all_challenges)
        } else {
            mTvEmptyMessage.text = getString(R.string.empty_msg_ongoing_challenge)
        }
        mChallengeAdapter.notifyDataSetChanged()
        mchallengeSwipe.isRefreshing = false
    }

    /*
    private class AllChallenges(var context: FragmentChallenges) : AsyncTask<Void, Void, MutableList<ChallengesEntity>>() {

        override fun doInBackground(vararg params: Void?): MutableList<ChallengesEntity> {
            var tempList: MutableList<ChallengesEntity> = mutableListOf()
            if(context.mAppDb?.challengeDao()?.allChallengeCount!! >0)
            {
                tempList=context.mAppDb?.challengeDao()?.onNewChallenges!!
                return tempList
            }
            return tempList
        }
        override fun onPostExecute(tempList:  MutableList<ChallengesEntity> ?) {
            context.challengeList.clear()
            if (tempList!!.size != 0) {
                context.mTvEmptyMessage.visibility = View.GONE
                context.rvChallengeList.visibility = View.VISIBLE
                context.challengeList.addAll(tempList!!)
            } else {
                context.mTvEmptyMessage.visibility = View.VISIBLE
                context.rvChallengeList.visibility = View.GONE
                context.mTvEmptyMessage.text = context.getString(R.string.empty_msg_all_challenges)
            }
            context.mChallengeAdapter.notifyDataSetChanged()
            context.mchallengeSwipe.isRefreshing = false
        }
    }
    */

}
