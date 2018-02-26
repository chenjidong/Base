package com.kuky.base.module

import android.os.Handler
import android.view.ViewGroup
import com.kuky.base.R
import com.kuky.base.presenter.MainPresenter
import com.kuky.base.view.activity.MainActivity
import com.kuky.base.view.dialog.ListDialog
import com.kuky.base.view.popup.ListPopup
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Module
class MainActivityModule(private var mMainActivity: MainActivity, private var mAnimStyle: Int = R.style.animationForBottomAndBottom,
                         private var mPopupWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
                         private var mPopupHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT) {

    @Singleton
    @Provides
    fun provideMainPresenter(): MainPresenter {
        return MainPresenter(this.mMainActivity)
    }

    @Provides
    fun provideListPopup(): ListPopup {
        return ListPopup(this.mMainActivity, mAnimStyle, mPopupWidth, mPopupHeight)
    }

    @Provides
    fun provideListDialog(): ListDialog {
        return ListDialog(this.mMainActivity)
    }

    @Provides
    fun provideHandler(): Handler {
        return Handler()
    }
}