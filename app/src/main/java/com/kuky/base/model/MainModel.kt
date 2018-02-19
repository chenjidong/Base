package com.kuky.base.model

import com.kuky.base.contract.MainContract
import com.kuky.baselib.ResultCallBack

/**
 * @author Kuky
 */
class MainModel : MainContract.IMainModel {
    override fun getPopupAndDialogList(callback: ResultCallBack.OnResultBack<MutableList<String>>) {
        val list: MutableList<String> = mutableListOf()

        (1..20).mapTo(list) { "$it" }

        callback.onResult(list)
    }
}