package com.liuhao.orange.presenter;

import android.content.Context;

import com.liuhao.orange.listener.ILocationListener;
import com.liuhao.orange.model.LocationModelImp;
import com.liuhao.orange.model.iface.ILocationModel;
import com.liuhao.orange.presenter.iface.ILocationPresenter;
import com.liuhao.orange.view.ILocationView;

/**
 * Created by liuhao on 2016/10/25.
 */
public class LocationPresenterImp implements ILocationPresenter, ILocationListener {
    private ILocationModel mLocationModel;
    private ILocationView mLocationView;

    public LocationPresenterImp(Context mContext,ILocationView mLocationView) {
        this.mLocationView = mLocationView;
        mLocationModel = new LocationModelImp(mContext, this);
    }

    @Override
    public void onLocation() {
        mLocationModel.onLocation();
    }

    @Override
    public void unRegister() {
        mLocationModel.unRegister();
    }

    @Override
    public void onSuccess(String cityName, String districtName) {
        mLocationView.onLocationSuccess(cityName,districtName);
    }

    @Override
    public void unSuccessful(int type) {
        mLocationView.unLocationSuccessful(type);
    }


}
