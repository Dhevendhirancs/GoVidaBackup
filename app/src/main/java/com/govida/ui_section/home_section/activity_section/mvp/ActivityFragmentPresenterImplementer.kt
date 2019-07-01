package com.govida.ui_section.home_section.activity_section.mvp

import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BasePresenter
import com.govida.ui_section.base_class_section.MvpView
import com.govida.ui_section.home_section.model.ActivitySyncRequest
import com.govida.ui_section.home_section.model.ActivitySyncResponse

class ActivityFragmentPresenterImplementer(mHomeView: ActivityFragmentMVP.ActivityFragmentView): BasePresenter<MvpView>(),ActivityFragmentMVP.ActivityFragmentPresenter,ActivityFragmentMVP.ActivityFragmentModel.OnActivitySyncFinishedListener {
    var mActivityHomeView: ActivityFragmentMVP.ActivityFragmentView?=mHomeView
    private var mActivityHomeModel=ActivityFragmentModelImplementer()
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
    private var mOnActivitySyncFinishedListener: ActivityFragmentMVP.ActivityFragmentModel.OnActivitySyncFinishedListener ? = null

    /**
     *  @Function : attachView()
     *  @params   : homeView: ActivityFragmentMVP.ActivityFragmentView
     *  @Return   : void
     * 	@Usage	  : attach presenter to activity
     *  @Author   : 1276
     */
    override fun attachView(homeView: ActivityFragmentMVP.ActivityFragmentView) {
        mActivityHomeView=homeView
    }

    /**
     *  @Function : destroyView()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : detach presenter to activity
     *  @Author   : 1276
     */
    override fun destroyView() {
        mActivityHomeView=null
    }

    /**
     *  @Function : onActivitySyncPostRequested()
     *  @params   : accessToken: String, onActivitySyncFinishedListener: ActivityFragmentMVP.ActivityFragmentModel.OnActivitySyncFinishedListener, syncRequest: ActivitySyncRequest
     *  @Return   : void
     * 	@Usage	  : process request for activity sync data
     *  @Author   : 1276
     */
    override fun onActivitySyncPostRequested(accessToken: String,syncRequest: ActivitySyncRequest) {
        if(mActivityHomeView!=null){
            //mActivityHomeView!!.showLoading()
            mActivityHomeModel.activitySyncProcess(mApiInterfaceService,this,accessToken,syncRequest)
        }
    }

    /**
     *  @Function : onActivitySyncFinished()
     *  @params   : receivedActivityEntities: ActivitySyncResponse.Data
     *  @Return   : void
     * 	@Usage	  : used when activity sync is done
     *  @Author   : 1276
     */
    override fun onActivitySyncFinished(receivedActivityEntities: ActivitySyncResponse) {
        if(mActivityHomeView!=null){
            //mActivityHomeView!!.hideLoading()
            mActivityHomeView!!.onActivitySyncReceivedSuccessfully(receivedActivityEntities)
        }
    }

    /**
     *  @Function : onActivitySyncFailure()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : used when activity sync failed
     *  @Author   : 1276
     */
    override fun onActivitySyncFailure(warnings: String) {
        if(mActivityHomeView!=null){
            mActivityHomeView!!.hideLoading()
            mActivityHomeView!!.onActivitySyncReceivedFailed(warnings)
        }
    }
}