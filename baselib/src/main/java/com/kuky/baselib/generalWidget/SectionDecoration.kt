package com.kuky.baselib.generalWidget

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.kuky.baselib.OnGroupListener

/**
 * @author Kuky
 */
class SectionDecoration(groupListener: OnGroupListener?) : RecyclerView.ItemDecoration() {
    private var mGroupListener = groupListener
    private var mIsAlignLeft = true
    private var mGroupHeight = 80

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val groupName = getGroupName(position) ?: return
        if (position == 0 || isFirstInGroup(position)) outRect.top = mGroupHeight
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        /**
         * 获取 item 数量 , 左右边距 padding
         */
        val itemCount = state!!.itemCount
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        var preGroupName: String?
        var currentGroupName: String? = null

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            preGroupName = currentGroupName
            currentGroupName = getGroupName(position)
            if (currentGroupName == null || TextUtils.equals(preGroupName, currentGroupName)) {
                continue
            }
            val viewBottom = view.bottom
            /**
             * 决定当前顶部悬浮 Group 的位置
             */
            var top = Math.max(mGroupHeight, view.top)

            if (position + 1 < itemCount) {
                val nextGroupName = getGroupName(position + 1)
                if (!TextUtils.equals(currentGroupName, nextGroupName) && viewBottom < top) top = viewBottom
            }

            /**
             * 根据 position 获取 view
             */
            val groupView = getGroupView(position) ?: return
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mGroupHeight)
            groupView.layoutParams = lp
            groupView.isDrawingCacheEnabled = true
            groupView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            /**
             * 指定高度、宽度的 GroupView
             */
            groupView.layout(0, 0, right, mGroupHeight)
            groupView.buildDrawingCache()
            val bitmap = groupView.drawingCache
            val marginLeft = if (mIsAlignLeft) 0 else right - groupView.measuredWidth
            c.drawBitmap(bitmap, (left + marginLeft).toFloat(), (top - mGroupHeight).toFloat(), null)
        }
    }

    private fun isFirstInGroup(position: Int): Boolean {
        return if (position == 0) true
        else {
            val preGroupName = getGroupName(position - 1)
            val groupName = getGroupName(position)
            !TextUtils.equals(preGroupName, groupName)
        }
    }

    private fun getGroupName(position: Int): String? {
        return if (mGroupListener == null) null else mGroupListener!!.getGroupName(position)
    }

    private fun getGroupView(position: Int): View? {
        return if (mGroupListener == null) null else mGroupListener!!.getGroupView(position)
    }

    class Builder private constructor(onGroupListener: OnGroupListener) {
        var mDecoration: SectionDecoration? = SectionDecoration(onGroupListener)

        companion object {
            fun init(onGroupListener: OnGroupListener): Builder {
                return Builder(onGroupListener)
            }
        }

        fun setGroupHeight(groupHeight: Int): Builder {
            mDecoration!!.mGroupHeight = groupHeight
            return this@Builder
        }

        fun isAlignLeft(alignLeft: Boolean): Builder {
            mDecoration!!.mIsAlignLeft = alignLeft
            return this@Builder
        }

        fun build(): SectionDecoration {
            return mDecoration!!
        }
    }
}