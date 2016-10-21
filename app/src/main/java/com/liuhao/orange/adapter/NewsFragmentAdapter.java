package com.liuhao.orange.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.constant.Constant;

import java.util.List;

/**
 * Created by liuhao on 2016/10/22.
 */
public class NewsFragmentAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragmentList;

    public NewsFragmentAdapter(FragmentManager fm, List<BaseFragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList != null ? mFragmentList.get(position) : null;
    }

    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return Constant.JUHE_NEWS_TITLE[position];
    }
}
