/**
 * @Class : UserProfileEditRequest
 * @Usage : This class is used to create user request for edit profile
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserProfileEditRequest {

    @SerializedName("contact_number")
    @Expose
    var contactNumber: String? = null
    @SerializedName("dob")
    @Expose
    var dob: String? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("gender")
    @Expose
    var gender: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("personal_email")
    @Expose
    var personalEmail: String? = null

}