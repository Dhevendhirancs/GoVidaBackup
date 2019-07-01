/**
 * @Class : VenueMVP
 * @Usage : This class is used for providing MVP functionality to Checkin and SelectVenue Activities
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse

class VenueMVP {

    /**
     * @Interface : CheckoutView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1769
     */
    interface CheckoutView: MvpView {
        // usage : function will be used when user checkout the session successfully
        fun onCheckoutSuccessfully()
        // usage : function will be used when user checkout failed
        fun onCheckoutFailed(warnings: String)
    }

    /**
     * @Interface : CheckoutPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1769
     */
    interface CheckoutPresenter {
        fun attachCheckoutView(checkoutView: CheckoutView)
        fun destroyCheckoutView()
        // usage : initiate checkout request to the server
        fun onCheckoutRequested(accessToken: String, checkinId: Int, userLatitude: Double, userLongitude: Double)
    }

    /**
     * @Interface : CheckoutModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1769
     */
    interface CheckoutModel {
        // usage : update checkout status to the server
        fun checkout(accessToken: String, checkinId: Int, userLatitude: Double, userLongitude: Double, apiInterface: ApiInterface,
                     onFinishedCheckoutListener: OnFinishedCheckoutListener)
        // usage : provide listener for checkout process
        interface OnFinishedCheckoutListener {
            fun onCheckoutFinished()
            fun onCheckoutFailed(warnings: String)
        }
    }

    /**
     * @Interface : VenueView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1769
     */
    interface VenueView: MvpView {
        // usage : function will be used when user received venue list based on search string successfully
        fun onVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?)
        // usage : function will be used when user venue list based on search string api failed
        fun onVenueReceivedFailed(warnings: String)
        // usage : function will be used when user received preferred venue list successfully
        fun onPreferredVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?)
        // usage : function will be used when user preferred venue list api failed
        fun onPreferredVenueReceivedFailed(warnings: String)
        // usage : function will be used when user received verified venue list successfully
        fun onVerifiedVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?)
        // usage : function will be used when user verified venue list api failed
        fun onVerifiedVenueReceivedFailed(warnings: String)
    }

    /**
     * @Interface : VenuePresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1769
     */
    interface VenuePresenter{
        fun attachView(venueView:VenueView)
        fun destroyView()
        // usage : initiate venue list request based on search string to the server
        fun onVenueRequested(accessToken: String, searchString: String, latitude: Double, longitude: Double)
        // usage : initiate preferred venue list request to the server
        fun onPreferredVenueRequested(accessToken: String)
        // usage : initiate verified venue list request to the server
        fun onVerifiedVenueRequested(accessToken: String, latitude: Double, longitude: Double)
    }

    /**
     * @Interface : VenueModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1769
     */
    interface VenueModel{
        // usage : get venue list based on user search from the server
        fun getVenueList(searchString: String, latitude: Double, longitude: Double, apiInterface: ApiInterface, onFinishedVenueListener:OnFinishedVenueListener, accessToken: String)
        // usage : get preferred venue list from the server
        fun getPreferredVenueList(apiInterface: ApiInterface, onFinishedPreferredVenueListener:OnFinishedPreferredVenueListener, accessToken: String)
        // usage : get verified venue list from the server
        fun getVerifiedVenueList(latitude: Double, longitude: Double, apiInterface: ApiInterface, onFinishedVerifiedVenueListener:OnFinishedVerifiedVenueListener, accessToken: String)
        // usage : provide listener for get venue list based on user search process
        interface OnFinishedVenueListener {
            fun onFinished(venueList: List<VenueResponse.Data>?)
            fun onFailure(warnings:String)
        }
        // usage : provide listener for get preferred venue list process
        interface OnFinishedPreferredVenueListener {
            fun onPreferredVenueFinished(venueList: List<VenueResponse.Data>?)
            fun onPreferredVenueFailure(warnings:String)
        }
        // usage : provide listener for get verified venue list process
        interface OnFinishedVerifiedVenueListener {
            fun onVerifiedVenueFinished(venueList: List<VenueResponse.Data>?)
            fun onVerifiedVenueFailure(warnings:String)
        }
    }
}