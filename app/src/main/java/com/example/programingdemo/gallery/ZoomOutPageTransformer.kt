package com.example.programingdemo.gallery

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            val scaleFactor = Math.max(0.85f, 1 - Math.abs(position))
            val verticalMargin = height * (1 - scaleFactor) / 2
            val horizontalMargin = width * (1 - scaleFactor) / 2
            translationX =
                if (position < 0) horizontalMargin - verticalMargin / 2 else horizontalMargin + verticalMargin / 2
            scaleX = scaleFactor
            scaleY = scaleFactor

            alpha = if (position < -1 || position > 1) {
                0f
            } else {
                1f
            }
        }
    }
}
