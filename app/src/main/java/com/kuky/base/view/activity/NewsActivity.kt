package com.kuky.base.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.kuky.base.R
import com.kuky.base.databinding.ActivityNewsBinding
import com.kuky.base.entity.C
import com.kuky.base.view.fragment.NewsFragment
import com.kuky.baselib.baseAdapter.BaseFragmentPagerAdapter
import com.kuky.baselib.baseClass.BaseActivity

class NewsActivity : BaseActivity<ActivityNewsBinding>() {
    private lateinit var vpAdapter: BaseFragmentPagerAdapter
    private val titles = arrayOf("Tops", "Sports")
    private val fragments: MutableList<Fragment> = mutableListOf()

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
    }

    override fun setListener() {

    }
}
