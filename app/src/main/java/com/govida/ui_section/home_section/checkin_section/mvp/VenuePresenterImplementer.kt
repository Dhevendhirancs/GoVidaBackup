/**
 * @Class : VenuePresenterImplementer
 * @Usage : This class is used for providing MVP functionality to Checkin and SelectVenue Activities
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse

class VenuePresenterImplementer(mView: VenueMVP.VenueView?, mCheckoutView: VenueMVP.CheckoutView?): VenueMVP.VenuePresenter, VenueMVP.CheckoutPresenter,
    VenueMVP.VenueModel.OnFinishedVenueListener,
    VenueMVP.VenueModel.OnFinishedPreferredVenueListener,
    VenueMVP.VenueModel.OnFinishedVerifiedVenueListener,
    VenueMVP.CheckoutModel.OnFinishedCheckoutListener{

    private var mVenueView: VenueMVP.VenueView? = mView
    private var mCheckoutView: VenueMVP.CheckoutView? = mCheckoutView
    private var mVenueModel: VenueMVP.VenueModel = VenueModelImplementer()
    private var mCheckoutModel: VenueMVP.CheckoutModel = VenueModelImplementer()
    private var mApiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)

    /**
     *  @Function : attachView()
     *  @params   : venueView: VenueMVP.VenueView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1769
     */
    override fun attachView(venueView: VenueMVP.VenueView) {
        mVenueView=venueView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1769
     */
    override fun destroyView() {
        mVenueView = null
    }

    /**
     *  @Function : onVenueRequested()
     *  @params   : accessToken: String, searchString: String, latitude: Double, longitude: Double
     *  @Return   : void
     * 	@Usage	  : precess when user requested venue list based on search string
     *  @Author   : 1769
     */
    override fun onVenueRequested(accessToken: String, searchString: String, latitude: Double, longitude: Double) {
        if (mVenueView != null) {
            if(mVenueView!!.isNetworkConnected){
                mVenueView!!.showLoading()
                mVenueModel.getVenueList(searchString, latitude, longitude, mApiInterface, this, accessToken)
            } else{
                mVenueView!!.hideLoading()
                mVenueView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onFinished()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for venue list based on search received successfully
     *  @Author   : 1769
     */
    override fun onFinished(venueList: List<VenueResponse.Data>?) {
        if (mVenueView != null) {
            mVenueView!!.hideLoading()
            mVenueView!!.onVenueReceivedSuccesfully(venueList)
        }
    }

    /**
     *  @Function : onFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for venue list based on search operation failed
     *  @Author   : 1769
     */
    override fun onFailure(warnings: String) {
        if (mVenueView != null) {
            mVenueView!!.hideLoading()
            mVenueView!!.onVenueReceivedFailed(warnings)
        }
    }

    /**
     *  @Function : onPreferredVenueRequested()
     *  @params   : accessToken: String
     *  @Return   : void
     * 	@Usage	  : precess when user requested preferred venue list
     *  @Author   : 1769
     */
    override fun onPreferredVenueRequested(accessToken: String) {
        if (mVenueView != null) {
            if(mVenueView!!.isNetworkConnected){
                mVenueView!!.showLoading()
                mVenueModel.getPreferredVenueList(mApiInterface, this, accessToken)
            } else{
                mVenueView!!.hideLoading()
                mVenueView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onPreferredVenueFinished()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for preferred venue list received successfully
     *  @Author   : 1769
     */
    override fun onPreferredVenueFinished(venueList: List<VenueResponse.Data>?) {
        if (mVenueView != null) {
            mVenueView!!.hideLoading()
            mVenueView!!.onPreferredVenueReceivedSuccesfully(venueList)
        }
    }

    /**
     *  @Function : onPreferredVenueFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for getting preferred venue list operation failed
     *  @Author   : 1769
     */
    override fun onPreferredVenueFailure(warnings: String) {
        if (mVenueView != null) {
            mVenueView!!.hideLoading()
            mVenueView!!.onPreferredVenueReceivedFailed(warnings)
        }
    }

    /**
     *  @Function : onVerifiedVenueRequested()
     *  @params   : accessToken: String, latitude: Double, longitude: Double
     *  @Return   : void
     * 	@Usage	  : precess when user requested verified venue list
     *  @Author   : 1769
     */
    override fun onVerifiedVenueRequested(accessToken: String, latitude: Double, longitude: Double) {
        if (mVenueView != null) {
            if(mVenueView!!.isNetworkConnected){
                mVenueView!!.showLoading()
                mVenueModel.getVerifiedVenueList(latitude, longitude, mApiInterface, this, accessToken)
            } else{
                mVenueView!!.hideLoading()
                mVenueView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onVerifiedVenueFinished()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for verified venue list received successfully
     *  @Author   : 1769
     */
    override fun onVerifiedVenueFinished(venueList: List<VenueResponse.Data>?) {
        if (mVenueView != null) {
            mVenueView!!.hideLoading()
            mVenueView!!.onVerifiedVenueReceivedSuccesfully(venueList)
        }
    }

    /**
     *  @Function : onVerifiedVenueFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for getting verified venue list operation failed
     *  @Author   : 1769
     */
    override fun onVerifiedVenueFailure(warnings: String) {
        if (mVenueView != null) {
            mVenueView!!.hideLoading()
            mVenueView!!.onVerifiedVenueReceivedFailed(warnings)
        }
    }

    /**
     *  @Function : attachCheckoutView()
     *  @params   : checkoutView: VenueMVP.CheckoutView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1769
     */
    override fun attachCheckoutView(checkoutView: VenueMVP.CheckoutView) {
        mCheckoutView = checkoutView
    }

    /**
     *  @Function : destroyCheckoutView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1769
     */
    override fun destroyCheckoutView() {
        mCheckoutView = null
    }

    /**
     *  @Function : onCheckoutRequested()
     *  @params   : accessToken: String, checkinId: Int, userLatitude: Double, userLongitude: Double
     *  @Return   : void
     * 	@Usage	  : precess when user requested for checkout the current session
     *  @Author   : 1769
     */
    override fun onCheckoutRequested(accessToken: String, checkinId: Int, userLatitude: Double, userLongitude: Double) {
        if (mCheckoutView != null) {
            if(mCheckoutView!!.isNetworkConnected){
                mCheckoutView!!.showLoading()
                mCheckoutModel.checkout(accessToken, checkinId, userLatitude, userLongitude, mApiInterface, this)
            } else{
                mCheckoutView!!.hideLoading()
                mCheckoutView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onCheckoutFinished()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for user checkout the current session successfully
     *  @Author   : 1769
     */
    override fun onCheckoutFinished() {
        if (mCheckoutView != null) {
            mCheckoutView!!.hideLoading()
            mCheckoutView!!.onCheckoutSuccessfully()
        }
    }

    /**
     *  @Function : onCheckoutFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for checkout operation failed
     *  @Author   : 1769
     */
    override fun onCheckoutFailed(warnings: String) {
        if (mCheckoutView != null) {
            mCheckoutView!!.hideLoading()
            mCheckoutView!!.onCheckoutFailed(warnings)
        }
    }
}