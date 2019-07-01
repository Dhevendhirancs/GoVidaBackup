/**
 * @Class : BeConnectedPresenterImplementer
 * @Usage : This class is used for providing MVP functionality to BeConnected Activity
 * @Author : 1276
 */
package com.govida.ui_section.be_connected.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.BasePresenter
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.be_connected.model.BonusPointResponseObject.Data


class BeConnectedPresenterImplementer(mBeConnectedView:BeConnectedMVP.BeConnectedView): BasePresenter<MvpView>(),BeConnectedMVP.BeConnectedPresenter,
        BeConnectedMVP.BeConnectedModel.OnFinishGoogleFitPermissionListener,BeConnectedMVP.BeConnectedModel.OnFinishGPSPermissionListener{
    private var mBeConnectedView:BeConnectedMVP.BeConnectedView?=mBeConnectedView
    private var mBeConnectedModel=BeConnectedModelImplementer()
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)


    /**
     *  @Function : attachView()
     *  @params   : beConnectedView: BeConnectedMVP.BeConnectedView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1276
     */
    override fun attachView(beConnectedView: BeConnectedMVP.BeConnectedView) {
        mBeConnectedView=beConnectedView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1276
     */
    override fun destroyView() {
        mBeConnectedView=null
    }


    /**
     *  @Function : onGoogleFitBonusPointClicked()
     *  @params   : googleFitState: Boolean, onBoardingState: Boolean,accessToken: String
     *  @Return   : void
     * 	@Usage	  : process when google fit is clicked
     *  @Author   : 1276
     */
     override fun onGoogleFitBonusPointClicked(googleFitState: Boolean, onBoardingState: Boolean,accessToken: String) {
        mBeConnectedView!!.hideKeyboard()
        if(mBeConnectedView!!.isNetworkConnected){
            mBeConnectedView!!.showLoading()
            mBeConnectedModel.processGoogleFit(googleFitState,onBoardingState,mApiInterfaceService,this,accessToken)
        }
        else{
            mBeConnectedView!!.hideLoading()
            mBeConnectedView!!.onError(R.string.not_connected_to_internet)
        }
    }

    /**
     *  @Function : onGPSBonusPointClicked()
     *  @params   : gpsState: Boolean, onBoardingState: Boolean,accessToken: String
     *  @Return   : void
     * 	@Usage	  : process when gps is clicked
     *  @Author   : 1276
     */
    override fun onGPSBonusPointClicked(gpsState: Boolean, onBoardingState: Boolean,accessToken: String) {
        mBeConnectedView!!.hideKeyboard()
        if(mBeConnectedView!!.isNetworkConnected){
            mBeConnectedView!!.showLoading()
            mBeConnectedModel.processGPS(gpsState,onBoardingState,mApiInterfaceService,this,accessToken)
        }
        else{
            mBeConnectedView!!.hideLoading()
            mBeConnectedView!!.onError(R.string.not_connected_to_internet)
        }
    }

    /**
     *  @Function : onFinished()
     *  @params   : result: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for operation completion successfully
     *  @Author   : 1276
     */
    override fun onFinished(receivedData: Data) {
        if(mBeConnectedView!=null) {
            mBeConnectedView!!.hideLoading()
            mBeConnectedView!!.onGoogleFitBonusPointPostSuccessful(receivedData)
        }
    }

    /**
     *  @Function : onFailure()
     *  @params   : result: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for operation failed
     *  @Author   : 1276
     */
    override fun onFailure(warnings: String) {
        mBeConnectedView!!.hideLoading()
        if(warnings == ""){
            mBeConnectedView!!.onError(R.string.some_error)
        }else{
            mBeConnectedView!!.onError(warnings)
        }
    }

    /**
     *  @Function : onGPSFinished()
     *  @params   : receivedData: Data
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for  gps operation completion successfully
     *  @Author   : 1276
     */
    override fun onGPSFinished(receivedData: Data) {
        if(mBeConnectedView!=null) {
            mBeConnectedView!!.hideLoading()
            mBeConnectedView!!.onGpsPointsPostSuccessful(receivedData)
        }
    }

    /**
     *  @Function : onGPSFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for operation completion failed
     *  @Author   : 1276
     */
    override fun onGPSFailure(warnings: String) {
        mBeConnectedView!!.hideLoading()
        if(warnings == ""){
            mBeConnectedView!!.onError(R.string.some_error)
        }else{
            mBeConnectedView!!.onError(warnings)
        }
    }

}