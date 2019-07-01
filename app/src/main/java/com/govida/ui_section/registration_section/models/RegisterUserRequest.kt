/**
 * @Class : RegisterUserRequest
 * @Usage : This class is used as pogo for User object.
 * @Author : 1276
 */
package com.govida.ui_section.registration_section.models

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(@SerializedName("first_name") var first_name: String, @SerializedName("last_name") var last_name: String, @SerializedName("official_email") var official_email: String)