package com.liuhao.orange.model;

import com.liuhao.orange.listener.IDownLoadListener;
import com.liuhao.orange.bean.WeatherBean;
import com.liuhao.orange.constant.Constant;
import com.liuhao.orange.http.weather.HttpWeatherInstance;
import com.liuhao.orange.model.iface.IWeatherModel;

import rx.Subscriber;

/**
 * Created by liuhao on 2016/10/23.
 */
public class WeatherModelImp implements IWeatherModel {
    private IDownLoadListener<WeatherBean> listener;

    public WeatherModelImp(IDownLoadListener<WeatherBean> listener) {
        this.listener = listener;
    }

    private Subscriber<WeatherBean> mSubscriber;

    @Override
    public void downWeather(String cityName) {
        mSubscriber = new Subscriber<WeatherBean>() {
            @Override
            public void onStart() {
                listener.start();
            }

            @Override
            public void onCompleted() {
                listener.complete();
            }

            @Override
            public void onError(Throwable e) {
                listener.unSuccessful(e);
            }

            @Override
            public void onNext(WeatherBean weatherBean) {
                listener.successful(weatherBean);
            }
        };
        HttpWeatherInstance.getInstance().getWeatherBean(mSubscriber, cityName, Constant.JUHE_APP_KEY_WEATHER);
    }

    @Override
    public void unSubscribe() {
        if (mSubscriber != null && mSubscriber.isUnsubscribed()) {
            mSubscriber.unsubscribe();
        }
    }
}
