/**
 * @Class : ActivityChangePassword
 * @Usage : To manage the change password page
 * @Author : 1769
 */
package com.govida.ui_section.setting_section

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.login_section.ActivityLogin
import com.govida.ui_section.setting_section.model.ChangePasswordRequest
import com.govida.ui_section.setting_section.model.ChangePasswordResponse
import com.govida.ui_section.setting_section.mvp.ChangePasswordMVP
import com.govida.ui_section.setting_section.mvp.ChangePasswordPresenterImplementer
import com.govida.utility_section.KeyboardUtils

class ActivityChangePassword : BaseActivity(), View.OnClickListener,ChangePasswordMVP.ChangePasswordView {


    private lateinit var mEtCurrentPassword: EditText
    private lateinit var mEtNewPassword: EditText
    private lateinit var mBtnChangePassword: Button
    private lateinit var mIvShowCurrentPassword: ImageView
    private lateinit var mIvShowNewPassword: ImageView
    private lateinit var mEtConfirmPassword: EditText
    private lateinit var mIvShowConfirmPassword: ImageView
    private var mIsPasswordShow: Boolean = false
    private var mAppDb: AppDatabase? = null
    private lateinit var mPresenter: ChangePasswordPresenterImplementer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        setupUI()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        mEtCurrentPassword = findViewById(R.id.et_current_password)
        mEtNewPassword = findViewById(R.id.et_new_password)
        mEtConfirmPassword = findViewById(R.id.et_confirm_password)
        mBtnChangePassword = findViewById(R.id.btn_change_password)
        mBtnChangePassword.setOnClickListener(this)
        mIvShowCurrentPassword = findViewById(R.id.iv_show_current_password)
        mIvShowCurrentPassword.setOnClickListener(this)
        mIvShowNewPassword = findViewById(R.id.iv_show_new_password)
        mIvShowNewPassword.setOnClickListener(this)
        mIvShowConfirmPassword = findViewById(R.id.iv_show_confirm_password)
        mIvShowConfirmPassword.setOnClickListener(this)
        mAppDb= AppDatabase.getDatabase(this)
        mPresenter= ChangePasswordPresenterImplementer(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        mPresenter.destroyView()
        super.onDestroy()
    }

    /**
     *  @Function : onClick()
     *  @params   : View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_change_password -> {
                if (mEtCurrentPassword.text.isNullOrBlank() || mEtNewPassword.text.isNullOrBlank() || mEtNewPassword.text.isNullOrBlank()) {
                    onError(getString(R.string.empty_fields))
                } else {
                    showAlert(getString(R.string.password_change_alert))
                }
            } R.id.iv_show_current_password -> {
                KeyboardUtils.hideSoftInput(this)
                if(mIsPasswordShow){
                    // at start password show is true and user want to hide password hence hide password
                    mIsPasswordShow=false
                    mIvShowCurrentPassword.setImageResource(R.drawable.ic_eye_unselected)
                    mEtCurrentPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                else{
                    // at start password show is false and user want to see password hence show password
                    mIsPasswordShow=true
                    mIvShowCurrentPassword.setImageResource(R.drawable.ic_eye_selected)
                    mEtCurrentPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
            } R.id.iv_show_new_password -> {
                KeyboardUtils.hideSoftInput(this)
                if(mIsPasswordShow){
                    // at start password show is true and user want to hide password hence hide password
                    mIsPasswordShow=false
                    mIvShowNewPassword.setImageResource(R.drawable.ic_eye_unselected)
                    mEtNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                else{
                    // at start password show is false and user want to see password hence show password
                    mIsPasswordShow=true
                    mIvShowNewPassword.setImageResource(R.drawable.ic_eye_selected)
                    mEtNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
            } R.id.iv_show_confirm_password -> {
                if(mIsPasswordShow){
                    // at start password show is true and user want to hide password hence hide password
                    mIsPasswordShow=false
                    mIvShowConfirmPassword.setImageResource(R.drawable.ic_eye_unselected)
                    mEtConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                else{
                    // at start password show is false and user want to see password hence show password
                    mIsPasswordShow=true
                    mIvShowConfirmPassword.setImageResource(R.drawable.ic_eye_selected)
                    mEtConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
            }
        }
    }

    /**
     *  @Function : showAlert()
     *  @params   : message: String
     *  @Return   : void
     * 	@Usage	  : default alert box with only positive button
     *  @Author   : 1769
     */
    fun showAlert(alertMsg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GoVida")
        builder.setMessage(alertMsg)
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, which ->
            val appPreference=AppPreference(this)
            val changePassRequest=ChangePasswordRequest()
            changePassRequest.currentPassword=mEtCurrentPassword.text.toString()
            changePassRequest.newPassword=mEtNewPassword.text.toString()
            changePassRequest.confirmPassword=mEtConfirmPassword.text.toString()
            mPresenter.onRequestLeaderboard(appPreference.authorizationToken,changePassRequest)
        }

        builder.show()
    }

    /**
     * @Class : ResetDataBase
     * @Usage : This class is used for performing reset operation on database
     * @Author : 1276
     */
    inner class ResetDataBase : AsyncTask<Void, Void, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            hideKeyboard()
            showLoading()
        }

        override fun doInBackground(vararg params: Void?): Boolean? {
            mAppDb?.challengeDao()?.onResetChallenge()
            mAppDb?.rewardDao()?.deleteAllRewards()
            mAppDb?.myRewardsDao()?.deleteMyRewards()
            return true
        }
        override fun onPostExecute(receivedData:  Boolean?) {
            hideLoading()
            val appPreference= AppPreference(this@ActivityChangePassword)
            appPreference.clearAllPreferences()
            val mainActIntent = Intent(applicationContext, ActivityLogin::class.java)
            mainActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            mainActIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            mainActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(mainActIntent)
            finish()
        }
    }
    override fun onChangePasswordSuccessful(result: ChangePasswordResponse.Data?) {
        ResetDataBase().execute()
    }

    override fun onChangePasswordFailed(errorMsg: String) {

    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }
}