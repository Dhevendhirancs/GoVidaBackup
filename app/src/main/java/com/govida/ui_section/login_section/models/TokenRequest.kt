/**
 * @Class : TokenRequest
 * @Usage : This model class is for providing token request object
 * @Author : 1276
 */
package com.govida.ui_section.login_section.models

import com.google.gson.annotations.SerializedName

data class TokenRequest(@SerializedName("username") var username: String,
                        @SerializedName("password") var password: String,
                        @SerializedName("grant_type") var grant_type: String)