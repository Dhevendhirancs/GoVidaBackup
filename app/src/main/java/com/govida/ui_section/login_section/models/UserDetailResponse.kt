/*
  @Class : UserDetailResponse
 * @Usage : This model class is for providing user details response object
 * @Author : 1276
 */
package com.govida.ui_section.login_section.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDetailResponse {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("status_code")
    @Expose
    var statusCode: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("responseBody")
    @Expose
    var responseBody: ResponseBody? = null

    inner class ResponseBody {

        @SerializedName("data")
        @Expose
        var data: Data? = null

    }

    inner class Data {
        @SerializedName("first_name")
        @Expose
        var firstName: String? = null

        @SerializedName("last_name")
        @Expose
        var lastName: String? = null

        @SerializedName("official_email")
        @Expose
        var officialEmail: String? = null

        @SerializedName("dob")
        @Expose
        var dob: String? = null

        @SerializedName("personal_email")
        @Expose
        var personalEmail: String? = null

        @SerializedName("contact_number")
        @Expose
        var contactNumber: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("profile_completion_percentage")
        @Expose
        var profileCompletionPercentage: Int? = null

        @SerializedName("designation")
        @Expose
        var designation: String? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null


    }

}