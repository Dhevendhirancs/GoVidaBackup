package com.govida.ui_section.home_section.rewards_section.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
class RewardsEntity {
    @PrimaryKey(autoGenerate = true)
    var rewardId: Int = 0

    @ColumnInfo(name = "reward_name")
    var rewardName: String? = null

    @ColumnInfo(name = "reward_info")
    var rewardInfo: String? = null

    @ColumnInfo(name = "reward_description")
    var rewardDescription: String? = null

    @ColumnInfo(name = "reward_point")
    var rewardPoints: String? = null

    @ColumnInfo(name = "reward_status")
    var rewardStatus: String? = null
}
