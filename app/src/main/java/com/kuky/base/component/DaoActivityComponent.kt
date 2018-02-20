package com.kuky.base.component

import com.kuky.base.module.DaoActivityModule
import com.kuky.base.view.activity.DaoActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Singleton
@Component(modules = [(DaoActivityModule::class)])
interface DaoActivityComponent {
    fun inject(daoActivity: DaoActivity)
}