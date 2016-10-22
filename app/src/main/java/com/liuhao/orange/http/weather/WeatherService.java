package com.liuhao.orange.http.weather;

import com.liuhao.orange.bean.WeatherBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liuhao on 2016/10/22.
 */
public interface WeatherService {
    //http://op.juhe.cn/onebox/weather/query?cityname=武汉&key=589aa0be81486d0f53ee8c1e1f054ce3
    @GET("onebox/weather/query?")
    Observable<WeatherInfo<WeatherBean>> getWetherBean(@Query("cityname") String cityname, @Query("key") String key);
}
