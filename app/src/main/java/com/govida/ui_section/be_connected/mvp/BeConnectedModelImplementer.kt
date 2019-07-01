/**
 * @Class : BeConnectedModelImplementer
 * @Usage : This class is used for providing MVP functionality to BeConnected Activity
 * @Author : 1276
 */
package com.govida.ui_section.be_connected.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.be_connected.model.BonusPointRequestObject
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeConnectedModelImplementer:BeConnectedMVP.BeConnectedModel {
    /**
     *  @Function : processGoogleFit()
     *  @params   : googleFitState: Boolean, onBoardingState: Boolean, apiInterface: ApiInterface, onFinishedGoogleFitListener: BeConnectedMVP.BeConnectedModel.OnFinishGoogleFitPermissionListener,accessToken: String
     *  @Return   : void
     * 	@Usage	  : set google fit status to server
     *  @Author   : 1276
     */
    override fun processGoogleFit(googleFitState: Boolean, onBoardingState: Boolean, apiInterface: ApiInterface, onFinishedGoogleFitListener: BeConnectedMVP.BeConnectedModel.OnFinishGoogleFitPermissionListener,accessToken: String) {
        try{
            val tempObj= BonusPointRequestObject()
            tempObj.deviceIntegration=googleFitState
            tempObj.onboarding=onBoardingState
            tempObj.gps=false
            val call=apiInterface.postBonusPoints(accessToken,tempObj)
            call.enqueue(object : Callback<BonusPointResponseObject> {
                override fun onResponse(call: Call<BonusPointResponseObject>, response: Response<BonusPointResponseObject>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedGoogleFitListener.onFinished(response.body()?.responseBody?.data!!)
                            }else{
                                onFinishedGoogleFitListener.onFailure("")
                            }
                        }
                        else {
                            // status failure
                            onFinishedGoogleFitListener.onFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        // status failure
                        onFinishedGoogleFitListener.onFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<BonusPointResponseObject>, t: Throwable) {
                    onFinishedGoogleFitListener.onFailure("")
                    Crashlytics.logException(t)
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            onFinishedGoogleFitListener.onFailure("")
            Crashlytics.logException(e)
            AppLogger.e(e.message!!)
        }
    }

    /**
     *  @Function : processGPS()
     *  @params   : gpsState: Boolean, onBoardingState: Boolean, apiInterface: ApiInterface, onFinishedGPSListener: BeConnectedMVP.BeConnectedModel.OnFinishGPSPermissionListener,accessToken: String
     *  @Return   : void
     * 	@Usage	  : set gps status to server
     *  @Author   : 1276
     */
    override fun processGPS(gpsState: Boolean, onBoardingState: Boolean, apiInterface: ApiInterface, onFinishedGPSListener: BeConnectedMVP.BeConnectedModel.OnFinishGPSPermissionListener,accessToken: String) {
        try{
            val tempObj=BonusPointRequestObject()
            tempObj.gps=gpsState
            tempObj.deviceIntegration=false
            tempObj.onboarding=onBoardingState
            val call=apiInterface.postBonusPoints(accessToken,tempObj)
            call.enqueue(object : Callback<BonusPointResponseObject> {
                override fun onResponse(call: Call<BonusPointResponseObject>, response: Response<BonusPointResponseObject>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedGPSListener.onGPSFinished(response.body()?.responseBody?.data!!)
                            }else{
                                onFinishedGPSListener.onGPSFailure("")
                            }
                        }
                        else {
                            // status failure
                            onFinishedGPSListener.onGPSFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        // status failure
                        onFinishedGPSListener.onGPSFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<BonusPointResponseObject>, t: Throwable) {
                    Crashlytics.logException(t)
                    onFinishedGPSListener.onGPSFailure("")
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            Crashlytics.logException(e)
            onFinishedGPSListener.onGPSFailure("")
            AppLogger.e(e.message!!)
        }

    }
}