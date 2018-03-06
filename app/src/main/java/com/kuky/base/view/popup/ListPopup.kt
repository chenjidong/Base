package com.kuky.base.view.popup

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.kuky.base.R
import com.kuky.base.databinding.ListSelectBinding
import com.kuky.base.view.adapter.ListAdapter
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.baseClass.BasePopupWindow

/**
 * @author Kuky
 */
class ListPopup(context: Context, animStyle: Int = R.style.animationForBottomAndBottom,
                width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
                height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) :
        BasePopupWindow<ListSelectBinding>(context, animStyle, width, height) {

    private lateinit var adapter: ListAdapter

    override fun getLayoutId(): Int {
        return R.layout.list_select
    }

    override fun initPopupView() {
        adapter = ListAdapter(mContext!!)
        mViewBinding.listItems.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mViewBinding.listItems.itemAnimator = DefaultItemAnimator()
        mViewBinding.listItems.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        mViewBinding.listItems.adapter = adapter
    }

    fun setPopupList(items: MutableList<String>, listener: BaseRvHeaderFooterAdapter.OnItemClickListener?) {
        adapter.updateAdapterData(items)
        if (listener != null) adapter.setOnItemClickListener(listener)
    }

    fun getAdapter(): ListAdapter {
        return adapter
    }
}