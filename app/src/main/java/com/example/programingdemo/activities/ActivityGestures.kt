package com.example.programingdemo.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.programingdemo.R

class ActivityGestures : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var navController: NavController

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    private val fragmentList = listOf(
        R.id.fragmentHome,
        R.id.fragmentCart,
        R.id.fragmentSearch
    )

    private var currentFragmentIndex = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestures)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityGestureMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentGesture) as? NavHostFragment
        navController = navHostFragment!!.navController

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null && e2 != null) {
                    val diffX = e2.x - e1.x
                    val diffY = e2.y - e1.y
                    if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(
                            velocityX
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        if (diffX > 0) {
                            currentFragmentIndex =
                                (currentFragmentIndex - 1 + fragmentList.size) % fragmentList.size
                        } else {
                            currentFragmentIndex = (currentFragmentIndex + 1) % fragmentList.size
                        }

                        navigateToFragment(fragmentList[currentFragmentIndex])
                        return true
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })

        scaleGestureDetector = ScaleGestureDetector(
            this,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                }
            })

        findViewById<View>(R.id.navHostFragmentGesture).setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            scaleGestureDetector.onTouchEvent(event)
            true
        }
    }
    private fun navigateToFragment(fragmentId: Int) {
        if (fragmentId == R.id.fragmentHome) {
            if (!navController.popBackStack(R.id.fragmentHome, false)) {
                navController.navigate(R.id.fragmentHome)
            }
        } else {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentHome, false)
                .build()

            navController.navigate(fragmentId, null, navOptions)
        }
    }
}
