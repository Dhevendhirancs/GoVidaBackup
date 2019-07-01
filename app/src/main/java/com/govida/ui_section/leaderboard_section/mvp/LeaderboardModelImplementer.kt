/**
 * @Class : LeaderboardModelImplementer
 * @Usage : This class is used for providing MVP functionality to Leaderboard Page
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse
import com.govida.utility_section.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardModelImplementer:LeaderboardMVP.LeaderboardModel {
    /**
     *  @Function : processLeaderboard()
     *  @params   : accessToken: String, apiInterface: ApiInterface, onFinishedListener: LeaderboardMVP.LeaderboardModel.OnFinishedListener
     *  @Return   : void
     * 	@Usage	  : To get leaderboard data
     *  @Author   : 1769
     */
    override fun processLeaderboard(accessToken: String, apiInterface: ApiInterface, onFinishedListener: LeaderboardMVP.LeaderboardModel.OnFinishedListener) {
        try {
            var call = apiInterface.getLeaderboard(accessToken)
            call.enqueue(object: Callback<LeaderboardResponse> {

                override fun onResponse(call: Call<LeaderboardResponse>, response: Response<LeaderboardResponse>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()!!.status.equals(AppConstants.STATUS_CODE_SUCCESS) && response.body()!!.responseBody != null && response.body()!!.responseBody!!.data != null) {
                            onFinishedListener.onFinished(response.body()!!.responseBody!!.data)
                        } else {
                            onFinishedListener.onFailure("")
                        }
                    } else {
                        onFinishedListener.onFailure("")
                    }
                }
                override fun onFailure(call: Call<LeaderboardResponse>, t: Throwable) {
                    onFinishedListener.onFailure(t.message!!)
                    Crashlytics.logException(t)
                }
            })
        } catch (e: Exception) {
            onFinishedListener.onFailure("")
            Crashlytics.logException(e)
        }
    }
}