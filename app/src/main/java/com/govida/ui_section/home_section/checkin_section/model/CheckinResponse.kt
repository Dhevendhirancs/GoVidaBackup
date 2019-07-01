/**
 * @Class : CheckinResponse
 * @Usage : This class is used for providing pojo functionality
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CheckinResponse {

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

        @SerializedName("checkin_id")
        @Expose
        var checkinId: Int? = null

    }

}
