package com.kuky.baselib.baseClass

import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kuky.baselib.ActivityManager
import com.kuky.baselib.OnPermissionListener
import org.greenrobot.eventbus.EventBus

/**
 * @author Kuky
 */
abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var mViewBinding: VB
    private var mPermissionListener: OnPermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (enabledEventBus()) EventBus.getDefault().register(this)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && enableTransparentStatus()) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT

            if (supportActionBar != null)
                supportActionBar!!.hide()
        }
        ActivityManager.addActivity(this)
        mViewBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initActivity(savedInstanceState)
        setListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (enabledEventBus()) EventBus.getDefault().unregister(this)
        ActivityManager.removeActivity(this)
    }

    abstract fun enabledEventBus(): Boolean

    abstract fun enableTransparentStatus(): Boolean

    abstract fun getLayoutId(): Int

    abstract fun initActivity(savedInstanceState: Bundle?)

    abstract fun setListener()

    protected fun onRuntimePermissionsAsk(permissions: kotlin.Array<String>, listener: OnPermissionListener) {
        this.mPermissionListener = listener
        val activity = ActivityManager.getTopActivity()
        val deniedPermissions: MutableList<String> = mutableListOf()

        permissions
                .filterNot { ContextCompat.checkSelfPermission(activity!!, it) == PackageManager.PERMISSION_GRANTED }
                .forEach { deniedPermissions.add(it) }

        if (deniedPermissions.isEmpty())
            mPermissionListener!!.onGranted()
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
                        .mapTo(deniedPermissions) { permissions[it] }

                if (deniedPermissions.isEmpty())
                    mPermissionListener!!.onGranted()
                else
                    mPermissionListener!!.onDenied(deniedPermissions)
            }
        }
    }
}
