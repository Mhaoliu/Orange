package com.liuhao.orange.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.liuhao.orange.R;
import com.liuhao.orange.activity.CachedActivity;
import com.liuhao.orange.adapter.VideoListAdapter;
import com.liuhao.orange.base.BaseFragment;
import com.liuhao.orange.constant.Constant;
import com.liuhao.orange.http.video.VideoInfo;
import com.liuhao.orange.presenter.VideoListPresenterImp;
import com.liuhao.orange.presenter.iface.IVideoListPresenter;
import com.liuhao.orange.view.IVideoListView;
import com.liuhao.orange.width.RecycleViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment implements IVideoListView {
    //https://api.youku.com/quality/video/by/keyword.json?client_id=9304521996778fe7&lengthtype=3&published=month&count=10
    @BindView(R.id.recycle_video)
    RecyclerView mRecyclerView;
    @BindView(R.id.video_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_cached)
    ImageView mVideoCached;
    private VideoListAdapter mVideoListAdapter;
    private List<VideoInfo.VideosBean> mVideoList = new ArrayList();
    private IVideoListPresenter mPresenter;

    @Override
    protected View getCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        getBaseActivity().setSupportActionBar(mToolbar);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL));
        mVideoListAdapter = new VideoListAdapter(getActivity(), mVideoList, R.layout.video_list_item);
        mRecyclerView.setAdapter(mVideoListAdapter);
        mPresenter = new VideoListPresenterImp(getActivity(), this);
        Map<String, String> map = new HashMap<>();
        //client_id=9304521996778fe7&lengthtype=3&published=month&count=10
        map.put("client_id", Constant.YOUKU_APP_KEY);
        map.put("lengthtype", "3");
        map.put("published", "month");
        map.put("count", "20");
        mPresenter.onDownLoad(map);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mVideoCached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CachedActivity.class);
                getActivity().startActivity(i);
            }
        });
    }


    @Override
    public void onDownloadSuccess(VideoInfo info) {
        mVideoList.clear();
        if (info != null) {
            mVideoList.addAll(info.getVideos());
            mVideoListAdapter.notifyDataSetChanged();
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
        mPresenter.unRegister();
    }
}
