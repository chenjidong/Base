package com.kuky.base.contract

import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpViewImpl

/**
 * @author Kuky
 */
class MainContract {
    interface IMainModel {
        fun getPopupAndDialogList(callback: ResultCallBack.OnResultBack<MutableList<String>>)
    }

    interface IMainView : BaseMvpViewImpl {
        fun queryPopupAndDialogList(data: MutableList<String>?)
    }

    interface IMainPresenter {
        fun setListToPopupAndDialog()
    }
}