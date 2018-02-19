package com.kuky.baselib.generalWidget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView

/**
 * @author Kuky
 */
class SmartScrollView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr) {
    private var mOnScrollChangeListener: OnScrollChangeListener? = null

    interface OnScrollChangeListener {
        fun onScrollChange(view: View, scrollX: Int, scrollY: Int, oldX: Int, odlY: Int)
    }

    fun setOnScrollChangeListener(listener: OnScrollChangeListener) {
        this.mOnScrollChangeListener = listener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener!!.onScrollChange(this, l, t, oldl, oldt)
        }
    }
}