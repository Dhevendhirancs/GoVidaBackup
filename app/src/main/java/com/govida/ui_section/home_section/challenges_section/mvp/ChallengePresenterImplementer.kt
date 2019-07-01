package com.govida.ui_section.home_section.challenges_section.mvp

import android.os.AsyncTask
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.model.ChallengesEntity

class ChallengePresenterImplementer( var mView: ChallengeMVP.ChallengeView): ChallengeMVP.ChallengePresenter, ChallengeMVP.ChallengeModel.OnChallengeFinishedListener {
    var mChallengeView: ChallengeMVP.ChallengeView? =mView
    private var mChallengeModel=ChallengeModelImplementer()
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
    private var mAppDbInstance: AppDatabase? = null
    var isAllChallengeButtonPressed:Boolean=true

    /**
     *  @Function : attachView()
     *  @params   : challengeView: ChallengeMVP.ChallengeView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1276
     */
    override fun attachView(challengeView: ChallengeMVP.ChallengeView) {
        mChallengeView=challengeView
    }
    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1276
     */
    override fun destroyView() {
        mChallengeView=null
    }

    /**
     *  @Function : onChallengeListRequested()
     *  @params   : accessToken: String, appDatabase: AppDatabase,isAllChallengeButton:Boolean
     *  @Return   : void
     * 	@Usage	  : request challenge list
     *  @Author   : 1276
     */
    override fun onChallengeListRequested(accessToken: String, appDatabase: AppDatabase,isAllChallengeButton:Boolean) {
        if(mChallengeView!=null){
            mChallengeView!!.showLoading()
            mAppDbInstance=appDatabase
            isAllChallengeButtonPressed=isAllChallengeButton
            if(isAllChallengeButtonPressed){
                mChallengeModel.challengeListProcessAll(mApiInterfaceService,this,accessToken,appDatabase)
            }
            else{
                mChallengeModel.challengeListProcessOngoing(mApiInterfaceService,this,accessToken,appDatabase)
            }
        }

    }

    /**
     *  @Function : onChallengeListRequestedPullToRefresh()
     *  @params   : accessToken: String, appDatabase: AppDatabase,isAllChallengeButton:Boolean
     *  @Return   : void
     * 	@Usage	  : request challenge list
     *  @Author   : 1276
     */
    override fun onChallengeListRequestedPullToRefresh(accessToken: String, appDatabase: AppDatabase,isAllChallengeButton:Boolean) {
        if(mChallengeView!=null){
            //mChallengeView!!.showLoading()
            mAppDbInstance=appDatabase
            isAllChallengeButtonPressed=isAllChallengeButton
            mChallengeModel.onChallengeListRequestedFromApi(mApiInterfaceService,this,accessToken,appDatabase,isAllChallengeButtonPressed)
        }

    }

    /**
     *  @Function : onChallengeFinished()
     *  @params   : receivedChallengesEntities: MutableList<ChallengesEntity>
     *  @Return   : void
     * 	@Usage	  : received challenge list from model
     *  @Author   : 1276
     */
    override fun onChallengeFinished(receivedChallengesEntities: MutableList<ChallengesEntity>) {
        if(mChallengeView!=null){
            mChallengeView!!.hideLoading()
            mChallengeView!!.onChallengeReceivedSuccessfully(receivedChallengesEntities)
        }

    }

    /**
     *  @Function : onChallengeFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : challenge list not received from model
     *  @Author   : 1276
     */
    override fun onChallengeFailure(warnings: String) {
        if(mChallengeView!=null){
            mChallengeView!!.hideLoading()
            mChallengeView!!.onChallengeReceivedFailed()
        }
    }

}