package com.govida.ui_section.registration_section.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class RegisterUserResponse {
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

    inner class Data : RegisterUserResponse() {
        var result: String? = null

        override fun toString(): String {
            return "ClassPojo [result = $result]"
        }
    }


}
