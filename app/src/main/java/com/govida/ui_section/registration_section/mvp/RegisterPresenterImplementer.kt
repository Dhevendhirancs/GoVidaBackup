/**
 * @Class : RegisterPresenterImplementer
 * @Usage : This class is used for presenter implementer for registration purpose.
 * @Author : 1276
 */

package com.govida.ui_section.registration_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.BasePresenter
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.registration_section.models.RegisterUserRequest
import com.govida.ui_section.registration_section.models.RegisterUserResponse

class RegisterPresenterImplementer (registerView: RegisterMVP.RegisterView) : BasePresenter<MvpView>(),RegisterMVP.RegisterPresenter, RegisterMVP.RegisterModel.OnFinishedListener {
    private var mRegisterView:RegisterMVP.RegisterView? = registerView
    private var mRegisterModel:RegisterMVP.RegisterModel = RegisterModelImplementer()
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)

    override fun attachView(rgView: RegisterMVP.RegisterView) {
        mRegisterView=rgView
    }

    override fun destroyView() {
        mRegisterView=null
    }

    override fun onRegisterButtonClicked(userDetails: RegisterUserRequest) {
        if(mRegisterView!=null)
        {
            mRegisterView!!.hideKeyboard()
            mRegisterView!!.showLoading()
            if(mRegisterView!!.isNetworkConnected)
            {
                mRegisterModel.processRegister(userDetails,mApiInterfaceService,this)
            }
            else{
                mRegisterView!!.hideLoading()
                mRegisterView!!.onError(R.string.not_connected_to_internet)
            }

        }
    }

    override fun onFinished(result: RegisterUserResponse.Data?) {
        mRegisterView!!.hideLoading()
        if(result?.result!=null){
            // Account create successfully
            mRegisterView!!.onRegisterSuccessful()
        }
    }

    override fun onFailure(warnings:String) {
        mRegisterView!!.hideLoading()
        if(warnings == ""){
            mRegisterView!!.onError(R.string.some_error)
        }else{
            mRegisterView!!.onError(warnings)
        }

    }
}