package com.govida.ui_section.home_section.mvp

import android.os.AsyncTask
import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BasePresenter
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.ui_section.home_section.mvp.ActivityHomePresenterImplementer.InsertChallengeTask.OnDownloadListener


class ActivityHomePresenterImplementer(mHomeView: ActivityHomeMVP.ActivityHomeView): BasePresenter<MvpView>(),
    ActivityHomeMVP.ActivityHomePresenter,ActivityHomeMVP.ActivityHomeModel.OnChallengeFinishedListener,ActivityHomeMVP.ActivityHomeModel.OnActivitySyncFinishedListener
{
    var mActivityHomeView: ActivityHomeMVP.ActivityHomeView?=mHomeView
    private var mActivityHomeModel=ActivityHomeModelImplementer()
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
    private var mAppDbInstance: AppDatabase? = null
    private var mOnDownloadListener:OnDownloadListener? = null
    override fun attachView(homeView: ActivityHomeMVP.ActivityHomeView) {
        mActivityHomeView=homeView
    }

    override fun destroyView() {
        mActivityHomeView=null
    }

    override fun onChallengeListRequested(accessToken: String,appDatabase:AppDatabase,onDownloadListener:OnDownloadListener) {
      if(mActivityHomeView!=null){
          mActivityHomeView!!.showLoading()
          mAppDbInstance=appDatabase
          mOnDownloadListener=onDownloadListener
          mActivityHomeModel.challengeListProcess(mApiInterfaceService,this,accessToken)
      }
    }

    override fun onChallengeFinished(receivedChallengesEntities: MutableList<ChallengesEntity>) {
        if(mActivityHomeView!=null){
            InsertChallengeTask(receivedChallengesEntities, mAppDbInstance!!,mOnDownloadListener!!).execute()
            mActivityHomeView!!.onChallengeReceivedSuccessfully(receivedChallengesEntities)
        }
    }

    override fun onChallengeFailure(warnings: String) {
        if(mActivityHomeView!=null){
            mActivityHomeView!!.hideLoading()
            mActivityHomeView!!.onError(R.string.some_error)
        }
    }

    override fun onActivitySyncPostRequested(accessToken: String, syncRequest: ActivitySyncRequest) {
        if(mActivityHomeView!=null){
            mActivityHomeView!!.showLoading()
            mActivityHomeModel.activitySyncProcess(mApiInterfaceService,this,accessToken,syncRequest)
        }
    }

    class InsertChallengeTask(var challengeList: MutableList<ChallengesEntity>, var appDB: AppDatabase,var onDownloadListener:OnDownloadListener) : AsyncTask<Void, Void, Boolean>() {
        interface OnDownloadListener {
            fun onDownloadFinished()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            appDB.challengeDao().insertMultipleListRecord(challengeList)
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            onDownloadListener.onDownloadFinished()
        }
    }

    override fun onActivitySyncFinished(receivedActivityEntities: ActivitySyncResponse.Data) {
        if(mActivityHomeView!=null){
            mActivityHomeView!!.hideLoading()
            mActivityHomeView!!.onActivitySyncReceivedSuccessfully(receivedActivityEntities)
        }
    }

    override fun onActivitySyncFailure(warnings: String) {
        if(mActivityHomeView!=null){
            mActivityHomeView!!.hideLoading()
            mActivityHomeView!!.onError(R.string.some_error)
        }
    }

}