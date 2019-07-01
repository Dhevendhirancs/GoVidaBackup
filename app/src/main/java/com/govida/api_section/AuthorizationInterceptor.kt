/**
 * @Class :  AuthorizationInterceptor
 * @Usage : This class is used for intercepting response if any authorization issue occurred
 * @Author : 1276
 */

package com.govida.api_section

import com.govida.app_sharedpreference.AppPreference
import com.govida.application_section.GoVidaApplication
import com.govida.ui_section.login_section.models.TokenResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import java.io.IOException
import java.util.HashMap

class AuthorizationInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var mainResponse = chain.proceed(chain.request())
        val mainRequest = chain.request()

        // if response code is 401 or 403, 'mainRequest' has encountered authentication error
        if (mainResponse.code() == 401 || mainResponse.code() == 403) {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "Basic bXktY2xpZW50Om15LXNlY3JldA=="
            // request to login API to get fresh token synchronously calling login API
            var appPreference=AppPreference(GoVidaApplication.mContext)
            val refreshResponse = ApiClient().getClient().create(ApiInterface::class.java).getAccessTokenFromRefresh(headers, "refresh_token", ""+appPreference.refreshToken).execute()

            if (refreshResponse.isSuccessful) {
                // login request succeed, new token generated
                val authorization = refreshResponse.body()
                // save the new token
                appPreference.authorizationToken = authorization!!.accessToken
                appPreference.refreshToken = authorization!!.refreshToken
                // retry the 'mainRequest' which encountered an authentication error
                // add new token into 'mainRequest' header and request again
                val builder = mainRequest.newBuilder().header("Authorization", authorization!!.accessToken).method(mainRequest.method(), mainRequest.body())
                mainResponse = chain.proceed(builder.build())
            }
        }
        return mainResponse
    }
}
