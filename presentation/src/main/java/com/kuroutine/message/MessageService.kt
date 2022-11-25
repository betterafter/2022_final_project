package com.kuroutine.message

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.kuroutine.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "KUROUTINE_MESSAGE_CHANNEL"
const val CHANNEL_NAME = "KUROUTINE_KULTURE"

class MessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //token을 서버로 전송
        Log.d("[keykat]", "onNewToken:: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        var builder: NotificationCompat.Builder? = null
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val profile =
            if (remoteMessage.data["userProfile"] != null && remoteMessage.data["userProfile"] != "") remoteMessage.data["userProfile"]
            else R.drawable.ic_noimage

        var target = Glide.with(this)
            .asBitmap()
            .load(profile)
            .submit()

        val bitmap = target.get()
        builder.setLargeIcon(bitmap)

        Glide.with(this).clear(target)

        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
        val notification: Notification = builder.build()
        notificationManager.notify(1, notification)
    }
}