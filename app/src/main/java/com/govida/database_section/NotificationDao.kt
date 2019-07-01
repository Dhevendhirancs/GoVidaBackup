/**
 * @Interface : NotificationDao
 * @Usage : This interface is used to manage queries for notification Section
 * @Author : 1276
 */

package com.govida.database_section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.govida.ui_section.notification_section.database.NotificationEntity

@Dao
interface NotificationDao {

    @get:Query("Select count(*) from notifications")
    val allNotificationCount: Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleListRecord(notificationList: MutableList<NotificationEntity>)

    @get:Query("select * from notifications")
    val allNotifications: MutableList<NotificationEntity>
}