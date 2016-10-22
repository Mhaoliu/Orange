package com.liuhao.orange.http.news;

import com.liuhao.orange.bean.NewsBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liuhao on 2016/10/22.
 */
public interface NewsService {
    //http://v.juhe.cn/toutiao/index?type=top&key=APPKEY
    @Headers("Cache-Control: public, max-age=3600")
    @GET("toutiao/index?")
    Observable<NewsInfo<NewsBean>> getNewsBean(@Query("type") String type, @Query("key") String key);
}
