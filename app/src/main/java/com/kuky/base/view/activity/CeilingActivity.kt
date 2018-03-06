package com.kuky.base.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuky.base.R
import com.kuky.base.component.DaggerCeilingActivityComponent
import com.kuky.base.contract.CeilingContract
import com.kuky.base.databinding.ActivityCeilingBinding
import com.kuky.base.entity.CeilingData
import com.kuky.base.module.CeilingActivityModule
import com.kuky.base.presenter.CeilingPresenter
import com.kuky.base.view.adapter.CeilingAdapter
import com.kuky.baselib.OnGroupListener
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.baseMvpClass.BaseMvpActivity
import com.kuky.baselib.baseUtils.ScreenUtils
import com.kuky.baselib.baseUtils.ToastUtils
import com.kuky.baselib.generalWidget.ListHandlerView
import com.kuky.baselib.generalWidget.SectionDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import javax.inject.Inject

class CeilingActivity : BaseMvpActivity<CeilingContract.ICeilingView, CeilingPresenter, ActivityCeilingBinding>(), CeilingContract.ICeilingView, ListHandlerView.OnListReloadListener, OnRefreshListener, OnLoadmoreListener {

    @Inject lateinit var ceilAdapter: CeilingAdapter
    @Inject lateinit var ceilPresenter: CeilingPresenter

    private lateinit var decoration: SectionDecoration

    override fun enableTransparentStatus(): Boolean {
        return false
    }

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun initPresenter(): CeilingPresenter {
        return ceilPresenter
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ceiling
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mViewBinding.ceilingAct = this@CeilingActivity

        DaggerCeilingActivityComponent.builder()
                .ceilingActivityModule(CeilingActivityModule(this@CeilingActivity))
                .build().inject(this@CeilingActivity)

        mViewBinding.ceilList.setListPages(ceilAdapter, true, object : BaseRvHeaderFooterAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                ToastUtils.showToast(this@CeilingActivity, ceilAdapter.getAdapterData()!![position].selfName)
            }
        }, LinearLayoutManager(this@CeilingActivity, LinearLayoutManager.VERTICAL, false), null, null)
    }

    override fun presenterActions() {
        mPresenter.setCeilingsToUi()
        /**
         * 不可以多次设置 Decoration, 放在数据加载后再添加 , 防止刷新后出现空白
         */
        mViewBinding.ceilList.setListDecoration(decoration)
    }

    override fun setListener() {
        mViewBinding.ceilList.setOnListReloadListener(this)
        mViewBinding.ceilList.setOnRefreshListener(this)
        mViewBinding.ceilList.setOnLoadMoreListener(this)
    }

    override fun queryCeilingsSucceed(ceilings: MutableList<CeilingData>) {
        decoration = SectionDecoration.Builder
                .init(object : OnGroupListener {
                    override fun getGroupName(position: Int): String? {
                        return if (ceilings.size > position) ceilings[position].groupName else null
                    }

                    override fun getGroupView(position: Int): View? {
                        return if (ceilings.size > position) {
                            val gv = LayoutInflater.from(this@CeilingActivity).inflate(R.layout.item_group_top, null, false)
                            Glide.with(this@CeilingActivity)
                                    .load(ceilings[position].groupLabel)
                                    .apply(RequestOptions()
                                            .error(R.drawable.ic_launcher)
                                            .placeholder(R.drawable.ic_launcher)
                                            .centerCrop())
                                    .into(gv.findViewById(R.id.g_image))
                            gv.findViewById<TextView>(R.id.g_text).text = (ceilings[position].groupName)
                            gv
                        } else null
                    }
                }).setGroupHeight(ScreenUtils.dip2Px(this@CeilingActivity, 40f)).build()

        mViewBinding.ceilList.updateData(ceilings)
        mViewBinding.ceilList.setHandlerState(ListHandlerView.LOAD_SUCCEED_STATE)
    }

    override fun queryCeilingEmpty() {
        mViewBinding.ceilList.setHandlerState(ListHandlerView.LOAD_EMPTY_STATE)
    }

    override fun queryCeilingFailed() {
        mViewBinding.ceilList.setHandlerState(ListHandlerView.LOAD_FAILED_STATE)
    }

    override fun queryMoreCeiling(ceilings: MutableList<CeilingData>) {
        mViewBinding.ceilList.addAllData(ceilings)
    }

    override fun noMoreCeiling(msg: String) {
        mViewBinding.ceilList.stopLoadMore()
        ToastUtils.showToast(this@CeilingActivity, msg)
    }

    override fun dataSize(): Int {
        return mViewBinding.ceilList.getListData()!!.size
    }

    override fun onListReload() {
        mViewBinding.ceilList.restartLoadMore()
        mPresenter.setCeilingsToUi()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mViewBinding.ceilList.restartLoadMore()
        mViewBinding.ceilList.getRefreshLoadContainer()
                .postDelayed({
                    mPresenter.setCeilingsToUi()
                    mViewBinding.ceilList.finishRefresh()
                }, 500)
    }

    override fun onLoadmore(refreshLayout: RefreshLayout?) {
        mViewBinding.ceilList.getRefreshLoadContainer()
                .postDelayed({
                    mPresenter.setMoreCeilingToUi()
                    mViewBinding.ceilList.finishLoadMore()
                }, 500)
    }
}
