package com.kuky.baselib.baseMvpClass

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.greenrobot.eventbus.EventBus

/**
 * @author Kuky
 */
abstract class BaseMvpFragment<V : BaseMvpViewImpl, P : BaseMvpPresenter<V>, VB : ViewDataBinding> : Fragment() {
    protected lateinit var mViewBinding: VB
    protected lateinit var mPresenter: P

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        if (enabledEventBus()) EventBus.getDefault().register(this)
        initFragment(savedInstanceState)
        mPresenter = initPresenter()
        mPresenter.attachView(this as V)
        presenterActions()
        setListener()
        return mViewBinding.root
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (enabledEventBus()) EventBus.getDefault().unregister(this)
        mPresenter.detachView()
    }

    abstract fun enabledEventBus(): Boolean

    abstract fun initPresenter(): P

    abstract fun getLayoutId(): Int

    abstract fun initFragment(savedInstanceState: Bundle?)

    abstract fun presenterActions()

    abstract fun setListener()
}