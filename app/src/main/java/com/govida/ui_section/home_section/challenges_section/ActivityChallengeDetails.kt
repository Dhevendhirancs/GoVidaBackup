package com.govida.ui_section.home_section.challenges_section

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.home_section.challenges_section.models.ChallengeAcceptRequest
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import com.govida.utility_section.CommonUtils

class ActivityChallengeDetails : BaseActivity(),View.OnClickListener {
    lateinit var mChallengeWebview:WebView
    private lateinit var mrlParent:RelativeLayout
    private lateinit var mIvChallengeBg:ImageView

    private var mAppDb: AppDatabase? = null
    var mChallengeId=0
    lateinit var tvDuration:TextView
    lateinit var tvCondition:TextView
    lateinit var tvPoints:TextView
    lateinit var tvProgressInWord:TextView
    lateinit var pgProgress: ProgressBar
    private lateinit var btnAccept: Button
    lateinit var rlProgress: RelativeLayout
    lateinit var rlAccept: RelativeLayout
    lateinit var mDetailsTvDuration:TextView
    lateinit var mTvPoints:TextView
    lateinit var mTvCompletion:TextView
    lateinit var mTvComplete:TextView
    lateinit var mBtmAccept:Button
    var mAlertTitle:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_details)
        setupUI()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        mAppDb= AppDatabase.getDatabase(this)
        mChallengeWebview=findViewById(R.id.challenge_details_webview)
        mrlParent=findViewById(R.id.rl_challenge_parent)
        mIvChallengeBg=findViewById(R.id.iv_challenge_bg)
        tvDuration=findViewById(R.id.challenge_tv_duration)
        tvCondition=findViewById(R.id.challenge_tv_condition)
        tvPoints=findViewById(R.id.challenge_tv_points)
        tvProgressInWord=findViewById(R.id.challenge_tv_progress_word)
        pgProgress=findViewById(R.id.challenge_pg_progress)
        btnAccept=findViewById(R.id.layout_btn_accept)
        rlProgress=findViewById(R.id.layout_rl_progress)
        rlAccept=findViewById(R.id.layout_rl_accpet)
        mDetailsTvDuration=findViewById(R.id.chl_details_tv_duration)
        mTvPoints=findViewById(R.id.chl_details_tv_points)
        mTvCompletion=findViewById(R.id.chl_details_tv_status)
        mTvComplete=findViewById(R.id.chl_details_tv_complete)
        mBtmAccept=findViewById(R.id.chl_details_btn_accept)
        mBtmAccept.setOnClickListener(this)
        btnAccept.setOnClickListener(this)
        val extraDetails=intent.extras
        if(extraDetails!=null){
            if(extraDetails.containsKey(AppConstants.SEND_ID)){
                mChallengeId=Integer.parseInt(extraDetails.getString(AppConstants.SEND_ID)!!)
                ChallengeDetails().execute()
            }
        }
    }

    /**
     *  @Function : onClick()
     *  @params   : v: View?
     *  @Return   : void
     * 	@Usage	  : to manage user click events
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.chl_details_btn_accept->{
                showAlert ("Are you ready to take the \""+mAlertTitle+"\"? The Challenge starts when you hit Accept.")
            }
            R.id.layout_btn_accept->{
                showAlert ("Are you ready to take the \""+mAlertTitle+"\"? The Challenge starts when you hit Accept.")
            }
        }
    }


    /**
     *  @Function : showAlert()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : to display simple alert message
     *  @Author   : 1769
     */
    private fun showAlert(alertMsg:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Accept") { dialog, which ->
            dialog.cancel()
            ChallengeUpdated().execute()
        }
        builder.setNegativeButton("Reject") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    inner class ChallengeDetails : AsyncTask<Void, Void, ChallengesEntity>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): ChallengesEntity? {
            return mAppDb?.challengeDao()?.getChallengeDetails(mChallengeId)
        }
        override fun onPostExecute(receivedData:  ChallengesEntity?) {
            if (receivedData!=null)
            {
                mAlertTitle=receivedData.name!!
                /*if (receivedData.name == "GoVida 24 Hour Step Challenge") {
                    mrlParent.background = getDrawable(R.drawable.hours_24_detail)
                } else if (receivedData.name == "GoVida Marathon Challenge") {
                    mrlParent.background = getDrawable(R.drawable.marathon_detail)
                } else if (receivedData.name == "GoVida 3 Day Challenge") {
                    mrlParent.background = getDrawable(R.drawable.days_3_detail)
                } else if (receivedData.name == "GoVida Check-in Challenge") {
                    mrlParent.background = getDrawable(R.drawable.check_in_detail)
                }*/
                Glide.with(this@ActivityChallengeDetails).load(receivedData.imageURL).into(mIvChallengeBg)
                mChallengeWebview.loadData(receivedData.description,"text/html; charset=UTF-8", null)
                tvDuration.text=receivedData.duration
                tvCondition.text= receivedData.subtask!!.replace(",", "")
                tvPoints.text=receivedData.completionPoints.toString()+" GoVPs"
                mTvPoints.text= receivedData.completionPoints.toString()+" GoVPs"?.replace("\\s".toRegex(),"\n")
                mDetailsTvDuration.text=receivedData.duration?.replace("\\s".toRegex(),"\n")
                if(receivedData.ongoingChallengeFlag==1){
                    rlProgress.visibility= View.VISIBLE
                    val progressValues=Integer.parseInt(receivedData.percentage.toString().replace("%",""))
                    tvProgressInWord.text=progressValues.toString()+"%"
                    pgProgress.progress=progressValues
                    rlAccept.visibility=View.GONE
                    mTvCompletion.text=progressValues.toString()+"%"
                    mTvComplete.visibility=View.VISIBLE
                    mTvCompletion.visibility=View.VISIBLE
                    mBtmAccept.visibility=View.GONE

                }else{
                    rlAccept.visibility=View.VISIBLE
                    rlProgress.visibility=View.GONE
                    mTvComplete.visibility=View.GONE
                    mTvCompletion.visibility=View.GONE
                    mBtmAccept.visibility=View.VISIBLE
                }
                hideLoading()
            }
        }
    }

    inner class ChallengeUpdated : AsyncTask<Void, Void, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            var isProcessSuccess=""
            try{
                var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
                val appPreference= AppPreference(this@ActivityChallengeDetails)

                var challengUser= ChallengeAcceptRequest()
                challengUser.challengeId=mChallengeId
                val call=mApiInterfaceService.acceptChallenges(appPreference.authorizationToken,challengUser)
                var response=call.execute()
                if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE)
                {
                    if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                        // status success
                        if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                            isProcessSuccess="yes"
                            mAppDb?.challengeDao()?.onUpdateChallenge(mChallengeId)
                            if (!appPreference.isFirstChallengeCardShow) {
                                appPreference.isFirstChallengeCardShow=true
                            }
                        }else{
                            isProcessSuccess="Something went wrong. Please try again later."
                        }
                    }
                    else {
                        // status failure
                        isProcessSuccess=response.body()?.message.toString()
                    }
                }
                else {
                    // status failure
                    isProcessSuccess=response.body()?.message.toString()
                }

            }catch (e:Exception){
                isProcessSuccess="Something went wrong. Please try again later."
                AppLogger.e(e.message!!)
            }
            if(isProcessSuccess.equals("yes")){
                return true
            }
            else{
                return false
            }

        }
        override fun onPostExecute(tempList:  Boolean?) {
            hideLoading()
            this@ActivityChallengeDetails.finish()
        }
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String?) {
    }

}
