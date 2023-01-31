package com.meafocus.adarkworld.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.meafocus.adarkworld.MainActivity
import infix.imrankst1221.codecanyon.R
import infix.imrankst1221.rocket.library.utility.Constants
import infix.imrankst1221.rocket.library.utility.PreferenceUtils
import infix.imrankst1221.rocket.library.utility.UtilMethods


class FirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "---Firebase"
    lateinit var mContext: Context

    override fun onNewToken(token: String) {
        mContext = this
        UtilMethods.printLog(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notificationUrl: String = ""
        var notificationUrlOpenType: String = ""

        mContext = this
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.notification != null) {
            UtilMethods.printLog(TAG,"title = " + remoteMessage.notification!!.title)
            UtilMethods.printLog(TAG,"body = " + remoteMessage.notification!!.body)
            UtilMethods.printLog(TAG,"sound = " + remoteMessage.notification!!.sound)

            val title = if (remoteMessage.notification!!.title != null) remoteMessage.notification!!.title else getString(R.string.app_name)
            val text = if (remoteMessage.notification!!.body != null) remoteMessage.notification!!.body else ""
            val sound = remoteMessage.notification!!.sound.orEmpty()
            if (remoteMessage.data["url"] != null) {
                UtilMethods.printLog(TAG, "url = " + remoteMessage.data["url"])
                notificationUrl = remoteMessage.data["url"].toString()
                notificationUrlOpenType = "INSIDE"
            }

            for (entry in remoteMessage.data.entries) {
                val key = entry.key.orEmpty().toLowerCase()
                val value = entry.value.orEmpty()

                UtilMethods.printLog(TAG, "key, $key value $value")
                if(key == Constants.KEY_NOTIFICATION_URL){
                    notificationUrl = value
                }else if(key == Constants.KEY_NOTIFICATION_OPEN_TYPE){
                    notificationUrlOpenType = value
                }
            }

            val channelId = getString(R.string.default_notification_channel_id)
            sendNotification(title.orEmpty(), text.orEmpty(), sound, channelId, notificationUrl, notificationUrlOpenType)
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        if(token != null && token.isNotEmpty()) {
            PreferenceUtils.getInstance().initPreferences(mContext)
            PreferenceUtils.editStringValue( Constants.KEY_FIREBASE_TOKEN, token)
        }
    }

    /**
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(title: String, text: String, sound: String, channelId: String,
                                 notificationUrl: String, notificationUrlOpenType: String) {
        val intent = Intent(mContext.applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
        if(notificationUrl.isNotEmpty() && notificationUrlOpenType.isNotEmpty()) {
            intent.putExtra(Constants.KEY_NOTIFICATION_URL, notificationUrl)
            intent.putExtra(Constants.KEY_NOTIFICATION_OPEN_TYPE, notificationUrlOpenType)
        }

        UtilMethods.printLog(TAG, notificationUrl)
        UtilMethods.printLog(TAG, notificationUrlOpenType)

        val pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        var defaultSoundUri: Uri? = null
        if (sound.isNotEmpty()) {
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }else {
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        //val color = ContextCompat.getColor(mContext, R.color.colorNotification)
        val notificationBuilder = NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                //.setColor(color)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}
