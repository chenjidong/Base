package com.kuky.base.view.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import android.net.Uri
import com.kuky.base.BR
import com.kuky.base.R
import com.kuky.base.databinding.ItemSelectPicShowBinding
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter

/**
 * @author Kuky
 */
class SelectPicAdapter(context: Context, closeRecycle: Boolean = false) : BaseRvHeaderFooterAdapter<Uri, ItemSelectPicShowBinding>(context, closeRecycle) {

    override fun getAdapterViewId(viewType: Int): Int {
        return R.layout.item_select_pic_show
    }

    override fun setVariable(viewBinding: ViewDataBinding, t: Uri) {
        viewBinding.setVariable(BR.uri, t)
    }

    override fun getItemType(position: Int): Int {
        return 0
    }
}
