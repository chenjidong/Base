package com.kuky.base.component

import com.kuky.base.module.SelectActivityModule
import com.kuky.base.view.activity.SelectActivity
import dagger.Component

/**
 * @author Kuky
 */
@Component(modules = [(SelectActivityModule::class)])
interface SelectActivityComponent {
    fun inject(selectActivity: SelectActivity)
}