package com.liuhao.orange.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.utils.log.LogUtils;

import butterknife.BindView;

public class NewsDetailsActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.details_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_title_img)
    ImageView mTitleImg;
    @BindView(R.id.collapse_toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    private String mWebPath;
    private String mImgPath;

    @Override
    protected void initContentView() {
        onNewIntent(getIntent());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_news_details);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            mWebPath = intent.getStringExtra("webpath");
            mImgPath = intent.getStringExtra("imgpath");
            LogUtils.i("webpath", mWebPath);
            LogUtils.i("imgpath", mImgPath);
        }
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("");
        mToolbarLayout.setTitle("新闻详情");
        mToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Glide.with(this).load(mImgPath).into(mTitleImg);
        // 设置编码
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        // 设置javaweb可以促发
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);// 是否使用缓存
        mWebView.loadUrl(mWebPath);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissProgressDialog();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();

        }

        @Override
        public void onReceivedError(final WebView view, int errorCode, String description,
                                    final String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            dismissProgressDialog();
        }
    }

}

