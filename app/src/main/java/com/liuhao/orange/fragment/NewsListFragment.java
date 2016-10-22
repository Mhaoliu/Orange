package com.liuhao.orange.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.liuhao.orange.R;
import com.liuhao.orange.activity.WebActivity;
import com.liuhao.orange.adapter.NewsListAdapter;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.bean.NewsBean;
import com.liuhao.orange.presenter.NewsListPresenterImp;
import com.liuhao.orange.presenter.iface.INewsListPresenter;
import com.liuhao.orange.utils.log.LogUtils;
import com.liuhao.orange.view.INewsListView;
import com.liuhao.orange.width.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends BaseFragment implements INewsListView {
    private static final String ARG_PARAM = "index";
    //当前fragment 的位置
    private int mIndex;
    @BindView(R.id.recycler_new_list)
    RecyclerView mRecyclerView;
    private List<NewsBean.DataBean> mNewsList = new ArrayList<>();
    private NewsListAdapter mListAdapter;
    private INewsListPresenter mNewsPresenter;

    public NewsListFragment() {

    }

    public static NewsListFragment newInstance(int index) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mListAdapter = new NewsListAdapter(getActivity(), mNewsList, R.layout.new_list_item_one);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
    }

    @Override
    protected void initData() {
        mNewsPresenter = new NewsListPresenterImp(this, getActivity(), mIndex);
        mNewsPresenter.loadNews();
    }

    @Override
    protected void initEvent() {
        mListAdapter.setOnItemClickListener(new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mNewsList != null && mNewsList.get(position) != null) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("webpath", mNewsList.get(position).getUrl());
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void showNewsList(NewsBean newsBean) {
        if (newsBean != null) {
            LogUtils.i("newsBean", newsBean.toString());
            mNewsList.addAll(newsBean.getData());
            mListAdapter.addAll(mNewsList);
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
        mNewsPresenter.unSubscribe();
    }
}
