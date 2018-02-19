package com.kuky.base.view.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import com.kuky.base.BR
import com.kuky.base.R
import com.kuky.base.databinding.ItemPopupDialogBinding
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter

/**
 * @author Kuky
 */
class ListAdapter(context: Context, closeRecycle: Boolean = false) :
        BaseRvHeaderFooterAdapter<String, ItemPopupDialogBinding>(context, closeRecycle) {

    override fun getAdapterViewId(viewType: Int): Int {
        return R.layout.item_popup_dialog
    }

    override fun setVariable(viewBinding: ViewDataBinding, t: String) {
        viewBinding.setVariable(BR.str, t)
    }

    override fun getItemType(position: Int): Int {
        return 0
    }
}