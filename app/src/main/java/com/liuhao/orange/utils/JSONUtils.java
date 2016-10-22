package com.liuhao.orange.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by liuhao on 2016/8/14.
 */
public class JSONUtils {
    private Gson mGson;

    private JSONUtils() {
        if (mGson == null)
            mGson = new Gson();
    }

    public static JSONUtils getInstance() {
        return new JSONUtils();
    }

    /**
     *
     * @param parser
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T gsonParser(String parser, Class<T> clazz) {
        Type type = new TypeToken<T>() {
        }.getType();
        return mGson.fromJson(parser, type);
    }

}
