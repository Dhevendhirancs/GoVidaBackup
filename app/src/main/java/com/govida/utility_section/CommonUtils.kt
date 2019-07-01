/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Utility Class related to CommonUtils
 * Date : 15 April 19
 */

package com.govida.utility_section

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.text.TextUtils
import com.govida.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.text.Html
import android.os.Build
import android.text.Spanned
import java.lang.Long
import java.util.concurrent.TimeUnit


object CommonUtils {

    private const val TAG = "CommonUtils"

    val timeStamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())

    val currentTime: String
        get() = SimpleDateFormat("hh:mm a",Locale.getDefault()).format(Date())

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        /*Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();*/
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    fun fromHtml(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
    }

    fun isNameValid(name: String): Boolean {
        return if (TextUtils.isEmpty(name)) {
            false
        } else {
            name.matches("^[A-Za-z]+$".toRegex())
        }
    }

    fun localToGMT(receivedDateInLongStringForGoogleFit: kotlin.Long): String {
//        tempData.datetime=""+android.text.format.DateFormat.format("dd/MM/yyyy HH:mm:ss", receivedDateInLongStringForGoogleFit)

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return ""+sdf.format(receivedDateInLongStringForGoogleFit)
    }

    fun convertStringToTime(time: String): Date? {
        val sdf = SimpleDateFormat("hh:mm a")
        var date: Date? = null
        try {
            date = sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }
}// This utility class is not publicly instantiable
