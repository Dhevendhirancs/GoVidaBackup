/**
 * @Class : LeaderboardPresenterImplementer
 * @Usage : This class is used for providing MVP functionality to Reward Page
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.mvp

import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse

class LeaderboardPresenterImplementer(mView: LeaderboardMVP.LeaderboardView):LeaderboardMVP.LeaderboardPresenter,LeaderboardMVP.LeaderboardModel.OnFinishedListener {
    private var mLeaderboardView: LeaderboardMVP.LeaderboardView? = mView
    private var mLeaderboardModel = LeaderboardModelImplementer()
    private var mApiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)

    /**
     *  @Function : attachView()
     *  @params   : lbView: LeaderboardMVP.LeaderboardView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1769
     */
    override fun attachView(lbView: LeaderboardMVP.LeaderboardView) {
        mLeaderboardView=lbView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1769
     */
    override fun destroyView() {
        mLeaderboardView=null
    }

    /**
     *  @Function : attachView()
     *  @params   : rewardView: RewardMVP.RewardView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1769
     */
    override fun onRequestLeaderboard(accessToken: String) {
        if(mLeaderboardView!=null){
            if(mLeaderboardView!!.isNetworkConnected){
                mLeaderboardView!!.showLoading()
                mLeaderboardModel.processLeaderboard(accessToken,mApiInterface,this)
            }
            else{
                mLeaderboardView!!.hideLoading()
                mLeaderboardView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    /**
     *  @Function : onFinished()
     *  @params   : result: LeaderboardResponse.Data?
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for leaderboard list received successfully
     *  @Author   : 1769
     */
    override fun onFinished(result: LeaderboardResponse.Data?) {
        if(mLeaderboardView!=null){
            mLeaderboardView!!.hideLoading()
            mLeaderboardView!!.onLeaderboardSuccessful(result)
        }
    }

    /**
     *  @Function : onFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : This is used to notify presenter for getting leaderboard list operation failed
     *  @Author   : 1769
     */
    override fun onFailure(warnings: String) {
        if(mLeaderboardView!=null){
            mLeaderboardView!!.hideLoading()
        }
    }
}