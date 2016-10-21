package com.liuhao.orange.presenter;


import com.liuhao.orange.base.IDownLoadListener;
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

    public NewsListPresenterImp(INewsListView mNewsView) {
        this.mNewsView = mNewsView;
        this.mNewsModel = new NewsListModelImp();
    }

    @Override
    public void loadNews() {
        mNewsModel.downNews(this);
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
