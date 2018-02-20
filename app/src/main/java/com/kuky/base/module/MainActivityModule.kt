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
class MainActivityModule(mainActivity: MainActivity, animStyle: Int = R.style.animationForBottomAndBottom,
                         popupWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
                         popupHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT) {

    var mMainActivity = mainActivity
    var mAnimStyle = animStyle
    var mPopupWidth = popupWidth
    var mPopupHeight = popupHeight

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