/**
 * @Interface : ChallengeDao
 * @Usage : This interface is used to manage queries for Challenge Section
 * @Author : 1276
 */
package com.govida.database_section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.govida.ui_section.home_section.model.ChallengesEntity

@Dao
interface ChallengeDao {
    @get:Query("select * from challenges")
    val allChallenges: List<ChallengesEntity>

    @get:Query("Select count(*) from challenges")
    val allChallengeCount: Int

    @get:Query("select * from challenges where ongoingChallengeFlag==1")
    val ongoingChallenges: MutableList<ChallengesEntity>

    @get:Query("select * from challenges where ongoingChallengeFlag==0")
    val onNewChallenges: MutableList<ChallengesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleListRecord(challengesList: MutableList<ChallengesEntity>)

    @Query("UPDATE challenges SET ongoingChallengeFlag=1, percentage='0%' WHERE id = :challengeId")
    fun onUpdateChallenge(challengeId:Int)

    @Query("UPDATE challenges SET ongoingChallengeFlag=0, percentage=''")
    fun onResetChallenge()

    @Query("select * from challenges where id=:challengeId")
    fun getChallengeDetails(challengeId:Int): ChallengesEntity
}
