/**
 * @Class : LeaderboardMVP
 * @Usage : This class is used for providing MVP functionality to Leaderboard Page
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse

class LeaderboardMVP {
    /**
    * @Interface : LeaderboardView
    * @Usage : This interface is used for managing UI related functionality
    * @Author : 1276
    */
    interface LeaderboardView: MvpView {
        // usage : function will be used when Leaderboard is successful.
        fun onLeaderboardSuccessful(result: LeaderboardResponse.Data?)
        // usage : function will be used when Leaderboard is failed.
        fun onLeaderboardFailed(errorMsg: String)
    }

    /**
     * @Interface : LeaderboardPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface LeaderboardPresenter{
        fun attachView(lbView:LeaderboardView)
        fun destroyView()
        // usage : initiate process of Leaderboard
        fun onRequestLeaderboard(accessToken: String)
    }

    /**
     * @Interface : LeaderboardModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface LeaderboardModel{
        // usage : Leaderboard api call
        fun processLeaderboard(accessToken: String, apiInterface: ApiInterface, onFinishedListener:OnFinishedListener)
        // usage : provide listener for leaderboard process
        interface OnFinishedListener {
            fun onFinished(result: LeaderboardResponse.Data?)
            fun onFailure(warnings:String)
        }
    }
}