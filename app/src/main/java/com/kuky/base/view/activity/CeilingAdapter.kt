package com.kuky.base.view.activity

import android.content.Context
import android.databinding.ViewDataBinding
import com.kuky.base.BR
import com.kuky.base.R
import com.kuky.base.databinding.ItemScrollCeilingBinding
import com.kuky.base.entity.CeilingData
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter

/**
 * @author Kuky
 */
class CeilingAdapter(context: Context, closeRecycler: Boolean = false) : BaseRvHeaderFooterAdapter<CeilingData, ItemScrollCeilingBinding>(context, closeRecycler) {

    override fun getAdapterViewId(viewType: Int): Int {
        return R.layout.item_scroll_ceiling
    }

    override fun setVariable(viewBinding: ViewDataBinding, t: CeilingData) {
        viewBinding.setVariable(BR.ceil, t)
    }

    override fun getItemType(position: Int): Int {
        return 0
    }
}