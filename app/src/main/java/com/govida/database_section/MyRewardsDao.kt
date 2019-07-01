package com.govida.database_section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsEntity

@Dao
interface MyRewardsDao {

    @get:Query("select * from myRewards")
    val getMyRewardsList: MutableList<MyRewardsEntity>

    @get:Query("Select count(*) from myRewards")
    val myRewardsCount: Int

    @Query("select * from myRewards where rewardId=:rewardId")
    fun getMyRewardDetails(rewardId:Int): MyRewardsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMyRewardsList(rewardsList: MutableList<MyRewardsEntity>)

    @Query("DELETE FROM myRewards")
    fun deleteMyRewards()
}