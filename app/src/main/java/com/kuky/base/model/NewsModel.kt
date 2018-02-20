package com.kuky.base.model

import com.kuky.base.contract.NewsContract
import com.kuky.base.entity.C
import com.kuky.base.entity.News
import com.kuky.base.http.RetrofitApi
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.RetrofitManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Kuky
 */
class NewsModel : NewsContract.INewsModel {
    override fun getNews(type: String, callBack: ResultCallBack.OnListResultBack<MutableList<News.ResultBean.DataBean>>) {
        RetrofitManager
                .provideClient(C.BASE, null)
                .create(RetrofitApi::class.java)
                .getNews(type, C.KEY)
                .flatMap { t -> Observable.fromArray(t.result!!) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { t ->
                            if (t.data!!.isEmpty()) {
                                callBack.onEmpty()
                            } else {
                                callBack.onSucceed(t.data!! as MutableList<News.ResultBean.DataBean>)
                            }
                        },
                        { callBack.onFailed() })
    }
}
