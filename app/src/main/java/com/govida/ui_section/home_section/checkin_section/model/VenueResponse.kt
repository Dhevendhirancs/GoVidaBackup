/**
 * @Class : VenueResponse
 * @Usage : This class is used for providing pojo functionality
 * @Author : 1769
 */
package com.govida.ui_section.home_section.checkin_section.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VenueResponse {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("responseBody")
    @Expose
    var responseBody: ResponseBody? = null

    inner class ResponseBody {

        @SerializedName("data")
        @Expose
        var data: List<Data>? = null

    }

    inner class Data {

        @SerializedName("venue_name")
        @Expose
        var venueName: String? = null
        @SerializedName("address")
        @Expose
        var address: String? = null
        @SerializedName("longitude")
        @Expose
        var longitude: Double? = null
        @SerializedName("latitude")
        @Expose
        var latitude: Double? = null
        @SerializedName("govida_verified")
        @Expose
        var govidaVerified: Boolean? = null
        @SerializedName("venue_type")
        @Expose
        var venueType: String? = null
        @SerializedName("google_place_id")
        @Expose
        var googlePlaceId: String? = null

        @SerializedName("user_latitude")
        @Expose
        var userLatitude: String? = null

        @SerializedName("user_longitude")
        @Expose
        var userLongitude: String? = null

    }
}