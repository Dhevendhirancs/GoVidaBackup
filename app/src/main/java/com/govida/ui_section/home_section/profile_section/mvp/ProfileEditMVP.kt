/**
 * @Class : ProfileEditMVP
 * @Usage : This class is used for providing MVP functionality to Edit Profile Activity
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditRequest
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditResponse

class ProfileEditMVP {

    /**
     * @Interface : ProfileEditView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1769
     */
    interface ProfileEditView : MvpView {
        // usage : function will be used when user edited the profile successfully
        fun onEditProfileSuccessfully(userDetails:UserProfileEditResponse.Data)
        // usage : function will be used when edit profile api fails
        fun onEditProfileFailed(warnings: String)
    }

    /**
     * @Interface : ProfileEditPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1769
     */
    interface ProfileEditPresenter {
        fun attachView(profileEditView: ProfileEditView)
        fun destroyView()
        // usage : initiate profile edit request to the server
        fun onProfileEditRequested(accessToken: String, userProfileEditRequest: UserProfileEditRequest)
    }

    /**
     * @Interface : ProfileEditModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1769
     */
    interface ProfileEditModel {
        // usage : update edited profile data to the server
        fun editProfile( apiInterface: ApiInterface, onFinishedProfileEditListener: onFinishedProfileEditListener, accessToken: String,
            userProfileEditRequest: UserProfileEditRequest )
        // usage : provide listener for profile edit process
        interface onFinishedProfileEditListener {
            fun onProfileEditFinished(appRewards: UserProfileEditResponse.Data?)
            fun onProfileEditFailure(warnings: String)
        }
    }
}