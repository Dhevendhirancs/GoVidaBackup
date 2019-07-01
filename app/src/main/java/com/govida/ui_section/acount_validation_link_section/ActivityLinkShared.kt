/**
 * @Class : ActivityLinkShared
 * @Usage : This activity is used for providing information to user that link successfully sent on email address.
 * @Author : 1276
 */

package com.govida.ui_section.acount_validation_link_section

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.govida.R
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.login_section.ActivityLogin
import com.govida.utility_section.AppConstants


class ActivityLinkShared : BaseActivity(),View.OnClickListener {
    private lateinit var mBtnGotIt:Button
    private lateinit var mTvEmail:TextView
    private var mFromScreen:String? = null
    private var mTempEmail:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_shared)
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
        mBtnGotIt=findViewById(R.id.link_btn_got_it)
        mBtnGotIt.setOnClickListener(this)
        mTvEmail=findViewById(R.id.link_tv_email)
        val extraDetails=intent.extras
        if(extraDetails!=null){
            mFromScreen=extraDetails.getString(AppConstants.FROM_SCREEN)
            mTempEmail=extraDetails.getString(AppConstants.TEMP_EMAIL)
            mTvEmail.text=mTempEmail
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
        if (v!=null){
            when(v.id){
                R.id.link_btn_got_it->{
                    val mainActIntent = Intent(this, ActivityLogin::class.java)
                    mainActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    mainActIntent.putExtra(AppConstants.TEMP_EMAIL,""+mTvEmail.text.toString().trim())
                    startActivity(mainActIntent)
                    finish()
                }
            }
        }
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String?) {
    }

}
