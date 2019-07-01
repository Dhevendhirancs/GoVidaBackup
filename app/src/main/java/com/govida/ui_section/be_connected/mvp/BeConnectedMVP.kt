/**
 * @Class : BeConnectedMVP
 * @Usage : This class is used for providing MVP functionality to BeConnected Activity
 * @Author : 1276
 */
package com.govida.ui_section.be_connected.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.be_connected.model.BonusPointResponseObject.Data


class BeConnectedMVP{
    /**
     * @Interface : BeConnectedView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1276
     */

    interface BeConnectedView: MvpView {
        // usage : function will be used when google fit bonus points is successfully posted
        fun onGoogleFitBonusPointPostSuccessful(receivedData: Data)
        // usage : function will be used when google fit bonus points is failed
        fun onGooglefitBonusPointPostFailed()
        // usage : function will be used when gps bonus points is successfully posted
        fun onGpsPointsPostSuccessful(receivedData: Data)
        // usage : function will be used when gps bonus points is failed
        fun onGPSPointsPostFailed()
    }

    /**
     * @Interface : BeConnectedPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface BeConnectedPresenter{
        fun attachView(beConnectedView:BeConnectedView)
        fun destroyView()
        // usage : initiate gps,google fit and onboarding status with server
        fun onGoogleFitBonusPointClicked(googleFitState:Boolean,onBoardingState:Boolean,accessToken: String)
        fun onGPSBonusPointClicked(gpsState:Boolean,onBoardingState:Boolean,accessToken: String)
    }

    /**
     * @Interface : BeConnectedModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface BeConnectedModel{
        // usage : update gps,google fit and onboarding status on server
        fun processGoogleFit(googleFitState:Boolean,onBoardingState:Boolean,apiInterface: ApiInterface,onFinishedGoogleFitListener:OnFinishGoogleFitPermissionListener,accessToken: String)
        fun processGPS(gpsState:Boolean,onBoardingState:Boolean,apiInterface: ApiInterface,onFinishedGPSListener:OnFinishGPSPermissionListener,accessToken: String)

        // usage : provide listener for Google fit process
        interface OnFinishGoogleFitPermissionListener {
            fun onFinished(receivedData: Data)
            fun onFailure(warnings:String)
        }
        // usage : provide listener for GPS process
        interface OnFinishGPSPermissionListener {
            fun onGPSFinished(receivedData: Data)
            fun onGPSFailure(warnings:String)
        }
    }
}