package com.liuhao.orange.view;

import android.widget.Toast;

import com.liuhao.orange.R;

/**
 * Created by liuhao on 2016/10/22.
 */
public interface IBaseView {

    void showProgressDialog();

    void showProgressDialog(CharSequence message);

    void dismissProgressDialog();

    void showToast(String msg, int time);


}
