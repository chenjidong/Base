package com.kuky.baselib_java.generalWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.kuky.baselib_java.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuky
 */
public class TagLayout extends ViewGroup {
    private List<Line> mLines = new ArrayList<>();
    /**
     * 垂直间距
     */
    private float mVerticalSpace;
    /**
     * 水平间距
     */
    private float mHorizontalSpace;
    /**
     * 是否平分剩余空间
     */
    private boolean mDivRemain;
    /**
     * 最大显示行数
     */
    private int mMaxLines;

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        mMaxLines = array.getInteger(R.styleable.TagLayout_max_line, 5);
        mHorizontalSpace = array.getDimension(R.styleable.TagLayout_horizontal_space, 0);
        mVerticalSpace = array.getDimension(R.styleable.TagLayout_vertical_space, 0);
        mDivRemain = array.getBoolean(R.styleable.TagLayout_div_avg, true);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 先进行清理，否则会覆盖之前的数据
         */
        mLines.clear();
        Line mCurrentLine = null;

        /**
         * 获取总的宽度
         */
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int maxWidth = width - getPaddingLeft() - getPaddingRight();

        /**
         * 遍历获取子布局
         */
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            /**
             * 测量子布局
             */
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            /**
             * 首次添加子布局
             */
            if (mCurrentLine == null) {
                mCurrentLine = new Line(maxWidth, mHorizontalSpace);
                mCurrentLine.addView(childView);
                mLines.add(mCurrentLine);
            } else {
                if (mCurrentLine.canAddView(childView)) {
                    /**
                     * 存在别的子布局先判断
                     */
                    mCurrentLine.addView(childView);
                } else if (mLines.size() < mMaxLines) {
                    /**
                     * 如果小于最大的行数则添加
                     */
                    mCurrentLine = new Line(maxWidth, mHorizontalSpace);
                    mCurrentLine.addView(childView);
                    mLines.add(mCurrentLine);
                }
            }
        }

        int height = getPaddingTop() + getPaddingBottom();
        /**
         * 所有行的高度
         */
        for (int i = 0; i < mLines.size(); i++) {
            height += mLines.get(i).getHeight();
        }
        /**
         * 所有数值方向的间距
         */
        height += (mLines.size() - 1) * mVerticalSpace;
        if (mLines.size() == 0) {
            setVisibility(View.GONE);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l = getPaddingLeft();
        t = getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.layout(t, l);
            t += line.getHeight();
            if (i != mLines.size() - 1) {
                /**
                 * 如果不是最后一行则添加一个间距
                 */
                t += mVerticalSpace;
            }
        }
    }

    public List<View> getChildren() {
        List<View> views = new ArrayList<>();
        for (Line l : mLines) {
            views.addAll(l.getViews());
        }
        return views;
    }

    class Line {
        /**
         * 定义一个行的集合来存放子View
         */
        private List<View> views = new ArrayList<>();

        private int maxWidth;

        private int useWidth;

        private int height;

        private float space;

        Line(int maxWidth, float mHorizontalSpace) {
            this.maxWidth = maxWidth;
            this.space = mHorizontalSpace;
        }

        /**
         * 往集合里添加孩子
         */
        void addView(View view) {
            int childWidth = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();

            /**
             * 更新行的使用宽度和高度
             */
            if (views.size() == 0) {
                if (childWidth > maxWidth) {
                    useWidth = maxWidth;
                    height = childHeight;
                } else {
                    useWidth = childWidth;
                    height = childHeight;
                }
            } else {
                useWidth += childWidth + space;
                height = childHeight > height ? childHeight : height;
            }
            /**
             * 添加孩子到集合
             */
            views.add(view);
        }


        /**
         * 判断当前的行是否能添加孩子
         *
         * @return
         */
        boolean canAddView(View view) {
            /**
             * 不存在数据时候可以添加
             */
            if (views.size() == 0) {
                return true;
            }
            /**
             * 大于该行的长度时候不可
             */
            if (view.getMeasuredWidth() > (maxWidth - useWidth - space)) {
                return false;
            }
            return true;
        }

        public void layout(int t, int l) {
            /**
             * 平分剩余空间
             */
            int avg = (maxWidth - useWidth) / views.size();

            /**
             * 指定孩子位置
             */
            for (View v : views) {
                int measuredWidth = v.getMeasuredWidth();
                int measuredHeight = v.getMeasuredHeight();

                if (mDivRemain) {
                    v.measure(MeasureSpec.makeMeasureSpec(measuredWidth + avg, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
                }

                measuredWidth = v.getMeasuredWidth();

                int top = t;
                int left = l;
                int bottom = measuredHeight + t;
                int right = measuredWidth + l;

                v.layout(left, top, right, bottom);

                l += measuredWidth + space;
            }
        }

        public List<View> getViews() {
            return views;
        }

        public void setViews(List<View> views) {
            this.views = views;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public int getUseWidth() {
            return useWidth;
        }

        public void setUseWidth(int useWidth) {
            this.useWidth = useWidth;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public float getSpace() {
            return space;
        }

        public void setSpace(float space) {
            this.space = space;
        }
    }
}
