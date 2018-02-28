package com.kuky.base.module

import com.kuky.base.presenter.CeilingPresenter
import com.kuky.base.view.activity.CeilingActivity
import com.kuky.base.view.adapter.CeilingAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Module
class CeilingActivityModule(private var mCeilingActivity: CeilingActivity, private var mCloseRecycle: Boolean = false) {

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