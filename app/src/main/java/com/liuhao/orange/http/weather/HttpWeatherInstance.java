package com.liuhao.orange.http.weather;


import com.liuhao.orange.bean.WeatherBean;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuhao on 2016/10/22.
 */
public class HttpWeatherInstance {
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private static HttpWeatherInstance instance;
    //http://op.juhe.cn/onebox/weather/query?cityname=武汉&key=589aa0be81486d0f53ee8c1e1f054ce3
    private String BasePath = "http://op.juhe.cn/";
    private WeatherService mService;

    public HttpWeatherInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BasePath)
                .build();
        mService = mRetrofit.create(WeatherService.class);
    }

    public static HttpWeatherInstance getInstance() {
        if (instance == null) {
            synchronized (HttpWeatherInstance.class) {
                if (instance == null) {
                    instance = new HttpWeatherInstance();
                }
            }
        }
        return instance;
    }

    public void getWeatherBean(Subscriber<WeatherBean> subscriber, String cityname, String key) {
        mService.getWetherBean(cityname, key).map(new Func1<WeatherInfo<WeatherBean>, WeatherBean>() {

            @Override
            public WeatherBean call(WeatherInfo<WeatherBean> info) {
                if (info.getError_code() != 0) {
                    return null;
                }
                return info.getResult();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        ;
    }
}
