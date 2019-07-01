/**
 * @Class : ActivityProfileView
 * @Usage : This activity is used for managing profile view page elements.
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.base_class_section.BaseActivity
import org.w3c.dom.Text

class ActivityProfileView : BaseActivity(), View.OnClickListener {

    private lateinit var mTvFirstname: TextView
    private lateinit var mTvPhone: TextView
    private lateinit var mTvOfficialEmail: TextView
    private lateinit var mTvPersonalEmail: TextView
    private lateinit var mTvDataOfBirth: TextView
    private lateinit var mTvGender: TextView
    private lateinit var mBtnProfileView: Button
    private lateinit var appPreference: AppPreference
    private lateinit var mIvPhoto: ImageView
    private lateinit var mLlPreferredVenues: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
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
        mTvFirstname = findViewById(R.id.tv_firstname)
        mTvPhone = findViewById(R.id.tv_phone)
        mTvOfficialEmail = findViewById(R.id.tv_official_email)
        mTvPersonalEmail = findViewById(R.id.tv_personal_email)
        mTvDataOfBirth = findViewById(R.id.tv_date_of_birth)
        mTvGender = findViewById(R.id.tv_gender)
        mBtnProfileView = findViewById(R.id.btn_profile_view)
        mBtnProfileView.setOnClickListener(this)
        mIvPhoto = findViewById(R.id.iv_photo)
        mLlPreferredVenues = findViewById(R.id.ll_preferred_venues)
        mLlPreferredVenues.setOnClickListener(this)
    }

    /**
     *  @Function : setProfilePicture()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to set user profile picture from URL
     *  @Author   : 1769
     */
    private fun setProfilePicture() {
        appPreference = AppPreference(this)
        if (appPreference.profilePicturePath.isNotEmpty()) {
            Glide.with(this).load(appPreference.profilePicturePath).into(mIvPhoto)
        } else {
            mIvPhoto.setImageResource(R.drawable.profile_pic_3)
        }
    }

    /**
     *  @Function : onResume()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Manage the flow when user returns to this page
     *  @Author   : 1769
     */
    override fun onResume() {
        super.onResume()
        setUserdata()
        setProfilePicture()
    }

    /**
     *  @Function : setUserData()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Set the user information to the respective fields
     *  @Author   : 1769
     */
    private fun setUserdata() {
        appPreference = AppPreference(this)
        mTvFirstname.text = appPreference.userFirstName
        mTvOfficialEmail.text = appPreference.userEmailId
        mTvPersonalEmail.text = appPreference.personalEmail
        mTvPhone.text = appPreference.phone
        mTvGender.text = appPreference.gender
        mTvDataOfBirth.text = appPreference.userDob
    }

    /**
     *  @Function : onClick()
     *  @params   : View
     *  @Return   : void
     * 	@Usage	  : listener function definition
     *  @Author   : 1769
     */
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_profile_view -> {
                    val mainActIntent = Intent(this, ActivityProfileEdit::class.java)
                    startActivity(mainActIntent)
                } R.id.ll_preferred_venues -> {
                    val mainActIntent = Intent(this, ActivityPreferredVenues::class.java)
                    startActivity(mainActIntent)
                }
            }
        }
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }
}