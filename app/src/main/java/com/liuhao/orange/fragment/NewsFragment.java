package com.liuhao.orange.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.NewsFragmentAdapter;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.constant.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment {
    @BindView(R.id.news_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.news_viewpager)
    ViewPager mViewPager;
    private NewsFragmentAdapter mFragmentAdapter;
    private List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_news;
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

}
