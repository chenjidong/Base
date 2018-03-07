package com.kuky.baselib_java.generalWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kuky.baselib_java.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuky
 */
public class IndicatorView extends View implements ViewPager.OnPageChangeListener{
    private Paint mIndicatorPaint;
    private int mNormalColor;
    private int mSelectedColor;
    private float mIndicatorRadius;
    private float mStrokeWidth;
    private float mSpace;
    private int mSelectPosition;
    private int mCount;
    private List<Indicator> mIndicators = new ArrayList<>();
    private ViewPager mViewPager = null;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float mDensity = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mNormalColor = ta.getColor(R.styleable.IndicatorView_indicatorNormalColor, Color.GREEN);
        mSelectedColor = ta.getColor(R.styleable.IndicatorView_indicatorSelectedColor, Color.YELLOW);
        mIndicatorRadius = ta.getDimension(R.styleable.IndicatorView_indicatorRadius, mDensity * 6);
        mStrokeWidth = ta.getDimension(R.styleable.IndicatorView_indicatorStrokeWidth, mDensity * 2);
        mSpace = ta.getDimension(R.styleable.IndicatorView_indicatorSpace, mDensity * 5);
        mSelectPosition = ta.getInt(R.styleable.IndicatorView_indicatorSelectPos, 0);
        ta.recycle();
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(mNormalColor);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width = (mIndicatorRadius + mStrokeWidth) * 2 * mCount + mSpace * (mCount - 1);
        float height = (mIndicatorRadius + mStrokeWidth) * 2;
        setMeasuredDimension((int) width, (int) height);
        measureIndicators();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mIndicators.size(); i++) {
            Indicator indicator = mIndicators.get(i);
            float x = indicator.indicatorX;
            float y = indicator.indicatorY;
            mIndicatorPaint.setColor(i == mSelectPosition ? mSelectedColor : mNormalColor);
            canvas.drawCircle(x, y, mIndicatorRadius, mIndicatorPaint);
        }
    }

    private void measureIndicators() {
        mIndicators.clear();
        float cx = 0;
        for (int i = 0; i < mCount; i++) {
            Indicator indicator = new Indicator();
            if (i == 0) {
                cx = mIndicatorRadius + mStrokeWidth;
            } else {
                cx += (mIndicatorRadius + mStrokeWidth) * 2 + mSpace;
            }
            indicator.indicatorX = cx;
            indicator.indicatorY = getMeasuredHeight() / 2;
            mIndicators.add(indicator);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float xDown = event.getX();
            float yDown = event.getY();
            for (int i = 0; i < mIndicators.size(); i++) {
                if (xDown > (mIndicators.get(i).indicatorX - mIndicatorRadius - mStrokeWidth)
                        && xDown < (mIndicators.get(i).indicatorX + mIndicatorRadius + mStrokeWidth)
                        && yDown > (mIndicators.get(i).indicatorY - mIndicatorRadius - mStrokeWidth)
                        && yDown < (mIndicators.get(i).indicatorY + mIndicatorRadius + mStrokeWidth))
                    mViewPager.setCurrentItem(i);
            }
        }
        return super.onTouchEvent(event);
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        releaseVp();
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
        setCount(mViewPager.getAdapter().getCount());
    }

    private void releaseVp() {
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(this);
            mViewPager = null;
        }
    }

    private void setCount(int count) {
        this.mCount = count;
        requestLayout();
    }

    public void setRadius(float radius) {
        this.mIndicatorRadius = radius;
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        invalidate();
    }

    public void setIndicatorColor(int color) {
        this.mNormalColor = color;
        invalidate();
    }

    public void setSelectColor(int color) {
        this.mSelectedColor = color;
        invalidate();
    }

    private void setSelectPosition(int position) {
        this.mSelectPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class Indicator {
        float indicatorX;
        float indicatorY;
    }
}
