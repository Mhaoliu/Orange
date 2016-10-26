package com.liuhao.orange.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baseproject.utils.Logger;
import com.liuhao.orange.R;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.http.video.VideoInfo;
import com.liuhao.orange.width.VideoDialogFragment;
import com.youku.player.ApiManager;
import com.youku.player.VideoQuality;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;
import com.youku.player.plugin.YoukuPlayerListener;
import com.youku.player.ui.interf.IMediaPlayerDelegate;
import com.youku.service.download.DownloadManager;
import com.youku.service.download.OnCreateDownloadListener;

import butterknife.BindView;

/**
 * 播放器播放界面，
 */
public class PlayerActivity extends BaseActivity {
    // 播放器控件
    @BindView(R.id.full_holder)
    YoukuPlayerView mYoukuPlayerView;
    @BindView(R.id.change_qingxidu)
    TextView mChangeQingxidu;
    @BindView(R.id.close_sound)
    ImageView mIvClose;
    @BindView(R.id.video_dowm)
    ImageView mVideoDown;
    private YoukuBasePlayerManager basePlayerManager;
    private VideoDialogFragment mVideoDialogFragment;
    // 需要播放的视频id
    private String vid;

    // 需要播放的本地视频的id
    private String local_vid;

    // 标示是否播放的本地视频
    private boolean isFromLocal = false;

    private String id = "";

    // YoukuPlayer实例，进行视频播放控制
    private YoukuPlayer youkuPlayer;
    //是否关闭声音
    private boolean isCloseSound = false;
    private VideoInfo.VideosBean mVideosBean;

    @Override
    protected void initContentView() {
        setContentView(R.layout.playactivity_activity);
    }

    @Override
    protected void initView() {
        init();
        mIvClose.setImageResource(R.drawable.ad_icon_volume);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mChangeQingxidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoDialogFragment = new VideoDialogFragment();
                mVideoDialogFragment.show(getFragmentManager(), "videodialog");
                mVideoDialogFragment.setOnItemClickListener(new VideoDialogFragment.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        mChangeQingxidu.setText(PlayerActivity.this.getResources().getStringArray(R.array.video_qingxidu)[position]);
                        switch (position) {
                            case 0: // 切换标清
                                change(VideoQuality.STANDARD);
                                break;
                            case 1: // 切换高清
                                change(VideoQuality.HIGHT);
                                break;
                            case 2: // 切换超清
                                change(VideoQuality.SUPER);
                                break;
                            case 3: // 切换为1080P
                                change(VideoQuality.P1080);
                                break;

                        }
                    }
                });
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCloseSound = !isCloseSound;
                mYoukuPlayerView.onMute(isCloseSound);
                if (isCloseSound) {
                    mIvClose.setImageResource(R.drawable.ad_icon_volume_off);

                } else {
                    mIvClose.setImageResource(R.drawable.ad_icon_volume);
                }
            }
        });
        mVideoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideosBean != null) {
                    doDownload(mVideosBean.getId(), mVideosBean.getTitle());
                    Intent i = new Intent(PlayerActivity.this, CachingActivity.class);
                    startActivity(i);

                }

            }
        });
    }

    private void init() {
        basePlayerManager = new YoukuBasePlayerManager(this) {

            @Override
            public void setPadHorizontalLayout() {

            }

            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                // 初始化成功后需要添加该行代码
                addPlugins();

                // 实例化YoukuPlayer实例
                youkuPlayer = player;

                // 进行播放
                goPlay();
            }

            @Override
            public void onSmallscreenListener() {

            }

            @Override
            public void onFullscreenListener() {

            }
        };
        basePlayerManager.onCreate();

        // 通过上个页面传递过来的Intent获取播放参数
        getIntentData(getIntent());

        if (TextUtils.isEmpty(id)) {
            vid = "XODQwMTY4NDg0"; // 默认视频
        } else {
            vid = id;
        }

        //控制竖屏和全屏时候的布局参数。这两句必填。
        mYoukuPlayerView
                .setSmallScreenLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mYoukuPlayerView
                .setFullScreenLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        // 初始化播放器相关数据
        mYoukuPlayerView.initialize(basePlayerManager);

        //添加播放器的回调
        basePlayerManager.setPlayerListener(new YoukuPlayerListener() {
            @Override
            public void onCompletion() {
                super.onCompletion();
            }

            @Override
            public void onStartBuffering() {
                super.onStartBuffering();
            }
        });
    }

    @Override
    public void onBackPressed() { // android系统调用
        Logger.d("sgh", "onBackPressed before super");
        super.onBackPressed();
        Logger.d("sgh", "onBackPressed");
        basePlayerManager.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        basePlayerManager.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basePlayerManager.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean managerKeyDown = basePlayerManager.onKeyDown(keyCode, event);
        if (basePlayerManager.shouldCallSuperKeyDown()) {
            return super.onKeyDown(keyCode, event);
        } else {
            return managerKeyDown;
        }

    }

    @Override
    public void onLowMemory() { // android系统调用
        super.onLowMemory();
        basePlayerManager.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        basePlayerManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        basePlayerManager.onResume();
    }

    @Override
    public boolean onSearchRequested() { // android系统调用
        return basePlayerManager.onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        basePlayerManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        basePlayerManager.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);

        // 通过Intent获取播放需要的相关参数
        getIntentData(intent);

        // 进行播放
        goPlay();
    }

    /**
     * 获取上个页面传递过来的数据
     */
    private void getIntentData(Intent intent) {

        if (intent != null) {
            // 判断是不是本地视频
            isFromLocal = intent.getBooleanExtra("isFromLocal", false);
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                VideoInfo.VideosBean model = bundle.getParcelable("videobean");
                mVideosBean = model;
            }
            if (isFromLocal) { // 播放本地视频
                local_vid = intent.getStringExtra("video_id");
            } else { // 在线播放
                id = intent.getStringExtra("vid");
                if (mVideosBean != null) {
                    id = mVideosBean.getId();
                    mChangeQingxidu.setText(mVideosBean.getQuality());
                }

            }
        }

    }

    private void goPlay() {
        if (isFromLocal) { // 播放本地视频
            youkuPlayer.playLocalVideo(local_vid);
        } else { // 播放在线视频
            youkuPlayer.playVideo(vid);
        }

        // XNzQ3NjcyNDc2
        // XNzQ3ODU5OTgw
        // XNzUyMzkxMjE2
        // XNzU5MjMxMjcy 加密视频
        // XNzYxNzQ1MDAw 万万没想到
        // XNzgyODExNDY4 魔女范冰冰扑倒黄晓明
        // XNDcwNjUxNzcy 姐姐立正向前走
        // XNDY4MzM2MDE2 向着炮火前进
        // XODA2OTkwMDU2 卧底韦恩突出现 劫持案愈发棘手
        // XODUwODM2NTI0 会员视频
        // XODQwMTY4NDg0 一个人的武林
    }

    /**
     * 更改视频的清晰度
     *
     * @param quality VideoQuality有四种枚举值：{STANDARD,HIGHT,SUPER,P1080}，分别对应：标清，高清，超清，
     *                1080P
     */

    private void change(VideoQuality quality) {
        try {
            // 通过ApiManager实例更改清晰度设置，返回值（1):成功；（0): 不支持此清晰度
            // 接口详细信息可以参数使用文档
            int result = ApiManager.getInstance().changeVideoQuality(quality,
                    basePlayerManager);
            if (result == 0) {
                showToast("不支持此清晰度", 1000);
            }
        } catch (Exception e) {
            showToast(e.getMessage(), 1000);
        }
    }

    /**
     * 简单展示下载接口的使用方法，用户可以根据自己的 通过DownloadManager下载视频
     */
    private void doDownload(String vid, String title) {
        // 通过DownloadManager类实现视频下载
        DownloadManager d = DownloadManager.getInstance();
        /**
         * 第一个参数为需要下载的视频id 第二个参数为该视频的标题title 第三个对下载视频结束的监听，可以为空null
         */
        d.createDownload(vid, title,
                new OnCreateDownloadListener() {

                    @Override
                    public void onfinish(boolean isNeedRefresh) {
                        showToast("视频已经下载完成", 1000);
                    }
                });
    }

}
