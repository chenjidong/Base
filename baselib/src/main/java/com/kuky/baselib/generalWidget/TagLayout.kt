package com.kuky.baselib.generalWidget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.kuky.baselib.R

/**
 * @author Kuky
 */
class TagLayout(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    private val mLines = ArrayList<Line>()
    /**
     * 垂直间距
     */
    private val verticalSpace: Float
    /**
     * 水平间距
     */
    private val horizontalSpace: Float
    /**
     * 是否平分剩余空间
     */
    private val divRemain: Boolean
    /**
     * 最大显示行数
     */
    private val maxLines: Int

    val children: List<View>
        get() {
            val views = java.util.ArrayList<View>()
            for (l in mLines) {
                views.addAll(l.getViews())
            }
            return views
        }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TagLayout)
        maxLines = array.getInteger(R.styleable.TagLayout_max_line, 5)
        horizontalSpace = array.getDimension(R.styleable.TagLayout_horizontal_space, 0f)
        verticalSpace = array.getDimension(R.styleable.TagLayout_vertical_space, 0f)
        divRemain = array.getBoolean(R.styleable.TagLayout_div_avg, true)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mLines.clear()
        var mCurrentLine: Line? = null

        val width = MeasureSpec.getSize(widthMeasureSpec)
        val maxWidth = width - paddingLeft - paddingRight

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            if (mCurrentLine == null) {
                mCurrentLine = Line(maxWidth, horizontalSpace)
                mCurrentLine.addChild(child)
                mLines.add(mCurrentLine)
            } else {
                if (mCurrentLine.canAddChild(child)) {
                    mCurrentLine.addChild(child)
                } else {
                    mCurrentLine = Line(maxWidth, horizontalSpace)
                    mCurrentLine.addChild(child)
                    mLines.add(mCurrentLine)
                }
            }
        }

        var height = paddingTop + paddingBottom + mLines.indices.sumBy { mLines[it].height }
        height += ((mLines.size - 1) * verticalSpace).toInt()
        if (mLines.isEmpty()) visibility = View.GONE
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var l = l
        var t = t

        l = paddingLeft
        t = paddingTop

        for (i in mLines.indices) {
            val line = mLines[i]
            line.layout(t, l)
            t += line.height
            if (i != mLines.size - 1)
                t = (t + verticalSpace).toInt()
        }
    }


    internal inner class Line(var maxWidth: Int, var space: Float) {
        private var views: MutableList<View> = ArrayList()

        var usedWidth: Int = 0

        var height: Int = 0

        fun addChild(view: View) {
            val childWidth = view.measuredWidth
            val childHeight = view.measuredHeight

            if (views.isEmpty()) {
                usedWidth = if (childWidth > maxWidth) maxWidth else childWidth
                height = childHeight
            } else {
                usedWidth += (childWidth + space).toInt()
                height = if (childHeight > height) childHeight else height
            }

            views.add(view)
        }

        fun canAddChild(view: View): Boolean {
            if (views.isEmpty()) return true

            return view.measuredWidth <= maxWidth.toFloat() - usedWidth.toFloat() - space
        }

        fun layout(t: Int, l: Int) {
            var rl = l

            val avg = if (views.isNotEmpty()) (maxWidth - usedWidth) / views.size else maxWidth

            for (child in views) {
                var measuredWidth = child.measuredWidth
                val measuredHeight = child.measuredHeight

                if (divRemain) {
                    child.measure(View.MeasureSpec.makeMeasureSpec(measuredWidth + avg, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(measuredHeight, View.MeasureSpec.EXACTLY))
                }

                measuredWidth = child.measuredWidth

                child.layout(rl, t, measuredWidth + rl, measuredHeight + t)

                rl += (measuredWidth + space).toInt()
            }
        }

        fun getViews(): List<View> {
            return views
        }

        fun setViews(views: MutableList<View>) {
            this.views = views
        }
    }
}