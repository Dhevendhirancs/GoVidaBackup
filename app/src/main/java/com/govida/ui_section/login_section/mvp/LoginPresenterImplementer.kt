/**
 * @Class : LoginPresenterImplementer
 * @Usage : This class is used as mediator between view and model
 * @Author : 1276
 */


package com.govida.ui_section.login_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.BasePresenter
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.login_section.models.TokenRequest
import com.govida.ui_section.login_section.models.TokenResponse
import com.govida.ui_section.login_section.models.UserDetailResponse

class LoginPresenterImplementer(loginView: LoginMVP.LoginView) : BasePresenter<MvpView>(),LoginMVP.LoginPresenter,
    LoginMVP.LoginModel.OnFinishedListener,LoginMVP.LoginModel.OnFinishedTokenListener,LoginMVP.LoginModel.OnFinishedBonusListener, LoginMVP.LoginModel.OnFinishedOnboardingListener {

    private var mloginView:LoginMVP.LoginView? = loginView
    private var mloginModel:LoginMVP.LoginModel = LoginModelImplementer()
    private var mApiInterfaceService:ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
    var accessToken=""

    /**
     *  @Function : attachView()
     *  @params   : ForgotPasswordMVP.ForgotPasswordView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1276
     */
    override fun attachView(loginView: LoginMVP.LoginView) {
        mloginView=loginView
    }

    /**
    *  @Function : destroyView()
    *  @params   : void
    *  @Return   : void
    * 	@Usage	  : detach presenter to activity
    *  @Author   : 1276
    */
    override fun destroyView() {
        mloginView=null
    }


    /**
     *  @Function : onLoginButtonClicked()
     *  @params   : tokenLoginDetails: TokenRequest
     *  @Return   : void
     * 	@Usage	  : process request of login button clicked
     *  @Author   : 1276
     */
    override fun onLoginButtonClicked(tokenLoginDetails: TokenRequest) {
        if(mloginView!=null)
        {
            mloginView!!.hideKeyboard()
            if(mloginView!!.isNetworkConnected){
                mloginView!!.showLoading()
                mloginModel.getUserAccessToken(tokenLoginDetails,mApiInterfaceService,this)
                //mloginModel.processLogin(mApiInterfaceService,this)
            }
            else{
                mloginView!!.hideLoading()
                mloginView!!.onError(R.string.not_connected_to_internet)
            }

        }
    }

    /**
     *  @Function : onFinished()
     *  @params   : receivedUserDetails: UserDetailResponse.Data
     *  @Return   : void
     * 	@Usage	  : process when the user details are received from model
     *  @Author   : 1276
     */
    override fun onFinished(receivedUserDetails: UserDetailResponse.Data) {
        if(mloginView!=null) {
            mloginView!!.onLoginSuccessful(receivedUserDetails)
            mloginModel.getBonusPoints(mApiInterfaceService,this, "Bearer $accessToken")
        }
    }


    /**
     *  @Function : onFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : process when the user details are failed from model
     *  @Author   : 1276
     */
    override fun onFailure(warnings: String) {
        mloginView!!.hideLoading()
        if(warnings == ""){
            mloginView!!.onError(R.string.some_error)
        }else{
            mloginView!!.onError(warnings)
        }

    }

    /**
     *  @Function : onTokenFinished()
     *  @params   : receivedToken: TokenResponse?
     *  @Return   : void
     * 	@Usage	  : process when the token is received
     *  @Author   : 1276
     */
    override fun onTokenFinished(receivedToken: TokenResponse?) {
        if(mloginView!=null){
            accessToken=receivedToken!!.accessToken
            mloginView!!.storeTokenData(receivedToken)
            mloginModel.processLogin(mApiInterfaceService,this,"Bearer "+ (receivedToken.accessToken))
        }
    }

    /**
     *  @Function : onTokenFinished()
     *  @params   : receivedToken: TokenResponse?
     *  @Return   : void
     * 	@Usage	  : process when the token request is failed
     *  @Author   : 1276
     */
    override fun onTokenFailure(warnings: String) {
        mloginView!!.hideLoading()
        if(warnings == ""){
            mloginView!!.onError(R.string.some_error)
        }else{
            mloginView!!.onError(warnings)
        }
    }

    override fun onBonusFinished(receivedBonusDetails: BonusPointResponseObject.Data) {
        if (!receivedBonusDetails.onboarding!!) {
            //for first time login
            mloginModel.processOnboarding(mApiInterfaceService, this, "Bearer $accessToken")
        } else {
            if(mloginView!=null){
                mloginView!!.hideLoading()
                mloginView!!.saveBonusDetails(receivedBonusDetails)
            }
        }
    }

    override fun onBonusFailure(warnings: String) {
        if(mloginView!=null){
            mloginView!!.hideLoading()
            mloginView!!.moveToNextPage()
        }
    }

    override fun onOnboardingFinished(receivedData: BonusPointResponseObject.Data) {
        if (mloginView != null) {
            mloginView!!.hideLoading()
            mloginView!!.saveBonusDetailsFirstTime(receivedData)
        }
    }

    override fun onOnboardingFailed(warnings: String) {
        if (mloginView != null) {
            mloginView!!.hideLoading()
            mloginView!!.moveToNextPage()
        }
    }
}