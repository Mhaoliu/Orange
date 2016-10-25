package com.liuhao.orange.presenter;

import android.content.Context;

import com.liuhao.orange.db.bean.WetherSearchBean;
import com.liuhao.orange.model.SearchWeatherModelImp;
import com.liuhao.orange.model.iface.ISearchWeatherModel;
import com.liuhao.orange.presenter.iface.ISearchWeatherPresenter;
import com.liuhao.orange.view.ISearchWetherView;

import java.util.List;

/**
 * Created by liuhao on 2016/10/25.
 */
public class SearchWeatherPresenterImp implements ISearchWeatherPresenter {
    private ISearchWeatherModel mModel;
    private ISearchWetherView mView;

    public SearchWeatherPresenterImp(ISearchWetherView mView, Context mContext) {
        this.mView = mView;
        mModel = new SearchWeatherModelImp();
    }

    @Override
    public void onDelete() {
        mModel.onDelete();
        mView.showSearch(null);
    }

    @Override
    public void onInsert(WetherSearchBean bean) {
        mModel.onInsert(bean);
    }

    @Override
    public void onQuere() {
        List<WetherSearchBean> list = mModel.onQuere();
        mView.showSearch(list);
    }
}
