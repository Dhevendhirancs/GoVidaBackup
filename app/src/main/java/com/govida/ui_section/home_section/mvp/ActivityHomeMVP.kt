/**
 * @Class : ActivityHomeMVP
 * @Usage : This class is used for providing MVP functionality to Home Activity
 * @Author : 1276
 */
package com.govida.ui_section.home_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.ui_section.home_section.model.ChallengesEntity

class ActivityHomeMVP {
    /**
     * @Interface :  ActivityHomeView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1276
     */

    interface  ActivityHomeView: MvpView {
        fun onChallengeReceivedSuccessfully(receivedChallengesEntities: MutableList<ChallengesEntity>)

        fun onActivitySyncReceivedSuccessfully(receivedActivityEntities: ActivitySyncResponse.Data)

    }

    /**
     * @Interface : ActivityHomePresenterImplementer
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface ActivityHomePresenter{
        fun attachView(homeView:ActivityHomeView)
        fun destroyView()
        // usage: This is used to trigger api/db call in model for requesting challenge list functionality
        fun onChallengeListRequested(accessToken: String,appDatabase:AppDatabase,onDownloadListener: ActivityHomePresenterImplementer.InsertChallengeTask.OnDownloadListener)

        fun onActivitySyncPostRequested(accessToken: String,syncRequest: ActivitySyncRequest)
    }

    /**
     * @Interface : ActivityHomeModelImplementer
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface ActivityHomeModel{
        // usage : This is used for making api call with the server to requesting challenge list
        fun challengeListProcess(apiInterface: ApiInterface, onFinishedListener:OnChallengeFinishedListener, accessToken: String)
        fun activitySyncProcess(apiInterface: ApiInterface, onActivitySyncFinishedListener: ActivityHomeModel.OnActivitySyncFinishedListener, accessToken: String, syncRequest: ActivitySyncRequest)

        interface OnChallengeFinishedListener {
            fun onChallengeFinished(receivedChallengesEntities: MutableList<ChallengesEntity>)
            fun onChallengeFailure(warnings:String)
        }

        interface OnActivitySyncFinishedListener {
            fun onActivitySyncFinished(receivedActivityEntities: ActivitySyncResponse.Data)
            fun onActivitySyncFailure(warnings:String)
        }

    }
}