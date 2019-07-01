/**
 * @Class : ForgotPasswordPresenterImplementer
 * @Usage : This class is used for providing api request response.
 * @Author : 1276
 */


package com.govida.ui_section.forgot_password_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.forgot_password_section.models.ForgotPasswordResponse
import com.govida.ui_section.forgot_password_section.models.ForgotPasswordRequest
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordModelImplementer:ForgotPasswordMVP.ForgotPasswordModel {
    /**
     *  @Function : processEmailLinkShared()
     *  @params   : emailAddress: String, apiInterface: ApiInterface,onFinishedListener: ForgotPasswordMVP.ForgotPasswordModel.OnFinishedListener
     *  @Return   : void
     * 	@Usage	  :  This is used for making api call with the server to send forgot password link to user's email address
     *  @Author   : 1276
     */
    override fun processEmailLinkShared(emailAddress: String, apiInterface: ApiInterface,onFinishedListener: ForgotPasswordMVP.ForgotPasswordModel.OnFinishedListener) {
        try {
            val userRequest = ForgotPasswordRequest(emailAddress)

            val call=apiInterface.forgotPassword(userRequest)

            call.enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE)
                    {
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success

                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null && response.body()?.responseBody?.data?.result!=null){
                                onFinishedListener.onFinished(response.body()?.responseBody?.data?.result)
                            }else{
                                onFinishedListener.onFailure("")
                            }
                        }
                        else {
                            // status failure
                            onFinishedListener.onFailure(response.body()?.responseBody?.data?.result.toString())
                        }
                    }
                    else{
                        onFinishedListener.onFailure(response.body()?.responseBody?.data?.result.toString())
                    }
                }
                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    // Log error here since request failed
                    Crashlytics.logException(t)
                    AppLogger.e(t.message!!)
                    onFinishedListener.onFailure("")
                }
            })
        }
        catch (ex: Exception){
            Crashlytics.logException(ex)
            AppLogger.e(ex.message!!)
            onFinishedListener.onFailure("")
        }

    }
}