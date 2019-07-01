/**
 * @Class : BonusPointResponseObject
 * @Usage : This class is used for providing pojo functionality
 * @Author : 1276
 */

package com.govida.ui_section.be_connected.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BonusPointResponseObject {

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

        @SerializedName("onboarding")
        @Expose
        var onboarding: Boolean? = null
        @SerializedName("device_integration")
        @Expose
        var deviceIntegration: Boolean? = null
        @SerializedName("gps")
        @Expose
        var gps: Boolean? = null
        @SerializedName("profile_completion")
        @Expose
        var profileCompletion: Boolean? = null
        @SerializedName("first_check_in")
        @Expose
        var firstCheckIn: Boolean? = null
        @SerializedName("reward_redemption")
        @Expose
        var rewardRedemption: Boolean? = null
        @SerializedName("first_challenge")
        @Expose
        var firstChallenge: Boolean? = null

    }
}
