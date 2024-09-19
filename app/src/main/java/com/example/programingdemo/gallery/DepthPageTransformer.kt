package com.example.programingdemo.gallery

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        when {
            position < -1 -> {
                // This page is way off-screen to the left.
                view.alpha = 0f
            }
            position <= 1 -> {
                // Modify the default slide transition to shrink the page as well.
                view.alpha = 1 - Math.abs(position)

                // Scale the page down (between MIN_SCALE and 1)
                val scaleFactor = Math.max(0.75f, 1 - Math.abs(position))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                // Translate the page to the left or right.
                view.translationX = view.width * -position
            }
            else -> {
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }
    }
}
