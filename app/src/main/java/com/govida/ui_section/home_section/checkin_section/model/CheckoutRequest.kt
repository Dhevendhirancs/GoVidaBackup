/**
 * @Class : CheckoutRequest
 * @Usage : To create an object for checkout request
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.model

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(
    @SerializedName("checkin_id")
    var checkin_id: Int,
    @SerializedName("user_latitude")
    var user_latitude: Double,
    @SerializedName("user_longitude")
    var user_longitude: Double)