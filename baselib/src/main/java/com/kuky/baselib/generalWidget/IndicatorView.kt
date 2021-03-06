package com.kuky.baselib.generalWidget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.kuky.baselib.R
import com.kuky.baselib.baseUtils.LogUtils

/**
 * @author Kuky
 */
class IndicatorView : View, ViewPager.OnPageChangeListener {

    private val mIndicatorPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 正常指示器颜色
     */
    private var mNormalColor = Color.WHITE
    /**
     * 选中指示器颜色
     */
    private var mSelectedColor = Color.GRAY
    /**
     * 指示器半径
     */
    private var mIndicatorRadius = context.resources.displayMetrics.density * 6
    private var mStrokeWidth = context.resources.displayMetrics.density * 2
    /**
     * 指示器之间间隔
     */
    private var mSpace = context.resources.displayMetrics.density * 5
    private var mSelectPosition = 0
    private var mCount = 0
    private var mIndicators: MutableList<Indicator> = mutableListOf()
    private var mViewPager: ViewPager? = null
    private var mDensity = context.resources.displayMetrics.density
    /**
     * 是否绑定循环 Banner 适配器
     */
    private var mIsLoopAdapter = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView)
        mNormalColor = ta.getColor(R.styleable.IndicatorView_indicatorNormalColor, Color.GREEN)
        mSelectedColor = ta.getColor(R.styleable.IndicatorView_indicatorSelectedColor, Color.YELLOW)
        mIndicatorRadius = ta.getDimension(R.styleable.IndicatorView_indicatorRadius, mDensity * 6)
        mStrokeWidth = ta.getDimension(R.styleable.IndicatorView_indicatorStrokeWidth, mDensity * 2)
        mSpace = ta.getDimension(R.styleable.IndicatorView_indicatorSpace, mDensity * 5)
        mSelectPosition = ta.getInt(R.styleable.IndicatorView_indicatorSelectPos, 0)
        mIsLoopAdapter = ta.getBoolean(R.styleable.IndicatorView_bindLooperAdapter, false)
        ta.recycle()
        mIndicatorPaint.color = mNormalColor
        mIndicatorPaint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = (mIndicatorRadius + mStrokeWidth) * 2 * mCount + mSpace * (mCount - 1)
        val height = (mIndicatorRadius + mStrokeWidth) * 2
        setMeasuredDimension(width.toInt(), height.toInt())
        measureIndicators()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mIndicators.indices.forEach {
            val indicator = mIndicators[it]
            val x = indicator.indicatorX
            val y = indicator.indicatorY
            mIndicatorPaint.color = if (mSelectPosition == it) mSelectedColor else mNormalColor
            canvas!!.drawCircle(x, y, mIndicatorRadius, mIndicatorPaint)
        }
    }

    private fun measureIndicators() {
        mIndicators.clear()
        var cx = 0F
        for (i in 0 until mCount) {
            val indicator = Indicator()
            if (i == 0) cx = mIndicatorRadius + mStrokeWidth else cx += (mIndicatorRadius + mStrokeWidth) * 2 + mSpace
            indicator.indicatorX = cx
            indicator.indicatorY = measuredHeight / 2F
            mIndicators.add(indicator)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_DOWN) {
            val xDown = event.x
            val yDown = event.y
            mIndicators.indices
                    .filter {
                        xDown > (mIndicators[it].indicatorX - mIndicatorRadius - mStrokeWidth)
                                && xDown < (mIndicators[it].indicatorX + mIndicatorRadius + mStrokeWidth)
                                && yDown > (mIndicators[it].indicatorY - mIndicatorRadius - mStrokeWidth)
                                && yDown < (mIndicators[it].indicatorY + mIndicatorRadius + mStrokeWidth)
                    }
                    .forEach {
                        val count = if (mIsLoopAdapter) it + 1 else it
                        LogUtils.e("Select $count")
                        mViewPager!!.setCurrentItem(if (mIsLoopAdapter) it + 1 else it, false)
                    }
        }
        return super.onTouchEvent(event)
    }

    fun setUpWithViewPager(viewPager: ViewPager) {
        releaseVp()
        this.mViewPager = viewPager
        mViewPager!!.addOnPageChangeListener(this)
        setCount(if (mIsLoopAdapter && mViewPager!!.adapter!!.count > 3) mViewPager!!.adapter!!.count - 2 else mViewPager!!.adapter!!.count)
    }

    private fun releaseVp() {
        if (mViewPager != null) {
            mViewPager!!.removeOnPageChangeListener(this)
            mViewPager = null
        }
    }

    /**
     * @Link{ LooperBannerAdapter }
     */
    fun isLoopAdapter(isLoop: Boolean) {
        this.mIsLoopAdapter = isLoop
        requestLayout()
    }

    private fun setCount(count: Int) {
        this.mCount = count
        requestLayout()
    }

    fun setRadius(radius: Float) {
        this.mIndicatorRadius = radius
        invalidate()
    }

    fun setStrokeWidth(strokeWidth: Float) {
        this.mStrokeWidth = strokeWidth
        invalidate()
    }

    fun setIndicatorColor(color: Int) {
        this.mNormalColor = color
        invalidate()
    }

    fun setSelectColor(color: Int) {
        this.mSelectedColor = color
        invalidate()
    }

    private fun setSelectPosition(position: Int) {
        this.mSelectPosition = position
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        setSelectPosition(if (mIsLoopAdapter) position - 1 else position)
    }

    inner class Indicator {
        var indicatorX = 0F
        var indicatorY = 0F
    }
}