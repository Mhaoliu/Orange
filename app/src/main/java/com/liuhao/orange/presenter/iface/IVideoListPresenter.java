package com.liuhao.orange.presenter.iface;

import java.util.Map;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface IVideoListPresenter {
    void onDownLoad(Map<String, String> map);

    void unRegister();
}
