package com.liuhao.orange.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.NewsFragmentAdapter;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.constant.Constant;
import com.liuhao.orange.view.ILocationView;
import com.liuhao.orange.width.NestedViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment implements ILocationView {
    @BindView(R.id.news_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.news_viewpager)
    NestedViewPager mViewPager;
    @BindView(R.id.tv_place)
    TextView mTvPlace;
    private NewsFragmentAdapter mFragmentAdapter;
    private List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    protected View getCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        getBaseActivity().setSupportActionBar(mToolbar);


    }

    @Override
    protected void initData() {
        for (int i = 0; i < Constant.JUHE_NEWS_TITLE.length; i++) {
            mFragmentList.add(NewsListFragment.newInstance(i));
        }
        mFragmentAdapter = new NewsFragmentAdapter(getFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onLocationSuccess(String cityName, String districtName) {
        if (districtName != null) {
            mTvPlace.setText(cityName + "-" + districtName);
        } else {
            mTvPlace.setText(cityName);
        }

    }

    @Override
    public void unLocationSuccessful(int type) {

    }
}
