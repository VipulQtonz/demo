package com.example.programingdemo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.programingdemo.activities.ActivityAsyncTaskDemo
import com.example.programingdemo.activities.ActivityBottomNavigationBar
import com.example.programingdemo.activities.ActivityContactList
import com.example.programingdemo.activities.ActivityContentProvider
import com.example.programingdemo.activities.ActivityDeviceImage
import com.example.programingdemo.activities.ActivityFindSongs
import com.example.programingdemo.activities.ActivityGestures
import com.example.programingdemo.activities.ActivityImageSlider
import com.example.programingdemo.activities.ActivityNavGraph
import com.example.programingdemo.activities.ActivityNavigationDrawer
import com.example.programingdemo.activities.ActivityNestedRecyclerView
import com.example.programingdemo.activities.ActivityReceiverDemo
import com.example.programingdemo.activities.ActivityRecyclerView
import com.example.programingdemo.activities.ActivityServiceDemo
import com.example.programingdemo.activities.ActivityViewPager
import com.example.programingdemo.activities.ActivityWhatsapp
import com.example.programingdemo.activities.LoginActivity
import com.example.programingdemo.appFlowDemo.ActivitySplashScreen
import com.example.programingdemo.databinding.ActivityMainBinding
import com.example.programingdemo.intent.ActivityExplicitIntentDemoOne
import com.example.programingdemo.intent.ActivityImplicitIntentDemoOne
import com.example.programingdemo.intent.ActivityImplicitIntentDemoTwo
import com.example.programingdemo.intent.ActivityNotificationDemo
import com.example.programingdemo.room.ActivityRoomDatabase
import com.example.programingdemo.services.ServiceReceivedIntent
import com.example.programingdemo.sharedPrefrences.ActivitySharedPrefrences
import com.example.programingdemo.userDetails.ActivityEditUserDetails
import com.google.android.material.snackbar.Snackbar

class ActivityMain : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llActivityMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        startForeGroundService()
        addOnClickListener()
    }

    private fun startForeGroundService() {
        val serviceIntent = Intent(this, ServiceReceivedIntent::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun addOnClickListener() {
        binding.btnImplicitIntentDemoOne.setOnClickListener(this)
        binding.btnImplicitIntentDemoTwo.setOnClickListener(this)
        binding.btnExplicitIntentDemoOne.setOnClickListener(this)
        binding.btnExplicitIntentDemoTwo.setOnClickListener(this)
        binding.btnUserDetailForm.setOnClickListener(this)
        binding.btnSharedPreferences.setOnClickListener(this)
        binding.btnServiceDemo.setOnClickListener(this)
        binding.btnFlow.setOnClickListener(this)
        binding.btnReceiverDemo.setOnClickListener(this)
        binding.btnAsyncTaskDemo.setOnClickListener(this)
        binding.btnStaticDemo.setOnClickListener(this)
        binding.btnViewPagerDemo.setOnClickListener(this)
        binding.btnImageSliderDemo.setOnClickListener(this)
        binding.btnNavGraph.setOnClickListener(this)
        binding.btnBottomNavigationBar.setOnClickListener(this)
        binding.btnNavigationDrawer.setOnClickListener(this)
        binding.btnRecyclerView.setOnClickListener(this)
        binding.btnContentProvider.setOnClickListener(this)
        binding.btnWhatsapp.setOnClickListener(this)
        binding.btnGesture.setOnClickListener(this)
        binding.btnDevicePhotos.setOnClickListener(this)
        binding.btnContactList.setOnClickListener(this)
        binding.btnFindSongs.setOnClickListener(this)
        binding.btnRoomDatabase.setOnClickListener(this)
        binding.btnNestedRecyclerView.setOnClickListener(this)
        binding.btnRegisterWithEmailAndPassWord.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnImplicitIntentDemoOne -> {
                startActivity(Intent(this@ActivityMain, ActivityImplicitIntentDemoOne::class.java))
            }

            R.id.btnImplicitIntentDemoTwo -> {
                startActivity(Intent(this@ActivityMain, ActivityImplicitIntentDemoTwo::class.java))
            }

            R.id.btnExplicitIntentDemoOne -> {
                startActivity(Intent(this@ActivityMain, ActivityExplicitIntentDemoOne::class.java))
            }

            R.id.btnExplicitIntentDemoTwo -> {
                startActivity(Intent(this@ActivityMain, ActivityNotificationDemo::class.java))
            }

            R.id.btnUserDetailForm -> {
                startActivity(Intent(this@ActivityMain, ActivityEditUserDetails::class.java))
            }

            R.id.btnSharedPreferences -> {
                startActivity(Intent(this@ActivityMain, ActivitySharedPrefrences::class.java))
            }

            R.id.btnServiceDemo -> {
                startActivity(Intent(this@ActivityMain, ActivityServiceDemo::class.java))
            }

            R.id.btnFlow -> {
                startActivity(Intent(this@ActivityMain, ActivitySplashScreen::class.java))
            }

            R.id.btnReceiverDemo -> {
                startActivity(Intent(this@ActivityMain, ActivityReceiverDemo::class.java))
            }

            R.id.btnAsyncTaskDemo -> {
                startActivity(Intent(this@ActivityMain, ActivityAsyncTaskDemo::class.java))
            }

            R.id.btnViewPagerDemo -> {
                startActivity(Intent(this@ActivityMain, ActivityViewPager::class.java))
            }

            R.id.btnImageSliderDemo -> {
                startActivity(Intent(this@ActivityMain, ActivityImageSlider::class.java))
            }

            R.id.btnNavGraph -> {
                startActivity(Intent(this@ActivityMain, ActivityNavGraph::class.java))
            }

            R.id.btnBottomNavigationBar -> {
                startActivity(Intent(this@ActivityMain, ActivityBottomNavigationBar::class.java))
            }

            R.id.btnNavigationDrawer -> {
                startActivity(Intent(this@ActivityMain, ActivityNavigationDrawer::class.java))
            }

            R.id.btnRecyclerView -> {
                startActivity(Intent(this@ActivityMain, ActivityRecyclerView::class.java))
            }

            R.id.btnContentProvider -> {
                startActivity(Intent(this@ActivityMain, ActivityContentProvider::class.java))
            }

            R.id.btnWhatsapp -> {
                startActivity(Intent(this@ActivityMain, ActivityWhatsapp::class.java))
            }

            R.id.btnGesture -> {
                startActivity(Intent(this@ActivityMain, ActivityGestures::class.java))
            }

            R.id.btnDevicePhotos -> {
                startActivity(Intent(this@ActivityMain, ActivityDeviceImage::class.java))
            }

            R.id.btnContactList -> {
                startActivity(Intent(this@ActivityMain, ActivityContactList::class.java))
            }

            R.id.btnFindSongs -> {
                startActivity(Intent(this@ActivityMain, ActivityFindSongs::class.java))
            }

            R.id.btnRoomDatabase -> {
                startActivity(Intent(this@ActivityMain, ActivityRoomDatabase::class.java))
            }
            R.id.btnNestedRecyclerView -> {
                startActivity(Intent(this@ActivityMain, ActivityNestedRecyclerView::class.java))
            }

            R.id.btnRegisterWithEmailAndPassWord -> {
                startActivity(Intent(this@ActivityMain, LoginActivity::class.java))
            }

            R.id.btnStaticDemo -> {
                val snackbar = Snackbar.make(
                    binding.btnStaticDemo,
                    getString(R.string.message_sent), Snackbar.LENGTH_LONG
                )
                snackbar.setAction(getString(R.string.undo)) {
                }
                snackbar.show()
            }
        }
    }
}
