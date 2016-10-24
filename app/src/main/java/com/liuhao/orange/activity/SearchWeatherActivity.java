package com.liuhao.orange.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.constant.Constant;


public class SearchWeatherActivity extends BaseActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            intent.putExtra("cityname", "广州");
            setResult(Constant.RESULTCODE, intent);
            finish();
        }
    };

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_search_weather);
    }

    @Override
    protected void initView() {
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }
}
