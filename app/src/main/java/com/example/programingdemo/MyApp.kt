package com.example.programingdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_CHANNEL_DESCRIPTION
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_CHANNEL_NAME
import com.example.programingdemo.utlis.Const.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.example.programingdemo.utlis.Const.GENERAL_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.programingdemo.utlis.Const.GENERAL_NOTIFICATION_CHANNEL_ID
import com.example.programingdemo.utlis.Const.GENERAL_NOTIFICATION_CHANNEL_NAME
import com.example.programingdemo.utlis.GeneralUsage.Companion.checkVersion
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MyApp : Application() {

    companion object {
        lateinit var firebaseAuth: FirebaseAuth
        lateinit var firebaseDatabase: FirebaseDatabase
        lateinit var firestore: FirebaseFirestore
        lateinit var firebaseStorage: FirebaseStorage
    }

    override fun onCreate() {
        super.onCreate()

        initFirebase()
        createNotificationChannel(
            GENERAL_NOTIFICATION_CHANNEL_ID,
            GENERAL_NOTIFICATION_CHANNEL_NAME,
            GENERAL_NOTIFICATION_CHANNEL_DESCRIPTION
        )
        createNotificationChannel(
            FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID,
            FOREGROUND_SERVICE_CHANNEL_NAME,
            FOREGROUND_SERVICE_CHANNEL_DESCRIPTION
        )
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
    }

    private fun createNotificationChannel(
        channelID: String,
        channelName: String,
        channelDescription: String
    ) {
        if (checkVersion()) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                channelID,
                channelName,
                importance
            ).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}