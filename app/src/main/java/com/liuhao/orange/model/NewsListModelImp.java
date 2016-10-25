package com.liuhao.orange.model;

import android.content.Context;

import com.liuhao.orange.listener.IDownLoadListener;
import com.liuhao.orange.bean.NewsBean;
import com.liuhao.orange.constant.Constant;
import com.liuhao.orange.http.news.HttpNewsInstance;
import com.liuhao.orange.model.iface.INewsListModel;

import rx.Subscriber;

/**
 * Created by liuhao on 2016/10/22.
 */
public class NewsListModelImp implements INewsListModel {
    private IDownLoadListener<NewsBean> listener;

    private Subscriber<NewsBean> subscriber = new Subscriber<NewsBean>() {
        @Override
        public void onStart() {
            listener.start();
        }

        @Override
        public void onCompleted() {
            listener.complete();
        }

        @Override
        public void onError(Throwable e) {
            listener.unSuccessful(e);
        }

        @Override
        public void onNext(NewsBean newsBean) {
            listener.successful(newsBean);
        }
    };

    public NewsListModelImp(IDownLoadListener<NewsBean> listener) {
        this.listener = listener;
    }

    @Override
    public void downNews(Context context, int index) {
//        HttpNewsInstance.getInstance(context).getNewsBean(new Subscriber<NewsBean>() {
//            @Override
//            public void onStart() {
//                listener.start();
//            }
//
//            @Override
//            public void onCompleted() {
//                listener.complete();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                listener.unSuccessful(e);
//            }
//
//            @Override
//            public void onNext(NewsBean newsBean) {
//                listener.successful(newsBean);
//            }
//        }, Constant.JUHE_NEWS_TOP[index], Constant.JUHE_APP_KEY);

        HttpNewsInstance.getInstance(context).getNewsBean(subscriber, Constant.JUHE_NEWS_TOP[index], Constant.JUHE_APP_KEY);
    }

    @Override
    public void unSubscribe() {
        if (subscriber != null && subscriber.isUnsubscribed())
            subscriber.unsubscribe();
    }
}
