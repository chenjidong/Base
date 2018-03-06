package com.kuky.base.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.kuky.base.R
import com.kuky.base.databinding.ActivityNewsBinding
import com.kuky.base.entity.C
import com.kuky.base.view.fragment.NewsFragment
import com.kuky.baselib.baseAdapter.BaseFragmentPagerAdapter
import com.kuky.baselib.baseClass.BaseActivity
import com.kuky.baselib.baseUtils.ScreenUtils

class NewsActivity : BaseActivity<ActivityNewsBinding>() {
    private lateinit var vpAdapter: BaseFragmentPagerAdapter
    private val titles = arrayOf("Tops", "Sports")
    private val fragments: MutableList<Fragment> = mutableListOf()
    private lateinit var linear: LinearLayout

    override fun enableTransparentStatus(): Boolean {
        return false
    }

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_news
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        fragments.add(NewsFragment().setNewsType(C.TOP))
        fragments.add(NewsFragment().setNewsType(C.SPORT))

        vpAdapter = BaseFragmentPagerAdapter(supportFragmentManager, fragments, titles)
        mViewBinding.newsVp.adapter = vpAdapter
        mViewBinding.newsVp.offscreenPageLimit = titles.size
        mViewBinding.newsTab.setupWithViewPager(mViewBinding.newsVp)

        linear = mViewBinding.newsTab.getChildAt(0) as LinearLayout
        linear.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        linear.dividerDrawable = ContextCompat.getDrawable(this@NewsActivity, R.drawable.shape_tab_divider_gray)
        linear.dividerPadding = ScreenUtils.dip2Px(this@NewsActivity, 15f)
    }

    override fun setListener() {

    }
}
