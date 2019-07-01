/**
 * @Class : ActivityProfileEdit
 * @Usage : To manage the profile edit page activity
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditRequest
import com.govida.ui_section.home_section.profile_section.model.UserProfileEditResponse
import com.govida.ui_section.home_section.profile_section.mvp.ProfileEditMVP
import com.govida.ui_section.home_section.profile_section.mvp.ProfileEditPresenterImplementer
import com.govida.utility_section.AppLogger
import com.govida.utility_section.CommonUtils
import com.govida.utility_section.KeyboardUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ActivityProfileEdit : BaseActivity(), View.OnClickListener, ProfileEditMVP.ProfileEditView,DatePickerDialog.OnDateSetListener {
    private lateinit var mEtFirstName: EditText
    private lateinit var mEtLastName: EditText
    private lateinit var mEtOfficialEmail: EditText
    private lateinit var mEtPersonalEmail: EditText
    private lateinit var mEtPhone: EditText
    private lateinit var mBtnSave: Button
    private lateinit var mAppPreference: AppPreference
    private lateinit var mSpGender: Spinner
    private lateinit var mTvChangePhoto: TextView
    private lateinit var mIvPhoto: ImageView
    private lateinit var imageFilePath: String
    private val GALLERY = 1
    private var REQUEST_CAPTURE_IMAGE = 100
    private lateinit var genderSelected: String
    private lateinit var mProfileEditPresenter: ProfileEditMVP.ProfileEditPresenter
    private lateinit var mEtBirthday:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        mProfileEditPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mProfileEditPresenter.destroyView()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        mAppPreference = AppPreference(this)
        mEtFirstName = findViewById(R.id.et_first_name)
        mEtLastName = findViewById(R.id.et_last_name)
        mEtOfficialEmail = findViewById(R.id.et_official_email)
        mEtPersonalEmail = findViewById(R.id.et_personal_email)
        mEtPhone = findViewById(R.id.et_phone)
        mBtnSave = findViewById(R.id.btn_save)
        mBtnSave.setOnClickListener(this)
        mSpGender = findViewById(R.id.sp_gender)
        mTvChangePhoto = findViewById(R.id.tv_change_photo)
        mTvChangePhoto.setOnClickListener(this)
        mIvPhoto = findViewById(R.id.iv_photo)
        mEtBirthday=findViewById(R.id.et_birthday)
        mEtBirthday.setOnClickListener(this)
        mProfileEditPresenter = ProfileEditPresenterImplementer(this)
        setProfilePicture()
        setGenderData()
        setUserData()
    }

    /**
     *  @Function : setProfilePicture()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to set the profile picture from URL
     *  @Author   : 1769
     */
    private fun setProfilePicture() {
        if (mAppPreference.profilePicturePath.isNotEmpty()) {
            Glide.with(this).load(mAppPreference.profilePicturePath).into(mIvPhoto)
        } else {
            mIvPhoto.setImageResource(R.drawable.profile_pic_3)
        }
    }

    /**
     *  @Function : setGenderData()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to set the profile gender from shared preference
     *  @Author   : 1769
     */
    private fun setGenderData() {
        val gender = listOf<String>("Male", "Female", "Other")
        val dataAdapter:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gender)
        mSpGender.adapter = dataAdapter
        mSpGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                genderSelected = gender[position]
//                appPreference.gender = gender[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    /**
     *  @Function : setUserData()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Set the user information to the respective fields
     *  @Author   : 1769
     */
    private fun setUserData() {
        mEtFirstName.setText(mAppPreference.userFirstName)
        mEtLastName.setText(mAppPreference.userLastName)
        mEtOfficialEmail.setText(mAppPreference.userEmailId)
        mEtPersonalEmail.setText(mAppPreference.personalEmail)
        mEtBirthday.setText(mAppPreference.userDob)
        mEtPhone.setText(mAppPreference.phone)
        if (mAppPreference.gender.equals("Male",true))
            mSpGender.setSelection(0)
        else if (mAppPreference.gender.equals("Female",true))
            mSpGender.setSelection(1)
        else
            mSpGender.setSelection(2)
        KeyboardUtils.hideSoftInput(this)
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
                R.id.btn_save -> {
                    if(!CommonUtils.isEmailValid(mEtOfficialEmail.text.toString().trim()) && !CommonUtils.isEmailValid(mEtPersonalEmail.text.toString().trim()) && mEtOfficialEmail.text.isNotEmpty() && mEtPersonalEmail.text.isNotEmpty()) {
                        onError(R.string.both_email_error)
                    } else if (!CommonUtils.isEmailValid(mEtOfficialEmail.text.toString().trim()) && mEtOfficialEmail.text.isNotEmpty()){
                        onError (R.string.official_email_error)
                    } else if (!CommonUtils.isEmailValid(mEtPersonalEmail.text.toString().trim()) && mEtPersonalEmail.text.isNotEmpty()) {
                        onError (R.string.personal_email_error)
                    } else {
                        val userProfileEditRequest = UserProfileEditRequest()
                        userProfileEditRequest.contactNumber = mEtPhone.text.toString()
                        userProfileEditRequest.firstName = mEtFirstName.text.toString()
                        userProfileEditRequest.lastName = mEtLastName.text.toString()
                        userProfileEditRequest.personalEmail = mEtPersonalEmail.text.toString()
                        userProfileEditRequest.gender = genderSelected
                        userProfileEditRequest.dob=mEtBirthday.text.toString()
                        mProfileEditPresenter.onProfileEditRequested(mAppPreference.authorizationToken, userProfileEditRequest)
                    }
                } R.id.tv_change_photo -> {
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
//                    showPictureDialog()
                }
                R.id.et_birthday->{
                    var calendar=Calendar.getInstance(TimeZone.getDefault())
                    var thisYear = calendar.get(Calendar.YEAR)
                    var thisMonth = calendar.get(Calendar.MONTH)
                    var thisdate = calendar.get(Calendar.DATE)
                    SpinnerDatePickerDialogBuilder()
                        .context(this)
                        .callback(this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(thisYear,thisMonth,thisdate)
                        .maxDate(thisYear,thisMonth,thisdate)
                        .minDate(1960, 0, 1)
                        .build()
                        .show();

                }
            }
        }
    }


    /**
     *  @Function : onDateSet()
     *  @params   : view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int
     *  @Return   : void
     * 	@Usage	  : Used to get selected date from calender
     *  @Author   : 1276
     */
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US);
        AppLogger.d(""+simpleDateFormat.format(calendar.time))
        mEtBirthday.setText(""+simpleDateFormat.format(calendar.time))
    }


    /**
     *  @Function : showPictureDialog()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Used to show the Action Dialogs to the user (while changing the profile picture)
     *  @Author   : 1769
     */
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> requestReadWritePermission()
                1 -> requestCameraPermission()
            }
        }
        pictureDialog.show()
    }

    /**
     *  @Function : choosePhotoFromGallary()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Start the action to get image from local file explorer
     *  @Author   : 1769
     */
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    /**
     *  @Function : createImageFile()
     *  @params   : void
     *  @Return   : File
     * 	@Usage	  : Used to create the Image file and Path to store the Picture taken from camera
     *  @Author   : 1769
     */
    private fun createImageFile(): File? {
        try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName: String = "IMG_" + timeStamp + "_"
            val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image: File = File.createTempFile(imageFileName, ".jpg", storageDir)
            imageFilePath = image.absolutePath
            return image
        } catch (e: Exception) {
            return null
        }
    }

    /**
     *  @Function : openCameraIntent()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Start the action for opening camera and taking picture with storage file path
     *  @Author   : 1769
     */
    private fun openCameraIntent() {
        val pictureIntent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
            }
            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(this, this.packageName + ".provider", photoFile)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
            }
        }
    }

    /**
     *  @Function : onActivityResult()
     *  @params   : requestCode:Int, resultCode:Int, data: Intent
     *  @Return   : void
     * 	@Usage	  : Manage the result from multiple actions
     *  @Author   : 1769
     */
    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAPTURE_IMAGE) {
                mAppPreference.profilePicturePath = imageFilePath
                Glide.with(this).load(mAppPreference.profilePicturePath).into(mIvPhoto)
            }
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data!!.data
                    var path: String = getFilePath(this, contentURI)
                    var imgFile: File = File(path)
                    imageFilePath = imgFile.absolutePath
                    mAppPreference.profilePicturePath = imageFilePath
                    Glide.with(this).load(imageFilePath).into(mIvPhoto)
                }
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                var result: CropImage.ActivityResult  = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    var resultUri: Uri  = result.getUri()
                    var imageFile: File = File(resultUri.getPath())
                    imageFilePath = imageFile.absolutePath
                    mAppPreference.profilePicturePath = imageFilePath
                    Glide.with(this).load(imageFilePath).into(mIvPhoto)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    var error: Exception  = result.getError()
                }
              }
        }
    }

    /**
     *  @Function : requestReadWritePermission()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Asking dynamic permissions for External Storage Read and Write
     *  @Author   : 1769
     */
    private fun requestReadWritePermission () {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        choosePhotoFromGallary()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            }).check()
    }

    /**
     *  @Function : requestCameraPermission()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Asking dynamic permission for accessing system camera
     *  @Author   : 1769
     */
    private fun requestCameraPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    openCameraIntent()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    /**
     *  @Function : showAlert()
     *  @params   : message: String
     *  @Return   : void
     * 	@Usage	  : To make an alert box and get user response for that alert
     *  @Author   : 1769
     */
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setCancelable(false)
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
            finish()
        }
        builder.show()
    }

    /**
     *  @Function : showAlert()
     *  @params   : message: String
     *  @Return   : void
     * 	@Usage	  : To make an alert box and get user response for that alert
     *  @Author   : 1769
     */
    private fun showAlertForFailure(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setCancelable(false)
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    /**
     *  @Function : onEditProfileSuccessfully()
     *  @params   : userDetails: UserProfileEditResponse.Data
     *  @Return   : void
     * 	@Usage	  : Execute if update profile api success
     *  @Author   : 1769
     */
    override fun onEditProfileSuccessfully(userDetails: UserProfileEditResponse.Data) {
        mAppPreference.userFirstName = userDetails.firstName!!
        mAppPreference.userLastName = userDetails.lastName!!
        mAppPreference.userEmailId = userDetails.officialEmail!!
        mAppPreference.personalEmail = userDetails.personalEmail!!
        mAppPreference.phone = userDetails.contactNumber!!
        mAppPreference.userDob = userDetails.dob!!
        mAppPreference.gender = userDetails.gender!!
        mAppPreference.profileCompletionStatus = userDetails.profileCompletionPercentage!!
        showAlert(getString(R.string.edit_message))
    }

    /**
     *  @Function : onEditProfileFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : Execute if update profile api fails
     *  @Author   : 1769
     */
    override fun onEditProfileFailed(warnings: String) {
        if (warnings.isNullOrEmpty()) {
            showAlertForFailure(getString(R.string.checkout_api_failed))
        } else {
            showAlertForFailure(warnings)   
        }
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }
}