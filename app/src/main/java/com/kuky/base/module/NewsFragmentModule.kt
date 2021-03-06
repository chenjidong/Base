package com.kuky.base.module

import com.kuky.base.presenter.NewsPresenter
import com.kuky.base.view.adapter.NewsAdapter
import com.kuky.base.view.fragment.NewsFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Module
class NewsFragmentModule(private var mNewsFragment: NewsFragment, private var mCloseRecycle: Boolean = false) {

    @Singleton
    @Provides
    fun provideNewsPresenter(): NewsPresenter {
        return NewsPresenter(this.mNewsFragment)
    }

    @Provides
    fun provideNewsAdapter(): NewsAdapter {
        return NewsAdapter(this.mNewsFragment.activity!!, this.mCloseRecycle)
    }
}