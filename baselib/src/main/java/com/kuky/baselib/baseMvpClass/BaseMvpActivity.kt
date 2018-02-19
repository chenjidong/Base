package com.kuky.baselib.baseMvpClass

import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.kuky.baselib.ActivityManager
import com.kuky.baselib.OnPermissionListener
import org.greenrobot.eventbus.EventBus

/**
 * @author Kuky
 */
abstract class BaseMvpActivity<V : BaseMvpViewImpl, P : BaseMvpPresenter<V>, VB : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var mViewBinding: VB
    protected lateinit var mPresenter: P
    private var mOnPermissionLister: OnPermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enabledEventBus()) EventBus.getDefault().register(this)
        mPresenter = initPresenter()
        mPresenter.attachView(this as V)
        ActivityManager.addActivity(this)
        mViewBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initActivity(savedInstanceState)
        setListener()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (enabledEventBus()) EventBus.getDefault().unregister(this)
        mPresenter.detachView()
        ActivityManager.removeActivity(this)
    }

    abstract fun enabledEventBus(): Boolean

    abstract fun initPresenter(): P

    abstract fun getLayoutId(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)

    abstract fun setListener()

    fun onRuntimePermissionsAsk(permissions: Array<String>, permissionListener: OnPermissionListener) {
        this.mOnPermissionLister = permissionListener
        val activity = ActivityManager.getTopActivity()
        val deniedPermissions: MutableList<String> = mutableListOf()

        permissions
                .filterNot { ContextCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_GRANTED }
                .forEach { deniedPermissions.add(it) }

        if (deniedPermissions.isEmpty())
            mOnPermissionLister!!.onGranted()
        else
            ActivityCompat.requestPermissions(activity!!, deniedPermissions.toTypedArray(), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            val deniedPermissions: MutableList<String> = mutableListOf()
            if (grantResults.isNotEmpty()) {
                grantResults
                        .filter { it != PackageManager.PERMISSION_GRANTED }
                        .mapTo(deniedPermissions) { deniedPermissions[it] }

                if (deniedPermissions.isEmpty()) {
                    mOnPermissionLister!!.onGranted()
                } else {
                    mOnPermissionLister!!.onDenied(deniedPermissions)
                }
            }
        }
    }
}
