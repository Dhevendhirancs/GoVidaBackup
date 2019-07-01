package com.govida.ui_section.home_section.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActivitySyncResponse {

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
    @SerializedName("last_sync_time")
    @Expose
    var lastSyncTime: String? = null

    inner class ResponseBody {
        @SerializedName("data")
        @Expose
        var data: Data? = null

    }

    inner class Data {
        @SerializedName("day")
        @Expose
        var day: Day? = null
        @SerializedName("week")
        @Expose
        var week: Week? = null
        @SerializedName("month")
        @Expose
        var month: Month? = null
        @SerializedName("year")
        @Expose
        var year: Year? = null
        @SerializedName("total_GoVp")
        @Expose
        var totalGoVp: TotalGoVp? = null
    }


    inner class DayUserActivity {

        @SerializedName("steps")
        @Expose
        var steps: Int? = null
        @SerializedName("checkIn")
        @Expose
        var checkIn: Int? = null

    }

    inner class WeekUserActivity {

        @SerializedName("steps")
        @Expose
        var steps: Int? = null
        @SerializedName("checkIn")
        @Expose
        var checkIn: Int? = null

    }

    inner class MonthUserActivity {

        @SerializedName("steps")
        @Expose
        var steps: Int? = null
        @SerializedName("checkIn")
        @Expose
        var checkIn: Int? = null

    }

    inner class YearUserActivity {

        @SerializedName("steps")
        @Expose
        var steps: Int? = null
        @SerializedName("checkIn")
        @Expose
        var checkIn: Int? = null

    }

    inner class TotalGoVpsUserActivity {

        @SerializedName("steps")
        @Expose
        var steps: Int? = null
        @SerializedName("checkIn")
        @Expose
        var checkIn: Int? = null

    }


    inner class Day {

        @SerializedName("checkin_count")
        @Expose
        var checkinCount: Int? = null
        @SerializedName("activity")
        @Expose
        var dayActivity: DayUserActivity? = null
        @SerializedName("bonus")
        @Expose
        var bonus: Int? = null
        @SerializedName("charity_earnings")
        @Expose
        var charityEarnings: Double? = null
        @SerializedName("challenge")
        @Expose
        var challengePoint: Int? = null
        @SerializedName("redeemed")
        @Expose
        var redeemed: Int? = null

    }

    inner class Week {

        @SerializedName("checkin_count")
        @Expose
        var checkinCount: Int? = null
        @SerializedName("activity")
        @Expose
        var weekActivity: WeekUserActivity? = null
        @SerializedName("bonus")
        @Expose
        var bonus: Int? = null
        @SerializedName("charity_earnings")
        @Expose
        var charityEarnings: Double? = null
        @SerializedName("challenge")
        @Expose
        var challengePoint: Int? = null
        @SerializedName("redeemed")
        @Expose
        var redeemed: Int? = null
    }

    inner class Month {

        @SerializedName("checkin_count")
        @Expose
        var checkinCount: Int? = null
        @SerializedName("activity")
        @Expose
        var monthActivity: MonthUserActivity? = null
        @SerializedName("bonus")
        @Expose
        var bonus: Int? = null
        @SerializedName("charity_earnings")
        @Expose
        var charityEarnings: Double? = null
        @SerializedName("challenge")
        @Expose
        var challengePoint: Int? = null
        @SerializedName("redeemed")
        @Expose
        var redeemed: Int? = null
    }


    inner class Year {

        @SerializedName("checkin_count")
        @Expose
        var checkinCount: Int? = null
        @SerializedName("activity")
        @Expose
        var yearActivity: YearUserActivity? = null
        @SerializedName("bonus")
        @Expose
        var bonus: Int? = null
        @SerializedName("charity_earnings")
        @Expose
        var charityEarnings: Double? = null
        @SerializedName("challenge")
        @Expose
        var challengePoint: Int? = null
        @SerializedName("redeemed")
        @Expose
        var redeemed: Int? = null
    }

    inner class TotalGoVp {

        @SerializedName("checkin_count")
        @Expose
        var checkinCount: Int? = null
        @SerializedName("activity")
        @Expose
        var totalGoVpsActivity: TotalGoVpsUserActivity? = null
        @SerializedName("bonus")
        @Expose
        var bonus: Int? = null
        @SerializedName("charity_earnings")
        @Expose
        var charityEarnings: Double? = null
        @SerializedName("challenge")
        @Expose
        var challengePoint: Int? = null
        @SerializedName("redeemed")
        @Expose
        var redeemed: Int? = null

    }
}
