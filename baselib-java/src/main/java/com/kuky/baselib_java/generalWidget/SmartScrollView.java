package com.kuky.baselib_java.generalWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author Kuky
 */
public class SmartScrollView extends ScrollView {
    private OnScrollChangeListener mChangeListener;

    public void setOnScrollChangeListener(OnScrollChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public SmartScrollView(Context context) {
        this(context, null);
    }

    public SmartScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mChangeListener != null) {
            mChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChange(View view, int scrollX, int scrollY, int oldX, int oldY);
    }
}
