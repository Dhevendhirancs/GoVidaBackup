/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Utility Class related to View
 */

package com.govida.utility_section

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.govida.R

object ViewUtils {

    fun pxToDp(px: Float): Float {
        val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        return px / (densityDpi / 160f)
    }

    fun dpToPx(dp: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return Math.round(dp * density)
    }

    fun changeIconDrawableToGray(context: Context, drawable: Drawable?) {
        if (drawable != null) {
            drawable.mutate()
            drawable.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP)
        }
    }
}// This utility class is not publicly instantiable
