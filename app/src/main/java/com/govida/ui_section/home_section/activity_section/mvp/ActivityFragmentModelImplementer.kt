package com.govida.ui_section.home_section.activity_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityFragmentModelImplementer: ActivityFragmentMVP.ActivityFragmentModel {
    /**
     *  @Function : activitySyncProcess()
     *  @params   : apiInterface: ApiInterface,onActivitySyncFinishedListener: ActivityFragmentMVP.ActivityFragmentModel.OnActivitySyncFinishedListener,accessToken: String,syncRequest: ActivitySyncRequest
     *  @Return   : void
     * 	@Usage	  : api request for activity sync data
     *  @Author   : 1276
     */
    override fun activitySyncProcess(apiInterface: ApiInterface,onActivitySyncFinishedListener: ActivityFragmentMVP.ActivityFragmentModel.OnActivitySyncFinishedListener,accessToken: String,syncRequest: ActivitySyncRequest) {
        try{
            val call=apiInterface.postActivitySyncData(accessToken,syncRequest)
            call.enqueue(object : Callback<ActivitySyncResponse> {
                override fun onResponse(call: Call<ActivitySyncResponse>, response: Response<ActivitySyncResponse>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onActivitySyncFinishedListener.onActivitySyncFinished(response.body()!!)
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