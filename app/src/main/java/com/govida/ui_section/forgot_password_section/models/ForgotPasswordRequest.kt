/**
 * @Class : ForgotPasswordRequest
 * @Usage : This model class is for providing forgot password request object
 * @Author : 1276
 */
package com.govida.ui_section.forgot_password_section.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPasswordRequest(
    @field:SerializedName("username")
    @field:Expose
    var userName: String?
)
