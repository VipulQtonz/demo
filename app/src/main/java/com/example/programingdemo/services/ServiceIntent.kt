package com.example.programingdemo.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.programingdemo.activities.ActivityServiceDemo
import com.example.programingdemo.R
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_CHANNEL_DESCRIPTION
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_CHANNEL_NAME
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_DISCRIPTION
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_TITLE
import com.example.programingdemo.utlis.Const.NOTIFICATION_ACTION_NEXT
import com.example.programingdemo.utlis.Const.NOTIFICATION_ACTION_STOP
import com.example.programingdemo.utlis.Const.SERVICE_ACTION_STOP

class ServiceIntent : Service() {
    private lateinit var player: MediaPlayer
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val action1 = intent.action
        if (action1 == SERVICE_ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val existingChannel =
                notificationManager.getNotificationChannel(
                    FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
                )

            if (existingChannel == null) {
                val channel = NotificationChannel(
                    FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID,
                    FOREGROUND_SERVICE_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = FOREGROUND_SERVICE_CHANNEL_DESCRIPTION
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        val stopPendingIntent: PendingIntent = stopActionIntent()
        val redirectPendingIntent: PendingIntent = nextActionIntent()

        val notification =
            NotificationCompat.Builder(this, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(FOREGROUND_SERVICE_TITLE)
                .setContentText(FOREGROUND_SERVICE_DISCRIPTION)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .addAction(R.drawable.back_arrow_round, NOTIFICATION_ACTION_STOP, stopPendingIntent)
                .addAction(
                    R.drawable.back_arrow_round,
                    NOTIFICATION_ACTION_NEXT,
                    redirectPendingIntent
                )
                .build()

        startForeground(1, notification)

        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()

        Toast.makeText(this, R.string.service_running, Toast.LENGTH_SHORT).show()

        return START_STICKY
    }

    private fun nextActionIntent(): PendingIntent {
        val redirectIntent = Intent(this, ActivityServiceDemo::class.java)
        redirectIntent.flags =
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION

        val redirectPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return redirectPendingIntent
    }

    private fun stopActionIntent(): PendingIntent {
        val stopIntent = Intent(this, ServiceIntent::class.java).apply {
            action = SERVICE_ACTION_STOP
        }

        val stopPendingIntent: PendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return stopPendingIntent
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this@ServiceIntent, R.string.service_stoped, Toast.LENGTH_SHORT).show()
        player.stop()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}