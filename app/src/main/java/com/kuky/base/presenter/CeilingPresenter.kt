package com.kuky.base.presenter

import com.kuky.base.contract.CeilingContract
import com.kuky.base.entity.CeilingData
import com.kuky.base.model.CeilingModel
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpPresenter

/**
 * @author Kuky
 */
class CeilingPresenter(ceilingView: CeilingContract.ICeilingView) : BaseMvpPresenter<CeilingContract.ICeilingView>(), CeilingContract.ICeilingPresenter {

    private val mCeilingView = ceilingView
    private val mCeilingModel = CeilingModel()

    override fun setCeilingsToUi() {
        mCeilingModel.getCeilings(object : ResultCallBack.OnListResultBack<MutableList<CeilingData>> {
            override fun onSucceed(t: MutableList<CeilingData>) {
                mCeilingView.queryCeilingsSucceed(t)
            }

            override fun onEmpty() {
                mCeilingView.queryCeilingEmpty()
            }

            override fun onFailed() {
                mCeilingView.queryCeilingFailed()
            }
        })
    }

    override fun setMoreCeilingToUi() {
        mCeilingModel.getMoreCeilings(mCeilingView.dataSize(), object : ResultCallBack.OnHttpResultBack<MutableList<CeilingData>> {
            override fun onSucceed(t: MutableList<CeilingData>) {
                mCeilingView.queryMoreCeiling(t)
            }

            override fun onFailed(msg: String) {
                mCeilingView.noMoreCeiling(msg)
            }
        })
    }
}