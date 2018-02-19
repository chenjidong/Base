package com.kuky.baselib.generalWidget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.kuky.baselib.R;


/**
 * @author Kuky
 */
public class SwipeMenuLayout extends ViewGroup {
    private int mScaleTouchSlop;
    private int mMaxVelocity;
    private int mPointerId;
    private int mHeight;
    /**
     * 右侧菜单宽度总和
     */
    private int mRightMenuWidths;
    private int mLimit;
    private View mContentView;
    private PointF mLastP = new PointF();
    private boolean isUnMoved = true;
    private PointF mFirstP = new PointF();
    private boolean isUserSwiped;
    private SwipeMenuLayout mViewCache;
    private static boolean isTouching;
    private VelocityTracker mVelocityTracker;

    /**
     * 右滑删除功能的开关,默认开
     */
    private boolean isSwipeEnable = true;

    /**
     * IOS、QQ式交互，默认开
     */
    private boolean isIos = true;

    private boolean iosInterceptFlag;

    /**
     * 左滑右滑的开关,默认左滑打开菜单
     */
    private boolean isLeftSwipe = true;

    /**
     * 平滑展开
     */
    private ValueAnimator mExpandAnim, mCloseAnim;

    private boolean isExpand;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isSwipeEnable() {
        return isSwipeEnable;
    }

    /**
     * 设置侧滑功能开关
     *
     * @param swipeEnable
     */
    public void setSwipeEnable(boolean swipeEnable) {
        isSwipeEnable = swipeEnable;
    }


    public boolean isIos() {
        return isIos;
    }

    /**
     * 设置是否开启 IOS 阻塞式交互
     *
     * @param ios
     */
    public SwipeMenuLayout setIos(boolean ios) {
        isIos = ios;
        return this;
    }

    public boolean isLeftSwipe() {
        return isLeftSwipe;
    }

    /**
     * 设置是否开启左滑出菜单，设置false 为右滑出菜单
     *
     * @param leftSwipe
     * @return
     */
    public SwipeMenuLayout setLeftSwipe(boolean leftSwipe) {
        isLeftSwipe = leftSwipe;
        return this;
    }

    /**
     * 返回ViewCache
     *
     * @return
     */
    public SwipeMenuLayout getViewCache() {
        return mViewCache;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout);
        isSwipeEnable = ta.getBoolean(R.styleable.SwipeMenuLayout_swipeEnable, true);
        isIos = ta.getBoolean(R.styleable.SwipeMenuLayout_ios, true);
        isLeftSwipe = ta.getBoolean(R.styleable.SwipeMenuLayout_leftSwipe, true);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);
        mRightMenuWidths = 0; // 由于ViewHolder的复用机制，每次这里要手动恢复初始值
        mHeight = 0;
        int contentWidth = 0; // 适配GridLayoutManager，将以第一个子Item(即ContentItem)的宽度为控件宽度
        int childCount = getChildCount();
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setClickable(true);

            if (childView.getVisibility() != View.GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                final MarginLayoutParams mlp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight());

                if (measureMatchParentChildren && mlp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }

                if (i > 0) {
                    /**
                     * 第一个布局是Left item，从第二个开始才是RightMenu
                     */
                    mRightMenuWidths += childView.getMeasuredWidth();
                } else {
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                }
            }
        }

        /**
         * 宽度取第一个Item(Content)的宽度
         */
        setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth,
                mHeight + getPaddingTop() + getPaddingBottom());

        /**
         * 滑动判断的临界值
         */
        mLimit = mRightMenuWidths * 2 / 5;

        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private void forceUniformHeight(int childCount, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = getPaddingLeft();
        int right = getPaddingLeft();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) {
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                } else {
                    if (isLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
            acquireVelocityTracker(ev);
            final VelocityTracker verTracker = mVelocityTracker;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isUserSwiped = false;
                    isUnMoved = true;
                    iosInterceptFlag = false;
                    if (isTouching) {
                        return false;
                    } else {
                        isTouching = true;
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    mFirstP.set(ev.getRawX(), ev.getRawY());

                    if (mViewCache != null) {
                        if (mViewCache != this) {
                            mViewCache.smoothClose();

                            iosInterceptFlag = isIos;
                        }
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    mPointerId = ev.getPointerId(0);
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (iosInterceptFlag) {
                        break;
                    }
                    float gap = mLastP.x - ev.getRawX();

                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                    if (Math.abs(gap) > mScaleTouchSlop) {
                        isUnMoved = false;
                    }

                    scrollBy((int) (gap), 0);
                    if (isLeftSwipe) {
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0);
                        }
                    } else {
                        if (getScrollX() < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }

                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        isUserSwiped = true;
                    }

                    if (!iosInterceptFlag) {
                        verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                        final float velocityX = verTracker.getXVelocity(mPointerId);
                        if (Math.abs(velocityX) > 1000) {
                            if (velocityX < -1000) {
                                if (isLeftSwipe) {
                                    smoothExpand();
                                } else {
                                    smoothClose();
                                }
                            } else {
                                if (isLeftSwipe) {
                                    smoothClose();
                                } else {
                                    smoothExpand();
                                }
                            }
                        } else {
                            if (Math.abs(getScrollX()) > mLimit) {
                                smoothExpand();
                            } else {
                                smoothClose();
                            }
                        }
                    }
                    releaseVelocityTracker();
                    isTouching = false;
                    break;

                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isSwipeEnable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (isLeftSwipe) {
                        if (getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() < getWidth() - getScrollX()) {
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    } else {
                        if (-getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() > -getScrollX()) {
                                if (isUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    if (isUserSwiped) {
                        return true;
                    }
                    break;
            }
            if (iosInterceptFlag) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void smoothExpand() {
        mViewCache = SwipeMenuLayout.this;

        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }

        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }

    /**
     * 每次执行动画之前都应该先取消之前的动画
     */
    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }

    /**
     * 平滑关闭
     */
    public void smoothClose() {
        mViewCache = null;
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }

        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isExpand = false;

            }
        });
        mCloseAnim.setDuration(300).start();
    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see VelocityTracker#obtain()
     * @see VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * * 释放VelocityTracker
     *
     * @see VelocityTracker#clear()
     * @see VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this == mViewCache) {
            mViewCache.smoothClose();
            mViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean performLongClick() {
        return Math.abs(getScrollX()) <= mScaleTouchSlop && super.performLongClick();
    }

    /**
     * 快速关闭。
     * 用于 点击侧滑菜单上的选项,同时想让它快速关闭(删除 置顶)。
     * 这个方法在ListView里是必须调用的，
     * 在RecyclerView里，视情况而定，如果是mAdapter.notifyItemRemoved(pos)方法不用调用。
     */
    public void quickClose() {
        if (this == mViewCache) {
            cancelAnim();
            mViewCache.scrollTo(0, 0);//关闭
            mViewCache = null;
        }
    }
}
