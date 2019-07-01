/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Utility Class related to ScreenUtils
 * Date : 15 April 19
 */

package com.govida.utility_section

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenUtils {

    fun getScreenWidth(context: Context): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}// This utility class is not publicly instantiable
