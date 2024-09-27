package com.example.programingdemo.firebase

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.GENERAL_NOTIFICATION_CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseToken", "New token: $token")
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val notificationBuilder = NotificationCompat.Builder(this, GENERAL_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_bell_svg)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationId = System.currentTimeMillis().toInt()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
