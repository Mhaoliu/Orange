package com.liuhao.orange.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.liuhao.orange.R;
import com.liuhao.orange.view.IBaseView;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements IBaseView {
    private BaseActivity mBaseActivity;
    protected View mLayoutView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutView != null) {
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (parent != null) {
                parent.removeView(mLayoutView);
            }
        } else {
            mLayoutView = getCreateView(inflater, container);
            ButterKnife.bind(this, mLayoutView);
            //初始化布局
            initView();
            initData();
            initEvent();
        }
        return mLayoutView;
    }

    protected abstract View getCreateView(LayoutInflater inflater, ViewGroup container);


//    private View getCreateView(LayoutInflater inflater, ViewGroup container) {
//        return inflater.inflate(getLayoutResources(), container, false);
//    }

//    /**
//     * 获取布局文件
//     *
//     * @return
//     */
//    protected abstract int getLayoutResources();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据源
     */
    protected abstract void initData();

    /**
     * 初始化监听
     */
    protected abstract void initEvent();

    /**
     * 获取Activity
     *
     * @return
     */
    public BaseActivity getBaseActivity() {
        if (mBaseActivity == null) {
            mBaseActivity = (BaseActivity) getActivity();
        }
        return mBaseActivity;
    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }

    @Override
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.app_data_loading));
    }

    @Override
    public void showProgressDialog(CharSequence message) {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showProgressDialog(message);
            }
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.dismissProgressDialog();
            }
        }
    }

    @Override
    public void showToast(String msg, int time) {
        if (getStatus()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showToast(msg, time);
            }
        }
    }
}
