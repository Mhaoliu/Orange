package com.liuhao.orange.utils.log;

import android.util.Log;

/**
 * Created by liuhao on 2016/10/22.
 */
public class LogUtils {
    private static boolean isLog = true;

    public static void d(String type, String msg) {
        if (isLog)
            Log.d(type, msg);
    }

    public static void i(String type, String msg) {
        if (isLog)
            Log.i(type, msg);
    }

    public static void e(String type, String msg) {
        if (isLog)
            Log.e(type, msg);
    }
}
