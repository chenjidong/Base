package com.kuky.base.component

import com.kuky.base.module.MainActivityModule
import com.kuky.base.view.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Singleton
@Component(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}
