package com.liuhao.orange.presenter;


import com.liuhao.orange.base.IDownLoadListener;
import com.liuhao.orange.bean.WeatherBean;
import com.liuhao.orange.model.WeatherModelImp;
import com.liuhao.orange.model.iface.IWeatherModel;
import com.liuhao.orange.presenter.iface.IWatherPresenter;
import com.liuhao.orange.view.IWeatherView;

/**
 * Created by liuhao on 2016/10/23.
 */
public class WeatherPresenterImp implements IWatherPresenter, IDownLoadListener<WeatherBean> {
    private String mCityName;
    private IWeatherModel mWeatherModel;
    private IWeatherView mWeatherView;

    public WeatherPresenterImp(String mCityName, IWeatherView mWeatherView) {
        this.mCityName = mCityName;
        mWeatherModel = new WeatherModelImp(this);
        this.mWeatherView = mWeatherView;
    }

    @Override
    public void loadWeather() {
        mWeatherModel.downWeather(mCityName);
    }

    @Override
    public void onSearch(String cityName) {
        mWeatherModel.downWeather(cityName);
    }

    @Override
    public void unSubscribe() {
        mWeatherModel.unSubscribe();
    }

    @Override
    public void successful(WeatherBean weatherBean) {
        mWeatherView.showWather(weatherBean);
    }

    @Override
    public void unSuccessful(Throwable throwable) {
        mWeatherView.failed(throwable);
    }

    @Override
    public void start() {
        mWeatherView.start();
    }

    @Override
    public void complete() {
        mWeatherView.complete();
    }
}
