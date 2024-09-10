package com.example.programingdemo.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.programingdemo.R
import com.example.programingdemo.adapters.ImageSliderAdapter
import com.example.programingdemo.data.CallLogItem
import com.example.programingdemo.data.ChatItem

class ActivityImageSlider : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    val arrayList = ArrayList<String>(10)
    val hasMap = HashMap<Int, String>(10)
    val hasSet = HashSet<String>(10)
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val images = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5
    )

    private var currentPage = 0
    private val runnable = object : Runnable {
        override fun run() {
            if (currentPage == images.size) currentPage = 0
            viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image_slider)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ActivityImageSliderMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
    }

    private fun init() {
        viewPager = findViewById(R.id.vpTwo)
        imageSliderAdapter = ImageSliderAdapter(images)
        viewPager.adapter = imageSliderAdapter
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000)
    }
}