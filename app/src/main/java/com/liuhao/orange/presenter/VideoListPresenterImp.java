package com.liuhao.orange.presenter;

import android.content.Context;

import com.liuhao.orange.http.video.VideoInfo;
import com.liuhao.orange.listener.IDownLoadListener;
import com.liuhao.orange.model.VideoListModelImp;
import com.liuhao.orange.model.iface.IVideoListModel;
import com.liuhao.orange.presenter.iface.IVideoListPresenter;
import com.liuhao.orange.view.IVideoListView;
import com.youku.service.download.DownloadInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by liuhao on 2016/10/26.
 */
public class VideoListPresenterImp implements IVideoListPresenter, IDownLoadListener<VideoInfo> {
    private IVideoListModel mVideoListModel;
    private IVideoListView mVideoView;

    public VideoListPresenterImp(Context context, IVideoListView mVideoView) {
        mVideoListModel = new VideoListModelImp(context,this);
        this.mVideoView = mVideoView;
    }

    @Override
    public void onDownLoad(Map<String,String> map) {
        mVideoListModel.onDownLoad(map);
    }

    @Override
    public void unRegister() {
        mVideoListModel.unRegister();
    }


    @Override
    public void successful(VideoInfo info) {
        mVideoView.onDownloadSuccess(info);
    }

    @Override
    public void unSuccessful(Throwable throwable) {
        mVideoView.failed(throwable);
    }

    @Override
    public void start() {
        mVideoView.start();
    }

    @Override
    public void complete() {
        mVideoView.complete();
    }
}
