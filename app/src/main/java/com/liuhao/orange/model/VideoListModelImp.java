package com.liuhao.orange.model;

import android.content.Context;

import com.liuhao.orange.http.video.HttpVideoInstance;
import com.liuhao.orange.http.video.VideoInfo;
import com.liuhao.orange.listener.IDownLoadListener;
import com.liuhao.orange.model.iface.IVideoListModel;
import com.youku.service.download.DownloadInfo;
import com.youku.service.download.DownloadManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by liuhao on 2016/10/26.
 */
public class VideoListModelImp implements IVideoListModel {
    private IDownLoadListener<VideoInfo> mIDownLoadListener;
    private Subscriber<VideoInfo> mSubscriber;
    private Context mContext;

    public VideoListModelImp(Context mContext, IDownLoadListener<VideoInfo> mIDownLoadListener) {
        this.mIDownLoadListener = mIDownLoadListener;
        this.mContext = mContext;
    }


    @Override
    public void onDownLoad(Map<String, String> map) {
        mSubscriber = new Subscriber<VideoInfo>() {
            @Override
            public void onStart() {
                mIDownLoadListener.start();
            }

            @Override
            public void onCompleted() {
                mIDownLoadListener.complete();
            }

            @Override
            public void onError(Throwable e) {
                mIDownLoadListener.unSuccessful(e);
            }

            @Override
            public void onNext(VideoInfo info) {
                mIDownLoadListener.successful(info);
            }
        };
        HttpVideoInstance.getInstance(mContext).getVideoBean(mSubscriber, map);
    }

    @Override
    public void unRegister() {
        if (mSubscriber != null && !mSubscriber.isUnsubscribed()) {
            mSubscriber.unsubscribe();
        }
    }
}
