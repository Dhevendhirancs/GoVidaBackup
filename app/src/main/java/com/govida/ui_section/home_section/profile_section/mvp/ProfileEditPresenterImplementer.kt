/**
 * @Class : ProfileEditPresenterImplementer
 * @Usage : This class is used for providing MVP functionality to Edit Profile Page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditRequest
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditResponse

class ProfileEditPresenterImplementer (mView: ProfileEditMVP.ProfileEditView):
    ProfileEditMVP.ProfileEditPresenter,
    ProfileEditMVP.ProfileEditModel.onFinishedProfileEditListener{

    private var mProfileEditView: ProfileEditMVP.ProfileEditView? = mView
    private var mProfileEditModel = ProfileEditModelImplementer()
    private var mApiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)

    /**
     *  @Function : attachView()
     *  @params   : profileEditView: ProfileEditMVP.ProfileEditView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1769
     */
    override fun attachView(profileEditView: ProfileEditMVP.ProfileEditView) {
        mProfileEditView = profileEditView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1769
     */
    override fun destroyView() {
        mProfileEditView = null
    }

    /**
     *  @Function : onProfileEditRequested()
     *  @params   : accessToken: String, userProfileEditRequest: UserProfileEditRequest
     *  @Return   : void
     * 	@Usage	  : precess when user requested to update profile
     *  @Author   : 1769
     */
    override fun onProfileEditRequested(accessToken: String, userProfileEditRequest: UserProfileEditRequest) {
        if (mProfileEditView != null) {
            if(mProfileEditView!!.isNetworkConnected){
                mProfileEditView!!.showLoading()
                mProfileEditModel.editProfile(mApiInterface, this, accessToken, userProfileEditRequest)
            }
            else{
                mProfileEditView!!.hideLoading()
                mProfileEditView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onProfileEditFinished()
     *  @params   : userDetails: UserProfileEditResponse.Data?
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for profile updated succussfully on server
     *  @Author   : 1769
     */
    override fun onProfileEditFinished(userDetails: UserProfileEditResponse.Data?) {
        if (mProfileEditView != null) {
            mProfileEditView!!.hideLoading()
            mProfileEditView!!.onEditProfileSuccessfully(userDetails!!)
        }
    }

    /**
     *  @Function : onProfileEditFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for profile edit api failed
     *  @Author   : 1769
     */
    override fun onProfileEditFailure(warnings: String) {
        if (mProfileEditView != null) {
            mProfileEditView!!.hideLoading()
            mProfileEditView!!.onEditProfileFailed(warnings)
        }
    }

}