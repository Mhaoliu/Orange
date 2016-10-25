package com.liuhao.orange.view;

import com.liuhao.orange.db.bean.WetherSearchBean;

import java.util.List;

/**
 * Created by liuhao on 2016/10/25.
 */
public interface ISearchWetherView {
    void showSearch(List<WetherSearchBean> list);
}
