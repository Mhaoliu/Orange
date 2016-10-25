package com.liuhao.orange.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.HotCityAdapter;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.constant.Constant;
import com.liuhao.orange.db.bean.WetherSearchBean;
import com.liuhao.orange.presenter.SearchWeatherPresenterImp;
import com.liuhao.orange.presenter.iface.ISearchWeatherPresenter;
import com.liuhao.orange.view.ISearchWetherView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchWeatherActivity extends BaseActivity implements ISearchWetherView {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.bt_search)
    Button mBtSearch;
    @BindView(R.id.recycle_hotcity)
    RecyclerView mRecycleHotcity;
    @BindView(R.id.recycle_search_record)
    RecyclerView mRecycleRecord;
    @BindView(R.id.tv_nodata)
    TextView mTvNodeata;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    private HotCityAdapter mHotCityAdapter;
    private HotCityAdapter mSearchCityAdapter;
    private List<String> mSearchList = new ArrayList();
    private ISearchWeatherPresenter mPresenter;

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_search_weather);
    }

    @Override
    protected void initView() {

        GridLayoutManager managerG1 = new GridLayoutManager(this, 3);
        mRecycleHotcity.setLayoutManager(managerG1);
        mHotCityAdapter = new HotCityAdapter(this, getResources().getStringArray(R.array.hot_city), R.layout.item_city);
        mRecycleHotcity.setAdapter(mHotCityAdapter);

        GridLayoutManager managerG2 = new GridLayoutManager(this, 3);
        mRecycleRecord.setLayoutManager(managerG2);
        mSearchCityAdapter = new HotCityAdapter(this, mSearchList, R.layout.item_city);
        mRecycleRecord.setAdapter(mSearchCityAdapter);

        mBtSearch.setEnabled(false);
    }

    @Override
    protected void initData() {
        mPresenter = new SearchWeatherPresenterImp(this, this);
        mPresenter.onQuere();
    }

    @Override
    protected void initEvent() {
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mBtSearch.setClickable(false);
                if ("".equals(s.toString())) {
                    mBtSearch.setEnabled(false);
                } else {
                    mBtSearch.setEnabled(true);
                    mBtSearch.setClickable(true);
                }
            }
        });
        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = mEtSearch.getText().toString();
                onResult(cityName);
            }
        });
        mHotCityAdapter.setOnItemClickListener(new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String cityName = getResources().getStringArray(R.array.hot_city)[position];
                onResult(cityName);
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onDelete();
            }
        });

    }

    /**
     * @param cityName
     */
    private void onResult(String cityName) {
        WetherSearchBean searchBean = new WetherSearchBean();
        searchBean.setCityName(cityName);
        searchBean.setUserName("liuhao");
        searchBean.setSearTime(System.currentTimeMillis());
        mPresenter.onInsert(searchBean);
        Intent intent = new Intent();
        intent.putExtra("cityname", cityName);
        setResult(Constant.RESULTCODE, intent);
        finish();
    }


    @Override
    public void showSearch(List<WetherSearchBean> list) {
        mSearchList.clear();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                mSearchList.add(list.get(i).getCityName());
            }
            mSearchCityAdapter.notifyDataSetChanged();
            mTvDelete.setVisibility(View.VISIBLE);
            mTvNodeata.setVisibility(View.GONE);
        } else {
            mSearchCityAdapter.notifyDataSetChanged();
            mTvDelete.setVisibility(View.GONE);
            mTvNodeata.setVisibility(View.VISIBLE);
        }
    }
}
