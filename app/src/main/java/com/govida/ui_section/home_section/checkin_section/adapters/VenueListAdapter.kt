package com.govida.ui_section.home_section.checkin_section.adapters

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.crashlytics.android.Crashlytics
import com.google.android.gms.location.FusedLocationProviderClient
import com.govida.R
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.home_section.checkin_section.ActivityCheckin
import com.govida.ui_section.home_section.checkin_section.ActivitySelectVenue
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse
import com.govida.utility_section.AppConstants
import com.govida.utility_section.CommonUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class VenueListAdapter(var venueList: List<VenueResponse.Data>, var mContext: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<VenueListAdapter.VenueItemAdapter>() {
    var mProgressDialog: ProgressDialog?=null
    lateinit var act: Activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_venue, parent, false)
        return VenueItemAdapter(itemView)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder:VenueItemAdapter, position: Int) {
        val sampleDataModel= venueList[position]
        holder.tvTitle.text=sampleDataModel.venueName
        holder.tvAddress.text=sampleDataModel.address
        if (sampleDataModel.govidaVerified!!) {
            holder.tvVerified.text = mContext.getString(R.string.verified)
            if (Build.VERSION.SDK_INT >= 23){
                holder.tvVerified.setBackgroundColor(ContextCompat.getColor(mContext, R.color.notification_color))
            }
            else{
                holder.tvVerified.setBackgroundColor(mContext.resources.getColor(R.color.notification_color))
            }
//            holder.ivNonVerified.visibility = View.GONE
        } else {
            holder.tvVerified.text = mContext.getString(R.string.non_verified)
//            holder.ivNonVerified.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= 23){
                holder.tvVerified.setBackgroundColor(ContextCompat.getColor(mContext, R.color.text_subtext))
            }
            else{
                holder.tvVerified.setBackgroundColor(mContext.resources.getColor(R.color.text_subtext))
            }
        }
    }

    inner class VenueItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_gym_name)
        var tvAddress: TextView = itemView.findViewById(R.id.tv_gym_address)
        var tvVerified: TextView = itemView.findViewById(R.id.tv_verified)
//        var ivNonVerified: ImageView = itemView.findViewById(R.id.iv_non_verified)
        var llParent: LinearLayout = itemView.findViewById(R.id.ll_parent)
        var appPreference: AppPreference = AppPreference(mContext)
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        private lateinit var fusedLocationClient: FusedLocationProviderClient
        init {
            llParent.setOnClickListener(this)
        }

        /**
         *  @Function : onClick()
         *  @params   : v: View?
         *  @Return   : void
         * 	@Usage	  : to handle user click events
         *  @Author   : 1769
         */
        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.ll_parent -> {
                    if (venueList!!.get(adapterPosition).govidaVerified!!) {
                        showAlert(mContext.getString(R.string.start_alert) + venueList!!.get(adapterPosition).venueName + " ?")
                    } else {
                        showAlert(mContext.getString(R.string.non_verified_venue_alert))
                    }
                }
            }
        }

        /**
         *  @Function : showAlert()
         *  @params   : alertMsg:String
         *  @Return   : void
         * 	@Usage	  : to display simple alert message with positive and negative buttons
         *  @Author   : 1769
         */
        fun showAlert(alertMsg:String) {
            val builder = AlertDialog.Builder(itemView.rootView.context, R.style.AlertDialogCustom)
//            val builder = AlertDialog.Builder(itemView.rootView.context)
            builder.setTitle("GoVida")
            builder.setMessage(alertMsg)
            builder.setCancelable(false)
            builder.setPositiveButton("Checkin") { dialog, which ->
                Checkin().execute()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }

        /**
         *  @Class : Checkin
         * 	@Usage : to send the checkin status to server
         *  @Author : 1769
         */
        inner class Checkin : AsyncTask<Void, Void, String>() {
            override fun onPreExecute() {
                super.onPreExecute()
                mProgressDialog = CommonUtils.showLoadingDialog(mContext)
                if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                    mProgressDialog!!.cancel()
                    mProgressDialog!!.show()
                }
            }

            override fun doInBackground(vararg params: Void?): String {
                var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)
                var isProcessSuccess = "Something went wrong. Please try again later."
                try {
                    val call =
                        mApiInterfaceService.checkin(appPreference.authorizationToken, venueList!!.get(adapterPosition))
                    var response = call.execute()
                    if (response.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                        if (response.body()?.status.equals(AppConstants.STATUS_CODE_SUCCESS, true)) {
                            // status success
                            if (response.body()?.responseBody != null) {
                                appPreference.checkinId = response.body()?.responseBody!!.data!!.checkinId!!
                                appPreference.isCheckoutDone = false
                                isProcessSuccess = "yes"
                            } else {
                                isProcessSuccess = "Something went wrong. Please try again later."
                            }
                        } else {
                            // status failure
                            isProcessSuccess = response.body()?.message.toString()
                        }
                    } else {
                        // status failure
                        isProcessSuccess = response.body()?.message.toString()
                    }

                } catch (e: Exception) {
                    isProcessSuccess = "Something went wrong. Please try again later."
                    Crashlytics.logException(e)
                }
                return isProcessSuccess
            }

            override fun onPostExecute(tempList: String?) {
                if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
                    mProgressDialog!!.cancel()
                }
                if (tempList.equals("yes", true)) {
//                    if (!appPreference.isCheckInBonusReceived) {
//                        appPreference.isCheckInBonusReceived = true
//                    }
                    val mainActIntent = Intent(mContext, ActivityCheckin::class.java)
                    mainActIntent.putExtra(AppConstants.VENUE_NAME,venueList!!.get(adapterPosition).venueName)
                    mContext.startActivity(mainActIntent)
                    (mContext as Activity).finish()
                } else {
                    showAlertForError(tempList!!)
                }
            }

            /**
             *  @Function : showAlertForError()
             *  @params   : alertMsg:String
             *  @Return   : void
             * 	@Usage	  : to display simple alert message with only positive button
             *  @Author   : 1769
             */
            private fun showAlertForError(alertMsg: String) {
                val builder = AlertDialog.Builder(itemView.rootView.context, R.style.AlertDialogCustom)
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
}