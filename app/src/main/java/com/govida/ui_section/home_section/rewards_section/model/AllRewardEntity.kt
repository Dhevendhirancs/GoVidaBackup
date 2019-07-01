/**
 * @Class : AllRewardEntity
 * @Usage : This class is used for providing pojo functionality and
 * @Author : 1769
 */
package com.govida.ui_section.home_section.rewards_section.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "allRewards")
class AllRewardEntity {

    @PrimaryKey
    @SerializedName("rewardId")
    @Expose
    @ColumnInfo(name = "rewardId")
    var rewardId: Int? = null

    @SerializedName("employerId")
    @Expose
    @ColumnInfo(name = "employerId")
    var employerId: Int? = null

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title: String? = null

    @SerializedName("subTitle")
    @Expose
    @ColumnInfo(name = "subTitle")
    var subTitle: String? = null

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    var description: String? = null

    @SerializedName("imageUrl")
    @Expose
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = null

    @SerializedName("value")
    @Expose
    @ColumnInfo(name = "value")
    var value: Int? = null

    @SerializedName("pointsRequired")
    @Expose
    @ColumnInfo(name = "pointsRequired")
    var pointsRequired: Int? = null

}
