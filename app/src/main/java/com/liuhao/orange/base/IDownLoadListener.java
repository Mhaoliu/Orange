package com.liuhao.orange.base;

/**
 * Created by liuhao on 2016/10/22.
 */
public interface IDownLoadListener<T> {
    /**
     * 数据加载成功回调
     *
     * @param t
     */
    void successful(T t);

    /**
     * 数据加载失败回调
     *
     * @param throwable
     */
    void unSuccessful(Throwable throwable);

    /**
     * 数据开始下载
     */
    void start();

    /**
     * 数据下载完成
     */
    void complete();
}
