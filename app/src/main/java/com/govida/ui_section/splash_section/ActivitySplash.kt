/**
 * @Class : ActivitySplash
 * @Usage : This activity is used for displaying splash screen.
 * @Author : 1276
 */

package com.govida.ui_section.splash_section

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.be_connected.ActivityBeConnected
import com.govida.ui_section.home_section.ActivityHome
import com.govida.ui_section.login_section.ActivityLogin
import com.govida.ui_section.welcome_slider_section.ActivityWelcome

class ActivitySplash : AppCompatActivity() {
    private var mAppDb: AppDatabase? = null
    override fun onResume() {
        super.onResume()
        moveToNextSection()
    }

    /**
     *  @Function : moveToNextSection()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Decide which screen for to show next.
     *  @Author   : 1276
     */
    private fun moveToNextSection() {
        val appPreference= AppPreference(this)
        mAppDb= AppDatabase.getDatabase(this)
        if(appPreference.isUserOnLoginPage)
        {
            if(appPreference.isUserLoggedIn){
                if(appPreference.isHomeSetupDone){
                    val mainActIntent = Intent(applicationContext, ActivityHome::class.java)
                    startActivity(mainActIntent)
                }else{
                    val mainActIntent = Intent(applicationContext, ActivityBeConnected::class.java)
                    startActivity(mainActIntent)
                }
            }
            else
            {
                val mainActIntent = Intent(this, ActivityLogin::class.java)
                startActivity(mainActIntent)
            }
        }else{
            val mainActIntent = Intent(this, ActivityWelcome::class.java)
            startActivity(mainActIntent)
        }
        finish()
    }
}
