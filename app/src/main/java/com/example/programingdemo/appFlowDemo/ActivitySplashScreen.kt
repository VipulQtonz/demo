    package com.example.programingdemo.appFlowDemo

    import android.annotation.SuppressLint
    import android.content.Intent
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.example.programingdemo.R
    import com.example.programingdemo.userDetails.ActivityEditUserDetails
    import com.example.programingdemo.utlis.Const.IS_OPEN
    import com.example.programingdemo.utlis.Const.SHARED_PREF_USER_DETAILS
    import com.example.programingdemo.utlis.SharedPreferencesHelper

    @SuppressLint("CustomSplashScreen")
    class ActivitySplashScreen : AppCompatActivity() {

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
            handler = Handler(Looper.getMainLooper())
        }

        override fun onResume() {
            super.onResume()
            handler.postDelayed(
                runnable,
                2000
            )
        }

        override fun onPause() {
            super.onPause()
            handler.removeCallbacks(runnable)
        }

        private fun isOpenFirst(): Boolean {
            val sharedPreferencesHelper =
                SharedPreferencesHelper(this, SHARED_PREF_USER_DETAILS, MODE_PRIVATE)
            return sharedPreferencesHelper.getInt(IS_OPEN, 0) == 0
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(runnable)
        }
    }