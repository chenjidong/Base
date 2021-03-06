package com.kuky.base.view.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.kuky.base.R
import com.kuky.base.component.DaggerNewsFragmentComponent
import com.kuky.base.contract.NewsContract
import com.kuky.base.databinding.BottomFooterBinding
import com.kuky.base.databinding.FragmentNewsBinding
import com.kuky.base.databinding.TopHeaderBinding
import com.kuky.base.entity.C
import com.kuky.base.entity.News
import com.kuky.base.module.NewsFragmentModule
import com.kuky.base.presenter.NewsPresenter
import com.kuky.base.view.activity.NewsDetailActivity
import com.kuky.base.view.adapter.NewsAdapter
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.baseMvpClass.BaseMvpLazyLoadingFragment
import com.kuky.baselib.generalWidget.ListHandlerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import javax.inject.Inject

/**
 * @author Kuky
 */
class NewsFragment : BaseMvpLazyLoadingFragment<NewsContract.INewsView, NewsPresenter, FragmentNewsBinding>(),
        NewsContract.INewsView, ListHandlerView.OnListReloadListener, OnRefreshListener {

    @Inject lateinit var newsPresenter: NewsPresenter

    @Inject lateinit var mNewsAdapter: NewsAdapter

    private var mType = C.TOP

    private lateinit var header: TopHeaderBinding

    private lateinit var footer: BottomFooterBinding

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun initPresenter(): NewsPresenter {
        return newsPresenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun initFragment(savedInstanceState: Bundle?) {
        DaggerNewsFragmentComponent.builder()
                .newsFragmentModule(NewsFragmentModule(this@NewsFragment))
                .build().inject(this@NewsFragment)

        mViewBinding.newsListHandler.enabledLoadMore(false)
        mViewBinding.newsListHandler.setListPages(mNewsAdapter, false, object : BaseRvHeaderFooterAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                NewsDetailActivity.startDetail(activity!!, mNewsAdapter.getAdapterData()!![position].url!!)
            }
        }, LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false), null, null)

        header = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.top_header, mViewBinding.newsListHandler.getListContainer(), false)
        footer = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_footer, mViewBinding.newsListHandler.getListContainer(), false)
        mViewBinding.newsListHandler.addHeader(header)
        mViewBinding.newsListHandler.addFooter(footer)
    }

    override fun presenterActions() {

    }

    override fun lazyLoading() {
        mPresenter.setNewsToUi()
    }

    override fun setListener() {
        mViewBinding.newsListHandler.setOnListReloadListener(this)
        mViewBinding.newsListHandler.setOnRefreshListener(this)
    }

    fun setNewsType(type: String): NewsFragment {
        if (TextUtils.equals(type, C.TOP) || TextUtils.equals(type, C.SPORT))
            this.mType = type
        else throw IllegalAccessException("type must be one of ${C.TOP} and ${C.SPORT}")
        return this@NewsFragment
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

    override fun getType(): String {
        return mType
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