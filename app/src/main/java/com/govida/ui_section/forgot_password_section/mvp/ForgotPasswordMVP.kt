/**
 * @Class : ForgotPasswordMVP
 * @Usage : This class is used for providing MVP functionality to Forgot Password Activity
 * @Author : 1276
 */
package com.govida.ui_section.forgot_password_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView

class ForgotPasswordMVP {
    /**
     * @Interface : ForgotPasswordView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1276
     */

    interface ForgotPasswordView: MvpView {
        // usage : once email link shared with client we use this function to define next functionality
        fun onEmailLinkSharedSuccessfully()
        // usage : email link sharing failed then we use this to notify user
        fun onEmailLinkSharedFailed(warnings: String)
    }

    /**
     * @Interface : ForgotPasswordPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface ForgotPasswordPresenter{
        fun attachView(fgView:ForgotPasswordView)
        fun destroyView()
        // usage: This is used to trigger api/db call in model for completing reset link functionality
        fun onEmailLinkSharedButtonClicked(emailAddress:String)
    }

    /**
     * @Interface : ForgotPasswordModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface ForgotPasswordModel{
        // usage : This is used for making api call with the server to send forgot password link to user's email address
        fun processEmailLinkShared(emailAddress:String,apiInterface: ApiInterface,onFinishedListener:OnFinishedListener)
        interface OnFinishedListener {
            fun onFinished(result: String?)
            fun onFailure(warnings:String)
        }
    }
}