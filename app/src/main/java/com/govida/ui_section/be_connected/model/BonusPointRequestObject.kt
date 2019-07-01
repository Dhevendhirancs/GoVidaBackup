/**
 * @Class : BonusPointRequestObject
 * @Usage : This class is used for providing pojo functionality
 * @Author : 1276
 */
package com.govida.ui_section.be_connected.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BonusPointRequestObject {

    @SerializedName("device_integration")
    @Expose
    var deviceIntegration: Boolean? = null
    @SerializedName("gps")
    @Expose
    var gps: Boolean? = null
    @SerializedName("onboarding")
    @Expose
    var onboarding: Boolean? = null

}
