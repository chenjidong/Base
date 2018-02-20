package com.kuky.base.component

import com.kuky.base.module.DaoActivityModule
import com.kuky.base.view.activity.DaoActivity
import dagger.Component

/**
 * @author Kuky
 */
@Component(modules = [(DaoActivityModule::class)])
interface DaoActivityComponent {
    fun inject(daoActivity: DaoActivity)
}