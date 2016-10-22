package com.liuhao.orange.http.news;

import android.content.Context;
import android.util.Log;

import com.liuhao.orange.bean.NewsBean;
import com.liuhao.orange.utils.log.LogUtils;
import com.liuhao.orange.utils.network.NetworkConnection;

import java.io.File;
import java.io.IOException;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuhao on 2016/10/22.
 */
public class HttpNewsInstance {
    private final String BASEPATH = "http://v.juhe.cn/";
    private OkHttpClient mOkClient;
    private Retrofit mRetrofit;
    private static HttpNewsInstance mInstance;
    //超时时间
    private static final int DEFAULT_TIMEOUT = 5;
    private NewsService mNewsService;

    public HttpNewsInstance(final Context mContext) {
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
                .baseUrl(BASEPATH)
                .build();

        mNewsService = mRetrofit.create(NewsService.class);

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static HttpNewsInstance getInstance(Context mContext) {
        if (mInstance == null) {
            synchronized (HttpNewsInstance.class) {
                if (mInstance == null) {
                    mInstance = new HttpNewsInstance(mContext);
                }
            }
        }
        return mInstance;
    }

    public void getNewsBean(Subscriber<NewsBean> subscriber, String type, String key) {
        mNewsService.getNewsBean(type, key).map(new Func1<NewsInfo<NewsBean>, NewsBean>() {

            @Override
            public NewsBean call(NewsInfo<NewsBean> info) {
                if (info.getError_code() != 0) {
                    return null;
                }
                return info.getResult();
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }
}
