package com.kuky.base.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kuky.base.R
import com.kuky.base.contract.TopNewsContract
import com.kuky.base.databinding.FragmentNewsBinding
import com.kuky.base.entity.News
import com.kuky.base.presenter.TopNewsPresenter
import com.kuky.base.view.adapter.NewsAdapter
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.baseMvpClass.BaseMvpLazyLoadingFragment
import com.kuky.baselib.baseUtils.ToastUtils
import com.kuky.baselib.generalWidget.ListHandlerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 * @author Kuky
 */
class TopNewsFragment : BaseMvpLazyLoadingFragment<TopNewsContract.ITopNewsView, TopNewsPresenter, FragmentNewsBinding>(),
        TopNewsContract.ITopNewsView, ListHandlerView.OnListReloadListener, OnRefreshListener {

    private lateinit var mNewsAdapter: NewsAdapter

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun initPresenter(): TopNewsPresenter {
        return TopNewsPresenter(this@TopNewsFragment)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun initFragment(savedInstanceState: Bundle?) {
        mNewsAdapter = NewsAdapter(activity!!, true)

        mViewBinding.newsListHandler.enabledLoadMore(false)
        mViewBinding.newsListHandler.setListPages(mNewsAdapter, object : BaseRvHeaderFooterAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                ToastUtils.showToast(activity!!, mNewsAdapter.getAdapterData()!![position].title!!)
            }
        }, LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false), null, null)
    }

    override fun lazyLoad() {
        mPresenter.setNewsToUi()
    }

    override fun setListener() {
        mViewBinding.newsListHandler.setOnListReloadListener(this)
        mViewBinding.newsListHandler.setOnRefreshListener(this)
    }

    override fun queryNewsSucceed(news: MutableList<News.ResultBean.DataBean>) {
        mViewBinding.newsListHandler.updateData(news)
        mViewBinding.newsListHandler.setHandlerState(ListHandlerView.LOAD_SUCCEED_STATE)
    }

    override fun queryNewsEmpty() {
        mViewBinding.newsListHandler.setHandlerState(ListHandlerView.LOAD_EMPTY_STATE)
    }

    override fun queryNewsFailed() {
        mViewBinding.newsListHandler.setHandlerState(ListHandlerView.LOAD_FAILED_STATE)
    }

    override fun onListReload() {
        mPresenter.setNewsToUi()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mViewBinding.newsListHandler.getListContainer()
                .postDelayed({
                    mPresenter.setNewsToUi()
                    mViewBinding.newsListHandler.finishRefresh()
                }, 500)
    }
}