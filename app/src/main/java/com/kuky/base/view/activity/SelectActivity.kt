package com.kuky.base.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kuky.base.AuxGlideEngine
import com.kuky.base.R
import com.kuky.base.component.DaggerSelectActivityComponent
import com.kuky.base.databinding.ActivitySelectBinding
import com.kuky.base.module.SelectActivityModule
import com.kuky.base.view.adapter.SelectPicAdapter
import com.kuky.baselib.OnPermissionListener
import com.kuky.baselib.baseClass.BaseActivity
import com.kuky.baselib.baseUtils.LogUtils
import com.kuky.baselib.baseUtils.ToastUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import javax.inject.Inject

class SelectActivity : BaseActivity<ActivitySelectBinding>() {
    private val CHOOSE_CODE = 0x1001

    @Inject lateinit var selectAdapter: SelectPicAdapter

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mViewBinding.select = this@SelectActivity

        DaggerSelectActivityComponent.builder()
                .selectActivityModule(SelectActivityModule(this@SelectActivity))
                .build().inject(this@SelectActivity)

        mViewBinding.picShow.layoutManager = LinearLayoutManager(this@SelectActivity, LinearLayoutManager.HORIZONTAL, false)
        mViewBinding.picShow.itemAnimator = DefaultItemAnimator()
        mViewBinding.picShow.adapter = selectAdapter
    }

    override fun setListener() {

    }

    fun normalSelect(view: View) {
        onRuntimePermissionsAsk(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                object : OnPermissionListener {
                    override fun onGranted() {
                        Matisse.from(this@SelectActivity)
                                .choose(MimeType.allOf())
                                .theme(R.style.Matisse_Zhihu)
                                .countable(true)
                                .capture(true)
                                .captureStrategy(CaptureStrategy(true, "com.kuky.base.fileprovider"))
                                .maxSelectable(9)
                                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f)
                                .imageEngine(AuxGlideEngine())
                                .forResult(CHOOSE_CODE)
                    }

                    override fun onDenied(deniedPermissions: MutableList<String>) {
                        ToastUtils.showToast(this@SelectActivity, "permissions were denied, and can't continue." +
                                "please grant permissions in setting.")
                    }
                })
    }

    fun draculaSelect(view: View) {
        onRuntimePermissionsAsk(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                object : OnPermissionListener {
                    override fun onGranted() {
                        Matisse.from(this@SelectActivity)
                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                                .theme(R.style.Matisse_Dracula)
                                .countable(false)
                                .maxSelectable(9)
                                .imageEngine(AuxGlideEngine())
                                .forResult(CHOOSE_CODE)
                    }

                    override fun onDenied(deniedPermissions: MutableList<String>) {
                        ToastUtils.showToast(this@SelectActivity, "permissions were denied, and can't continue." +
                                "please grant permissions in setting.")
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_CODE && resultCode == Activity.RESULT_OK) {
            selectAdapter.updateAdapterData(Matisse.obtainResult(data))
            LogUtils.e("urls are ${Matisse.obtainResult(data)}")
            LogUtils.e("rv adapter data are ${selectAdapter.getAdapterData()}")
        }
    }
}
