package com.liuhao.orange.http.video;


import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by liuhao on 2016/10/26.
 */
public interface VideoService {

    //https://api.youku.com/quality/video/by/keyword.json?client_id=9304521996778fe7&lengthtype=3&published=month&count=10
    @Headers("Cache-Control: public, max-age=3600")
    @GET("quality/video/by/keyword.json?")
    Observable<VideoInfo> getVideoBean(@QueryMap() Map<String, String> map);
}
