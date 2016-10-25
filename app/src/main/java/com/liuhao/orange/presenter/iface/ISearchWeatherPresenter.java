package com.liuhao.orange.presenter.iface;

import com.liuhao.orange.db.bean.WetherSearchBean;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface ISearchWeatherPresenter {

    void onDelete();

    void onInsert(WetherSearchBean bean);

    void onQuere();
}
