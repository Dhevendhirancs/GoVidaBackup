package com.govida.ui_section.setting_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.setting_section.model.ChangePasswordRequest
import com.govida.ui_section.setting_section.model.ChangePasswordResponse

class ChangePasswordMVP {
/*
    * @Interface : ChangePasswordView
    * @Usage : This interface is used for managing UI related functionality
    * @Author : 1276
    */

    interface ChangePasswordView: MvpView {
        // usage : function will be used when Leaderboard is successful.
        fun onChangePasswordSuccessful(result: ChangePasswordResponse.Data?)
        // usage : function will be used when Leaderboard is failed.
        fun onChangePasswordFailed(errorMsg: String)
    }

    /**
     * @Interface : ChangePasswordPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface ChangePasswordPresenter{
        fun attachView(lcpView:ChangePasswordView)
        fun destroyView()
        // usage : initiate process of Leaderboard
        fun onRequestLeaderboard(accessToken: String,passordRequest: ChangePasswordRequest)
    }

    /**
     * @Interface : ChangePasswordModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface ChangePasswordModel{
        // usage : Leaderboard api call
        fun processChangePassword(accessToken: String, apiInterface: ApiInterface, onFinishedListener:OnFinishedListener,passordRequest: ChangePasswordRequest)

        interface OnFinishedListener {
            fun onFinished(result: ChangePasswordResponse.Data?)
            fun onFailure(warnings:String)
        }
    }
}