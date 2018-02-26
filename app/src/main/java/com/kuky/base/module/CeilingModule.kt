package com.kuky.base.module

import com.kuky.base.presenter.CeilingPresenter
import com.kuky.base.view.activity.CeilingActivity
import com.kuky.base.view.activity.CeilingAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Module
class CeilingModule(activity: CeilingActivity, closeRecycle: Boolean = false) {

    val mCeilingActivity = activity
    val mCloseRecycle = closeRecycle

    @Provides
    fun provideCeilingAdapter(): CeilingAdapter {
        return CeilingAdapter(this.mCeilingActivity, this.mCloseRecycle)
    }

    @Singleton
    @Provides
    fun provideCeilingPresenter(): CeilingPresenter {
        return CeilingPresenter(this.mCeilingActivity)
    }
}