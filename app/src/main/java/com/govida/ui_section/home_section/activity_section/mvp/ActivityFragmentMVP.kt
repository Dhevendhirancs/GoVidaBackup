package com.govida.ui_section.home_section.activity_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse

class ActivityFragmentMVP {
    /**
     * @Interface :  ActivityFragmentView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1276
     */
    interface  ActivityFragmentView: MvpView

    {
        fun onActivitySyncReceivedSuccessfully(receivedChallengesEntities:ActivitySyncResponse)
        fun onActivitySyncReceivedFailed(warning:String)
    }

    /**
     * @Interface : ActivityFragmentPresenter
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface ActivityFragmentPresenter{
        fun attachView(homeView:ActivityFragmentView)
        fun destroyView()

        fun onActivitySyncPostRequested(accessToken: String,syncRequest: ActivitySyncRequest)
    }

    /**
     * @Interface : ActivityFragmentModel
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface ActivityFragmentModel{
        // usage : This is used for making api call with the server to requesting challenge list
        fun activitySyncProcess(apiInterface: ApiInterface, onActivitySyncFinishedListener: ActivityFragmentModel.OnActivitySyncFinishedListener, accessToken: String, syncRequest: ActivitySyncRequest)

        interface OnActivitySyncFinishedListener {
            fun onActivitySyncFinished(receivedActivityEntities: ActivitySyncResponse)
            fun onActivitySyncFailure(warnings:String)
        }

    }
}
