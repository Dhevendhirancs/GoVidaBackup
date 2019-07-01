/**
 * @Class : RedeemRewardsResponse
 * @Usage : This class is used for providing pojo functionality and
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RedeemRewardsResponse {
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
    var responseBody: RedeemRewardsResponseBody? = null

    class RedeemRewardsResponseBody {
        @SerializedName("data")
        @Expose
        var data: List<RedeemRewardData>? = null
        @SerializedName("myGoVPs")
        @Expose
        var myGoVPs: Int? = null

    }

    class RedeemRewardData {
        @SerializedName("rewardId")
        @Expose
        var rewardId: Int? = null

    }

}
