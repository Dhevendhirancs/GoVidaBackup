/**
 * @Class : RewardPresenterImplementer
 * @Usage : This class is used for providing MVP functionality to Reward Page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section.mvp

import android.os.AsyncTask
import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.rewards_section.models.AllRewardEntity
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsEntity

class RewardPresenterImplementer (mView: RewardMVP.RewardView): RewardMVP.RewardPresenter, RewardMVP.RewardModel.onFinishedAllRewardListener,
    RewardMVP.RewardModel.onFinishedMyRewardListener, RewardMVP.RewardModel.onFinishedRedeemRewardListener {

    private var mRewardView: RewardMVP.RewardView? = mView
    private var mRewardModel = RewardModelImplementer()
    private var mApiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
    private var mAppDbInstance: AppDatabase? = null
    private var mOnDownloadListener: InsertAllRewards.OnDownloadListener? = null
    private var mMyDownloadListener: InsertMyRewards.OnDownloadListener? = null

    /**
     *  @Function : attachView()
     *  @params   : rewardView: RewardMVP.RewardView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1769
     */
    override fun attachView(rewardView: RewardMVP.RewardView) {
        mRewardView = rewardView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1769
     */
    override fun destroyView() {
        mRewardView = null
    }

    /**
     *  @Function : onAllRewardsListRequested()
     *  @params   : accessToken: String, appDatabase:AppDatabase, onDownloadListener: InsertAllRewards.OnDownloadListener, isButtonPressed: Boolean
     *  @Return   : void
     * 	@Usage	  : precess when user requested all rewards list
     *  @Author   : 1769
     */
    override fun onAllRewardsListRequested(accessToken: String, appDatabase:AppDatabase, onDownloadListener: InsertAllRewards.OnDownloadListener, isButtonPressed: Boolean) {
        if (mRewardView != null) {

            if(mRewardView!!.isNetworkConnected){
                if (isButtonPressed) {
                    mRewardView!!.showLoading()
                }
                mAppDbInstance=appDatabase
                mOnDownloadListener=onDownloadListener
                mRewardModel.allRewardsList(mApiInterface, this, accessToken)
            }
            else{
                mRewardView!!.hideLoading()
                mRewardView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onAllRewardFinished()
     *  @params   : allRewards: MutableList<AllRewardEntity>?, myGoVps: Int?
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for all rewards list received successfully
     *  @Author   : 1769
     */
    override fun onAllRewardFinished(allRewards: MutableList<AllRewardEntity>?, myGoVps: Int?) {
        if (mRewardView != null) {
            InsertAllRewards (allRewards!!, mAppDbInstance!!, mOnDownloadListener!!).execute()
            mRewardView!!.hideLoading()
            mRewardView!!.onAllRewardsReceivedSuccessfully(allRewards, myGoVps!!)
        }
    }

    /**
     *  @Function : onAllRewardFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for getting all rewards list operation failed
     *  @Author   : 1769
     */
    override fun onAllRewardFailure(warnings: String) {
        if (mRewardView != null) {
            mRewardView!!.hideLoading()
        }
    }

    class InsertAllRewards(var rewardsList: MutableList<AllRewardEntity>, var appDB: AppDatabase, var onDownloadListener: OnDownloadListener) : AsyncTask<Void, Void, Boolean>() {
        interface OnDownloadListener {
            fun onMyRewardsDownloadFinished()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            appDB.rewardDao().insertAllRewardsList(rewardsList)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            onDownloadListener.onMyRewardsDownloadFinished()
        }
    }

    /**
     *  @Function : onMyRewardsListRequested()
     *  @params   : accessToken: String, appDatabase: AppDatabase, onDownloadListener: InsertMyRewards.OnDownloadListener, isButtonPressed: Boolean
     *  @Return   : void
     * 	@Usage	  : precess when user requested my reward list
     *  @Author   : 1769
     */
    override fun onMyRewardsListRequested( accessToken: String, appDatabase: AppDatabase, onDownloadListener: InsertMyRewards.OnDownloadListener, isButtonPressed: Boolean ) {
        if (mRewardView != null) {

            if(mRewardView!!.isNetworkConnected){
                if (isButtonPressed) {
                    mRewardView!!.showLoading()
                }
                mAppDbInstance=appDatabase
                mMyDownloadListener=onDownloadListener
                mRewardModel.myRewardsList(mApiInterface, this, accessToken)
            }
            else{
                mRewardView!!.hideLoading()
                mRewardView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onMyRewardFinished()
     *  @params   : myRewards: MutableList<MyRewardsEntity>?, myGoVps: Int
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for my rewards list received successfully
     *  @Author   : 1769
     */
    override fun onMyRewardFinished(myRewards: MutableList<MyRewardsEntity>?, myGoVps: Int) {
        if (mRewardView != null) {
            InsertMyRewards (myRewards!!, mAppDbInstance!!, mMyDownloadListener!!).execute()
            mRewardView!!.hideLoading()
            mRewardView!!.onMyRewardsReceivedSuccessfully(myRewards, myGoVps)
        }
    }

    /**
     *  @Function : onMyRewardFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for getting my rewards list operation failed
     *  @Author   : 1769
     */
    override fun onMyRewardFailure(warnings: String) {
        if (mRewardView != null) {
            mRewardView!!.hideLoading()

        }
    }

    class InsertMyRewards(var rewardsList: MutableList<MyRewardsEntity>, var appDB: AppDatabase, var onDownloadListener: OnDownloadListener) : AsyncTask<Void, Void, Boolean>() {
        interface OnDownloadListener {
            fun onDownloadFinished()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            appDB.myRewardsDao().insertMyRewardsList(rewardsList)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            onDownloadListener.onDownloadFinished()
        }
    }

    /**
     *  @Function : onRedeemRewardRequested()
     *  @params   : accessToken: String, rewardId: Int
     *  @Return   : void
     * 	@Usage	  : precess when user requested redeem reward
     *  @Author   : 1769
     */
    override fun onRedeemRewardRequested(accessToken: String, rewardId: Int) {
        if (mRewardView != null) {
            mRewardView!!.showLoading()
            mRewardModel.redeemReward(mApiInterface, this, accessToken, rewardId)
        }
    }

    /**
     *  @Function : onRedeemRewardFinished()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for redeem reward successfully
     *  @Author   : 1769
     */
    override fun onRedeemRewardFinished() {
        if (mRewardView != null) {
            mRewardView!!.hideLoading()

        }
    }

    /**
     *  @Function : onRedeemRewardFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for redeem reward operation failed
     *  @Author   : 1769
     */
    override fun onRedeemRewardFailed() {
        if (mRewardView != null) {
            mRewardView!!.hideLoading()

        }
    }

//    override fun onAllRewardsListRequestedPullToRefresh(
//        accessToken: String,
//        appDatabase: AppDatabase,
//        isAllChallengeButtonPressed: Boolean
//    ) {
//        if (isAllChallengeButtonPressed) {
//
//        }
//    }
}