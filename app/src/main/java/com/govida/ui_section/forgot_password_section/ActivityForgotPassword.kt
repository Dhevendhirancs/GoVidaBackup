/**
 * @Class : ActivityForgotPassword
 * @Usage : This activity is used for providing forgot password functionality.
 * @Author : 1276
 */

package com.govida.ui_section.forgot_password_section

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.govida.R
import com.govida.ui_section.acount_validation_link_section.ActivityLinkShared
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.forgot_password_section.mvp.ForgotPasswordMVP
import com.govida.ui_section.forgot_password_section.mvp.ForgotPasswordPresenterImplementer
import com.govida.ui_section.registration_section.ActivityRegister
import com.govida.utility_section.AppConstants
import com.govida.utility_section.CommonUtils

class ActivityForgotPassword : BaseActivity(), View.OnClickListener,ForgotPasswordMVP.ForgotPasswordView {
    private lateinit var mEtEmail:EditText
    private lateinit var  mBtnReset:Button
    private lateinit var mBtnRegister:Button
    private lateinit var mForgotPasswordPresenter:ForgotPasswordMVP.ForgotPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        mForgotPasswordPresenter.attachView(this)
    }

    override fun onDestroy() {
        mForgotPasswordPresenter.destroyView()
        super.onDestroy()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1276
     */
    private fun setupUI() {
        mEtEmail=findViewById(R.id.forgot_et_email)
        mBtnReset=findViewById(R.id.forgot_btn_reset)
        mBtnRegister=findViewById(R.id.tv_link_register)
        mBtnReset.setOnClickListener(this)
        mBtnRegister.setOnClickListener(this)
        mForgotPasswordPresenter=ForgotPasswordPresenterImplementer(this)
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
                R.id.forgot_btn_reset->{
                    if(isDataValid()){
                        mForgotPasswordPresenter.onEmailLinkSharedButtonClicked(mEtEmail.text.toString().trim())
                    }
                } R.id.tv_link_register->{
                    navigateToRegister()
                }
            }
        }
    }

    /**
     *  @Function : navigateToRegister()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Navigate user to  Register page
     *  @Author   : 1276
     */
    private fun navigateToRegister() {
        val mainActIntent = Intent(this, ActivityRegister::class.java)
        startActivity(mainActIntent)
    }

    /**
     *  @Function : onEmailLinkSharedSuccessfully()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : once email link shared with client we use this function to define next functionality
     *  @Author   : 1276
     */
    override fun onEmailLinkSharedSuccessfully() {
        val mainActIntent = Intent(this, ActivityLinkShared::class.java)
        mainActIntent.putExtra(AppConstants.TEMP_EMAIL,""+mEtEmail.text.toString().trim())
        mainActIntent.putExtra(AppConstants.FROM_SCREEN,"forgot_password_page")
        startActivity(mainActIntent)
    }

    /**
     *  @Function : onEmailLinkSharedFailed()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : email link sharing failed then we use this to notify user
     *  @Author   : 1276
     */
    override fun onEmailLinkSharedFailed(warnings: String) {
        if (warnings.isNotEmpty()) {
            onError(warnings)
        } else {
            onError(getString(R.string.unknown_error))
        }
    }

    /**
     *  @Function : isDataValid()
     *  @params   : void
     *  @Return   : Boolean
     * 	@Usage	  : check for validation of UI return true when data is valid and false when validation failed.
     *  @Author   : 1276
     */
    private fun isDataValid():Boolean{
        hideKeyboard()
        return if(CommonUtils.isEmailValid(mEtEmail.text.toString().trim())){
            true
        } else{
            onError(R.string.valid_email_error)
            false
        }
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }

}
