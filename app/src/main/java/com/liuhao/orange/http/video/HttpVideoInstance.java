package com.liuhao.orange.http.video;


import android.content.Context;

import com.liuhao.orange.bean.WeatherBean;
import com.liuhao.orange.http.weather.WeatherInfo;
import com.liuhao.orange.http.weather.WeatherService;
import com.liuhao.orange.utils.log.LogUtils;
import com.liuhao.orange.utils.network.NetworkConnection;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuhao on 2016/10/22.
 */
public class HttpVideoInstance {
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private static HttpVideoInstance instance;
    private String BasePath = "https://api.youku.com/";
    private VideoService mService;
    //超时时间
    private static final int DEFAULT_TIMEOUT = 5;

    public HttpVideoInstance(final Context mContext) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存路径
        File httpCacheDirectory = new File(mContext.getCacheDir(), "OrangeCache");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        builder.cache(cache);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                boolean isNetConnection = NetworkConnection.isNetworkConnected(mContext);
                if (!isNetConnection) {
                    LogUtils.d("network", "网络没有连接");
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (isNetConnection) {
                    LogUtils.d("network", "网络连接");
                    String cacheControl = request.cacheControl().toString();
                    return response.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    LogUtils.d("network", "网络没有连接");
                    return response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                            .removeHeader("Pragma")
                            .build();
                }
            }
        };
        builder.addInterceptor(cacheInterceptor);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BasePath)
                .build();

        mService = mRetrofit.create(VideoService.class);
    }

    public static HttpVideoInstance getInstance(Context context) {
        if (instance == null) {
            synchronized (HttpVideoInstance.class) {
                if (instance == null) {
                    instance = new HttpVideoInstance(context);
                }
            }
        }
        return instance;
    }

    public void getVideoBean(Subscriber<VideoInfo> subscriber, Map<String, String> map) {
        mService.getVideoBean(map).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        ;
    }
}
