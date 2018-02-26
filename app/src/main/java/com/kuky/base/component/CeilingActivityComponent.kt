package com.kuky.base.component

import com.kuky.base.module.CeilingActivityModule
import com.kuky.base.view.activity.CeilingActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Singleton
@Component(modules = [(CeilingActivityModule::class)])
interface CeilingActivityComponent {
    fun inject(ceilingActivity: CeilingActivity)
}