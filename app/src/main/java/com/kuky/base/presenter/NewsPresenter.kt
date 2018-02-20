package com.kuky.base.presenter

import com.kuky.base.contract.NewsContract
import com.kuky.base.entity.News
import com.kuky.base.model.NewsModel
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpPresenter

/**
 * @author Kuky
 */
class NewsPresenter(topView: NewsContract.INewsView) : BaseMvpPresenter<NewsContract.INewsView>(),
        NewsContract.INewsPresenter {

    private var mNewsView: NewsContract.INewsView = topView
    private var mNewsModel: NewsContract.INewsModel = NewsModel()

    override fun setNewsToUi() {
        mNewsModel.getNews(mNewsView.getType(),
                object : ResultCallBack.OnListResultBack<MutableList<News.ResultBean.DataBean>> {
                    override fun onSucceed(t: MutableList<News.ResultBean.DataBean>) {
                        mNewsView.queryNewsSucceed(t)
                    }

                    override fun onEmpty() {
                        mNewsView.queryNewsEmpty()
                    }

                    override fun onFailed() {
                        mNewsView.queryNewsFailed()
                    }
                })
    }
}
