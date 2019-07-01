package com.govida.ui_section.setting_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.setting_section.model.ChangePasswordRequest
import com.govida.ui_section.setting_section.model.ChangePasswordResponse
import com.govida.utility_section.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordModelImplementer:ChangePasswordMVP.ChangePasswordModel {
    override fun processChangePassword(
        accessToken: String,
        apiInterface: ApiInterface,
        onFinishedListener: ChangePasswordMVP.ChangePasswordModel.OnFinishedListener,
        passordRequest: ChangePasswordRequest
    ) {
        try {
            var call = apiInterface.changePasswordRequest(accessToken,passordRequest)
            call.enqueue(object: Callback<ChangePasswordResponse> {

                override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE || response.code() == AppConstants.HTTP_STATUS_CREATED_CODE_202 ) {
                        if (response.body()!!.status == AppConstants.STATUS_CODE_SUCCESS && response.body()!!.responseBody != null && response.body()!!.responseBody!!.data != null) {
                            onFinishedListener.onFinished(response.body()!!.responseBody!!.data)
                        } else {
                            onFinishedListener.onFailure(response.body()!!.message)
                        }
                    } else {
                        onFinishedListener.onFailure("")
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                    onFinishedListener.onFailure(t.message!!)
                    Crashlytics.logException(t)
                }

            })
        } catch (e: Exception) {
            onFinishedListener.onFailure("")
            Crashlytics.logException(e)
        }
    }
}