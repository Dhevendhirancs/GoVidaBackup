/**
 * @Class : RewardMVP
 * @Usage : This class is used for providing MVP functionality to Reward Page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.rewards_section.models.AllRewardEntity
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsEntity

class RewardMVP {
    /**
     * @Interface : RewardView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1769
     */
    interface RewardView: MvpView {
        // usage : function will be used when all rewards received successfully
        fun onAllRewardsReceivedSuccessfully (allRewards: MutableList<AllRewardEntity>, myGoVps: Int)
        // usage : function will be used when all rewards received fails
        fun onAllRewardsReceivedFailed (warnings: String)
        // usage : function will be used when all rewards received successfully
        fun onMyRewardsReceivedSuccessfully (myRewards: MutableList<MyRewardsEntity>, myGoVps: Int)
        // usage : function will be used when my rewards received fails
        fun onMyRewardsReceivedFailed (warnings: String)
        // usage : function will be used when redeem reward successful
        fun onRedeemRewardSuccessfully ()
        // usage : function will be used when redeem reward fails
        fun onRedeemRewardFailed (warnings: String)
    }

    /**
     * @Interface : RewardPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1769
     */
    interface RewardPresenter {
        fun attachView(rewardView: RewardMVP.RewardView)
        fun destroyView()
        // usage : initiate all rewards list request to the server
        fun onAllRewardsListRequested(accessToken: String, appDatabase: AppDatabase, onDownloadListener: RewardPresenterImplementer.InsertAllRewards.OnDownloadListener, isButtonPressed:Boolean)
        // usage : initiate my rewards list request to the server
        fun onMyRewardsListRequested (accessToken: String, appDatabase: AppDatabase, onDownloadListener: RewardPresenterImplementer.InsertMyRewards.OnDownloadListener, isButtonPressed:Boolean)
        // usage : initiate redeem reward request to the server
        fun onRedeemRewardRequested (accessToken: String, rewardId: Int)
//        fun onAllRewardsListRequestedPullToRefresh(accessToken: String, appDatabase: AppDatabase, isAllChallengeButtonPressed:Boolean)
    }

    /**
     * @Interface : CheckoutModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1769
     */
    interface RewardModel {
        // usage : get all rewards list from the server
        fun allRewardsList(apiInterface: ApiInterface, onFinishedAllRewardListener: RewardMVP.RewardModel.onFinishedAllRewardListener, accessToken: String)
        // usage : get my rewards list from the server
        fun myRewardsList(apiInterface: ApiInterface, onFinishedMyRewardListener: RewardMVP.RewardModel.onFinishedMyRewardListener, accessToken: String)
        // usage : notify redeem reward status to the server
        fun redeemReward (apiInterface: ApiInterface, onFinishedRedeemRewardListener: RewardMVP.RewardModel.onFinishedRedeemRewardListener, accessToken: String, rewardId: Int)
        // usage : provide listener for all rewards list process
        interface onFinishedAllRewardListener {
            fun onAllRewardFinished(appRewards: MutableList<AllRewardEntity>?, myGoVps: Int?)
            fun onAllRewardFailure(warnings: String)
        }
        // usage : provide listener for my rewards list process
        interface onFinishedMyRewardListener {
            fun onMyRewardFinished (myRewards: MutableList<MyRewardsEntity>?, myGoVps: Int)
            fun onMyRewardFailure (warnings: String)
        }
        // usage : provide listener for redeem reward process
        interface onFinishedRedeemRewardListener {
            fun onRedeemRewardFinished ()
            fun onRedeemRewardFailed ()
        }
    }
}