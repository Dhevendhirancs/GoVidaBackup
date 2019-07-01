package com.govida.ui_section.home_section.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "challenges")
class ChallengesEntity {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    var id: Int? = null

    @SerializedName("challangeType")
    @Expose
    @ColumnInfo(name = "challangeType")
    var challangeType: String? = null

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    var name: String? = null

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    var description: String? = null

    @SerializedName("subtask")
    @Expose
    @ColumnInfo(name = "subtask")
    var subtask: String? = null

    @SerializedName("bonusPoints")
    @Expose
    @ColumnInfo(name = "bonusPoints")
    var bonusPoints: Int? = null

    @SerializedName("completionPoints")
    @Expose
    @ColumnInfo(name = "completionPoints")
    var completionPoints: Int? = null

    @SerializedName("duration")
    @Expose
    @ColumnInfo(name = "duration")
    var duration: String? = null

    @SerializedName("ongoingChallengeFlag")
    @Expose
    @ColumnInfo(name = "ongoingChallengeFlag")
    var ongoingChallengeFlag: Int? = null

    @SerializedName("percentage")
    @Expose
    @ColumnInfo(name = "percentage")
    var percentage: String? = null

    @SerializedName("imageUrl")
    @Expose
    @ColumnInfo(name = "imageUrl")
    var imageURL: String? = null
}
