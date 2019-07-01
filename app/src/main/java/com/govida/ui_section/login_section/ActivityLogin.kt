/**
 * @Class : ActivityLogin
 * @Usage : This activity is used for providing login functionality.
 * @Author : 1276
 */
package com.govida.ui_section.login_section

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.be_connected.ActivityBeConnected
import com.govida.ui_section.be_connected.model.BonusPointResponseObject
import com.govida.ui_section.forgot_password_section.ActivityForgotPassword
import com.govida.ui_section.login_section.models.TokenRequest
import com.govida.ui_section.login_section.models.TokenResponse
import com.govida.ui_section.login_section.models.UserDetailResponse
import com.govida.ui_section.login_section.mvp.LoginMVP
import com.govida.ui_section.login_section.mvp.LoginPresenterImplementer
import com.govida.ui_section.registration_section.ActivityRegister
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import com.govida.utility_section.CommonUtils
import com.govida.utility_section.KeyboardUtils
import java.text.SimpleDateFormat
import java.util.*

class ActivityLogin : BaseActivity(),View.OnClickListener,LoginMVP.LoginView {

    private lateinit var mTvLinkRegister:Button
    private lateinit var mEtEmailAddress:EditText
    private lateinit var mEtPassword:EditText
    private lateinit var mIvShowPassword:ImageView
    private lateinit var mBtnLogin:Button
    private lateinit var mTvLinkForgotPassword:TextView
    private var mIsPasswordShow:Boolean=false
    private lateinit var mLoginPresenter:LoginMVP.LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        mLoginPresenter.attachView(this)
    }

    override fun onDestroy() {
        mLoginPresenter.destroyView()
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
        mTvLinkRegister=findViewById(R.id.login_tv_link_register)
        mEtEmailAddress=findViewById(R.id.login_et_email)
        mEtPassword=findViewById(R.id.login_et_password)
        mIvShowPassword=findViewById(R.id.login_iv_show_password)
        mBtnLogin=findViewById(R.id.login_btn_login)
        mTvLinkForgotPassword=findViewById(R.id.login_tv_link_forgot_password)
        mLoginPresenter=LoginPresenterImplementer(this)
        mTvLinkRegister.setOnClickListener(this)
        mIvShowPassword.setOnClickListener(this)
        mBtnLogin.setOnClickListener(this)
        mTvLinkForgotPassword.setOnClickListener(this)
        val appPreference= AppPreference(this)
        appPreference.isUserOnLoginPage = true
        val extraDetails=intent.extras
        if(extraDetails!=null){
            val tempEmail=extraDetails.getString(AppConstants.TEMP_EMAIL)
            if(tempEmail!=null){
                mEtEmailAddress.setText(tempEmail)
            }

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
                R.id.login_tv_link_register ->{
                    val mainActIntent = Intent(this, ActivityRegister::class.java)
                    startActivity(mainActIntent)
                }
                R.id.login_iv_show_password->{
                    KeyboardUtils.hideSoftInput(this)
                    if(mIsPasswordShow){
                        // at start password show is true and user want to hide password hence hide password
                        mIsPasswordShow=false
                        mIvShowPassword.setImageResource(R.drawable.ic_eye_unselected)
                        mEtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                    else{
                        // at start password show is false and user want to see password hence show password
                        mIsPasswordShow=true
                        mIvShowPassword.setImageResource(R.drawable.ic_eye_selected)
                        mEtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }
                }
                R.id.login_btn_login->{
                    if(isDataValid()) {
                        val user = TokenRequest(mEtEmailAddress.text.toString().trim(),mEtPassword.text.toString().trim(),"password")
                        mLoginPresenter.onLoginButtonClicked(user)
                    }

                }
                R.id.login_tv_link_forgot_password->{
                    val mainActIntent = Intent(this, ActivityForgotPassword::class.java)
                    startActivity(mainActIntent)
                }

            }
        }
    }

    /**
     *  @Function : onLoginSuccessful()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : open next screen as the functionality of this screen complete
     *  @Author   : 1276
     */
    override fun onLoginSuccessful(userDetails: UserDetailResponse.Data) {
        val appPreference= AppPreference(this)
        appPreference.setUserLogin(true)
        storeLoginDate()
        if(userDetails!=null){
            if(userDetails.firstName.isNullOrEmpty()){
                appPreference.userFirstName=""
            }
            else{
                appPreference.userFirstName=userDetails.firstName!!
            }
            if(userDetails.lastName.isNullOrEmpty()){
                appPreference.userLastName=""
            }
            else{
                appPreference.userLastName=userDetails.lastName!!
            }
            if(userDetails.officialEmail.isNullOrEmpty()){
                appPreference.userEmailId=""
            }
            else{
                appPreference.userEmailId=userDetails.officialEmail!!
            }
            if(userDetails.dob.isNullOrEmpty()){
                appPreference.userDob=""
            }
            else{
                appPreference.userDob=userDetails.dob!!
            }
            if(userDetails.personalEmail.isNullOrEmpty()){
                appPreference.personalEmail=""
            }
            else{
                appPreference.personalEmail=userDetails.personalEmail!!
            }
            if(userDetails.contactNumber.isNullOrEmpty()){
                appPreference.phone=""
            }
            else{
                appPreference.phone=userDetails.contactNumber!!
            }
            if(userDetails.gender.isNullOrEmpty()){
                appPreference.gender=""
            }
            else{
                appPreference.gender=userDetails.gender!!
            }
            appPreference.employeeId = userDetails.id!!
            appPreference.profileCompletionStatus=userDetails.profileCompletionPercentage!!


        }
    }
    override fun storeTokenData(receivedToken: TokenResponse?) {
        val appPreference=AppPreference(this)
        appPreference.authorizationToken=receivedToken!!.accessToken
        appPreference.refreshToken=receivedToken!!.refreshToken

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    AppLogger.d("getInstanceId failed"+task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                appPreference.notificationDeviceToken=token!!
                appPreference.notificationDeviceId= task.result?.id!!
            })
        // [END retrieve_current_token]

    }

    /**
     *  @Function : storeLoginDate()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Store login date when user logged in first time
     *  @Author   : 1276
     */
    private fun storeLoginDate() {
        val appPreference=AppPreference(this)
        //val dateToday = Date(System.currentTimeMillis());
        //val dateInMili=dateToday.time;
        //appPreference.userLastSyncActivityDate=loginDate.format(now)

        val now = Calendar.getInstance().time
        val startFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val startDate = startFormat.format(now) + " 00:00:00"
        val fmt = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH)
        val dateInMili=fmt.parse(startDate).time


        appPreference.userLastSyncActivityDate=dateInMili
    }


    override fun moveToNextPage() {
        val mainActIntent = Intent(this, ActivityBeConnected::class.java)
        startActivity(mainActIntent)
        finish()
    }

    override fun saveBonusDetails(receivedBonusDetails: BonusPointResponseObject.Data) {
        val appPreference= AppPreference(this)
        appPreference.showOnboardingPointsAlert(receivedBonusDetails.onboarding!!)
        appPreference.showDeviceIntegrationPointsAlert(receivedBonusDetails.deviceIntegration!!)
        appPreference.showGPSPointsAlert(receivedBonusDetails.gps!!)
        appPreference.isProfileCompletionBonusReceived=receivedBonusDetails.profileCompletion!!
        appPreference.isCheckInBonusReceived=receivedBonusDetails.firstCheckIn!!
        appPreference.isFirstChallengeCardShow=receivedBonusDetails.firstChallenge!!
        moveToNextPage()
    }

    override fun saveBonusDetailsFirstTime(receivedBonusDetails: BonusPointResponseObject.Data) {
        val appPreference= AppPreference(this)
        appPreference.showGPSPointsAlert(receivedBonusDetails.gps!!)
        appPreference.showDeviceIntegrationPointsAlert(receivedBonusDetails.deviceIntegration!!)
        appPreference.showOnboardingPointsAlert(receivedBonusDetails.onboarding!!)
        showAlert()
    }

    /**
     *  @Function : onLoginFailed()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : display error when login is failed
     *  @Author   : 1276
     */
    override fun onLoginFailed() {

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
        return if (mEtEmailAddress.text.toString().isNotEmpty() || mEtPassword.text.toString().isNotEmpty()) {
                    if(CommonUtils.isEmailValid(mEtEmailAddress.text.toString().trim())){
                        if (mEtPassword.text.toString().isNotEmpty() && mEtPassword.text.toString() != "null") {
                            true
                        } else {
                            onError(R.string.email_error)
                            false
                        }
                    } else{
                        onError(R.string.email_error)
                        false
                    }
                } else {
                    onError(R.string.empty_error)
                    false
                }
    }

    /**
     *  @Function : showAlert()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Display alert message for user.
     *  @Author   : 1276
     */
    private fun showAlert() {
        //showMessage("")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setCancelable(false)
        builder.setMessage(resources.getString(R.string.login_success))
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
            val mainActIntent = Intent(this, ActivityBeConnected::class.java)
            startActivity(mainActIntent)
            finish()

        }
        builder.show()
    }
}
