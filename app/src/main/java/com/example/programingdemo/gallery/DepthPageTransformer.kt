package com.example.programingdemo.gallery

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        when {
            position < -1 -> {
                view.alpha = 0f
            }

            position <= 1 -> {
                view.alpha = 1 - Math.abs(position)

                val scaleFactor = Math.max(0.75f, 1 - Math.abs(position))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                view.translationX = view.width * -position
            }

            else -> {
                view.alpha = 0f
            }
        }
    }
}
