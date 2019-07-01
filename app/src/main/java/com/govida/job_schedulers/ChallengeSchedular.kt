/**
 * @Class : ChallengeSchedular
 * @Usage : This class is used to schedule challenge status of user.
 * @Author : 1276
 */
package com.govida.job_schedulers

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import com.crashlytics.android.Crashlytics
import com.govida.api_section.ApiClient
import com.govida.api_section.ApiInterface
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.model.ChallengeResponse
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class ChallengeSchedular : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        val appPreference = AppPreference(this)
        val dbObject=AppDatabase.getDatabase(this)
        AllChallengeAsyncTask(this, params,appPreference.authorizationToken,dbObject!!).execute()
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    /**
     * @Class : AllChallengeAsyncTask
     * @Usage : This class is used to get latest information from api and store it in database
     * @Author : 1276
     */
    private inner class AllChallengeAsyncTask internal constructor(private val mContext: Context, private val params: JobParameters,private val authToken:String,private val dbObject:AppDatabase) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg voids: Void): Boolean? {
            val call: Call<ChallengeResponse>
            val challengeResponse: Response<ChallengeResponse>
            try {
                val apiClient = ApiClient()
                call = apiClient.getClient().create(ApiInterface::class.java).getChallengeList(authToken)
                challengeResponse = call.execute()
                if (challengeResponse.code() == AppConstants.HTTP_STATUS_CREATED_CODE) {
                    if (challengeResponse.body() != null) {
                        if (challengeResponse.body()!!.status.equals(AppConstants.STATUS_CODE_SUCCESS, ignoreCase = true))
                        {
                            // status success
                            if (challengeResponse.body()!!.responseBody != null && challengeResponse.body()!!.responseBody?.data != null) {
                                dbObject.challengeDao().insertMultipleListRecord(challengeResponse.body()!!.responseBody?.data!!)
                            } else {
                                AppLogger.e("AllChallengeAsynTask  error")
                            }
                        } else {
                            // status failure
                            AppLogger.e("AllChallengeAsynTask  data is null")
                        }
                    }
                } else {
                    // status failure
                    AppLogger.e("AllChallengeAsynTask  code issue")
                }
            } catch (e: IOException) {
                Crashlytics.logException(e)
                e.printStackTrace()
                AppLogger.e("AllChallengeAsynTask exception.")
            }
            return true
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            jobFinished(params, (!aBoolean!!))
        }
    }

}
