package com.kuky.baselib_java.generalWidget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.kuky.baselib_java.OnGroupListener;

/**
 * @author Kuky
 */
public class SectionDecoration extends RecyclerView.ItemDecoration {
    private OnGroupListener mOnGroupListener;
    private boolean mIsAlignLeft = true;
    private int mGroupHeight = 80;

    public SectionDecoration(OnGroupListener groupListener) {
        this.mOnGroupListener = groupListener;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        String groupName = getGroupName(position);
        if (TextUtils.isEmpty(groupName)) return;
        if (position == 0 || isFirstInGroup(position)) outRect.top = mGroupHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getLeft();
        int right = parent.getRight();
        String preGroupName;
        String currentGroupName = null;

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupName = currentGroupName;
            currentGroupName = getGroupName(position);
            if (currentGroupName == null || TextUtils.equals(preGroupName, currentGroupName)) {
                continue;
            }

            int viewBottom = view.getBottom();
            int top = Math.max(mGroupHeight, view.getTop());
            if (position + 1 < itemCount) {
                String nextGroupName = getGroupName(position + 1);
                if (!TextUtils.equals(currentGroupName, nextGroupName) && viewBottom < top) top = viewBottom;
            }

            View groupView = getGroupView(position);
            if (groupView == null) return;
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mGroupHeight);
            groupView.setLayoutParams(lp);
            groupView.setDrawingCacheEnabled(true);
            groupView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            groupView.layout(0, 0, right, mGroupHeight);
            groupView.buildDrawingCache();
            Bitmap bitmap = groupView.getDrawingCache();
            int marginLeft = mIsAlignLeft ? 0 : right - groupView.getMeasuredWidth();
            c.drawBitmap(bitmap, left + marginLeft, top - mGroupHeight, null);
        }
    }

    private boolean isFirstInGroup(int position) {
        if (position == 0) {
            return true;
        } else {
            String preGroupName = getGroupName(position - 1);
            String currentGroupName = getGroupName(position);
            return !TextUtils.equals(preGroupName, currentGroupName);
        }
    }

    private String getGroupName(int position) {
        return mOnGroupListener == null ? null : mOnGroupListener.getGroupName(position);
    }

    private View getGroupView(int position) {
        return mOnGroupListener == null ? null : mOnGroupListener.getGroupView(position);
    }

    public static class Builder {
        private SectionDecoration mDecoration;

        private Builder(OnGroupListener listener) {
            mDecoration = new SectionDecoration(listener);
        }

        public static Builder init(OnGroupListener listener) {
            return new Builder(listener);
        }

        public Builder setGroupHeight(int height) {
            mDecoration.mGroupHeight = height;
            return this;
        }

        public Builder isAlignLeft(boolean alignLeft) {
            mDecoration.mIsAlignLeft = alignLeft;
            return this;
        }

        public SectionDecoration build() {
            return mDecoration;
        }
    }
}
