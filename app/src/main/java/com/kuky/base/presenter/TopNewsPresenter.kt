package com.kuky.base.presenter

import com.kuky.base.contract.TopNewsContract
import com.kuky.base.entity.News
import com.kuky.base.model.TopNewsModel
import com.kuky.baselib.ResultCallBack
import com.kuky.baselib.baseMvpClass.BaseMvpPresenter

/**
 * @author Kuky
 */
class TopNewsPresenter(topView: TopNewsContract.ITopNewsView) : BaseMvpPresenter<TopNewsContract.ITopNewsView>(),
        TopNewsContract.ITopNewsPresenter {

    private var mTopView: TopNewsContract.ITopNewsView = topView
    private var mTopModel: TopNewsContract.ITopNewsModel = TopNewsModel()

    override fun setNewsToUi() {
        mTopModel.getTopNews(object : ResultCallBack.OnListResultBack<MutableList<News.ResultBean.DataBean>>{
            override fun onSucceed(t: MutableList<News.ResultBean.DataBean>) {
                mTopView.queryNewsSucceed(t)
            }

            override fun onEmpty() {
                mTopView.queryNewsEmpty()
            }

            override fun onFailed() {
                mTopView.queryNewsFailed()
            }
        })
    }
}
