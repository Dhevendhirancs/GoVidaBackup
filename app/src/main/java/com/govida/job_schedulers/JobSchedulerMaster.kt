/**
 * @Class : JobSchedulerMaster
 * @Usage : This class is used to schedule all the jobs in application
 * @Author : 1276
 */
package com.govida.job_schedulers

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

class JobSchedulerMaster(private val mContext: Context) {

    /**
     * @Function : initAllJobs()
     * @params   : void
     * @Return   : void
     * @Usage      : Initialise and run all jobs
     * @Author   : 1276
     */
    fun initAllJobs() {
        startChallengeJob()
    }

    /**
     * @Function : startChallengeJob()
     * @params   : void
     * @Return   : void
     * @Usage      : Initialise and run challenge job
     * @Author   : 1276
     */
    private fun startChallengeJob(): Boolean {
        val jobScheduler = mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(
            JobInfo.Builder(
                2,
                ComponentName(mContext, ChallengeSchedular::class.java)
            )
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build()
        )
        return true
    }
}
