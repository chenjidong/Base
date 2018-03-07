package com.kuky.baselib_java.generalWidget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.kuky.baselib_java.BR;
import com.kuky.baselib_java.R;
import com.kuky.baselib_java.baseAdapter.BaseRvHeaderFooterAdapter;
import com.kuky.baselib_java.databinding.ListHandlerBinding;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * @author Kuky
 */
public class ListHandlerView<T> extends RelativeLayout {
    public static final int LOAD_LOADING_STATE = 0x00010000;
    public static final int LOAD_EMPTY_STATE = 0x00010001;
    public static final int LOAD_FAILED_STATE = 0x00010002;
    public static final int LOAD_SUCCEED_STATE = 0x00010003;
    public static final int HORIZONTAL_PROGRESS = 0x00020000;
    public static final int CIRCLE_PROGRESS = 0x00020001;

    private ListHandlerBinding mHandlerBinding;
    private int mListState = LOAD_LOADING_STATE;
    private int mProgressStyle = HORIZONTAL_PROGRESS;
    private OnListReloadListener mOnListReloadListener;
    private BaseRvHeaderFooterAdapter mListAdapter;
    private List<T> mData;

    /**
     * 重新加载监听
     */
    public interface OnListReloadListener {
        void reLoad();
    }

    public void setOnListReloadListener(OnListReloadListener loadListener) {
        this.mOnListReloadListener = loadListener;
    }

    /**
     * 刷新监听
     *
     * @param refreshListener
     */
    public void setRefreshListener(OnRefreshListener refreshListener) {
        if (refreshListener != null) {
            mHandlerBinding.listPage.setOnRefreshListener(refreshListener);
        }
    }

    /**
     * 加载更多监听
     *
     * @param loadMoreListener
     */
    public void setLoadMoreListener(OnLoadmoreListener loadMoreListener) {
        if (loadMoreListener != null) {
            mHandlerBinding.listPage.setOnLoadmoreListener(loadMoreListener);
        }
    }

    public ListHandlerView(Context context) {
        this(context, null);
    }

    public ListHandlerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListHandlerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHandlerView(context);
    }

    private void initHandlerView(Context context) {
        mHandlerBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_handler, this, true);
        mHandlerBinding.setVariable(BR.listHandle, this);
        hideAllPage();
        switch (mProgressStyle) {
            case HORIZONTAL_PROGRESS:
                mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_horizontal_loading_progress);
                mHandlerBinding.loadingPage.setBackgroundColor(Color.TRANSPARENT);
                break;
            case CIRCLE_PROGRESS:
                mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_circle_loading_progress);
                mHandlerBinding.loadingPage.setBackgroundColor(Color.parseColor("#75000000"));
                break;
        }

        switch (mListState) {
            case LOAD_LOADING_STATE:
                mHandlerBinding.loadingPage.setVisibility(VISIBLE);
                ((AnimationDrawable) mHandlerBinding.loadingPage.getDrawable()).start();
                break;
            case LOAD_EMPTY_STATE:
                mHandlerBinding.nothingPage.setVisibility(VISIBLE);
                break;
            case LOAD_FAILED_STATE:
                mHandlerBinding.reloadPage.setVisibility(VISIBLE);
                break;
            case LOAD_SUCCEED_STATE:
                mHandlerBinding.listPage.setVisibility(VISIBLE);
                break;
        }
    }

    private void hideAllPage() {
        mHandlerBinding.loadingPage.setVisibility(GONE);
        mHandlerBinding.nothingPage.setVisibility(GONE);
        mHandlerBinding.reloadPage.setVisibility(GONE);
        mHandlerBinding.listPage.setVisibility(GONE);
    }

    /**
     * 设置加载界面
     *
     * @param style
     * @return
     */
    public ListHandlerView setLoadingPage(int style) {
        this.mProgressStyle = style;
        switch (mProgressStyle) {
            case HORIZONTAL_PROGRESS:
                mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_horizontal_loading_progress);
                mHandlerBinding.loadingPage.setBackgroundColor(Color.TRANSPARENT);
                break;
            case CIRCLE_PROGRESS:
                mHandlerBinding.loadingPage.setImageResource(R.drawable.ic_circle_loading_progress);
                mHandlerBinding.loadingPage.setBackgroundColor(Color.parseColor("#75000000"));
                break;
        }
        ((AnimationDrawable) mHandlerBinding.loadingPage.getDrawable()).start();
        return this;
    }

    public void replaceLoadingProgress(@DrawableRes int drawableRes) {
        mHandlerBinding.loadingPage.setImageResource(drawableRes);
        ((AnimationDrawable) mHandlerBinding.loadingPage.getDrawable()).start();
    }

    public void replaceLoadingProgress(AnimationDrawable drawable) {
        mHandlerBinding.loadingPage.setImageDrawable(drawable);
        drawable.start();
    }

    /**
     * 设置加载失败界面图片
     *
     * @param drawableRes
     * @return
     */
    public void setEmptyDrawable(@DrawableRes int drawableRes) {
        mHandlerBinding.nothingPage.setImageResource(drawableRes);
    }

    /**
     * 设置无数据界面图片
     *
     * @param drawableRes
     * @return
     */
    public void setFailedDrawable(@DrawableRes int drawableRes) {
        mHandlerBinding.reloadPage.setImageResource(drawableRes);
    }

    /**
     * 列表界面
     *
     * @param adapter
     * @param listener
     * @param rvManager
     * @param anim
     * @param decoration
     * @return
     */
    public void setLoadSuccessPage(@NonNull BaseRvHeaderFooterAdapter adapter, boolean showBackTop,
                                   BaseRvHeaderFooterAdapter.OnItemClickListener listener,
                                   @NonNull final RecyclerView.LayoutManager rvManager,
                                   RecyclerView.ItemAnimator anim,
                                   RecyclerView.ItemDecoration decoration) {
        this.mListAdapter = adapter;
        this.mData = adapter.getAdapterData();
        mHandlerBinding.listContainer.setAdapter(mListAdapter);
        mHandlerBinding.listContainer.setLayoutManager(rvManager);

        if (showBackTop) {
            getListContainer().addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (rvManager instanceof LinearLayoutManager) {
                        int firstVisibleItemPosition = ((LinearLayoutManager) rvManager).findFirstVisibleItemPosition();
                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_IDLE:
                                mHandlerBinding.backTop.setVisibility(firstVisibleItemPosition <= 2 ? View.GONE : View.VISIBLE);
                                break;
                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                mHandlerBinding.backTop.setVisibility(View.GONE);
                                break;
                        }
                    } else if (rvManager instanceof GridLayoutManager) {
                        int firstVisibleItemPosition = ((GridLayoutManager) rvManager).findFirstVisibleItemPosition();
                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_IDLE:
                                mHandlerBinding.backTop.setVisibility(firstVisibleItemPosition <= 6 ? View.GONE : View.VISIBLE);
                                break;
                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                mHandlerBinding.backTop.setVisibility(GONE);
                                break;
                        }
                    } else if (rvManager instanceof StaggeredGridLayoutManager) {
                        int[] firstVisibleItemPositions = new int[1];
                        ((StaggeredGridLayoutManager) rvManager).findFirstVisibleItemPositions(firstVisibleItemPositions);
                        switch (newState) {
                            case RecyclerView.SCROLL_STATE_IDLE:
                                mHandlerBinding.backTop.setVisibility(firstVisibleItemPositions[0] <= 6 ? View.GONE : View.VISIBLE);
                                break;
                            case RecyclerView.SCROLL_STATE_DRAGGING:
                                mHandlerBinding.backTop.setVisibility(GONE);
                                break;
                        }
                    }
                }
            });
        } else {
            mHandlerBinding.backTop.setVisibility(GONE);
        }

        if (listener != null) {
            mListAdapter.setOnItemClickListener(listener);
        }

        mHandlerBinding.listContainer.setItemAnimator(anim == null ? new DefaultItemAnimator() : anim);

        if (decoration != null) {
            mHandlerBinding.listContainer.addItemDecoration(decoration);
        }
    }

    public void setListAnim(@NonNull RecyclerView.ItemAnimator anim) {
        mHandlerBinding.listContainer.setItemAnimator(anim);
    }

    public void setListDecoration(@NonNull RecyclerView.ItemDecoration decoration) {
        mHandlerBinding.listContainer.addItemDecoration(decoration);
    }

    public void setBackTopButtonResource(@DrawableRes int drawableRes) {
        mHandlerBinding.backTop.setImageResource(drawableRes);
    }

    public void addHeader(ViewDataBinding headerBinding) {
        mListAdapter.addHeaderBinding(headerBinding);
    }

    public void addFooter(ViewDataBinding footerBinding) {
        mListAdapter.addFooterBinding(footerBinding);
    }

    public List<ViewDataBinding> getHeaders() {
        return mListAdapter.getHeaderBindings();
    }

    public List<ViewDataBinding> getFooters() {
        return mListAdapter.getFooterBindings();
    }

    public void reloadClick(View view) {
        if (mOnListReloadListener != null && mListState == LOAD_FAILED_STATE) {
            mOnListReloadListener.reLoad();
        }
    }

    public void backTop(View view) {
        getListContainer().smoothScrollToPosition(0);
    }

    /**
     * 更新界面数据
     *
     * @param data
     */
    public void updateData(List<T> data) {
        this.mData = data;
        this.mListAdapter.updateAdapterData(mData);
    }

    public List<T> getListData() {
        return mListAdapter.getAdapterData();
    }

    /**
     * 添加数据
     *
     * @param t
     */
    public void addData(T t) {
        this.mListAdapter.addData(mData);
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addData(List<T> data) {
        this.mListAdapter.addAllData(mData);
    }

    /**
     * 去除特定数据
     *
     * @param t
     */
    public void removeData(T t) {
        mListAdapter.removeData(mData);
    }

    /**
     * 去除特定数据
     *
     * @param position
     */
    public void removeData(int position) {
        mListAdapter.removeData(mData);
    }

    /**
     * 清除所有数据
     */
    public void cleanData() {
        mListAdapter.cleanData();
    }

    /**
     * 重置状态
     *
     * @param state
     */
    public void setViewState(int state) {
        this.mListState = state;
        hideAllPage();
        switch (mListState) {
            case LOAD_LOADING_STATE:
                mHandlerBinding.loadingPage.setVisibility(VISIBLE);
                ((AnimationDrawable) mHandlerBinding.loadingPage.getDrawable()).start();
                break;
            case LOAD_EMPTY_STATE:
                mHandlerBinding.nothingPage.setVisibility(VISIBLE);
                break;
            case LOAD_FAILED_STATE:
                mHandlerBinding.reloadPage.setVisibility(VISIBLE);
                break;
            case LOAD_SUCCEED_STATE:
                mHandlerBinding.listPage.setVisibility(VISIBLE);
                break;
        }
    }

    public SmartRefreshLayout getRefreshLoadContainer() {
        return mHandlerBinding.listPage;
    }

    public RecyclerView getListContainer() {
        return mHandlerBinding.listContainer;
    }

    public void enabledRefresh(boolean enabledRefresh) {
        getRefreshLoadContainer().setEnableRefresh(enabledRefresh);
    }

    public void enabledLoadMore(boolean enabledLoadMore) {
        getRefreshLoadContainer().setEnableLoadmore(enabledLoadMore);
    }

    public void enabledAutoLoadMore(boolean enabledAutoLoadMore) {
        getRefreshLoadContainer().setEnableLoadmore(enabledAutoLoadMore);
    }

    public void enabledLoadMoreWhenNotFull(boolean enabledLoadMoreWhenNotFull) {
        getRefreshLoadContainer().setEnableLoadmoreWhenContentNotFull(enabledLoadMoreWhenNotFull);
    }

    public void stopLoadMore() {
        getRefreshLoadContainer().setLoadmoreFinished(true);
    }

    public void restartLoadMore() {
        getRefreshLoadContainer().setLoadmoreFinished(false);
    }

    public void finishRefresh() {
        getRefreshLoadContainer().finishRefresh();
    }

    public void finishLoadMore() {
        getRefreshLoadContainer().finishLoadmore();
    }
}
