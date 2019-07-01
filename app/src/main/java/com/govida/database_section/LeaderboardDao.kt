/**
 * @Interface : LeaderboardDao
 * @Usage : This interface is used to manage queries for leaderboard Section
 * @Author : 1276
 */

package com.govida.database_section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.govida.ui_section.leaderboard_section.database.LeaderboardEntity

@Dao
interface LeaderboardDao {

    @get:Query("Select count(*) from leaderboard")
    val allLeadersCount: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleListRecord(leaderboardList: MutableList<LeaderboardEntity>)

    @get:Query("select * from leaderboard")
    val allLeaders: MutableList<LeaderboardEntity>
}