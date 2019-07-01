/**
 * @Class : LeaderboardEntity
 * @Usage : Used to provide entity class for notification
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard")
class LeaderboardEntity {
    @PrimaryKey(autoGenerate = true)
    var leaderId: Int = 0

    @ColumnInfo(name = "leader_title")
    var leaderName: String? = null

    @ColumnInfo(name = "leader_steps")
    var leaderSteps: String? = null

    @ColumnInfo(name = "leader_distance")
    var leaderDistance: String? = null

    @ColumnInfo(name = "leader_position")
    var leaderPosition: Int = 0
}