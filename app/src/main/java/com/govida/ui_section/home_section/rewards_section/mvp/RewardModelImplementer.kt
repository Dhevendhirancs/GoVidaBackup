/**
 * @Class : RewardModelImplementer
 * @Usage : This class is used for providing MVP functionality to Reward Page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.rewards_section.models.*
import com.govida.utility_section.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardModelImplementer: RewardMVP.RewardModel {

    /**
     *  @Function : allRewardsList()
     *  @params   : apiInterface: ApiInterface, onFinishedAllRewardListener: RewardMVP.RewardModel.onFinishedAllRewardListener, accessToken: String
     *  @Return   : void
     * 	@Usage	  : To get all reward list from server
     *  @Author   : 1769
     */
    override fun allRewardsList(apiInterface: ApiInterface, onFinishedAllRewardListener: RewardMVP.RewardModel.onFinishedAllRewardListener, accessToken: String) {
        try {
            var call = apiInterface.getAllRewards(accessToken)
            call.enqueue(object: Callback<AllRewardResponse> {

                override fun onResponse(call: Call<AllRewardResponse>, response: Response<AllRewardResponse>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()!!.status.equals(AppConstants.STATUS_CODE_SUCCESS) && response.body()!!.responseBody != null && response.body()!!.responseBody!!.data != null) {
                            onFinishedAllRewardListener.onAllRewardFinished(response.body()!!.responseBody!!.data, response.body()!!.responseBody!!.myGoVPs)
                        } else {
                            onFinishedAllRewardListener.onAllRewardFailure("")
                        }
                    } else {
                        onFinishedAllRewardListener.onAllRewardFailure("")
                    }
                }

                override fun onFailure(call: Call<AllRewardResponse>, t: Throwable) {
                    onFinishedAllRewardListener.onAllRewardFailure(t.message!!)
                    Crashlytics.logException(t)
                }

            })
        } catch (e: Exception) {
            onFinishedAllRewardListener.onAllRewardFailure("")
            Crashlytics.logException(e)
        }
    }

    /**
     *  @Function : myRewardsList()
     *  @params   : apiInterface: ApiInterface, onFinishedMyRewardListener: RewardMVP.RewardModel.onFinishedMyRewardListener, accessToken: String
     *  @Return   : void
     * 	@Usage	  : To get my reward list from server
     *  @Author   : 1769
     */
    override fun myRewardsList( apiInterface: ApiInterface, onFinishedMyRewardListener: RewardMVP.RewardModel.onFinishedMyRewardListener, accessToken: String ) {
        try {
            var call = apiInterface.getMyRewards(accessToken)
            call.enqueue(object: Callback<MyRewardsResponse> {

                override fun onResponse(call: Call<MyRewardsResponse>, response: Response<MyRewardsResponse>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()!!.status.equals(AppConstants.STATUS_CODE_SUCCESS) && response.body()!!.responseBody != null && response.body()!!.responseBody!!.data != null) {
                            onFinishedMyRewardListener.onMyRewardFinished(response.body()!!.responseBody!!.data , response.body()!!.responseBody!!.myGoVPs!!)
                        } else {
                            onFinishedMyRewardListener.onMyRewardFailure("")
                        }
                    } else {
                        onFinishedMyRewardListener.onMyRewardFailure("")
                    }
                }

                override fun onFailure(call: Call<MyRewardsResponse>, t: Throwable) {
                    onFinishedMyRewardListener.onMyRewardFailure(t.message!!)
                    Crashlytics.logException(t)
                }

            })
        } catch (e: Exception) {
            onFinishedMyRewardListener.onMyRewardFailure("")
            Crashlytics.logException(e)
        }
    }

    /**
     *  @Function : redeemReward()
     *  @params   : apiInterface: ApiInterface, onFinishedRedeemRewardListener: RewardMVP.RewardModel.onFinishedRedeemRewardListener, accessToken: String, rewardId: Int
     *  @Return   : void
     * 	@Usage	  : To notify redeem reward status to server
     *  @Author   : 1769
     */
    override fun redeemReward( apiInterface: ApiInterface, onFinishedRedeemRewardListener: RewardMVP.RewardModel.onFinishedRedeemRewardListener, accessToken: String, rewardId: Int ) {
        try {
            var call = apiInterface.redeemReward(accessToken, rewardId)
            call.enqueue(object: Callback<RedeemRewardsResponse> {

                override fun onResponse(call: Call<RedeemRewardsResponse>, response: Response<RedeemRewardsResponse>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()!!.status.equals(AppConstants.STATUS_CODE_SUCCESS) && response.body()!!.responseBody != null && response.body()!!.responseBody!!.data != null) {
                            onFinishedRedeemRewardListener.onRedeemRewardFinished()
                        } else {
                            onFinishedRedeemRewardListener.onRedeemRewardFailed()
                        }
                    } else {
                        onFinishedRedeemRewardListener.onRedeemRewardFailed()
                    }
                }

                override fun onFailure(call: Call<RedeemRewardsResponse>, t: Throwable) {
                    onFinishedRedeemRewardListener.onRedeemRewardFailed()
                    Crashlytics.logException(t)
                }

            })
        } catch (e: Exception) {
            onFinishedRedeemRewardListener.onRedeemRewardFailed()
            Crashlytics.logException(e)
        }
    }
}
