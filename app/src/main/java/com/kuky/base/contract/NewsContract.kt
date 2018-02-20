package com.kuky.base.contract

import com.kuky.base.entity.News
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpViewImpl

/**
 * @author Kuky
 */
class NewsContract {
    interface INewsModel {
        fun getNews(type: String, callBack: ResultCallBack.OnListResultBack<MutableList<News.ResultBean.DataBean>>)
    }

    interface INewsView : BaseMvpViewImpl {
        fun queryNewsSucceed(news: MutableList<News.ResultBean.DataBean>)

        fun queryNewsEmpty()

        fun queryNewsFailed()

        fun getType(): String
    }

    interface INewsPresenter {
        fun setNewsToUi()
    }
}