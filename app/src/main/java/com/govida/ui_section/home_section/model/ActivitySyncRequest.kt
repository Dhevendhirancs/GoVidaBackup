package com.govida.ui_section.home_section.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class ActivitySyncRequest {
    @SerializedName("last_sync_time")
    @Expose
    var lastSyncTime: String? = null
    @SerializedName("steps_history")
    @Expose
    var stepsHistory: MutableList<StepsHistory>? = null

    inner class StepsHistory {

        @SerializedName("count")
        @Expose
        var count: Int? = null
        @SerializedName("datetime")
        @Expose
        var datetime: String? = null

    }
}
