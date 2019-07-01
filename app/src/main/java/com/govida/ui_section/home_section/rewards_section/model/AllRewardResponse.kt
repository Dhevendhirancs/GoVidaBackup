/**
 * @Class : AllRewardResponse
 * @Usage : This class is used for providing pojo functionality and
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllRewardResponse {
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
        var data: MutableList<AllRewardEntity>? = null
        @SerializedName("myGoVPs")
        @Expose
        var myGoVPs: Int? = null
    }
}
