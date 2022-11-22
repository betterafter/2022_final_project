package com.kuroutine.message

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "KUROUTINE_MESSAGE_CHANNEL"
const val CHANNEL_NAME = "KUROUTINE_KULTURE"

class MessageService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //token을 서버로 전송
        Log.d("[keykat]", "onNewToken:: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("[keykat]", "remoteMessage: $remoteMessage")
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        var builder: NotificationCompat.Builder? = null
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        val title = remoteMessage.notification!!.title
        val body = remoteMessage.notification!!.body
        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.sym_def_app_icon)
        val notification: Notification = builder.build()
        notificationManager.notify(1, notification)
    }
}