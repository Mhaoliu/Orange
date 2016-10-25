package com.liuhao.orange.view;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface ILocationView {
    void onLocationSuccess(String cityName, String districtName);

    void unLocationSuccessful(int type);
}
