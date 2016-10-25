package com.liuhao.orange.db.utils;

import com.liuhao.orange.db.bean.WetherSearchBean;
import com.liuhao.orange.db.helper.BaseDbHelper;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by liuhao on 2016/10/25.
 */
public class WeatherSearchHelper extends BaseDbHelper<WetherSearchBean,Long>{
    public WeatherSearchHelper(AbstractDao dao) {
        super(dao);
    }

}
