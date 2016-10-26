package com.liuhao.orange;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.liuhao.orange.activity.CachedActivity;
import com.liuhao.orange.activity.CachingActivity;
import com.liuhao.orange.baidu.LocationService;
import com.liuhao.orange.db.helper.DbCore;
import com.youku.player.YoukuPlayerBaseConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao on 2016/9/28.
 */
public class OrgApplication extends Application {
    private List<Activity> mActivitys = new ArrayList<>();
    //百度定位服务
    public LocationService mLocationService;
    //优酷
    public YoukuPlayerBaseConfiguration configuration;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DbCore.init(this);
        //百度地图初始化
        mLocationService = new LocationService(getApplicationContext());
        //优酷初始化
        configuration = new YoukuPlayerBaseConfiguration(this) {


            /**
             * 通过覆写该方法，返回“正在缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的缓存界面
             */
            @Override
            public Class<? extends Activity> getCachingActivityClass() {
                return CachingActivity.class;
            }

            /**
             * 通过覆写该方法，返回“已经缓存视频信息的界面”，
             * 则在状态栏点击下载信息时可以自动跳转到所设定的界面.
             * 用户需要定义自己的已缓存界面
             */

            @Override
            public Class<? extends Activity> getCachedActivityClass() {
                return CachedActivity.class;
            }

            /**
             * 配置视频的缓存路径，格式举例： /appname/videocache/
             * 如果返回空，则视频默认缓存路径为： /应用程序包名/videocache/
             *
             */
            @Override
            public String configDownloadPath() {

                return null;
            }
        };
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        System.exit(0);
    }

    public void addActivity(Activity activity) {
        mActivitys.add(activity);
    }
}
