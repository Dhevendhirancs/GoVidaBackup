/**
 * @Class : ActivityHomeModelImplementer
 * @Usage : This class is used for providing model functionality to Home Activity
 * @Author : 1276
 */
package com.govida.ui_section.home_section.mvp

import android.os.AsyncTask
import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.ui_section.home_section.model.ChallengeResponse
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityHomeModelImplementer:ActivityHomeMVP.ActivityHomeModel {


    override fun challengeListProcess(apiInterface: ApiInterface, onFinishedListener: ActivityHomeMVP.ActivityHomeModel.OnChallengeFinishedListener, accessToken: String) {
        try{
            val call=apiInterface.getChallengeList(accessToken)
            call.enqueue(object : Callback<ChallengeResponse> {
                override fun onResponse(call: Call<ChallengeResponse>, response: Response<ChallengeResponse>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedListener.onChallengeFinished(response.body()?.responseBody?.data!!)
                            }else{
                                onFinishedListener.onChallengeFailure("")
                            }
                        }
                        else {
                            // status failure
                            onFinishedListener.onChallengeFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        // status failure
                        onFinishedListener.onChallengeFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<ChallengeResponse>, t: Throwable) {
                    Crashlytics.logException(t)
                    onFinishedListener.onChallengeFailure("")
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            Crashlytics.logException(e)
            onFinishedListener.onChallengeFailure("")
            AppLogger.e(e.message!!)
        }
    }


    override fun activitySyncProcess(apiInterface: ApiInterface, onActivitySyncFinishedListener: ActivityHomeMVP.ActivityHomeModel.OnActivitySyncFinishedListener, accessToken: String,syncRequest: ActivitySyncRequest) {
        try{
            val call=apiInterface.postActivitySyncData(accessToken,syncRequest)
            call.enqueue(object : Callback<ActivitySyncResponse> {
                override fun onResponse(call: Call<ActivitySyncResponse>, response: Response<ActivitySyncResponse>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onActivitySyncFinishedListener.onActivitySyncFinished(response.body()?.responseBody?.data!!)
                            }else{
                                onActivitySyncFinishedListener.onActivitySyncFailure("")
                            }
                        }
                        else {
                            // status failure
                            onActivitySyncFinishedListener.onActivitySyncFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        // status failure
                        onActivitySyncFinishedListener.onActivitySyncFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<ActivitySyncResponse>, t: Throwable) {
                    Crashlytics.logException(t)
                    onActivitySyncFinishedListener.onActivitySyncFailure("")
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            Crashlytics.logException(e)
            onActivitySyncFinishedListener.onActivitySyncFailure("")
            AppLogger.e(e.message!!)
        }
    }
}