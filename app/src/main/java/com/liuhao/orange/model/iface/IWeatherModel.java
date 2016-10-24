package com.liuhao.orange.model.iface;


/**
 * Created by liuhao on 2016/10/22.
 */
public interface IWeatherModel {
    void downWeather(String cityName);

    void unSubscribe();
}
