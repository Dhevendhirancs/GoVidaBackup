/**
 * @Class : ActivityTermsAndConditions
 * @Usage : This activity is used for providing terms and condition to user.
 * @Author : 1276
 */

package com.govida.ui_section.terms_and_condition_section

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.govida.R
import com.govida.ui_section.login_section.ActivityLogin
import com.govida.utility_section.AppConstants

class ActivityTermsAndConditions : AppCompatActivity(),View.OnClickListener {
    private lateinit var mBtnAccept:Button
    private var mFromScreen:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        setupUI()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */
    private fun setupUI() {
        mBtnAccept=findViewById(R.id.terms_btn_accept)
        mBtnAccept.setOnClickListener(this)
        val extraDetails=intent.extras
        if(extraDetails!=null){
            mFromScreen=extraDetails.getString(AppConstants.FROM_SCREEN)
        }
    }

    /**
     *  @Function : onClick()
     *  @params   : v: View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1276
     */
    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.terms_btn_accept ->{
                    if(mFromScreen!=null){
                        if(mFromScreen.equals("welcome_page")){
                            val mainActIntent = Intent(this, ActivityLogin::class.java)
                            startActivity(mainActIntent)
                            finish()
                        }
                        else if(mFromScreen.equals("register_page")){
                            finish()
                        }
                    }else{
                        val mainActIntent = Intent(this, ActivityLogin::class.java)
                        startActivity(mainActIntent)
                        finish()
                    }

                }
            }
        }
    }
}
