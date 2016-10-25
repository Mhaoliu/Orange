package com.liuhao.orange.model.iface;

import com.liuhao.orange.db.bean.WetherSearchBean;

import java.util.List;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface ISearchWeatherModel {
    void onDelete();

    void onInsert(WetherSearchBean bean);

    List<WetherSearchBean> onQuere();


}
