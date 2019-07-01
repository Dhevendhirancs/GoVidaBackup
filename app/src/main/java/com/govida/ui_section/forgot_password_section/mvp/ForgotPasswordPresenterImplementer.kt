/**
 * @Class : ForgotPasswordPresenterImplementer
 * @Usage : This class is used as mediator between view and model
 * @Author : 1276
 */

package com.govida.ui_section.forgot_password_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.BasePresenter
import com.govida.ui_section.base_class_section.MvpView

class ForgotPasswordPresenterImplementer(forgotView: ForgotPasswordMVP.ForgotPasswordView) : ForgotPasswordMVP.ForgotPasswordPresenter, BasePresenter<MvpView>(),
    ForgotPasswordMVP.ForgotPasswordModel.OnFinishedListener {
    private var mForgotView:ForgotPasswordMVP.ForgotPasswordView? = forgotView
    private var mForgotModel:ForgotPasswordMVP.ForgotPasswordModel = ForgotPasswordModelImplementer()
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)

    /**
     *  @Function : attachView()
     *  @params   : ForgotPasswordMVP.ForgotPasswordView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1276
     */
    override fun attachView(fgView: ForgotPasswordMVP.ForgotPasswordView) {
        mForgotView=fgView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1276
     */
    override fun destroyView() {
        mForgotView=null
    }

    /**
     *  @Function : onEmailLinkSharedButtonClicked()
     *  @params   : emailAddress: String
     *  @Return   : void
     * 	@Usage	  : This is used to trigger api/db call in model for completing reset link functionality
     *  @Author   : 1276
     */
    override fun onEmailLinkSharedButtonClicked(emailAddress: String) {
        if(mForgotView!=null){
            mForgotView!!.hideKeyboard()
            mForgotView!!.showLoading()
            if(mForgotView!!.isNetworkConnected){
                mForgotModel.processEmailLinkShared(emailAddress,mApiInterfaceService,this)
            }
            else{
                mForgotView!!.hideLoading()
                mForgotView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onFinished()
     *  @params   : result: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for operation completion successfully
     *  @Author   : 1276
     */
    override fun onFinished(result: String?) {
        if(mForgotView!=null) {
            mForgotView!!.hideLoading()
            mForgotView!!.onEmailLinkSharedSuccessfully()
        }
    }

    /**
     *  @Function : onFinished()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for operation completion failed
     *  @Author   : 1276
     */
    override fun onFailure(warnings: String) {
        if(mForgotView!=null) {
            mForgotView!!.hideLoading()
            mForgotView!!.onEmailLinkSharedFailed(warnings)
        }
    }
}