package com.example.programingdemo.broadCastReceiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.programingdemo.R
import com.example.programingdemo.activities.ActivityReceiveFromOtherApplication
import com.example.programingdemo.utlis.Const.MESSAGE
import com.example.programingdemo.utlis.Const.SENDER_NAME

class ReceiverIntentData : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val receivedSenderName = intent?.getStringExtra(SENDER_NAME).toString()
        val receivedMessage = intent?.getStringExtra(MESSAGE)
        sendNotification(context, receivedSenderName, receivedMessage, receivedSenderName)
    }

    private fun sendNotification(
        context: Context?,
        sender: String?,
        message: String?,
        senderName: String?
    ) {
        val intent = Intent(context, ActivityReceiveFromOtherApplication::class.java).apply {
            putExtra(SENDER_NAME, sender)
            putExtra(MESSAGE, message)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val notificationId = (sender?.hashCode() ?: 0) + (System.currentTimeMillis().toInt() % 1000)
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        getNotification(context, senderName, message, pendingIntent, notificationId)
    }

    private fun getNotification(
        context: Context?,
        senderName: String?,
        message: String?,
        pendingIntent: PendingIntent?,
        notificationId: Int
    ) {
        val notification =
            NotificationCompat.Builder(context!!, context.getString(R.string._1_))
                .setContentTitle(senderName)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}
