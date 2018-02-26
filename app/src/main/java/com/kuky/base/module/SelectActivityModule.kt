package com.kuky.base.module

import com.kuky.base.view.activity.SelectActivity
import com.kuky.base.view.adapter.SelectPicAdapter
import dagger.Module
import dagger.Provides

/**
 * @author Kuky
 */
@Module
class SelectActivityModule(private var mSelectActivity: SelectActivity, private var mCloseRecycle: Boolean = false) {

    @Provides
    fun provideAdapter(): SelectPicAdapter {
        return SelectPicAdapter(this.mSelectActivity, this.mCloseRecycle)
    }
}