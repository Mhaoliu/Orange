package com.liuhao.orange.db.utils;

import com.liuhao.orange.db.dao.WetherSearchBeanDao;
import com.liuhao.orange.db.helper.DbCore;

/**
 * Created by liuhao on 2016/10/25.
 */
public class DbUtil {
    private static WeatherSearchHelper sWeatherSearchHelper;


    private static WetherSearchBeanDao getDriverDao() {
        return DbCore.getDaoSession().getWetherSearchBeanDao();
    }

    public static WeatherSearchHelper getDriverHelper() {
        if (sWeatherSearchHelper == null) {
            sWeatherSearchHelper = new WeatherSearchHelper(getDriverDao());
        }
        return sWeatherSearchHelper;
    }
}
