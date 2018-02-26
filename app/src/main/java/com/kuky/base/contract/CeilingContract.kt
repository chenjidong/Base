package com.kuky.base.contract

import com.kuky.base.entity.CeilingData
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpViewImpl

/**
 * @author Kuky
 */
class CeilingContract {
    interface ICeilingModel {
        fun getCeilings(callBack: ResultCallBack.OnListResultBack<MutableList<CeilingData>>)

        fun getMoreCeilings(size: Int, callBack: ResultCallBack.OnHttpResultBack<MutableList<CeilingData>>)
    }

    interface ICeilingView : BaseMvpViewImpl {
        fun queryCeilingsSucceed(ceilings: MutableList<CeilingData>)

        fun queryCeilingEmpty()

        fun queryCeilingFailed()

        fun queryMoreCeiling(ceilings: MutableList<CeilingData>)

        fun noMoreCeiling(msg: String)

        fun dataSize(): Int
    }

    interface ICeilingPresenter {
        fun setCeilingsToUi()

        fun setMoreCeilingToUi()
    }
}