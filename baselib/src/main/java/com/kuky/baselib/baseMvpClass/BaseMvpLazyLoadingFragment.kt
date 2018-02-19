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
abstract class BaseMvpLazyLoadingFragment<V : BaseMvpViewImpl, P : BaseMvpPresenter<V>, VB : ViewDataBinding> : Fragment() {
    protected lateinit var mViewBinding: VB
    protected lateinit var mPresenter: P
    protected var isPageCreated: Boolean = false
    protected var isPageVisible: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPageCreated = true
        tryLoad()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter = initPresenter()
        mPresenter.attachView(this as V)
        mViewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        if (enabledEventBus()) EventBus.getDefault().register(this)
        initFragment(savedInstanceState)
        setListener()
        return mViewBinding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isPageVisible = true
            tryLoad()
        } else {
            isPageVisible = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (enabledEventBus()) EventBus.getDefault().unregister(this)
        mPresenter.detachView()
    }

    private fun tryLoad() {
        if (isPageCreated && isPageVisible) {
            lazyLoad()
            isPageCreated = false
            isPageVisible = false
        }
    }

    abstract fun enabledEventBus(): Boolean

    abstract fun initPresenter(): P

    abstract fun getLayoutId(): Int

    abstract fun initFragment(savedInstanceState: Bundle?)

    abstract fun lazyLoad()

    abstract fun setListener()
}
