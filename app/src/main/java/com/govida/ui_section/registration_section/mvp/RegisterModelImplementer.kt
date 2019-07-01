/**
 * @Class : RegisterModelImplementer
 * @Usage : This class is used for model implementer for registration purpose.
 * @Author : 1276
 */

package com.govida.ui_section.registration_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.registration_section.models.RegisterUserResponse
import com.govida.ui_section.registration_section.models.RegisterUserRequest
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterModelImplementer:RegisterMVP.RegisterModel{
    override fun processRegister(userDetails: RegisterUserRequest, apiInterface: ApiInterface, onFinishedListener: RegisterMVP.RegisterModel.OnFinishedListener) {
        try {
            val call=apiInterface.registerUser(userDetails)

            call.enqueue(object : Callback<RegisterUserResponse> {
                override fun onResponse(call: Call<RegisterUserResponse>, response: Response<RegisterUserResponse>)
                {
                    if(response.code()==AppConstants.HTTP_STATUS_CREATED_CODE)
                    {
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,false)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedListener.onFinished(response.body()?.responseBody?.data)
                            }else{
                                onFinishedListener.onFailure(response.body()?.message.toString())
                            }
                        }
                        else {
                            // status failure
                            onFinishedListener.onFailure(response.body()?.message.toString())
                        }
                    }
                    else{
                        onFinishedListener.onFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
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