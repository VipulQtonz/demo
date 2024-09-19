package com.example.programingdemo.appFlowDemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.R
import com.example.programingdemo.userDetails.ActivityEditUserDetails
import com.example.programingdemo.utlis.Const.IS_OPEN
import com.example.programingdemo.utlis.Const.PLASH_SCREEN_TIME
import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
import com.example.programingdemo.utlis.Const.TAG
import com.example.programingdemo.utlis.SharedPreferencesHelper
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

@SuppressLint("CustomSplashScreen")
class ActivitySplashScreen : AppCompatActivity() {
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var splashDuration: Long = 1000
    private lateinit var handler: Handler
    private val runnable = Runnable {
        if (isOpenFirst()) {
            startActivity(Intent(this, ActivityIntroScreen::class.java))
        } else {
            startActivity(Intent(this, ActivityEditUserDetails::class.java))
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clSplashScreenMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        remoteConfigSetUp()
        fetchSplashDuration()
        handler = Handler(Looper.getMainLooper())
    }

    private fun remoteConfigSetUp() {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3000)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d(TAG, getString(R.string.updated_keys, configUpdate.updatedKeys))
                if (configUpdate.updatedKeys.contains(PLASH_SCREEN_TIME)) {
                    remoteConfig.activate().addOnCompleteListener {

                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w(TAG, getString(R.string.config_update_error_with_code, error.code), error)
            }
        })
    }

    private fun fetchSplashDuration() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    splashDuration = remoteConfig.getLong(PLASH_SCREEN_TIME)
                    Log.d(TAG, splashDuration.toString())
                } else {
                    Log.w(
                        TAG,
                        getString(R.string.failed_to_fetch_remote_config_using_default_values)
                    )
                }
                handler.postDelayed(runnable, splashDuration)
            }
    }

    private fun isOpenFirst(): Boolean {
        val sharedPreferencesHelper =
            SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)
        return sharedPreferencesHelper.getInt(IS_OPEN, 0) == 0
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
