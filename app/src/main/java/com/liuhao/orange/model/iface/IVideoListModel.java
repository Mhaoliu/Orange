package com.liuhao.orange.model.iface;

import java.util.Map;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface IVideoListModel {
    void onDownLoad(Map<String, String> map);

    void unRegister();
}
