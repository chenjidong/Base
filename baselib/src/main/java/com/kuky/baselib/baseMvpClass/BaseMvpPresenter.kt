package com.kuky.baselib.baseMvpClass

/**
 * @author Kuky
 */
abstract class BaseMvpPresenter<V : BaseMvpViewImpl> {
    protected var mView: V? = null

    fun attachView(view: V) {
        this.mView = view
    }

    fun detachView() {
        this.mView = null
    }

    fun onResume() {

    }
}