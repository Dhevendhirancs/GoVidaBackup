/**
 * @Class : ActivityRegister
 * @Usage : This activity is used for providing registration functionality.
 * @Author : 1276
 */

package com.govida.ui_section.registration_section

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import com.govida.R
import com.govida.ui_section.acount_validation_link_section.ActivityLinkShared
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.login_section.ActivityLogin
import com.govida.ui_section.registration_section.models.RegisterUserRequest
import com.govida.ui_section.registration_section.mvp.RegisterMVP
import com.govida.ui_section.registration_section.mvp.RegisterPresenterImplementer
import com.govida.ui_section.terms_and_condition_section.ActivityTermsAndConditions
import com.govida.utility_section.AppConstants
import com.govida.utility_section.CommonUtils

class ActivityRegister : BaseActivity(),View.OnClickListener,RegisterMVP.RegisterView {
    private lateinit var mTvLinkLogin:Button
    private lateinit var mEtFirstName:EditText
    private lateinit var mEtLastName:EditText
    private lateinit var mEtEmail:EditText
    private lateinit var mBtnContinue:Button
    private lateinit var mRlTermsLink:RelativeLayout
    private lateinit var mRegisterPresenter:RegisterMVP.RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        mRegisterPresenter.attachView(this)
    }

    override fun onDestroy() {
        mRegisterPresenter.destroyView()
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
        mTvLinkLogin=findViewById(R.id.register_tv_link_login)
        mEtFirstName=findViewById(R.id.register_et_first_name)
        mEtLastName=findViewById(R.id.register_et_last_name)
        mEtEmail=findViewById(R.id.register_et_email)
        mBtnContinue=findViewById(R.id.register_btn_continue)
        mRlTermsLink=findViewById(R.id.register_rl_terms_link)

        mTvLinkLogin.setOnClickListener(this)
        mBtnContinue.setOnClickListener(this)
        mRlTermsLink.setOnClickListener(this)

        mRegisterPresenter=RegisterPresenterImplementer(this)
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
                R.id.register_tv_link_login ->{
                    val mainActIntent = Intent(this, ActivityLogin::class.java)
                    startActivity(mainActIntent)
                }
                R.id.register_btn_continue->{
                  if(isDataValid()){
                      val user = RegisterUserRequest(mEtFirstName.text.toString().trim(),mEtLastName.text.toString().trim(),mEtEmail.text.toString().trim())
                      mRegisterPresenter.onRegisterButtonClicked(user)
                  }
                }
                R.id.register_rl_terms_link->{
                    val mainActIntent = Intent(this, ActivityTermsAndConditions::class.java)
                    mainActIntent.putExtra(AppConstants.FROM_SCREEN,"register_page")
                    startActivity(mainActIntent)
                }
            }
        }
    }

    /**
     *  @Function : onRegisterSuccessful()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : open next screen as the functionality of this screen complete
     *  @Author   : 1276
     */
    override fun onRegisterSuccessful() {
        val mainActIntent = Intent(this, ActivityLinkShared::class.java)
        mainActIntent.putExtra(AppConstants.FROM_SCREEN,"register_page")
        mainActIntent.putExtra(AppConstants.TEMP_EMAIL,""+mEtEmail.text.toString().trim())
        startActivity(mainActIntent)
        finish()
    }

    /**
     *  @Function : onRegisterFailed()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : show snakbar as error/exception occurred
     *  @Author   : 1276
     */
    override fun onRegisterFailed(errorMsg: String) {
        onError(errorMsg)
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

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
        if(CommonUtils.isNameValid(mEtFirstName.text.toString().trim())){
            return if(CommonUtils.isNameValid(mEtLastName.text.toString().trim())){
                if(CommonUtils.isEmailValid(mEtEmail.text.toString().trim())){
                    true
                } else{
                    onError(R.string.email_error)
                    false
                }
            } else{
                onError(R.string.last_name_error)
                false
            }
        }
        else{
            onError(R.string.first_name_error)
            return false
        }
    }

}
