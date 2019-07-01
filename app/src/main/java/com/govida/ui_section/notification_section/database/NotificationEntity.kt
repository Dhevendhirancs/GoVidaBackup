package com.govida.ui_section.notification_section.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
class NotificationEntity {
    @PrimaryKey(autoGenerate = true)
    var notificationId: Int = 0

    @ColumnInfo(name = "notification_title")
    var notificationTitle: String? = null

    @ColumnInfo(name = "notification_info")
    var notificationInfo: String? = null

    @ColumnInfo(name = "notification_received_at")
    var notificationReceivedAt: String? = null

    @ColumnInfo(name = "notification_read")
    var notificationRead: Boolean? = null
}
