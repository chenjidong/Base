package com.kuky.base.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.content.FileProvider
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import com.kuky.base.R
import com.kuky.base.component.DaggerMainActivityComponent
import com.kuky.base.contract.MainContract
import com.kuky.base.databinding.ActivityMainBinding
import com.kuky.base.entity.C
import com.kuky.base.event.TransEvent
import com.kuky.base.module.MainActivityModule
import com.kuky.base.presenter.MainPresenter
import com.kuky.base.view.dialog.ListDialog
import com.kuky.base.view.popup.ListPopup
import com.kuky.baselib.ActivityManager
import com.kuky.baselib.OnPermissionListener
import com.kuky.baselib.baseAdapter.BaseRvHeaderFooterAdapter
import com.kuky.baselib.baseMvpClass.BaseMvpActivity
import com.kuky.baselib.baseUtils.ScreenUtils
import com.kuky.baselib.baseUtils.TimeTransformUtils
import com.kuky.baselib.baseUtils.ToastUtils
import com.kuky.looppicker.TimePickerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import zlc.season.rxdownload3.RxDownload
import zlc.season.rxdownload3.core.*
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("SetTextI18n")
open class MainActivity : BaseMvpActivity<MainContract.IMainView, MainPresenter, ActivityMainBinding>(),
        MainContract.IMainView {

    @Inject lateinit var mainPresenter: MainPresenter
    @Inject lateinit var listPopup: ListPopup
    @Inject lateinit var listDialog: ListDialog
    @Inject lateinit var mHandler: Handler
    private var mDisposable: Disposable? = null
    private var readyExit = false

    override fun initPresenter(): MainPresenter {
        return mainPresenter
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun enabledEventBus(): Boolean {
        return true
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mViewBinding.main = this@MainActivity

        DaggerMainActivityComponent.builder()
                .mainActivityModule(MainActivityModule(this@MainActivity,
                        popupHeight = ScreenUtils.getScreenHeight(this@MainActivity) / 2))
                .build().inject(this@MainActivity)

        listDialog.setDialogHeight(ScreenUtils.getScreenHeight(this@MainActivity) / 2)
    }

    override fun presenterActions() {
        mPresenter.setListToPopupAndDialog()
    }

    override fun setListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null && !mDisposable!!.isDisposed)
            mDisposable!!.dispose()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click()
        }
        return false
    }

    override fun queryPopupAndDialogList(data: MutableList<String>?) {
        if (data != null) {
            listPopup.setPopupList(data, object : BaseRvHeaderFooterAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, view: View) {
                    ToastUtils.showToast(this@MainActivity,
                            "Click ${listPopup.getAdapter().getAdapterData()!![position]}")
                    listPopup.dismiss()
                }
            })

            listDialog.setDialogList(data, object : BaseRvHeaderFooterAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, view: View) {
                    ToastUtils.showToast(this@MainActivity,
                            "Click ${listDialog.getAdapter().getAdapterData()!![position]}")
                    listDialog.dismiss()
                }
            })
        }
    }

    fun toNewsList(view: View) {
        startActivity(Intent(this@MainActivity, NewsActivity::class.java))
    }

    fun showPopup(view: View) {
        listPopup.setBackgroundAlpha(0.7f)
        listPopup.showAtLocation(view, Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0, 0)
    }

    fun showDialog(view: View) {
        listDialog.show()
    }

    fun eventTrans(view: View) {
        startActivity(Intent(this@MainActivity, EventActivity::class.java))
    }

    fun apkDown(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            onRuntimePermissionsAsk(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                    object : OnPermissionListener {
                        override fun onGranted() {
                            startDownload(C.DOWNLOAD_URL)
                        }

                        override fun onDenied(deniedPermissions: MutableList<String>) {
                            ToastUtils.showToast(this@MainActivity, "permissions were denied, and can't continue." +
                                    "please grant permissions in setting.")
                        }
                    })
        } else {
            startDownload(C.DOWNLOAD_URL)
        }
    }

    fun timePick(view: View) {
        val timePicker = TimePickerView.Builder(this@MainActivity,
                TimePickerView.OnTimeSelectListener { date, _ ->
                    mViewBinding.downState.visibility = View.VISIBLE
                    mViewBinding.downState.text = TimeTransformUtils.date2String(date)
                    mHandler.postDelayed({
                        mViewBinding.downState.visibility = View.GONE
                    }, 2000)
                }).build()
        timePicker.show(view)
    }

    fun database(view: View) {
        startActivity(Intent(this@MainActivity, DaoActivity::class.java))
    }

    private fun startDownload(url: String) {
        mDisposable = RxDownload
                .create(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t ->
                    when (t) {
                        is Normal, is Suspend -> {
                            mViewBinding.downState.visibility = View.VISIBLE
                            mViewBinding.downState.text = "start to download apk..."
                            mHandler.postDelayed({
                                mViewBinding.downState.visibility = View.GONE
                                RxDownload.start(url).subscribe()
                            }, 1000)
                        }

                        is Succeed -> {
                            mViewBinding.downState.visibility = View.VISIBLE
                            mViewBinding.downState.text = "apk download succeed..."
                            mHandler.postDelayed({
                                mViewBinding.downState.visibility = View.GONE

                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                val data: Uri
                                val fileSplit = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                val file = File(Environment.getExternalStorageDirectory(),
                                        C.DOWNLOAD_FOLDER + "/" + fileSplit[fileSplit.size - 1])
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    data = FileProvider.getUriForFile(this@MainActivity, "com.kuky.base.fileprovider", file)
                                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                } else {
                                    data = Uri.fromFile(file)
                                }
                                intent.setDataAndType(data, "application/vnd.android.package-archive")
                                startActivity(intent)
                            }, 1000)
                        }

                        is Failed -> {
                            mViewBinding.downState.visibility = View.VISIBLE
                            mViewBinding.downState.text = "apk download failed..."
                            mViewBinding.downState.setOnClickListener({ RxDownload.start(url).subscribe() })
                        }

                        is Downloading -> {
                            mViewBinding.downState.visibility = View.VISIBLE
                            mViewBinding.downState.text = "downloading...${t.percent()}"
                            mViewBinding.downState.setOnClickListener({ RxDownload.stop(url).subscribe() })
                        }

                        is Deleted -> {
                            mViewBinding.downState.visibility = View.VISIBLE
                            mViewBinding.downState.text = "apk was deleted..."
                            mViewBinding.downState.setOnClickListener({ RxDownload.start(url).subscribe() })
                        }

                        is Waiting -> {
                            mViewBinding.downState.visibility = View.VISIBLE
                            mViewBinding.downState.text = "pausing..."
                            mViewBinding.downState.setOnClickListener({ RxDownload.start(url).subscribe() })
                        }
                    }
                }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showTransformedContent(event: TransEvent) {
        mViewBinding.downState.visibility = View.VISIBLE
        mViewBinding.downState.text = event.message
        mHandler.postDelayed({
            mViewBinding.downState.visibility = View.GONE
        }, 10000)
    }

    private fun exitBy2Click() {
        if (!readyExit) {
            readyExit = true
            ToastUtils.showToast(this@MainActivity, "Press one more time to exit app")
            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { readyExit = false }
        } else {
            ActivityManager.finishAllActivity()
            System.exit(0)
        }
    }
}
