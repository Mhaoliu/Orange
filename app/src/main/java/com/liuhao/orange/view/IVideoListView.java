package com.liuhao.orange.view;

import com.liuhao.orange.http.video.VideoInfo;
import com.youku.service.download.DownloadInfo;

import java.util.List;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface IVideoListView extends IView{
    void onDownloadSuccess(VideoInfo info);
}
