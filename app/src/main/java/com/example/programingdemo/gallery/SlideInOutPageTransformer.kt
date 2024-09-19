package com.example.programingdemo.gallery

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class SlideInOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 1 -> {
                    alpha = 1f
                    translationX = -position * width
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}
