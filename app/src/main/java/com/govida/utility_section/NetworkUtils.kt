/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Utility Class related to NetworkUtils
 * Date : 15 April 19
 */

package com.govida.utility_section

import android.content.Context
import android.net.ConnectivityManager


object NetworkUtils {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}// This utility class is not publicly instantiable
