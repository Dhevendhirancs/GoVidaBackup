package com.govida.ui_section.home_section.rewards_section.adapters

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.rewards_section.ActivityRewardDetails
import com.govida.utility_section.AppConstants
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.ui_section.home_section.challenges_section.adapters.ChallengeListAdapter
import com.govida.ui_section.home_section.rewards_section.FragmentRewards
import com.govida.ui_section.home_section.rewards_section.models.*
import com.govida.ui_section.home_section.rewards_section.mvp.RewardMVP
import com.govida.ui_section.home_section.rewards_section.mvp.RewardPresenterImplementer
import com.govida.utility_section.AppLogger
import com.govida.utility_section.CommonUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsResponse
import com.govida.ui_section.home_section.rewards_section.models.RedeemRewardsResponse

class RewardListAdapter( var rewardList: MutableList<AllRewardEntity>?, var myRewardList: MutableList<MyRewardsEntity>?, var mContext: Context, var mAppDb: AppDatabase?)
    : androidx.recyclerview.widget.RecyclerView.Adapter<RewardListAdapter.RewardItemAdapter>() {

    var mProgressDialog: ProgressDialog?=null
    public var rewardId: Int = 0
    private var mApiInterface: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
    private lateinit var mAppPreference: AppPreference

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RewardItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_reward_card, parent, false)
        return RewardItemAdapter(itemView)
    }

    override fun getItemCount(): Int {
        return if (rewardList != null) {
            rewardList!!.size
        } else {
            myRewardList!!.size
        }
    }

    override fun onBindViewHolder(viewHolder: RewardItemAdapter, position: Int) {
        if (rewardList != null) {
            val sampleDataModel=rewardList!!.get(position)
            viewHolder.tvRewardName.text=sampleDataModel.title
            viewHolder.tvRewardInfo.text=sampleDataModel.subTitle
            viewHolder.tvRewards.text=sampleDataModel.pointsRequired.toString() + " " + mContext.getString (R.string.govps)
            Glide.with(mContext).load(sampleDataModel.imageUrl).into(viewHolder.ivRewardCard)
            viewHolder.rlAccpet.visibility= View.VISIBLE
            viewHolder.rlProgress.visibility= View.GONE
            rewardId = sampleDataModel.rewardId!!
        } else {
            val sampleDataModel=myRewardList!!.get(position)
            viewHolder.tvRewardName.text=sampleDataModel.title
            viewHolder.tvRewardInfo.text=sampleDataModel.subTitle
            viewHolder.tvRewards.text=sampleDataModel.pointsRequired.toString() + " " + mContext.getString (R.string.govps)
            Glide.with(mContext).load(sampleDataModel.imageUrl).into(viewHolder.ivRewardCard)
            viewHolder.rlAccpet.visibility= View.GONE
            viewHolder.rlProgress.visibility= View.VISIBLE
            viewHolder.rewardsStatus.text=sampleDataModel.status
            rewardId = sampleDataModel.rewardId!!
        }
    }

    inner class RewardItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var appPreference: AppPreference
        var tvRewardName: TextView
        var tvRewardInfo: TextView
        var tvRewards: TextView
        var rewardsStatus: TextView
        var btnAccept: Button
        val rlProgress: RelativeLayout
        var rlAccpet: RelativeLayout
        var llParent:LinearLayout
        var ivRewardCard: ImageView
        init {
            tvRewardName=itemView.findViewById(R.id.rewards_tv_reward_name)
            tvRewardInfo=itemView.findViewById(R.id.rewards_tv_info)
            tvRewards=itemView.findViewById(R.id.rewards_tv_points)
            rewardsStatus=itemView.findViewById(R.id.rewards_status)
            btnAccept=itemView.findViewById(R.id.layout_btn_accept)
            rlProgress=itemView.findViewById(R.id.layout_rl_progress)
            rlAccpet=itemView.findViewById(R.id.layout_rl_accpet)
            llParent=itemView.findViewById(R.id.ll_parent)
            ivRewardCard = itemView.findViewById(R.id.iv_reward_card)
            btnAccept.setOnClickListener(this)
            llParent.setOnClickListener(this)
        }

        /**
         *  @Function : onClick()
         *  @params   : View
         *  @Return   : void
         * 	@Usage	  : listener function definition
         *  @Author   : 1769
         */
        override fun onClick(view: View) {
            when(view.id){
                R.id.layout_btn_accept->{
                    mAppPreference = AppPreference(mContext)
                    if (mAppPreference.myTotalGoVps >= rewardList!!.get(adapterPosition).pointsRequired!!) {
                        showAlert(mContext.getString(R.string.redeem_alert_1)
                                + rewardList!!.get(adapterPosition).title
                                + mContext.getString(R.string.redeem_alert_2)
                                + rewardList!!.get(adapterPosition).pointsRequired
                                + mContext.getString(R.string.accept_reward_3))
                    } else {
                        showAlertForError(mContext.getString(R.string.insufficient_message))
                    }
                }
                R.id.ll_parent->{
                     val mainActIntent = Intent(mContext, ActivityRewardDetails::class.java)
                    if (rewardList != null) {
                        mainActIntent.putExtra(AppConstants.SEND_ID,""+rewardList!!.get(adapterPosition).rewardId)
                        mainActIntent.putExtra(AppConstants.REWARD_FLAG, AppConstants.ALL_REWARDS)
                        mainActIntent.putExtra(AppConstants.TITLE, ""+rewardList!!.get(adapterPosition).title)
                        mainActIntent.putExtra(AppConstants.SUB_TITLE, ""+rewardList!!.get(adapterPosition).subTitle)
                        mainActIntent.putExtra(AppConstants.POINTS_REQUIRED, rewardList!!.get(adapterPosition).pointsRequired)
                        mainActIntent.putExtra(AppConstants.DESCRIPTION, ""+rewardList!!.get(adapterPosition).description)
                        mainActIntent.putExtra(AppConstants.IMAGE_URL, ""+rewardList!!.get(adapterPosition).imageUrl)
                    } else {
                        mainActIntent.putExtra(AppConstants.SEND_ID,""+myRewardList!!.get(adapterPosition).rewardId)
                        mainActIntent.putExtra(AppConstants.REWARD_FLAG, AppConstants.MY_REWARDS)
                        mainActIntent.putExtra(AppConstants.TITLE, ""+myRewardList!!.get(adapterPosition).title)
                        mainActIntent.putExtra(AppConstants.SUB_TITLE, ""+myRewardList!!.get(adapterPosition).subTitle)
                        mainActIntent.putExtra(AppConstants.POINTS_REQUIRED, myRewardList!!.get(adapterPosition).pointsRequired)
                        mainActIntent.putExtra(AppConstants.DESCRIPTION, ""+myRewardList!!.get(adapterPosition).description)
                        mainActIntent.putExtra(AppConstants.IMAGE_URL, ""+myRewardList!!.get(adapterPosition).imageUrl)
                        mainActIntent.putExtra(AppConstants.STATUS, ""+myRewardList!!.get(adapterPosition).status)
                    }
                    mContext.startActivity(mainActIntent)
                }
            }

        }

        /**
         *  @Class : RedeemReward
         * 	@Usage	  : Redeem Reward API call
         *  @Author   : 1769
         */
        inner class RedeemReward : AsyncTask<Void, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                mProgressDialog= CommonUtils.showLoadingDialog(mContext)
                if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                    mProgressDialog!!.cancel()
                    mProgressDialog!!.show()
                }
            }
            override fun doInBackground(vararg params: Void?): String {
                var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
                val appPreference= AppPreference(mContext)
                var isProcessSuccess="Something went wrong. Please try again later."
                try{
                    var rewardId=rewardList!!.get(adapterPosition).rewardId
                    val call=mApiInterfaceService.redeemReward(appPreference.authorizationToken,rewardId!!)
                    var response=call.execute()
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE)
                    {
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null){
                                isProcessSuccess="yes"
                                appPreference.myTotalGoVps=response.body()?.responseBody?.myGoVPs!!
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
                    Crashlytics.logException(e)
                }
                return isProcessSuccess
            }
            override fun onPostExecute(tempList:  String?) {
                if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                    mProgressDialog!!.cancel()
                }
                if(tempList.equals("yes",true)){
                    appPreference = AppPreference(itemView.rootView.context)
                    var showMsg = ""
                    showMsg += mContext.getString(R.string.accept_challenge_1)
                    showMsg += appPreference.userFirstName
                    showMsg += mContext.getString(R.string.accept_challenge_2)
                    showMsg += rewardList!!.get(adapterPosition).title + " Reward"
                    Toast.makeText(mContext, showMsg, Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                }
                else{
                    showAlertForError(tempList!!)
                }

                val goVps:TextView=itemView.rootView.findViewById(R.id.rewards_info)
                if(goVps.visibility==View.VISIBLE){
                    goVps.text=""
                    goVps.text="My GoVPs:"+appPreference.myTotalGoVps
                }
            }
        }

        /**
         *  @Function : showAlert()
         *  @params   : alertMsg:String
         *  @Return   : void
         * 	@Usage	  : to display simple alert message with positive and negative button
         *  @Author   : 1769
         */
        fun showAlert(alertMsg:String) {
            val builder = AlertDialog.Builder(itemView.rootView.context)
            builder.setTitle("GoVida")
            builder.setMessage(alertMsg)
//            builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
            builder.setCancelable(false)
            builder.setPositiveButton("Redeem") { dialog, which ->
//                RewardsUpdated().execute()
                RedeemReward().execute()
                dialog.cancel()
            }
            builder.setNegativeButton("Reject") { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }

        /**
         *  @Function : showAlertForError()
         *  @params   : alertMsg:String
         *  @Return   : void
         * 	@Usage	  : to display simple alert message with positive button
         *  @Author   : 1769
         */
        private fun showAlertForError(alertMsg:String)
        {
            val builder = AlertDialog.Builder(itemView.rootView.context)
            builder.setTitle("GoVida")
            builder.setMessage(alertMsg)
            builder.setCancelable(true)
            builder.setPositiveButton("Ok") { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }
    }
}