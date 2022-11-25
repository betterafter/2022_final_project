package com.kuroutine.message

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.kuroutine.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kuroutine.kulture.EXTRA_KEY_ISPRIVATE
import com.kuroutine.kulture.EXTRA_KEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_KEY_USERS
import com.kuroutine.kulture.EXTRA_QKEY_MOVETOCHAT
import com.kuroutine.kulture.EXTRA_SHOULD_MOVE_TO_SPECIFIC_PAGE
import com.kuroutine.kulture.MainActivity
import com.kuroutine.kulture.PAGE_CHAT
import com.kuroutine.kulture.chat.ChatActivity
import com.kuroutine.kulture.intro.IntroActivity

const val CHANNEL_ID = "KUROUTINE_MESSAGE_CHANNEL"
const val CHANNEL_NAME = "KUROUTINE_KULTURE"

const val NOTIFICATION_ID = 10101

class MessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //token을 서버로 전송
        Log.d("[keykat]", "onNewToken:: $token")
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // notification 설정
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        var builder: NotificationCompat.Builder? = null
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)

        // 데이터 가져오기
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val profile =
            if (remoteMessage.data["userProfile"] != null && remoteMessage.data["userProfile"] != "") remoteMessage.data["userProfile"]
            else R.drawable.ic_noimage

        // intent 설정
        val intent = Intent(this, IntroActivity::class.java)

        intent.putExtra(EXTRA_SHOULD_MOVE_TO_SPECIFIC_PAGE, PAGE_CHAT)
        intent.putExtra(EXTRA_QKEY_MOVETOCHAT, remoteMessage.data["qid"])
        intent.putExtra(EXTRA_KEY_MOVETOCHAT, remoteMessage.data["uid"])
        intent.putExtra(EXTRA_KEY_USERS, remoteMessage.data["users"])
        intent.putExtra(EXTRA_KEY_ISPRIVATE, remoteMessage.data["isPrivate"])

        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
                    or PendingIntent.FLAG_IMMUTABLE
                    or PendingIntent.FLAG_ONE_SHOT
        )


        // 프로필 이미지 연결
        var target = Glide.with(this)
            .asBitmap()
            .load(profile)
            .submit()

        val bitmap = target.get()
        builder.setLargeIcon(bitmap)

        // notification 셋업
        val notification = builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1, notification)

        Glide.with(this).clear(target)
    }
}