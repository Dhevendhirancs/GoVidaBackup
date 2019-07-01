/**
 * @Interface : RewardsDao
 * @Usage : This interface is used to manage queries for rewards Section
 * @Author : 1276
 */

package com.govida.database_section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.govida.ui_section.home_section.rewards_section.models.AllRewardEntity

@Dao
interface RewardsDao {

    @get:Query("select * from allRewards")
    val getAllRewardsList: MutableList<AllRewardEntity>

    @get:Query("Select count(*) from allRewards")
    val allRewardsCount: Int

    @Query("select * from allRewards where rewardId=:rewardId")
    fun getRewardDetails(rewardId:Int): AllRewardEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRewardsList(rewardsList: MutableList<AllRewardEntity>)

    @Query("DELETE FROM allRewards")
    fun deleteAllRewards()
}