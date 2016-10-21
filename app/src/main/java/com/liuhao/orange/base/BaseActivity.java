package com.liuhao.orange.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhao.orange.OrgApplication;
import com.liuhao.orange.R;
import com.liuhao.orange.view.IBaseView;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    //ProgressDialog
    private ProgressDialog mProgressDialog;
    private TextView mProgressText;
    private View mProgressView;
    //是否结束程序
    protected boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化ProgressDialog
        initProgressDialog();
        //初始化布局
        initContentView();
        //初始化控件
        ButterKnife.bind(this);
        //初始化View
        initView();
        //初始化数据
        initData();
        //初始化监听
        initEvent();
    }

    @Override
    protected void onDestroy() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * 初始化ProgressDialog
     */
    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            // mProgressDialog.setCancelable(false);
            mProgressView = getLayoutInflater().inflate(R.layout.custom_load_dialog, null);
            mProgressText = (TextView) mProgressView.findViewById(R.id.tv_loading);
        }
    }

    @Override
    public void showProgressDialog() {
        this.showProgressDialog(getString(R.string.app_data_loading));
    }

    @Override
    public void showProgressDialog(CharSequence message) {

        if (mProgressDialog != null && !isDestroyed() && !mProgressDialog.isShowing()) {
            mProgressText.setText(message);
            mProgressDialog.show();
            mProgressDialog.setContentView(mProgressView);
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null && !isDestroyed() && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg, int time) {
        if (!isFinishing()) {
            Toast.makeText(this, msg, time).show();
        }
    }

    public OrgApplication getApp() {
        return (OrgApplication) getApplication();
    }

    /**
     * 初始化布局
     */
    protected abstract void initContentView();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 监听
     */
    protected abstract void initEvent();

}
