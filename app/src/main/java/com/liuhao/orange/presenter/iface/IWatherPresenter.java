package com.liuhao.orange.presenter.iface;

/**
 * Created by liuhao on 2016/10/23.
 */
public interface IWatherPresenter {
    void loadWeather();

    void onSearch(String cityName);

    void unSubscribe();
}
