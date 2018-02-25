package com.kuky.base.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.*
import com.kuky.base.R
import com.kuky.base.databinding.ActivityJsContractBinding
import com.kuky.baselib.baseClass.BaseActivity
import com.kuky.baselib.baseUtils.ToastUtils

class JsContractActivity : BaseActivity<ActivityJsContractBinding>() {
    private lateinit var dialog: AlertDialog

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_js_contract
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface")
    override fun initActivity(savedInstanceState: Bundle?) {
        mViewBinding.js = this@JsContractActivity
        mViewBinding.contentWeb.settings.javaScriptEnabled = true
        mViewBinding.contentWeb.settings.domStorageEnabled = true
        mViewBinding.contentWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url!!)
                return true
            }

            @TargetApi(21)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                shouldOverrideUrlLoading(view, request!!.url.toString())
                return true
            }
        }

        mViewBinding.contentWeb.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                mViewBinding.pro.visibility = if (newProgress == 0 || newProgress == 100) View.GONE else View.VISIBLE
                mViewBinding.pro.progress = newProgress
            }
        }
        mViewBinding.contentWeb.loadUrl("file:///android_asset/web.html")
        /**
         * object: Any -> 自定义类对象 , 映射成 JS 对象
         * name: String -> 别名 , JS 通过别名来调用方法 , 例如 JS 中的 onclick="window.android.startFunction()"
         */
        mViewBinding.contentWeb.addJavascriptInterface(JsInterface(this@JsContractActivity), "android")
    }

    override fun setListener() {

    }

    fun ktCallJs(view: View) {
        mViewBinding.contentWeb.loadUrl("javascript:ktcalljs()")
    }

    fun ktCallJsWithParam(view: View) {
        val arg = "https://github.com/kukyxs"
        mViewBinding.contentWeb.loadUrl("javascript:ktcalljswith($arg)")
    }

    override fun onBackPressed() {
        if (mViewBinding.contentWeb.canGoBack())
            mViewBinding.contentWeb.goBack()
        else
            super.onBackPressed()
    }

    inner class JsInterface(context: Context) {
        var mContext = context

        /**
         * 在 JS 中调用的方法
         */
        @JavascriptInterface
        fun startFunction() {
            runOnUiThread {
                ToastUtils.showToast(mContext, "start function without param")
            }
        }

        @JavascriptInterface
        fun startFunction(text: String) {
            runOnUiThread {
                dialog = AlertDialog.Builder(mContext)
                        .setMessage("open $text ?")
                        .setPositiveButton("open") { _, _ ->
                            mViewBinding.contentWeb.loadUrl(text)
                        }
                        .setNegativeButton("cancel") { _, _ -> dialog.dismiss() }
                        .create()
                dialog.show()
            }
        }
    }
}