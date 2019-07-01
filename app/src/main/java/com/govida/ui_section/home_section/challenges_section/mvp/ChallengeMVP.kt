package com.govida.ui_section.home_section.challenges_section.mvp

import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.model.ChallengesEntity

class ChallengeMVP
{
    /**
     * @Interface :  ActivityHomeView
     * @Usage : This interface is used for managing UI related functionality
     * @Author : 1276
     */

    interface  ChallengeView: MvpView {
        // usage : once email link shared with client we use this function to define next functionality
        fun onChallengeReceivedSuccessfully(receivedChallengesEntities: MutableList<ChallengesEntity>)
        fun onChallengeReceivedFailed()
    }


    /**
     * @Interface : ActivityHomePresenterImplementer
     * @Usage : This interface is used for managing communication between model and view
     * @Author : 1276
     */
    interface ChallengePresenter{
        fun attachView(challengeView:ChallengeView)
        fun destroyView()
        // usage: This is used to trigger api/db call in model for requesting challenge list functionality
        fun onChallengeListRequested(accessToken: String, appDatabase: AppDatabase,isButtonPressed:Boolean)
        fun onChallengeListRequestedPullToRefresh(accessToken: String, appDatabase: AppDatabase,isButtonPressed:Boolean)
    }

    /**
     * @Interface : ActivityHomeModelImplementer
     * @Usage : This interface is used for managing data for Activity either from api or db
     * @Author : 1276
     */
    interface ChallengeModel{
        // usage : This is used for making api call with the server to requesting challenge list
        fun challengeListProcessOngoing(apiInterface: ApiInterface, onFinishedListener:OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase)
        fun challengeListProcessAll(apiInterface: ApiInterface, onFinishedListener:OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase)
        fun onChallengeListRequestedFromApi(apiInterface: ApiInterface, onFinishedListener:OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase,isAllChallengeButtonPressed:Boolean)
        interface OnChallengeFinishedListener {
            fun onChallengeFinished(receivedChallengesEntities: MutableList<ChallengesEntity>)
            fun onChallengeFailure(warnings:String)
        }

    }
}
