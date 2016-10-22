package com.liuhao.orange.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.utils.log.LogUtils;
import butterknife.BindView;

public class WebActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView mWebView;
    private String mWebPath;

    @Override
    protected void initContentView() {
        mWebPath = getIntent().getStringExtra("webpath");
        LogUtils.i("webpath", mWebPath);
        setContentView(R.layout.activity_web);
    }

    @Override
    protected void initView() {
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
