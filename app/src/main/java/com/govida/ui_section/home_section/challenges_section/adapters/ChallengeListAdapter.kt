package com.govida.ui_section.home_section.challenges_section.adapters

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.challenges_section.ActivityChallengeDetails
import com.govida.ui_section.home_section.challenges_section.models.ChallengeAcceptRequest
import com.govida.ui_section.home_section.challenges_section.models.ChallengeAcceptResponse
import com.govida.ui_section.home_section.checkin_section.service.AlarmBroadcastReceiver
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import com.govida.utility_section.CommonUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChallengeListAdapter(var challengeList: MutableList<ChallengesEntity>, var mContext:Context, var mAppDb: AppDatabase?) : androidx.recyclerview.widget.RecyclerView.Adapter<ChallengeListAdapter.ChallengeItemAdapter>() {
    var mProgressDialog:ProgressDialog?=null
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChallengeItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_challange_card, parent, false)
        return ChallengeItemAdapter(itemView)
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

    override fun onBindViewHolder(viewHolder: ChallengeItemAdapter, position: Int) {
        val sampleDataModel= challengeList[position]
        viewHolder.tvDuration.text=sampleDataModel.name
        Glide.with(mContext).load(sampleDataModel.imageURL).into(viewHolder.ivChallengeCard)
        viewHolder.tvPoints.text=sampleDataModel.completionPoints.toString()+" GoVPs"
        if(sampleDataModel.ongoingChallengeFlag==1){
            viewHolder.rlProgress.visibility=View.VISIBLE
            val progressValues=Integer.parseInt(sampleDataModel.percentage.toString().replace("%",""))
            viewHolder.tvProgressInWord.text=progressValues.toString()+"%"

            viewHolder.pgProgress.progress=progressValues
            viewHolder.rlAccept.visibility=View.GONE

        }else{
            viewHolder.rlAccept.visibility=View.VISIBLE
            viewHolder.rlProgress.visibility=View.GONE
        }
    }

    inner class ChallengeItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView),View.OnClickListener {
        lateinit var appPreference: AppPreference
        var tvDuration:TextView = itemView.findViewById(R.id.challenge_tv_duration)
        var ivChallengeCard:ImageView=itemView.findViewById(R.id.iv_challenge_card)
        var tvCondition:TextView=itemView.findViewById(R.id.challenge_tv_condition)
        var tvPoints:TextView=itemView.findViewById(R.id.challenge_tv_points)
        var tvProgressInWord:TextView=itemView.findViewById(R.id.challenge_tv_progress_word)
        var pgProgress:ProgressBar=itemView.findViewById(R.id.challenge_pg_progress)
        private var btnAccept:Button=itemView.findViewById(R.id.layout_btn_accept)
        val rlProgress:RelativeLayout=itemView.findViewById(R.id.layout_rl_progress)
        var rlAccept:RelativeLayout=itemView.findViewById(R.id.layout_rl_accpet)
        private var llChallengeDetails:RelativeLayout=itemView.findViewById(R.id.ll_challenge_details)
        var llChallengeParent:LinearLayout = itemView.findViewById(R.id.ll_challenge_parent)
        init {
            btnAccept.setOnClickListener(this)
            llChallengeDetails.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when(view.id){
                R.id.layout_btn_accept->{
                    val appPreference= AppPreference(mContext)
                    if(appPreference.isGoogleFitPermissionGiven){
//                        val showMsg2="Are you ready to take the \""+challengeList.get(adapterPosition).name+"\"? The Challenge starts when you hit Accept."
                        showAlert (mContext.getString(R.string.alert_challenge_accept_1) + challengeList.get(adapterPosition).name +
                                mContext.getString(R.string.alert_challenge_accept_2))
                    }else{
                        showAlertForError(mContext.getString(R.string.fit_permission_missing))
                    }

                }
                R.id.ll_challenge_details->{
                    val mainActIntent = Intent(mContext, ActivityChallengeDetails::class.java)
                    mainActIntent.putExtra(AppConstants.SEND_ID,""+challengeList.get(adapterPosition).id)
                    mContext.startActivity(mainActIntent)
                }
            }
        }


        inner class ChallengeUpdated : AsyncTask<Void, Void, String>() {
            private var challengUser= ChallengeAcceptRequest()
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
                    challengUser.challengeId=challengeList.get(adapterPosition).id
                    val call=mApiInterfaceService.acceptChallenges(appPreference.authorizationToken,challengUser)
                    var response=call.execute()
                    if(response.code()== AppConstants.HTTP_STATUS_CREATED_CODE)
                    {
                        if(response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS,true)){
                            // status success
                            if(response.body()?.responseBody!=null && response.body()?.responseBody?.data!=null){
                                isProcessSuccess="yes"
                                if (!appPreference.isFirstChallengeCardShow) {
                                    appPreference.isFirstChallengeCardShow=true
                                }
                                ChallengeUpdatedForDB().execute()
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
                if(tempList.equals("yes",true)){
                    if (challengUser.challengeId!! == AppConstants.CHALLENGE_24_HOUR_ID) {
                        startAlarmBroadcastReceiver (mContext, 86400000)
//                        startAlarmBroadcastReceiver (mContext, 60000)
                    }
                    appPreference = AppPreference(itemView.rootView.context)
                    var showMsg = ""
                    showMsg += mContext.getString(R.string.accept_challenge_1)
                    showMsg += appPreference.userFirstName
                    showMsg += mContext.getString(R.string.accept_challenge_2)
                    showMsg += challengeList.get(adapterPosition).name
                    Toast.makeText(mContext, showMsg, Toast.LENGTH_SHORT).show()
                    challengeList.removeAt(adapterPosition)
                    notifyDataSetChanged()
                }
                else{
                    if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                        mProgressDialog!!.cancel()
                    }
                    showAlertForError(tempList!!)
                }
            }

            /**
             *  @Function : startAlarmBroadcastReceiver()
             *  @params   : context: Context, delay: Long
             *  @Return   : void
             * 	@Usage	  : Set alarm manager to trigger a particular action after a particular time
             *  @Author   : 1769
             */
            fun startAlarmBroadcastReceiver(context: Context, delay: Long) {
                var intent: Intent = Intent(context, AlarmBroadcastReceiver::class.java)
                intent.putExtra(AppConstants.NOTIFICATION_FLAG, AppConstants.FROM_CHALLENGE)
                var pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                // Remove any previous pending intent.
                alarmManager.cancel(pendingIntent)
                var SDK_INT: Int = Build.VERSION.SDK_INT
                if (SDK_INT < Build.VERSION_CODES.KITKAT)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
                else if (Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT < Build.VERSION_CODES.M)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
                else if (SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pendingIntent)
                }
            }
        }

        inner class ChallengeUpdatedForDB : AsyncTask<Void, Void, Boolean>(){
            override fun doInBackground(vararg params: Void?):Boolean {
                mAppDb?.challengeDao()?.onUpdateChallenge(challengeList.get(adapterPosition).id!!)
               return true
            }

            override fun onPostExecute(result: Boolean?) {
                super.onPostExecute(result)
                if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                    mProgressDialog!!.cancel()
                }
            }
        }
        /**
         *  @Function : showAlert()
         *  @params   : alertMsg:String
         *  @Return   : void
         * 	@Usage	  : to display simple alert message
         *  @Author   : 1769,1276
         */
        private fun showAlert(alertMsg:String)
        {
            val builder = AlertDialog.Builder(itemView.rootView.context)
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

