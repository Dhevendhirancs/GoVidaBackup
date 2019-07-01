/**
 * @Class : VenueModelImplementer
 * @Usage : This class is used for providing MVP functionality to ActivityCheckin and ActivitySelectVenue activities
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.checkin_section.model.CheckinResponse
import com.govida.ui_section.home_section.checkin_section.model.CheckoutRequest
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse
import com.govida.utility_section.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VenueModelImplementer: VenueMVP.VenueModel, VenueMVP.CheckoutModel {

    /**
     *  @Function : getVenueList()
     *  @params   : searchString: String, latitude: Double, longitude: Double, apiInterface: ApiInterface,
                    onFinishedVenueListener: VenueMVP.VenueModel.OnFinishedVenueListener, accessToken: String
     *  @Return   : void
     * 	@Usage	  : get venue list based on user search from server
     *  @Author   : 1769
     */
    override fun getVenueList(searchString: String, latitude: Double, longitude: Double, apiInterface: ApiInterface,
                              onFinishedVenueListener: VenueMVP.VenueModel.OnFinishedVenueListener, accessToken: String) {
        try {
            val call = apiInterface.getVenues(accessToken, searchString, latitude.toString(), longitude.toString())
            call.enqueue(object: Callback<VenueResponse> {
                override fun onResponse(call: Call<VenueResponse>, response: Response<VenueResponse>) {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedVenueListener.onFinished(response.body()!!.responseBody!!.data)
                            }else{
                                onFinishedVenueListener.onFailure("")
                            }
                        }
                        else {
                            onFinishedVenueListener.onFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        onFinishedVenueListener.onFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<VenueResponse>, t: Throwable) {
                    onFinishedVenueListener.onFailure(t.message!!)
                    Crashlytics.logException(t)
                }
            })
        } catch (e: Exception) {
            onFinishedVenueListener.onFailure(e.message!!)
            Crashlytics.logException(e)
        }
    }

    /**
     *  @Function : getPreferredVenueList()
     *  @params   : apiInterface: ApiInterface, onFinishedPreferredVenueListener: VenueMVP.VenueModel.OnFinishedPreferredVenueListener,
                    accessToken: String
     *  @Return   : void
     * 	@Usage	  : get preferred venue list from server
     *  @Author   : 1769
     */
    override fun getPreferredVenueList( apiInterface: ApiInterface, onFinishedPreferredVenueListener: VenueMVP.VenueModel.OnFinishedPreferredVenueListener,
                                        accessToken: String ) {
        try {
            val call = apiInterface.getpreferredVenues(accessToken)
            call.enqueue(object: Callback<VenueResponse> {
                override fun onResponse(call: Call<VenueResponse>, response: Response<VenueResponse>) {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedPreferredVenueListener.onPreferredVenueFinished(response.body()!!.responseBody!!.data)
                            }else{
                                onFinishedPreferredVenueListener.onPreferredVenueFailure("")
                            }
                        }
                        else {
                            onFinishedPreferredVenueListener.onPreferredVenueFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        onFinishedPreferredVenueListener.onPreferredVenueFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<VenueResponse>, t: Throwable) {
                    onFinishedPreferredVenueListener.onPreferredVenueFailure(t.message!!)
                    Crashlytics.logException(t)
                }
            })
        } catch (e: Exception) {
            onFinishedPreferredVenueListener.onPreferredVenueFailure(e.message!!)
            Crashlytics.logException(e)
        }
    }

    /**
     *  @Function : getVerifiedVenueList()
     *  @params   : apiInterface: ApiInterface, onFinishedPreferredVenueListener: VenueMVP.VenueModel.OnFinishedPreferredVenueListener,
                    accessToken: String
     *  @Return   : void
     * 	@Usage	  : get verified venue list from server
     *  @Author   : 1769
     */
    override fun getVerifiedVenueList( latitude: Double, longitude: Double,apiInterface: ApiInterface,
        onFinishedVerifiedVenueListener: VenueMVP.VenueModel.OnFinishedVerifiedVenueListener,accessToken: String ) {
        try {
            val call = apiInterface.getVerifiedVenues(accessToken, latitude.toString(), longitude.toString())
            call.enqueue(object: Callback<VenueResponse> {
                override fun onResponse(call: Call<VenueResponse>, response: Response<VenueResponse>) {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedVerifiedVenueListener.onVerifiedVenueFinished(response.body()!!.responseBody!!.data)
                            }else{
                                onFinishedVerifiedVenueListener.onVerifiedVenueFailure("")
                            }
                        }
                        else {
                            onFinishedVerifiedVenueListener.onVerifiedVenueFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        onFinishedVerifiedVenueListener.onVerifiedVenueFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<VenueResponse>, t: Throwable) {
                    onFinishedVerifiedVenueListener.onVerifiedVenueFailure(t.message!!)
                    Crashlytics.logException(t)
                }
            })
        } catch (e: Exception) {
            onFinishedVerifiedVenueListener.onVerifiedVenueFailure(e.message!!)
            Crashlytics.logException(e)
        }
    }

    /**
     *  @Function : checkout()
     *  @params   : accessToken: String, checkinId: Int, userLatitude: Double, userLongitude: Double, apiInterface: ApiInterface,
                    onFinishedCheckoutListener: VenueMVP.CheckoutModel.OnFinishedCheckoutListener
     *  @Return   : void
     * 	@Usage	  : update checkout status to server
     *  @Author   : 1769
     */
    override fun checkout( accessToken: String, checkinId: Int, userLatitude: Double, userLongitude: Double, apiInterface: ApiInterface,
        onFinishedCheckoutListener: VenueMVP.CheckoutModel.OnFinishedCheckoutListener ) {
        try {
            var checkoutRequest = CheckoutRequest(checkinId, userLatitude, userLongitude)
            val call = apiInterface.checkout(accessToken, checkoutRequest)
            call.enqueue(object: Callback<CheckinResponse> {
                override fun onResponse(call: Call<CheckinResponse>, response: Response<CheckinResponse>) {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                onFinishedCheckoutListener.onCheckoutFinished()
                            }else{
                                onFinishedCheckoutListener.onCheckoutFailed("")
                            }
                        }
                        else {
                            onFinishedCheckoutListener.onCheckoutFailed(response.body()?.message.toString())
                        }
                    }
                    else {
                        onFinishedCheckoutListener.onCheckoutFailed(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<CheckinResponse>, t: Throwable) {
                    onFinishedCheckoutListener.onCheckoutFailed(t.message!!)
                    Crashlytics.logException(t)
                }
            })
        } catch (e: Exception) {
            onFinishedCheckoutListener.onCheckoutFailed(e.message!!)
            Crashlytics.logException(e)
        }
    }

}