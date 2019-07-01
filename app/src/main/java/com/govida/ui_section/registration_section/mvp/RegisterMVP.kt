/**
 * @Class : RegisterMVP
 * @Usage : This class is used for providing MVP functionality to Register Activity
 * @Author : 1276
 */

package com.govida.ui_section.registration_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.registration_section.models.RegisterUserRequest
import com.govida.ui_section.registration_section.models.RegisterUserResponse

class RegisterMVP {
    /*
    * @Interface : RegisterView
    * @Usage : This interface is used for managing UI related functionality
    * @Author : 1276
    */

    interface RegisterView: MvpView {
        // usage : function will be used when user registration is successful.
        fun onRegisterSuccessful()
        // usage : function will be used when user registration is failed.
        fun onRegisterFailed(errorMsg: String)
    }

    /**
     * @Interface : RegisterPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface RegisterPresenter{
        fun attachView(rgView:RegisterView)
        fun destroyView()
        // usage : initiate process of user registration
        fun onRegisterButtonClicked(userDetails:RegisterUserRequest)
    }

    /**
     * @Interface : RegisterModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface RegisterModel{
        // usage : register user with server
        fun processRegister(userDetails: RegisterUserRequest, apiInterface: ApiInterface, onFinishedListener:OnFinishedListener)

        interface OnFinishedListener {
            fun onFinished(result: RegisterUserResponse.Data?)
            fun onFailure(warnings:String)
        }
    }
}