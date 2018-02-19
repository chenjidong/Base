package com.kuky.baselib

/**
 * @author Kuky
 */
class ResultCallBack {

    interface OnHttpResultBack<in T> {
        fun onSucceed(t: T)

        fun onFailed(msg: String)
    }

    interface OnListResultBack<in T> {
        fun onSucceed(t: T)

        fun onEmpty()

        fun onFailed()
    }

    interface OnResultBack<in T> {
        fun onResult(t: T)
    }
}