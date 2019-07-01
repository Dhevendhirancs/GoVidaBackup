/**
 * @Class : AppDatabase
 * @Usage : This abstract class is used to manage the initialisation of database and relevant processing on it.
 * @Author : 1276
 */
package com.govida.database_section

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.govida.ui_section.home_section.model.ChallengesEntity
import com.govida.ui_section.home_section.rewards_section.models.AllRewardEntity
import com.govida.ui_section.home_section.rewards_section.models.MyRewardsEntity
import com.govida.ui_section.leaderboard_section.database.LeaderboardEntity
import com.govida.ui_section.notification_section.database.NotificationEntity
import com.govida.utility_section.AppConstants

@Database(entities = [ChallengesEntity::class, AllRewardEntity::class,NotificationEntity::class,LeaderboardEntity::class, MyRewardsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun rewardDao(): RewardsDao
    abstract fun notificationDao(): NotificationDao
    abstract fun leaderboardDao(): LeaderboardDao
    abstract fun myRewardsDao(): MyRewardsDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, AppConstants.DB_NAME).build()
                }
            }
            return INSTANCE
        }

        internal val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }
    }
}
