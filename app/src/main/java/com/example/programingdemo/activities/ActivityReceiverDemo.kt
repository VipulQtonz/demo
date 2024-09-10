package com.example.programingdemo.activities

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.broadCastReceiver.ReceiverCombinedBroadcast

class ActivityReceiverDemo : AppCompatActivity() {

    private lateinit var combinedBroadcastReceiver: ReceiverCombinedBroadcast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_receiver_demo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ReceiverDemoMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

    }

    private fun init() {
        combinedBroadcastReceiver = ReceiverCombinedBroadcast()
        registerReceivers()
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


    override fun onStop() {
        super.onStop()
        unregisterReceiver(combinedBroadcastReceiver)
    }
}