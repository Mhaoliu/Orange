package com.liuhao.orange.activity;


import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.fragment.MeFragment;
import com.liuhao.orange.fragment.NewsFragment;
import com.liuhao.orange.fragment.VideoFragment;
import com.liuhao.orange.fragment.WeatherFragment;
import com.liuhao.orange.presenter.LocationPresenterImp;
import com.liuhao.orange.presenter.iface.ILocationPresenter;
import com.liuhao.orange.view.ILocationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ILocationView {

    @BindView(R.id.main_framlayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.main_radio_group)
    RadioGroup mRadioGroup;
    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private FragmentManager mFragmentManager;
    private ILocationPresenter mLocationPresenter;

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mFragmentList.add(new NewsFragment());
        mFragmentList.add(new VideoFragment());
        mFragmentList.add(new WeatherFragment());
        mFragmentList.add(new MeFragment());
        mLocationPresenter = new LocationPresenterImp(this, this);
    }

    @Override
    protected void initEvent() {

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int indexOfChild = group.indexOfChild(group.findViewById(checkedId));
                replaceFragment(mFragmentList.get(indexOfChild));
            }
        });
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    /**
     * 获取Fragment管理器
     *
     * @return
     */
    public FragmentManager getBaseFragmentManager() {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        return mFragmentManager;
    }

    /**
     * 替换一个Fragment
     *
     * @param fragment
     */
    public void replaceFragment(BaseFragment fragment) {
        getBaseFragmentManager().beginTransaction().replace(R.id.main_framlayout, fragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //定位
        mLocationPresenter.onLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解注册
        mLocationPresenter.unRegister();
    }

    @Override
    public void onLocationSuccess(String cityName, String districtName) {
       if (cityName != null) {
            ((NewsFragment) mFragmentList.get(0)).onLocationSuccess(cityName, districtName);
            ((WeatherFragment) mFragmentList.get(2)).onLocationSuccess(cityName, districtName);
        }
    }

    @Override
    public void unLocationSuccessful(int type) {

    }

}
