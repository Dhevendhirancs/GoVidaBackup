/**
 * @Class : ProfileEditModelImplementer
 * @Usage : This class is used for providing MVP functionality to Edit Profile Page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section.mvp

import com.crashlytics.android.Crashlytics
import com.govida.R
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditRequest
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditResponse
import com.govida.ui_section.home_section.rewards_section.models.AllRewardResponse
import com.govida.utility_section.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEditModelImplementer: ProfileEditMVP.ProfileEditModel {
    /**
     *  @Function : editProfile()
     *  @params   : apiInterface: ApiInterface, onFinishedProfileEditListener: ProfileEditMVP.ProfileEditModel.onFinishedProfileEditListener, accessToken: String,
                    userProfileEditRequest: UserProfileEditRequest
     *  @Return   : void
     * 	@Usage	  : Send the updated profile data to the server
     *  @Author   : 1769
     */
    override fun editProfile(
        apiInterface: ApiInterface,
        onFinishedProfileEditListener: ProfileEditMVP.ProfileEditModel.onFinishedProfileEditListener,
        accessToken: String,
        userProfileEditRequest: UserProfileEditRequest) {
        try {
            var call = apiInterface.editProfile(accessToken, userProfileEditRequest)
            call.enqueue(object: Callback<UserProfileEditResponse> {

                override fun onResponse(call: Call<UserProfileEditResponse>, response: Response<UserProfileEditResponse>) {
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()!!.status.equals(AppConstants.STATUS_CODE_SUCCESS) && response.body()!!.responseBody != null && response.body()!!.responseBody!!.data != null) {
                            onFinishedProfileEditListener.onProfileEditFinished(response.body()!!.responseBody!!.data)
                        } else {
                            onFinishedProfileEditListener.onProfileEditFailure("")
                        }
                    } else {
                        onFinishedProfileEditListener.onProfileEditFailure("")
                    }
                }

                override fun onFailure(call: Call<UserProfileEditResponse>, t: Throwable) {
                    onFinishedProfileEditListener.onProfileEditFailure(t.message!!)
                    Crashlytics.logException(t)
                }

            })
        } catch (e: Exception) {
            onFinishedProfileEditListener.onProfileEditFailure("")
            Crashlytics.logException(e)
        }
    }

}