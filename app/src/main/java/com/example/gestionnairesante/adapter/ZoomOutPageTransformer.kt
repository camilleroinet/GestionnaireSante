package com.example.gestionnairesante.adapter

import android.view.View
import androidx.viewpager.widget.ViewPager

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

class ZoomOutPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            val pageHeight = height

            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 1 -> {
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horizMargin = pageHeight * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horizMargin - vertMargin / 2
                    } else {
                        horizMargin + vertMargin / 2
                    }
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha =
                        (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))

                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }
}