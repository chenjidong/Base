package com.kuky.base

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.util.TypedValue
import android.view.View

/**
 * @author Kuky
 */
class ScaleTransformer(private val context: Context) : ViewPager.PageTransformer {
    private val MIN_ALPHA = 0.5F
    private val MIN_SCALE = 0.75F
    private var elevation: Float = 0F

    init {
        elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, context.resources.displayMetrics)
    }

    /**
     * 当页面从 第一个 -> 第二个
     * 第一个页面 position 变化为 [0, -1]
     * 第二个页面 position 变化为 [1, 0]
     */
    override fun transformPage(page: View, position: Float) {
        //(-∞ , -1) (1 , +∞)
        if (position < -1 || position > 1) {
            page.alpha = MIN_ALPHA
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE
        } else {
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val scale = if (position < 0) 1 + 0.3f * position else 1 - 0.3f * position
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            if (page is CardView) page.cardElevation = (1 - Math.abs(position)) * elevation
        }
    }
}