/*
 * Copyright (C) 2019 GoVida
 * Author : 1276
 * Usage : Utility Class related to AppUtils
 * Date : 15 April 19
 */

package com.govida.utility_section

import android.content.Context

object AppUtils {

    fun openPlayStoreForApp(context: Context) {
        /*
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_market_link) + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context
                            .getResources()
                            .getString(R.string.app_google_play_store_link) + appPackageName)));
        }
        */
    }

}// This class is not publicly instantiable
