package com.kuky.base.component

import com.kuky.base.module.CeilingModule
import com.kuky.base.view.activity.CeilingActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Singleton
@Component(modules = [(CeilingModule::class)])
interface CeilingComponent {
    fun inject(ceilingActivity: CeilingActivity)
}