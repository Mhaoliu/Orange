package com.liuhao.orange.view;

/**
 * 进度显示框
 */
public interface IBaseView {
    /**
     * 显示加载进度框
     */
    void showProgressDialog();

    /**
     * 显示进度框
     */
    void showProgressDialog(CharSequence message);

    /**
     * 消除
     */
    void dismissProgressDialog();

    /**
     * 显示toast
     */
    void showToast(String msg, int time);
}
