@file:Suppress("DEPRECATION")

package com.kuky.base.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kuky.base.R
import com.kuky.base.databinding.ActivityNewsDetailBinding
import com.kuky.base.entity.C
import com.kuky.baselib.baseClass.BaseActivity

class NewsDetailActivity : BaseActivity<ActivityNewsDetailBinding>() {

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_news_detail
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initActivity(savedInstanceState: Bundle?) {
        val intent = intent
        val url = intent.getStringExtra(C.URL)

        if (TextUtils.isEmpty(url)) {
            finish()
            return
        }

        mViewBinding.newsDetailWeb.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }

            @TargetApi(21)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                shouldOverrideUrlLoading(view, request!!.url.toString())
                return true
            }
        }

        val setting = mViewBinding.newsDetailWeb.settings
        setting.javaScriptEnabled = true
        setting.allowContentAccess = true
        setting.databaseEnabled = true
        setting.domStorageEnabled = true
        setting.setAppCacheEnabled(true)
        setting.savePassword = false
        setting.saveFormData = false
        setting.useWideViewPort = true
        setting.loadWithOverviewMode = true

        mViewBinding.newsDetailWeb.loadUrl(url)
    }

    override fun setListener() {

    }

    companion object {
        fun startDetail(context: Context, url: String) {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(C.URL, url)
            context.startActivity(intent)
        }
    }
}
