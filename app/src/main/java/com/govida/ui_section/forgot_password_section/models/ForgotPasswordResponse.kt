/**
 * @Class : ForgotPasswordResponse
 * @Usage : This model class is for providing forgot password response object
 * @Author : 1276
 */
package com.govida.ui_section.forgot_password_section.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPasswordResponse {
    @SerializedName("status_code")
    @Expose
    var status_code: String? = null

    @SerializedName("responseBody")
    @Expose
    var responseBody: ResponseBody? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    override fun toString(): String {
        return "ClassPojo [status_code = $status_code, responseBody = $responseBody, message = $message, status = $status]"
    }

    inner class ResponseBody {
        @SerializedName("data")
        @Expose
        var data: Data? = null
    }

    inner class Data {

        @SerializedName("result")
        @Expose
        var result: String? = null

    }

}
