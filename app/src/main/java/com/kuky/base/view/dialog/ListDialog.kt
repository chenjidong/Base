package com.kuky.base.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.kuky.base.R
import com.kuky.base.databinding.ListSelectBinding
import com.kuky.base.view.adapter.ListAdapter
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.baseClass.BaseDialog

/**
 * @author Kuky
 */
class ListDialog : BaseDialog<ListSelectBinding> {
    private lateinit var adapter: ListAdapter

    constructor(context: Context, themeResId: Int = R.style.AlertDialogStyle) : super(context, themeResId)

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener)

    override fun getLayoutId(): Int {
        return R.layout.list_select
    }

    override fun initDialogView() {
        adapter = ListAdapter(mContext!!)
        mViewBinding.listItems.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        mViewBinding.listItems.itemAnimator = DefaultItemAnimator()
        mViewBinding.listItems.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        mViewBinding.listItems.adapter = adapter
    }

    fun setDialogList(items: MutableList<String>, listener: BaseRvHeaderFooterAdapter.OnItemClickListener?) {
        adapter.updateAdapterData(items)
        if (listener != null) adapter.setOnItemClickListener(listener)
    }

    fun getAdapter(): ListAdapter {
        return adapter
    }
}
