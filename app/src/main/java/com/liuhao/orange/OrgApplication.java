package com.liuhao.orange;

import android.app.Activity;
import android.app.Application;

import com.liuhao.orange.db.helper.DbCore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao on 2016/9/28.
 */
public class OrgApplication extends Application {
    private List<Activity> mActivitys = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DbCore.init(this);
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
