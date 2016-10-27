package com.liuhao.orange.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuhao.orange.R;
import com.liuhao.orange.activity.SearchWeatherActivity;
import com.liuhao.orange.adapter.WeatherInfoAdapter;
import com.liuhao.orange.adapter.WeatherLifeApater;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.bean.WeatherBean;
import com.liuhao.orange.bean.WeatherLifeBean;
import com.liuhao.orange.constant.Constant;
import com.liuhao.orange.presenter.WeatherPresenterImp;
import com.liuhao.orange.presenter.iface.IWatherPresenter;
import com.liuhao.orange.utils.log.LogUtils;
import com.liuhao.orange.view.ILocationView;
import com.liuhao.orange.view.IWeatherView;
import com.liuhao.orange.width.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends BaseFragment implements IWeatherView, ILocationView {
    @BindView(R.id.tv_cityname)
    TextView mTvCityName;
    @BindView(R.id.data)
    TextView mData;
    @BindView(R.id.moon)
    TextView mMoon;
    @BindView(R.id.week)
    TextView mWeek;
    @BindView(R.id.temperature)
    TextView mTemperature;
    @BindView(R.id.info)
    TextView mInfo;
    @BindView(R.id.humidity)
    TextView mHumidity;
    @BindView(R.id.direct)
    TextView mDirect;
    @BindView(R.id.power)
    TextView mPower;
    @BindView(R.id.recy_life)
    RecyclerView mRecyLife;
    @BindView(R.id.recy_weather)
    RecyclerView mRecyWeather;
    @BindView(R.id.pm)
    TextView mPm;
    @BindView(R.id.quality)
    TextView mQuality;
    @BindView(R.id.des)
    TextView mDes;
    @BindView(R.id.weather_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_search)
    TextView mSearch;
    private IWatherPresenter mPresenter;
    private WeatherLifeApater mWeatherLifeApater;
    private List<WeatherLifeBean> mLifeList = new ArrayList();
    private WeatherInfoAdapter mWeatherInfoAdapter;
    private List<WeatherBean.DataBean.WeatherInfo> mWetherInfoList = new ArrayList();
    private String mCityName;

    public WeatherFragment() {
    }


    @Override
    protected View getCreateView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        getBaseActivity().setSupportActionBar(mToolbar);
        RecyclerView.LayoutManager managerH = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyWeather.setLayoutManager(managerH);
        mWeatherInfoAdapter = new WeatherInfoAdapter(getActivity(), mWetherInfoList, R.layout.item_weather_life_item);
        mRecyWeather.setAdapter(mWeatherInfoAdapter);
        mRecyWeather.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.VERTICAL));

        GridLayoutManager managerG = new GridLayoutManager(getActivity(), 2);
        mRecyLife.setLayoutManager(managerG);
        mWeatherLifeApater = new WeatherLifeApater(getActivity(), mLifeList, R.layout.item_weather_zhishu);
        mRecyLife.setAdapter(mWeatherLifeApater);
        mRecyLife.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.VERTICAL));

        mTvCityName.setText(mCityName);
    }

    @Override
    protected void initData() {
        mPresenter = new WeatherPresenterImp(mCityName, this);
        mPresenter.loadWeather();

    }

    @Override
    protected void initEvent() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchWeatherActivity.class);
                WeatherFragment.this.startActivityForResult(intent, Constant.REQUESTCODE);
            }
        });
    }

    private boolean isEmpty = true;

    @Override
    public void showWather(WeatherBean bean) {
        if (bean != null) {
            isEmpty = false;
            LogUtils.i("weatherBean", bean.toString());
            //未来5天天气
            mWetherInfoList.clear();
            mWetherInfoList.addAll(bean.getData().getWeather());
            mWeatherInfoAdapter.notifyDataSetChanged();
            //生活指数
            mLifeList.clear();
            WeatherBean.DataBean.LifeBean.InfoBean info = bean.getData().getLife().getInfo();
            mLifeList.add(new WeatherLifeBean(info.getChuanyi().get(0), info.getChuanyi().get(1)));
            mLifeList.add(new WeatherLifeBean(info.getGanmao().get(0), info.getGanmao().get(1)));
            mLifeList.add(new WeatherLifeBean(info.getKongtiao().get(0), info.getKongtiao().get(1)));
            mLifeList.add(new WeatherLifeBean(info.getXiche().get(0), info.getXiche().get(1)));
            mLifeList.add(new WeatherLifeBean(info.getYundong().get(0), info.getYundong().get(1)));
            mLifeList.add(new WeatherLifeBean(info.getZiwaixian().get(0), info.getZiwaixian().get(1)));
            mWeatherLifeApater.notifyDataSetChanged();
            //时间
            mData.setText(bean.getData().getRealtime().getDate());
            mMoon.setText(bean.getData().getRealtime().getMoon());
            mWeek.setText("周" + bean.getData().getRealtime().getWeek());
            //天气
            mTemperature.setText(bean.getData().getRealtime().getWeather().getTemperature());
            mInfo.setText(bean.getData().getRealtime().getWeather().getInfo());
            mHumidity.setText("湿度:"+bean.getData().getRealtime().getWeather().getHumidity());
            mDirect.setText(bean.getData().getRealtime().getWind().getDirect());
            mPower.setText(bean.getData().getRealtime().getWind().getPower());
            //空气质量相关
            mPm.setText(bean.getData().getPm25().getPm25().getPm25());
            mQuality.setText(bean.getData().getPm25().getPm25().getQuality());
            mDes.setText(bean.getData().getPm25().getPm25().getDes());

        }
    }

    @Override
    public void failed(Throwable throwable) {
        dismissProgressDialog();
    }

    @Override
    public void start() {
        showProgressDialog();
    }

    @Override
    public void complete() {
        dismissProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == Constant.REQUESTCODE && resultCode == Constant.RESULTCODE) {
                mPresenter.onSearch(data.getStringExtra("cityname"));
                mTvCityName.setText(data.getStringExtra("cityname"));
            }
        }

    }

    @Override
    public void onLocationSuccess(String cityName, String districtName) {
        mCityName = cityName;
    }

    @Override
    public void unLocationSuccessful(int type) {

    }
}
