package com.liuhao.orange.model;

import android.content.Context;
import android.util.Log;

import com.liuhao.orange.db.bean.WetherSearchBean;
import com.liuhao.orange.db.utils.DbUtil;
import com.liuhao.orange.model.iface.ISearchWeatherModel;

import java.util.List;

/**
 * Created by liuhao on 2016/10/25.
 */
public class SearchWeatherModelImp implements ISearchWeatherModel {


    public SearchWeatherModelImp() {

    }

    @Override
    public void onDelete() {
        DbUtil.getDriverHelper().deleteAll();
    }

    @Override
    public void onInsert(WetherSearchBean bean) {
        DbUtil.getDriverHelper().insert(bean);
    }


    @Override
    public List<WetherSearchBean> onQuere() {

        List<WetherSearchBean> list = DbUtil.getDriverHelper().queryAll();
        return list;
    }
}
