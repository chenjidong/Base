package com.kuky.base.component

import com.kuky.base.module.NewsFragmentModule
import com.kuky.base.view.fragment.NewsFragment
import dagger.Component
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Singleton
@Component(modules = [(NewsFragmentModule::class)])
interface NewsFragmentComponent {
    fun inject(newsFragment: NewsFragment)
}