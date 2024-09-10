package com.example.programingdemo.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.programingdemo.R
import com.example.programingdemo.adapters.ViewPagerAdapter
import com.example.programingdemo.databinding.ActivityViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

class ActivityViewPager : AppCompatActivity() {
    private lateinit var binding: ActivityViewPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.llViewPagerDemoMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tabLayout(viewPager2())
    }

    private fun tabLayout(viewPager: ViewPager2) {
        val tabLayout = binding.tlOne
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${(position + 1)}"
        }.attach()
    }

    private fun viewPager2(): ViewPager2 {
        val viewPager = binding.vpOne
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        return viewPager
    }
}