/**
 * @Class : BackgroundApiCall
 * @Usage : This service is used to manage the background checkout api call
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.service

import android.app.IntentService
import android.content.Intent
import android.os.AsyncTask
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.home_section.checkin_section.model.CheckoutRequest
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger

class BackgroundApiCall: IntentService(BackgroundApiCall::class.simpleName) {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onHandleIntent(intent: Intent?) {
        if (intent!!.hasExtra(AppConstants.LATITUDE) && intent!!.hasExtra(AppConstants.LONGITUDE)) {
            latitude = intent.getDoubleExtra(AppConstants.LATITUDE, 0.0)
            longitude = intent.getDoubleExtra(AppConstants.LONGITUDE, 0.0)
            Checkout().execute()
        }
    }

    /**
     * @Class : Checkout
     * @Usage : To manage checkout api call
     * @Author : 1769
     */
    inner class Checkout : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: Void?): String {
            var mAppPreference: AppPreference = AppPreference(this@BackgroundApiCall)
            var checkoutRequest = CheckoutRequest(mAppPreference.checkinId, latitude, longitude)
            var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
            var isProcessSuccess="Something went wrong. Please try again later."
            try{
                val call=mApiInterfaceService.checkout(mAppPreference.authorizationToken,checkoutRequest)
                var response=call.execute()
                if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE)
                {
                    if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                        // status success
                        if(response.body()?.responseBody!=null){
                            mAppPreference.isCheckoutDone = true
                            isProcessSuccess="yes"
                        }else{
                            isProcessSuccess="Something went wrong. Please try again later."
                        }
                    }
                    else {
                        // status failure
                        isProcessSuccess=response.body()?.message.toString()
                    }
                }
                else {
                    // status failure
                    isProcessSuccess=response.body()?.message.toString()
                }

            }catch (e:Exception){
                isProcessSuccess="Something went wrong. Please try again later."
                AppLogger.e(e.message!!)
            }
            return isProcessSuccess
        }
        override fun onPostExecute(tempList:  String?) {
            AppLogger.d (tempList!!)
        }
    }
}