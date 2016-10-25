package com.liuhao.orange.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.liuhao.orange.OrgApplication;
import com.liuhao.orange.baidu.LocationService;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.listener.ILocationListener;
import com.liuhao.orange.model.iface.ILocationModel;

/**
 * Created by liuhao on 2016/10/25.
 */
public class LocationModelImp implements ILocationModel {
    private LocationService mService;
    private ILocationListener mLocationListener;
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                String cityName = bdLocation.getCity();
                String districtName = bdLocation.getDistrict();

                Log.i("baidu", cityName + " -" + districtName);
                Log.i("baidu", bdLocation.getLocType() + "-");
                if (cityName != null && districtName != null) {
                    unRegister();
                }
                mLocationListener.onSuccess(cityName, districtName);
            } else {
                mLocationListener.unSuccessful(bdLocation.getLocType());
            }
        }
    };

    public LocationModelImp(Context mContext, ILocationListener mLocationListener) {
        this.mLocationListener = mLocationListener;
        OrgApplication app = ((BaseActivity) mContext).getApp();
        mService = app.mLocationService;
    }

    @Override
    public void onLocation() {
        mService.registerListener(mListener);
        mService.setLocationOption(mService.getDefaultLocationClientOption());
        mService.start();
    }

    @Override
    public void unRegister() {
        mService.unregisterListener(mListener);
        mService.stop();
    }
}
