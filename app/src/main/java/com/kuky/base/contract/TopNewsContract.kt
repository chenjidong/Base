package com.kuky.base.contract

import com.kuky.base.entity.News
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpViewImpl

/**
 * @author Kuky
 */
class TopNewsContract {
    interface ITopNewsModel {
        fun getTopNews(callBack: ResultCallBack.OnListResultBack<MutableList<News.ResultBean.DataBean>>)
    }

    interface ITopNewsView : BaseMvpViewImpl {
        fun queryNewsSucceed(news: MutableList<News.ResultBean.DataBean>)

        fun queryNewsEmpty()

        fun queryNewsFailed()
    }

    interface ITopNewsPresenter {
        fun setNewsToUi()
    }
}