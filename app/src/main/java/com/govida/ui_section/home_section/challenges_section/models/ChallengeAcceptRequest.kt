package com.govida.ui_section.home_section.challenges_section.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChallengeAcceptRequest {

    @SerializedName("challenge_id")
    @Expose
    var challengeId: Int? = null

}