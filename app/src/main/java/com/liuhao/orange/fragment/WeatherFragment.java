package com.liuhao.orange.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseFragment;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends BaseFragment {

    @BindView(R.id.weather_toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        getBaseActivity().setSupportActionBar(mToolbar);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

}
