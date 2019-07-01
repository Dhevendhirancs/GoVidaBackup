/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Utility Class related to Utilities
 * Date : 15 April 19
 */



package com.govida.utility_section

import android.content.Context
import android.telephony.TelephonyManager
import android.widget.Toast


object Utilities {

    fun getDeviceId(context: Context): String {
        val deviceId = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        return deviceId ?: android.os.Build.SERIAL
    }

    fun showSmallToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}
