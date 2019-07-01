package com.govida.ui_section.home_section.challenges_section.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChallengeAcceptResponse {
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

        @SerializedName("result")
        @Expose
        var result: String? = null

    }
}