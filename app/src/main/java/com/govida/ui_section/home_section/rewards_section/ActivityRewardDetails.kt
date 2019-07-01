/**
 * @Class : ActivityRewardDetails
 * @Usage : This activity is used to manage the Reward Details page
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section

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
import com.govida.ui_section.home_section.rewards_section.models.AllRewardEntity
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsEntity
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger

class ActivityRewardDetails : BaseActivity(), View.OnClickListener {

    private lateinit var mRewardWebview: WebView
    private lateinit var mllParent: LinearLayout
    private lateinit var mBtnRedeem:Button
    private lateinit var mTvRewardName:TextView
    private lateinit var mTvRewardInfo:TextView
    private lateinit var mTvRewardPoints:TextView
    private lateinit var mTvRewardStatus:TextView
    private lateinit var mTvRewardPointsBottom:TextView
    private lateinit var mBtnRedeemBottom:Button
    private lateinit var mTvRewardStatusBottom:TextView
    private lateinit var mIvRewardImage: ImageView
    private var mRewardId=0
    private lateinit var rewardFlag: String
    private var mAppDb: AppDatabase? = null
    private lateinit var mAppPreference: AppPreference
    private var requiredGoVps: Int = 0
    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_details)
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

        mBtnRedeem=findViewById(R.id.layout_btn_accept)
        mBtnRedeem.setOnClickListener(this)

        mRewardWebview=findViewById(R.id.reward_details_webview)
        mTvRewardName=findViewById(R.id.rewards_tv_reward_name)
        mTvRewardInfo=findViewById(R.id.rewards_tv_info)
        mTvRewardPoints=findViewById(R.id.rewards_tv_points)
        mTvRewardStatus=findViewById(R.id.rewards_status)
        mTvRewardPointsBottom=findViewById(R.id.tv_reward_points_bottom)

        mBtnRedeemBottom=findViewById(R.id.layout_rewards_btn_accept)
        mBtnRedeemBottom.setOnClickListener(this)
        mTvRewardStatusBottom=findViewById(R.id.rewards_status_bottom)
        mIvRewardImage = findViewById(R.id.iv_reward_image)

        val extraDetails=intent.extras
        if(extraDetails!=null){
            if(extraDetails.containsKey(AppConstants.SEND_ID)){
                mRewardId=Integer.parseInt(extraDetails.getString(AppConstants.SEND_ID)!!)
                rewardFlag = extraDetails.getString(AppConstants.REWARD_FLAG)

                Glide.with(this@ActivityRewardDetails).load(extraDetails.getString(AppConstants.IMAGE_URL)).into(mIvRewardImage)

                mRewardWebview.loadData(extraDetails.getString(AppConstants.DESCRIPTION),"text/html; charset=UTF-8", null)
                mTvRewardName.text=extraDetails.getString(AppConstants.TITLE)
                mTvRewardInfo.text=extraDetails.getString(AppConstants.SUB_TITLE)
                mTvRewardPoints.text=extraDetails.getInt(AppConstants.POINTS_REQUIRED).toString() + " GoVPs"
                mTvRewardPointsBottom.text=extraDetails.getInt(AppConstants.POINTS_REQUIRED).toString() + " GoVPs"
                requiredGoVps = extraDetails.getInt(AppConstants.POINTS_REQUIRED)
                title = extraDetails.getString(AppConstants.TITLE)

                if (rewardFlag == AppConstants.ALL_REWARDS) {
                    mBtnRedeem.visibility = View.VISIBLE
                    mBtnRedeemBottom.visibility = View.VISIBLE
                    mTvRewardStatus.visibility = View.GONE
                    mTvRewardStatusBottom.visibility = View.GONE
                } else {
                    mBtnRedeem.visibility = View.GONE
                    mBtnRedeemBottom.visibility = View.GONE
                    mTvRewardStatus.visibility = View.VISIBLE
                    mTvRewardStatusBottom.visibility = View.GONE
                    mTvRewardStatus.text = extraDetails.getString(AppConstants.STATUS)
                    mTvRewardStatusBottom.text = extraDetails.getString(AppConstants.STATUS)
                }

//                if (rewardFlag == AppConstants.ALL_REWARDS) {
//                    RewardDetails().execute()
//                } else {
//                    MyRewardDetails().execute()
//                }
            }
        }
        mAppPreference = AppPreference(this)
    }

    /**
     *  @Class : RewardDetails
     * 	@Usage	  : Get the reward details and update the UI
     *  @Author   : 1769
     */
    inner class RewardDetails : AsyncTask<Void, Void, AllRewardEntity>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): AllRewardEntity? {
            return mAppDb?.rewardDao()?.getRewardDetails(mRewardId)
        }
        override fun onPostExecute(receivedData:  AllRewardEntity?) {
            if (receivedData!=null)
            {
                Glide.with(this@ActivityRewardDetails).load(receivedData.imageUrl).into(mIvRewardImage)

                mRewardWebview.loadData(receivedData.description,"text/html; charset=UTF-8", null)
                mTvRewardName.text=receivedData.title
                mTvRewardInfo.text=receivedData.subTitle
                mTvRewardPoints.text=receivedData.pointsRequired.toString() + " GoVPs"
                mTvRewardPointsBottom.text=receivedData.pointsRequired.toString() + " GoVPs"
                mBtnRedeem.visibility = View.VISIBLE
                mBtnRedeemBottom.visibility = View.VISIBLE
                mTvRewardStatus.visibility = View.GONE
                mTvRewardStatusBottom.visibility = View.GONE
                requiredGoVps = receivedData.pointsRequired!!
                title = receivedData.title!!
                hideLoading()
            }
        }
    }

    /**
     *  @Class : MyRewardDetails
     * 	@Usage	  : Get the reward details and update the UI
     *  @Author   : 1769
     */
    inner class MyRewardDetails : AsyncTask<Void, Void, MyRewardsEntity>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): MyRewardsEntity? {
            return mAppDb?.myRewardsDao()?.getMyRewardDetails(mRewardId)
        }
        override fun onPostExecute(receivedData:  MyRewardsEntity?) {
            if (receivedData!=null)
            {
                Glide.with(this@ActivityRewardDetails).load(receivedData.imageUrl).into(mIvRewardImage)

                mRewardWebview.loadData(receivedData.description,"text/html; charset=UTF-8", null)
                mTvRewardName.text=receivedData.title
                mTvRewardInfo.text=receivedData.subTitle
                mTvRewardPoints.text=receivedData.pointsRequired.toString() + " GoVPs"
                mTvRewardPointsBottom.text=receivedData.pointsRequired.toString() + " GoVPs"
                mBtnRedeem.visibility = View.GONE
                mBtnRedeemBottom.visibility = View.GONE
                mTvRewardStatus.visibility = View.VISIBLE
                mTvRewardStatusBottom.visibility = View.GONE
                mTvRewardStatus.text = receivedData.status
                mTvRewardStatusBottom.text = receivedData.status
                requiredGoVps = receivedData.pointsRequired!!
                title = receivedData.title!!
                hideLoading()
            }
        }
    }

    /**
     *  @Class : RewardUpdated
     * 	@Usage	  : Update the Reward status once the user redeems
     *  @Author   : 1769
     */
    inner class RewardUpdated : AsyncTask<Void, Void, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
//            mAppDb?.rewardDao()?.onUpdateReward(mRewardId)
            return true
        }
        override fun onPostExecute(tempList:  Boolean?) {
            hideLoading()
            this@ActivityRewardDetails.finish()
        }
    }

    /**
     *  @Function : onClick()
     *  @params   : v: View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.layout_rewards_btn_accept -> {
                    if (mAppPreference.myTotalGoVps >= requiredGoVps) {
                        showAlert("Would you like to redeem " + mTvRewardName.text + ". " + mTvRewardPoints.text + " will be debited from your GoVida wallet.")
                    } else {
                        showAlertInSufficient("Insufficient GoVPs to Redeem Reward")
                    }
                } R.id.layout_btn_accept -> {
                    if (mAppPreference.myTotalGoVps >= requiredGoVps) {
                        showAlert("Would you like to redeem " + mTvRewardName.text + ". " + mTvRewardPoints.text + " will be debited from your GoVida wallet.")
                    } else {
                        showAlertInSufficient("Insufficient GoVPs to Redeem Reward")
                    }
                }
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
    fun showAlert(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Redeem") { dialog, which ->
            RedeemReward().execute()
            finish()
        }
        builder.setNegativeButton("Reject") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     *  @Function : showAlertInSufficient()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : to display simple alert message
     *  @Author   : 1769
     */
    fun showAlertInSufficient(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }

    /**
     *  @Class : RedeemReward()
     * 	@Usage	  : to display simple alert message
     *  @Author   : 1769
     */
    inner class RedeemReward : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showLoading()
        }
        override fun doInBackground(vararg params: Void?): String {
            var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
            var isProcessSuccess="Something went wrong. Please try again later."
            try{
                val call=mApiInterfaceService.redeemReward(mAppPreference.authorizationToken,mRewardId)
                var response=call.execute()
                if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE)
                {
                    if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                        // status success
                        if(response.body()?.responseBody!=null){
                            isProcessSuccess="yes"
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
            return isProcessSuccess
        }
        override fun onPostExecute(tempList:  String?) {
            hideLoading()
            if(tempList.equals("yes",true)){
                var showMsg = ""
                showMsg += getString(R.string.accept_challenge_1)
                showMsg += mAppPreference.userFirstName
                showMsg += getString(R.string.accept_challenge_2)
                showMsg += "$title Reward"
                Toast.makeText(applicationContext, showMsg, Toast.LENGTH_SHORT).show()
//                    rewardList!!.removeAt(adapterPosition)
//                notifyDataSetChanged()
            }
            else{
                showAlertForError(tempList!!)
            }
        }
    }

    /**
     *  @Function : showAlertForError()
     *  @params   : alertMsg:String
     *  @Return   : void
     * 	@Usage	  : to display simple alert message
     *  @Author   : 1769
     */
    private fun showAlertForError(alertMsg:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(true)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

}
