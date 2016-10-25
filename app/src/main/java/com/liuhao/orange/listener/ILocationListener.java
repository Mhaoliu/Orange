package com.liuhao.orange.listener;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface ILocationListener {
    void onSuccess(String cityName, String districtName);

    void unSuccessful(int type);
}
