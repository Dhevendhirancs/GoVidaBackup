/**
 * @Class : GoVidaApplication
 * @Usage : This Application class is used to manage the application level objects
 * @Author : 1276
 */
package com.govida.application_section

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary

class GoVidaApplication: Application() {
    companion object {
        lateinit var mContext: Context
    }
    override fun onCreate() {
        super.onCreate()

//        if(LeakCanary.isInAnalyzerProcess(this)){
//            // This process is dedicated to LeakCanary for heap analysis. You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(this)
        mContext=this
    }
}