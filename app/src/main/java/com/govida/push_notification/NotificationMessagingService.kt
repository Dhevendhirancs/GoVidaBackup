package com.govida.push_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.home_section.ActivityHome
import com.govida.utility_section.AppConstants
import com.govida.utility_section.AppLogger
import java.text.SimpleDateFormat
import java.util.*


class NotificationMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "NotificationMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        AppLogger.d(TAG, "From: ${remoteMessage?.from}")
        remoteMessage?.data?.isNotEmpty()?.let {
            AppLogger.d(TAG, "Message data payload: " + remoteMessage.data)
            val appPreference= AppPreference(this)
            if(appPreference.isUserLoggedIn){
                if(appPreference.isHomeSetupDone){
                    sendNotification(remoteMessage)
                }
            }
        }
        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            AppLogger.d(TAG, "Message Notification Body: ${it.body}")
        }
    }

    override fun onNewToken(token: String?) {
        AppLogger.d( "Refreshed token ---> : $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        AppLogger.d( "Refreshed token ---> : $token")
        val appPref=AppPreference(this)
        if(appPref.isUserLoggedIn){
            if(!token.isNullOrEmpty()){
                appPref.notificationDeviceToken=token
                val intent = Intent(this, NotificationTokenRegistrationService::class.java)
                startService(intent)
            }
        }else{

        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: RemoteMessage) {
        val intent = Intent(this, ActivityHome::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra(AppConstants.INTENT_NOTIFICATION_DATA,NotificationConfig().convertToNotificationBundle(messageBody))
        var id=getUniqueNotificationId()
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.app_icon)
            //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.app_icon))
            .setTicker(messageBody.notification?.title)
            .setAutoCancel(true)
            .setContentTitle(messageBody.notification?.title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody.notification?.body!!))
            .setContentText(messageBody.notification?.body!!)
            .setContentIntent(pendingIntent)
            .setSound(defaultSoundUri)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = ContextCompat.getColor(this, R.color.colorPrimary)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notificationBuilder.build())
    }

    fun getUniqueNotificationId():Int{
        var notiId=0
        val now = Calendar.getInstance().time
        val startFormat = SimpleDateFormat("ddHHmmss", Locale.ENGLISH)
        var startDate = startFormat.format(now)
        try{
            //startDate=startDate.substring(startDate.length-5,5)
            notiId=Integer.parseInt(startDate)
        }catch (e:Exception){

        }
        return notiId
    }
}