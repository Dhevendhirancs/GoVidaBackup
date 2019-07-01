/**
 * @Class : LeaderboardResponse
 * @Usage : This class is used for providing pojo functionality and
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LeaderboardResponse {
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
        @SerializedName("topLeaderboard")
        @Expose
        var topLeaderboard: TopLeaderboard? = null
    }

    inner class TopLeaderboard {

        @SerializedName("YEARLY")
        @Expose
        var yearly: MutableList<Yearly>? = null
        @SerializedName("WEEKLY")
        @Expose
        var weekly: MutableList<Weekly>? = null
        @SerializedName("DAILY")
        @Expose
        var daily: MutableList<Daily>? = null
        @SerializedName("MONTHLY")
        @Expose
        var monthly: MutableList<Monthly>? = null

        inner class Daily {

            @SerializedName("employeeId")
            @Expose
            var employeeId: Int? = null
            @SerializedName("employeeName")
            @Expose
            var employeeName: String? = null
            @SerializedName("rank")
            @Expose
            var rank: Int? = null
            @SerializedName("activityType")
            @Expose
            var activityType: String? = null
            @SerializedName("stepsWalked")
            @Expose
            var stepsWalked: Int? = null
            @SerializedName("distanceTravelled")
            @Expose
            var distanceTravelled: Double? = null

        }

        inner class Weekly {

            @SerializedName("employeeId")
            @Expose
            var employeeId: Int? = null
            @SerializedName("employeeName")
            @Expose
            var employeeName: String? = null
            @SerializedName("rank")
            @Expose
            var rank: Int? = null
            @SerializedName("activityType")
            @Expose
            var activityType: String? = null
            @SerializedName("stepsWalked")
            @Expose
            var stepsWalked: Int? = null
            @SerializedName("distanceTravelled")
            @Expose
            var distanceTravelled: Double? = null

        }

        inner class Monthly {

            @SerializedName("employeeId")
            @Expose
            var employeeId: Int? = null
            @SerializedName("employeeName")
            @Expose
            var employeeName: String? = null
            @SerializedName("rank")
            @Expose
            var rank: Int? = null
            @SerializedName("activityType")
            @Expose
            var activityType: String? = null
            @SerializedName("stepsWalked")
            @Expose
            var stepsWalked: Int? = null
            @SerializedName("distanceTravelled")
            @Expose
            var distanceTravelled: Double? = null

        }

        inner class Yearly {

            @SerializedName("employeeId")
            @Expose
            var employeeId: Int? = null
            @SerializedName("employeeName")
            @Expose
            var employeeName: String? = null
            @SerializedName("rank")
            @Expose
            var rank: Int? = null
            @SerializedName("activityType")
            @Expose
            var activityType: String? = null
            @SerializedName("stepsWalked")
            @Expose
            var stepsWalked: Int? = null
            @SerializedName("distanceTravelled")
            @Expose
            var distanceTravelled: Double? = null

        }

    }

}




