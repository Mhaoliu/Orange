package com.liuhao.orange.model.iface;

import android.content.Context;


/**
 * Created by liuhao on 2016/10/22.
 */
public interface INewsListModel {
    void downNews(Context context, int index);

    void unSubscribe();
}
