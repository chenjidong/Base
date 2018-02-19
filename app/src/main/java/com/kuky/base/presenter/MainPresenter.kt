package com.kuky.base.presenter

import com.kuky.base.contract.MainContract
import com.kuky.base.model.MainModel
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpPresenter

/**
 * @author Kuky
 */
class MainPresenter(mainView: MainContract.IMainView) : BaseMvpPresenter<MainContract.IMainView>(), MainContract.IMainPresenter {
    private var mMainView = mainView

    private var mMainModel: MainContract.IMainModel = MainModel()

    override fun setListToPopupAndDialog() {
        mMainModel.getPopupAndDialogList(object : ResultCallBack.OnResultBack<MutableList<String>> {
            override fun onResult(t: MutableList<String>) {
                mMainView.queryPopupAndDialogList(t)
            }
        })
    }
}
