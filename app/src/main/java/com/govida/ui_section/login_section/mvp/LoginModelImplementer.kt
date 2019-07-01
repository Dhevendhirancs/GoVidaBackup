/**
 * @Class : LoginModelImplementer
 * @Usage : This class is used for providing api request response.
 * @Author : 1276
 */

package com.govida.ui_section.login_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.be_connected.model.BonusPointRequestObject
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.login_section.models.TokenRequest
import com.govida.ui_section.login_section.models.TokenResponse
import com.govida.ui_section.login_section.models.UserDetailResponse
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModelImplementer:LoginMVP.LoginModel {
    /**
     *  @Function : getUserAccessToken()
     *  @params   : tokenLoginDetails: TokenRequest, apiInterface: ApiInterface,onFinishedTokenListener: LoginMVP.LoginModel.OnFinishedTokenListener
     *  @Return   : void
     * 	@Usage	  :  This is used for making api call with the server to get access token for user
     *  @Author   : 1276
     */
    override fun getUserAccessToken(tokenLoginDetails: TokenRequest, apiInterface: ApiInterface,onFinishedTokenListener: LoginMVP.LoginModel.OnFinishedTokenListener) {
        try{
            val headers = HashMap<String,String>()
            headers["Authorization"] = "Basic bXktY2xpZW50Om15LXNlY3JldA=="
            apiInterface.getToken(headers, tokenLoginDetails.grant_type, tokenLoginDetails.username, tokenLoginDetails.password)
                .enqueue(object : Callback<TokenResponse> {
                    override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                        if(response.code()==AppConstants.HTTP_STATUS_CREATED_CODE){
                            onFinishedTokenListener.onTokenFinished(response.body())
                        }
                        else {
                            // status failure
                            onFinishedTokenListener.onTokenFailure("Enter valid Credentials.")
                        }
                    }
                    override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                        onFinishedTokenListener.onTokenFailure("")
                    }
                })
        }catch (e:Exception){
            Crashlytics.logException(e)
            onFinishedTokenListener.onTokenFailure("")
            AppLogger.e(e.message!!)
        }
    }

    /**
     *  @Function : processLogin()
     *  @params   : apiInterface: ApiInterface, onFinishedListener: LoginMVP.LoginModel.OnFinishedListener, accessToken: String
     *  @Return   : void
     * 	@Usage	  : Get the users detail
     *  @Author   : 1276
     */
    override fun processLogin(apiInterface: ApiInterface, onFinishedListener: LoginMVP.LoginModel.OnFinishedListener, accessToken: String) {
        try{
            val call=apiInterface.getEmployeeDetails(accessToken)
            call.enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>)
                {
                    if(response.code()==AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                            onFinishedListener.onFinished(response.body()?.responseBody?.data!!)
                        }else{
                            onFinishedListener.onFailure("")
                        }
                    }
                    else {
                        // status failure
                        onFinishedListener.onFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    Crashlytics.logException(t)
                    onFinishedListener.onFailure("")
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            Crashlytics.logException(e)
            onFinishedListener.onFailure("")
            AppLogger.e(e.message!!)
        }
    }

    /**
     *  @Function : getBonusPoints()
     *  @params   : apiInterface: ApiInterface, onFinishedOnboardingListener: LoginMVP.LoginModel.OnFinishedBonusListener, accessToken: String
     *  @Return   : void
     * 	@Usage	  : Get the bonus point value
     *  @Author   : 1769
     */
    override fun getBonusPoints(apiInterface: ApiInterface, onFinishedListener: LoginMVP.LoginModel.OnFinishedBonusListener, accessToken: String) {
        try{
            val call=apiInterface.getBonusPoints(accessToken)
            call.enqueue(object : Callback<BonusPointResponseObject> {
                override fun onResponse(call: Call<BonusPointResponseObject>, response: Response<BonusPointResponseObject>)
                {
                    if(response.code()==AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status?.equals(AppConstants.STATUS_CODE_SUCCESS)!! && response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                            onFinishedListener.onBonusFinished(response.body()?.responseBody?.data!!)
                        }else{
                            onFinishedListener.onBonusFailure("")
                        }
                    }
                    else {
                        onFinishedListener.onBonusFailure("")
                    }

                }
                override fun onFailure(call: Call<BonusPointResponseObject>, t: Throwable) {
                    Crashlytics.logException(t)
                    onFinishedListener.onBonusFailure("")
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            Crashlytics.logException(e)
            onFinishedListener.onBonusFailure("")
            AppLogger.e(e.message!!)
        }
    }

    /**
     *  @Function : processOnboarding()
     *  @params   : apiInterface: ApiInterface, onFinishedOnboardingListener: LoginMVP.LoginModel.OnFinishedOnboardingListener, accessToken: String
     *  @Return   : void
     * 	@Usage	  : Set the OnBoarding value true using Bonus Post API
     *  @Author   : 1769
     */
    override fun processOnboarding(apiInterface: ApiInterface, onFinishedOnboardingListener: LoginMVP.LoginModel.OnFinishedOnboardingListener, accessToken: String) {
        try {
            val bonusPointRequestObject = BonusPointRequestObject()
            bonusPointRequestObject.deviceIntegration = false
            bonusPointRequestObject.gps = false
            bonusPointRequestObject.onboarding = true
            val call = apiInterface.postBonusPoints(accessToken, bonusPointRequestObject)
            call.enqueue(object : Callback <BonusPointResponseObject> {

                override fun onResponse(call: Call<BonusPointResponseObject>, response: Response<BonusPointResponseObject>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()?.responseBody != null && response.body()!!.responseBody?.data != null) {
                            onFinishedOnboardingListener.onOnboardingFinished(response.body()!!.responseBody?.data!!)
                        }
                    } else {
                        onFinishedOnboardingListener.onOnboardingFailed(response.body()?.message.toString())
                    }
                }

                override fun onFailure(call: Call<BonusPointResponseObject>, t: Throwable) {
                    Crashlytics.logException(t)
                    onFinishedOnboardingListener.onOnboardingFailed("")
                    AppLogger.e(t.message!!)
                }
            })
        } catch (e : Exception) {
            Crashlytics.logException(e)
            onFinishedOnboardingListener.onOnboardingFailed("")
            AppLogger.e(e.message!!)
        }
    }
}


