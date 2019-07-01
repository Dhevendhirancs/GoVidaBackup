package com.govida.ui_section.setting_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.setting_section.model.ChangePasswordRequest
import com.govida.ui_section.setting_section.model.ChangePasswordResponse

class ChangePasswordPresenterImplementer(mView: ChangePasswordMVP.ChangePasswordView):ChangePasswordMVP.ChangePasswordPresenter,ChangePasswordMVP.ChangePasswordModel.OnFinishedListener {

    private var mChangePasswordView: ChangePasswordMVP.ChangePasswordView? = mView
    private var mChangePasswordModel = ChangePasswordModelImplementer()
    private var mApiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)



    override fun attachView(lcpView: ChangePasswordMVP.ChangePasswordView) {
        mChangePasswordView=lcpView;
    }

    override fun destroyView() {
        mChangePasswordView=null
    }

    override fun onRequestLeaderboard(accessToken: String, passordRequest: ChangePasswordRequest) {
        if(mChangePasswordView!=null){
            if(mChangePasswordView!!.isNetworkConnected){
                mChangePasswordView!!.showLoading()
                mChangePasswordModel.processChangePassword(accessToken,mApiInterface,this,passordRequest)
            }
            else{
                mChangePasswordView!!.hideLoading()
                mChangePasswordView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    override fun onFinished(result: ChangePasswordResponse.Data?) {
        if(mChangePasswordView!=null){
            mChangePasswordView!!.hideLoading()
            mChangePasswordView!!.onChangePasswordSuccessful(result)
        }
    }

    override fun onFailure(warnings: String) {
        if(mChangePasswordView!=null){
            mChangePasswordView!!.hideLoading()
            if(warnings == ""){
                mChangePasswordView!!.onError(R.string.some_error)
            }else{
                mChangePasswordView!!.onError(warnings)
            }
        }
    }
}