package com.govida.push_notification

import android.app.IntentService
import android.content.Intent
import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.app_sharedpreference.AppPreference
import com.govida.push_notification.model.PushTokenRequest
import com.govida.push_notification.model.PushTokenResponse
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationTokenRegistrationService: IntentService("NotificationTokenRegistrationService") {
    override fun onHandleIntent(intent: Intent?) {
        val appPreference = AppPreference(this)
        val apiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
        if (appPreference.isUserLoggedIn) {
            val requestObj= PushTokenRequest()
            requestObj.deviceId=appPreference.notificationDeviceId
            requestObj.deviceToken=appPreference.notificationDeviceToken
            try{
                val call=apiInterface.sendFCMTokenToServer(appPreference.authorizationToken,requestObj)
                call.enqueue(object : Callback<PushTokenResponse> {
                    override fun onResponse(call: Call<PushTokenResponse>, response: Response<PushTokenResponse>)
                    {
                        if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                            if(response.body()?.status?.equals(AppConstants.STATUS_CODE_SUCCESS)!! && response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                AppLogger.d("Sucess")
                            }else{
                                AppLogger.e(""+response.body()?.message.toString())
                            }
                        }
                        else {
                            AppLogger.e(""+response.body()?.message.toString())
                        }
                    }
                    override fun onFailure(call: Call<PushTokenResponse>, t: Throwable) {
                        Crashlytics.logException(t)
                        AppLogger.e(t.message!!)
                    }
                })

            }catch (e:Exception){
                Crashlytics.logException(e)
                AppLogger.e(e.message!!)
            }
        } else {

        }
    }
}
