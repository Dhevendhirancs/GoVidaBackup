/**
 * @Class : LoginMVP
 * @Usage : This class is used for providing MVP functionality to Login Activity
 * @Author : 1276
 */

package com.govida.ui_section.login_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.login_section.models.TokenRequest
import com.govida.ui_section.login_section.models.TokenResponse
import com.govida.ui_section.login_section.models.UserDetailResponse


class LoginMVP {
    /**
     * @Interface : LoginView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1276
     */

     interface LoginView:MvpView{
        // usage : function will be used when user login is successful.
        fun onLoginSuccessful(userDetails: UserDetailResponse.Data)
        // usage : function will be used when user registration is failed.
        fun onLoginFailed()
        fun saveBonusDetails(receivedBonusDetails: BonusPointResponseObject.Data)
        fun saveBonusDetailsFirstTime(receivedBonusDetails: BonusPointResponseObject.Data)
        fun storeTokenData(receivedToken: TokenResponse?)
        fun moveToNextPage()
    }

    /**
     * @Interface : LoginPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface LoginPresenter{
        fun attachView(loginView:LoginView)
        fun destroyView()
        // usage : initiate process of user login
        fun onLoginButtonClicked(tokenLoginDetails: TokenRequest)
    }

    /**
     * @Interface : LoginModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface LoginModel{
        // usage : validate user with server
        fun getUserAccessToken(tokenLoginDetails: TokenRequest,apiInterface:ApiInterface,onFinishedListener: OnFinishedTokenListener)
        fun processLogin(apiInterface: ApiInterface, onFinishedListener: OnFinishedListener, accessToken: String)
        fun getBonusPoints(apiInterface: ApiInterface, onFinishedListener: OnFinishedBonusListener, accessToken: String)
        fun processOnboarding(apiInterface:ApiInterface, onFinishedOnboardingListener: OnFinishedOnboardingListener, accessToken: String)
        interface OnFinishedListener {
            fun onFinished(receivedUserDetails: UserDetailResponse.Data)
            fun onFailure(warnings:String)
        }

        interface OnFinishedTokenListener {
            fun onTokenFinished(receivedToken: TokenResponse?)
            fun onTokenFailure(warnings:String)
        }

        interface OnFinishedBonusListener {
            fun onBonusFinished(receivedBonusDetails: BonusPointResponseObject.Data)
            fun onBonusFailure(warnings:String)
        }

        interface OnFinishedOnboardingListener {
            fun onOnboardingFinished(receivedData: BonusPointResponseObject.Data)
            fun onOnboardingFailed(warnings: String)
        }
    }

}