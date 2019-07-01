package com.govida.ui_section.home_section.challenges_section.mvp

import android.os.AsyncTask
import android.view.View
import com.crashlytics.android.Crashlytics
import com.govida.R
import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.challenges_section.FragmentChallenges
import com.govida.ui_section.home_section.model.ChallengeResponse
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeModelImplementer: ChallengeMVP.ChallengeModel {

    override fun onChallengeListRequestedFromApi(apiInterface: ApiInterface, onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase,isAllChallengeButtonPressed:Boolean) {
        try{
            val call=apiInterface.getChallengeList(accessToken)
            call.enqueue(object : Callback<ChallengeResponse> {
                override fun onResponse(call: Call<ChallengeResponse>, response: Response<ChallengeResponse>)
                {
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE){
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                //onFinishedListener.onChallengeFinished(response.body()?.responseBody?.data!!)
                                StoreChallenge(appDatabase,onFinishedListener,response.body()?.responseBody?.data!!,isAllChallengeButtonPressed).execute()
                            }else{
                                onFinishedListener.onChallengeFailure("")
                            }
                        }
                        else {
                            // status failure
                            onFinishedListener.onChallengeFailure(response.body()?.message.toString())
                        }
                    }
                    else {
                        // status failure
                        onFinishedListener.onChallengeFailure(response.body()?.message.toString())
                    }
                }
                override fun onFailure(call: Call<ChallengeResponse>, t: Throwable) {
                    Crashlytics.logException(t)
                    onFinishedListener.onChallengeFailure("")
                    AppLogger.e(t.message!!)
                }
            })

        }catch (e:Exception){
            Crashlytics.logException(e)
            onFinishedListener.onChallengeFailure("")
            AppLogger.e(e.message!!)
        }
    }

    /**
     *  @Function : challengeListProcessOngoing()
     *  @params   : apiInterface: ApiInterface, onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase
     *  @Return   : void
     * 	@Usage	  : trigger database call for getting data from database
     *  @Author   : 1276
     */
    override fun challengeListProcessOngoing(apiInterface: ApiInterface, onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase) {
        OngoingChallenges(appDatabase,onFinishedListener).execute()
    }

    /**
     *  @Function : challengeListProcessAll()
     *  @params   : apiInterface: ApiInterface, onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase
     *  @Return   : void
     * 	@Usage	  : trigger database call for getting data from database
     *  @Author   : 1276
     */
    override fun challengeListProcessAll(apiInterface: ApiInterface, onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener, accessToken: String, appDatabase: AppDatabase) {
        AllChallenges(appDatabase,onFinishedListener).execute()
    }

    private class OngoingChallenges(var appDatabase: AppDatabase,var onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener) : AsyncTask<Void, Void, MutableList<ChallengesEntity>>() {

        override fun doInBackground(vararg params: Void?): MutableList<ChallengesEntity> {
            var tempList: MutableList<ChallengesEntity> = mutableListOf()

            if(appDatabase?.challengeDao()?.allChallengeCount!! >0)
            {
                tempList=appDatabase?.challengeDao()?.ongoingChallenges!!
                return tempList
            }
            return tempList
        }

        override fun onPostExecute(tempList:  MutableList<ChallengesEntity> ?) {
            if (tempList!!.size != 0) {
                onFinishedListener.onChallengeFinished(tempList)
            } else {
                onFinishedListener.onChallengeFailure("")
            }
        }
    }

    private class AllChallenges(var appDatabase: AppDatabase,var onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener) : AsyncTask<Void, Void, MutableList<ChallengesEntity>>() {

        override fun doInBackground(vararg params: Void?): MutableList<ChallengesEntity> {
            var tempList: MutableList<ChallengesEntity> = mutableListOf()

            if(appDatabase?.challengeDao()?.allChallengeCount!! >0)
            {
                tempList=appDatabase?.challengeDao()?.onNewChallenges!!
                return tempList
            }
            return tempList
        }

        override fun onPostExecute(tempList:  MutableList<ChallengesEntity> ?) {
            if (tempList!!.size != 0) {
                onFinishedListener.onChallengeFinished(tempList)
            } else {
                onFinishedListener.onChallengeFailure("")
            }
        }
    }

    private class StoreChallenge(var appDatabase: AppDatabase,var onFinishedListener: ChallengeMVP.ChallengeModel.OnChallengeFinishedListener,var challengeList:MutableList<ChallengesEntity>,var isAllChallengeButtonPressed:Boolean) : AsyncTask<Void, Void, MutableList<ChallengesEntity>>() {

        override fun doInBackground(vararg params: Void?): MutableList<ChallengesEntity> {
            var tempList: MutableList<ChallengesEntity> = mutableListOf()
            appDatabase?.challengeDao()?.insertMultipleListRecord(challengeList)
            if(appDatabase?.challengeDao()?.allChallengeCount!! >0)
            {
                if(isAllChallengeButtonPressed){
                    tempList=appDatabase?.challengeDao()?.onNewChallenges!!
                    return tempList
                }else{
                    tempList=appDatabase?.challengeDao()?.ongoingChallenges!!
                    return tempList
                }
            }
            return tempList
        }

        override fun onPostExecute(tempList:  MutableList<ChallengesEntity> ?) {
            if (tempList!!.size != 0) {
                onFinishedListener.onChallengeFinished(tempList)
            } else {
                onFinishedListener.onChallengeFailure("")
            }
        }
    }

}