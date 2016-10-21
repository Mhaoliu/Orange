package com.liuhao.orange.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.bean.NewsBean;
import com.liuhao.orange.presenter.NewsListPresenterImp;
import com.liuhao.orange.presenter.iface.INewsListPresenter;
import com.liuhao.orange.view.INewsListView;

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
    @BindView(R.id.text)
    TextView textView;
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
        textView.setText(mIndex + "aaa");
    }

    @Override
    protected void initData() {
        mNewsPresenter = new NewsListPresenterImp(this);
        mNewsPresenter.loadNews();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void showNewsList(NewsBean newsBean) {

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
}
