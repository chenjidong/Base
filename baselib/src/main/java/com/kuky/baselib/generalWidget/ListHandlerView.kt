package com.kuky.baselib.generalWidget

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.support.annotation.DrawableRes
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.kuky.baselib.R
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.databinding.ListHandlerBinding
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 * @author Kuky
 */
class ListHandlerView<T : Any> : RelativeLayout {
    private lateinit var mHandlerBinding: ListHandlerBinding
    private var mState = LOAD_LOADING_STATE
    private var mProgressStyle = HORIZONTAL_PROGRESS
    private var mListAdapter: BaseRvHeaderFooterAdapter<T, ViewDataBinding>? = null
    private var mData: MutableList<T>? = null
    private var mOnListReloadListener: OnListReloadListener? = null

    interface OnListReloadListener {
        fun onListReload()
    }

    fun setOnListReloadListener(listener: OnListReloadListener) {
        this.mOnListReloadListener = listener
    }

    fun setOnRefreshListener(listener: OnRefreshListener) {
        mHandlerBinding.listPage.setOnRefreshListener(listener)
    }

    fun setOnLoadMoreListener(listener: OnLoadmoreListener) {
        mHandlerBinding.listPage.setOnLoadmoreListener(listener)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        initHandlerView(context)
    }

    private fun initHandlerView(context: Context) {
        mHandlerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_handler, this, true)
        mHandlerBinding.listHandle = this@ListHandlerView
        hideAllPage()

        when (mProgressStyle) {
            HORIZONTAL_PROGRESS -> mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_horizontal_loading_progress)
            CIRCLE_PROGRESS -> {
                mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_circle_loading_progress)
                mHandlerBinding.loadingPage.setBackgroundColor(Color.BLACK)
            }
        }

        when (mState) {
            LOAD_LOADING_STATE -> {
                mHandlerBinding.loadingPage.visibility = View.VISIBLE
                (mHandlerBinding.loadingPage.drawable as AnimationDrawable).start()
            }
            LOAD_EMPTY_STATE -> mHandlerBinding.nothingPage.visibility = View.VISIBLE
            LOAD_FAILED_STATE -> mHandlerBinding.reloadPage.visibility = View.VISIBLE
            LOAD_SUCCEED_STATE -> mHandlerBinding.listPage.visibility = View.VISIBLE
        }
    }

    private fun hideAllPage() {
        mHandlerBinding.loadingPage.visibility = GONE
        mHandlerBinding.reloadPage.visibility = GONE
        mHandlerBinding.nothingPage.visibility = GONE
        mHandlerBinding.listPage.visibility = GONE
    }

    fun setLoadingStyle(style: Int) {
        this.mProgressStyle = style
        when (mProgressStyle) {
            HORIZONTAL_PROGRESS -> mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_horizontal_loading_progress)
            CIRCLE_PROGRESS -> mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_circle_loading_progress)
        }
        (mHandlerBinding.loadingPage.drawable as AnimationDrawable).start()
    }

    fun setEmptyDrawable(@DrawableRes drawableRes: Int) {
        mHandlerBinding.nothingPage.setImageResource(drawableRes)
    }

    fun setFailedDrawable(@DrawableRes drawableRes: Int) {
        mHandlerBinding.reloadPage.setImageResource(drawableRes)
    }

    fun setListPages(adapter: BaseRvHeaderFooterAdapter<T, ViewDataBinding>,
                     listener: BaseRvHeaderFooterAdapter.OnItemClickListener?,
                     rvManager: RecyclerView.LayoutManager,
                     anim: RecyclerView.ItemAnimator?,
                     decoration: RecyclerView.ItemDecoration?) {

        this.mListAdapter = adapter

        mHandlerBinding.listContainer.adapter = mListAdapter

        mHandlerBinding.listContainer.layoutManager = rvManager

        if (listener != null) {
            mListAdapter!!.setOnItemClickListener(listener)
        }

        mHandlerBinding.listContainer.itemAnimator = anim ?: DefaultItemAnimator()

        if (decoration != null) {
            mHandlerBinding.listContainer.addItemDecoration(decoration)
        }
    }

    fun addHeader(headerBinding: ViewDataBinding) {
        mListAdapter!!.addHeaderBinding(headerBinding)
    }

    fun addFooter(footerBinding: ViewDataBinding) {
        mListAdapter!!.addFooterBinding(footerBinding)
    }

    fun reloadClick(view: View) {
        if (mOnListReloadListener != null && mState == LOAD_FAILED_STATE) {
            mOnListReloadListener!!.onListReload()
        }
    }

    fun updateData(data: MutableList<T>?) {
        this.mListAdapter!!.setAdapterData(data)
    }

    fun addData(data: T) {
        this.mListAdapter!!.addData(data)
    }

    fun addData(data: MutableList<T>) {
        this.mListAdapter!!.addData(data)
    }

    fun removeData(data: T) {
        this.mListAdapter!!.removeData(data)
    }

    fun removeData(position: Int) {
        this.mListAdapter!!.removeData(position)
    }

    fun cleanData() {
        this.mListAdapter!!.clearData()
    }

    fun setHandlerState(state: Int) {
        this.mState = state
        hideAllPage()
        when (mState) {
            LOAD_LOADING_STATE -> {
                mHandlerBinding.loadingPage.visibility = View.VISIBLE
                (mHandlerBinding.loadingPage.drawable as AnimationDrawable).start()
            }
            LOAD_EMPTY_STATE -> mHandlerBinding.nothingPage.visibility = View.VISIBLE
            LOAD_FAILED_STATE -> mHandlerBinding.reloadPage.visibility = View.VISIBLE
            LOAD_SUCCEED_STATE -> mHandlerBinding.listPage.visibility = View.VISIBLE
        }
    }

    fun getRefreshLoadContainer(): SmartRefreshLayout {
        return mHandlerBinding.listPage
    }

    fun getListContainer(): RecyclerView {
        return mHandlerBinding.listContainer
    }

    fun enabledRefresh(enabledRefresh: Boolean = true) {
        getRefreshLoadContainer().isEnableRefresh = enabledRefresh
    }

    fun enabledLoadMore(enabledLoadMore: Boolean = true) {
        getRefreshLoadContainer().isEnableLoadmore = enabledLoadMore
    }

    fun enabledAutoRefresh(enabledAutoRefresh: Boolean = false) {
        getRefreshLoadContainer().isEnableAutoLoadmore = enabledAutoRefresh
    }

    fun enabledAutoLoadMore(enabledAutoLoadMore: Boolean = false) {
        getRefreshLoadContainer().isEnableAutoLoadmore = enabledAutoLoadMore
    }

    fun enabledLoadMoreWhenNotFull(enabledLoadMoreWhenNotFull: Boolean = false) {
        getRefreshLoadContainer().setEnableLoadmoreWhenContentNotFull(enabledLoadMoreWhenNotFull)
    }

    fun stopLoadMore() {
        getRefreshLoadContainer().isLoadmoreFinished = true
    }

    fun restartLoadMore() {
        getRefreshLoadContainer().isLoadmoreFinished = false
    }

    fun finishRefresh() {
        getRefreshLoadContainer().finishRefresh()
    }

    fun finishLoadMore() {
        getRefreshLoadContainer().finishLoadmore()
    }

    companion object {
        val LOAD_LOADING_STATE = 0x00010000
        val LOAD_EMPTY_STATE = 0x00010001
        val LOAD_FAILED_STATE = 0x00010002
        val LOAD_SUCCEED_STATE = 0x00010003
        val HORIZONTAL_PROGRESS = 0x00020000
        val CIRCLE_PROGRESS = 0x00020001
    }
}
