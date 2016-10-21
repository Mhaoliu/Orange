package com.liuhao.orange.model.iface;

import com.liuhao.orange.base.IDownLoadListener;
import com.liuhao.orange.bean.NewsBean;

/**
 * Created by liuhao on 2016/10/22.
 */
public interface INewsListModel {
    void downNews(IDownLoadListener<NewsBean> listener);
}
