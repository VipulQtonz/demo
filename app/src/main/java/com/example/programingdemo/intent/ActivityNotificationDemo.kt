package com.example.programingdemo.intent

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.databinding.ActivityNotificationDemoBinding
import com.example.programingdemo.utlis.Const.GENERAL_NOTIFICATION_CHANNEL_ID

class ActivityNotificationDemo : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityNotificationDemoBinding
    private var notificationIdCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ImplicitIntentDemoTwoMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }
        addOnClickListener()
    }

    private fun addOnClickListener() {
        binding.btnNotificationHigh.setOnClickListener(this)
        binding.btnNotificationLow.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnNotificationHigh -> {
                sendNotification(
                    binding.edtNotificationTitle.text.toString(),
                    binding.edtNotificationDescription.text.toString(),
                    NotificationCompat.PRIORITY_HIGH,
                    notificationIdCounter
                )
                notificationIdCounter++
            }

            R.id.btnNotificationLow -> {
                sendNotification(
                    binding.edtNotificationTitle.text.toString(),
                    binding.edtNotificationDescription.text.toString(),
                    NotificationCompat.PRIORITY_LOW,
                    notificationIdCounter
                )
                notificationIdCounter++
            }
        }
    }

    private fun sendNotification(
        title: String,
        description: String,
        priority: Int,
        notificationId: Int
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, ActivityNotificationDemo::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, GENERAL_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(notificationId, notification)
    }


}