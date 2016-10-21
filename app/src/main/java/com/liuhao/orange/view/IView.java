package com.liuhao.orange.view;

/**
 * Created by liuhao on 2016/10/22.
 */
public interface IView {
    /**
     * 数据下载失败
     */
    void failed(Throwable throwable);

    /**
     * 数据开始下载
     */
    void start();

    /**
     * 数据下载完成
     */
    void complete();
}
