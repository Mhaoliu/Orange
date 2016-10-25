package com.liuhao.orange.presenter;


import android.content.Context;

import com.liuhao.orange.listener.IDownLoadListener;
import com.liuhao.orange.bean.NewsBean;
import com.liuhao.orange.model.NewsListModelImp;
import com.liuhao.orange.model.iface.INewsListModel;
import com.liuhao.orange.presenter.iface.INewsListPresenter;
import com.liuhao.orange.view.INewsListView;


/**
 * Created by liuhao on 2016/10/22.
 */
public class NewsListPresenterImp implements INewsListPresenter, IDownLoadListener<NewsBean> {
    private INewsListView mNewsView;
    private INewsListModel mNewsModel;
    private Context mContext;
    private int mIndex;

    public NewsListPresenterImp(INewsListView mNewsView, Context mContext, int mIndex) {
        this.mNewsView = mNewsView;
        this.mContext = mContext;
        this.mIndex = mIndex;
        this.mNewsModel = new NewsListModelImp(this);
    }

    @Override
    public void loadNews() {
        mNewsModel.downNews(mContext, mIndex);
    }

    @Override
    public void unSubscribe() {
        mNewsModel.unSubscribe();
    }


    @Override
    public void successful(NewsBean newsBean) {
        mNewsView.showNewsList(newsBean);
    }

    @Override
    public void unSuccessful(Throwable throwable) {
        mNewsView.failed(throwable);
    }

    @Override
    public void start() {
        mNewsView.start();
    }

    @Override
    public void complete() {
        mNewsView.complete();
    }
}
