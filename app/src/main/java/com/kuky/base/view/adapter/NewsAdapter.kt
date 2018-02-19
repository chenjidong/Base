package com.kuky.base.view.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import com.kuky.base.BR
import com.kuky.base.R
import com.kuky.base.databinding.ItemNewsBinding
import com.kuky.base.entity.News
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter

/**
 * @author Kuky
 */
class NewsAdapter(context: Context, closeRecycle: Boolean = false) :
        BaseRvHeaderFooterAdapter<News.ResultBean.DataBean, ItemNewsBinding>(context, closeRecycle) {

    override fun getAdapterViewId(viewType: Int): Int {
        return R.layout.item_news
    }

    override fun setVariable(viewBinding: ViewDataBinding, t: News.ResultBean.DataBean) {
        viewBinding.setVariable(BR.news, t)
    }

    override fun getItemType(position: Int): Int {
        return 0
    }
}