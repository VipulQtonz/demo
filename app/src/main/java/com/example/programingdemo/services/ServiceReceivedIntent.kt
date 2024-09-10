package com.example.programingdemo.services

import android.app.Notification
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.programingdemo.R
import com.example.programingdemo.broadCastReceiver.ReceiverCombinedBroadcast
import com.example.programingdemo.utlis.Const.CALL_ENDED
import com.example.programingdemo.utlis.Const.CALL_IN_PROGRESS
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.example.programingdemo.utlis.Const.INCOMING_CALL

class ServiceReceivedIntent : Service() {
    private var telephonyManager: TelephonyManager? = null
    private var phoneStateListener: PhoneStateListener? = null
    private lateinit var combinedBroadcastReceiver: ReceiverCombinedBroadcast

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        combinedBroadcastReceiver = ReceiverCombinedBroadcast()
        registerReceivers()
        startForegroundServiceWithNotification()
        registerPhoneStateListener()
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val notification: Notification =
            NotificationCompat.Builder(this, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build()
        startForeground(1, notification)
    }

    private fun registerReceivers() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BOOT_COMPLETED)
            addAction(Intent.ACTION_DATE_CHANGED)
            addAction(Intent.ACTION_REBOOT)
            addAction(Intent.ACTION_CALL)
            addAction(Intent.ACTION_TIME_CHANGED)
        }
        registerReceiver(combinedBroadcastReceiver, intentFilter)
    }

    private fun registerPhoneStateListener() {
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Toast.makeText(
                            applicationContext,
                            INCOMING_CALL,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        Toast.makeText(applicationContext, CALL_IN_PROGRESS, Toast.LENGTH_LONG)
                            .show()
                    }

                    TelephonyManager.CALL_STATE_IDLE -> {
                        Toast.makeText(applicationContext, CALL_ENDED, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }
}

