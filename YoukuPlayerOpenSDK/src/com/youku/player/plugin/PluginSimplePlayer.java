package com.youku.player.plugin;

import javax.xml.datatype.Duration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baseproject.image.ImageResizer;
import com.baseproject.utils.Logger;
import com.baseproject.utils.UIUtils;
import com.baseproject.utils.Util;
import com.youku.download.DownInfo;
import com.youku.download.DownLoadManager;
import com.youku.player.ApiManager;
import com.youku.player.Track;
import com.youku.player.VideoQuality;
import com.youku.player.apiservice.ICacheInfo;
import com.youku.player.base.GoplayException;
import com.youku.player.base.Orientation;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.goplay.Profile;
import com.youku.player.goplay.StaticsUtil;
import com.youku.player.module.VideoCacheInfo;
import com.youku.player.module.VideoUrlInfo.Source;
import com.youku.player.ui.R;
import com.youku.player.ui.interf.IMediaPlayerDelegate;
import com.youku.player.util.DetailMessage;
import com.youku.player.util.DetailUtil;
import com.youku.player.util.PlayCode;
import com.youku.player.util.PlayerUtil;
import com.youku.player.util.ValidateUtil;
import com.youku.uplayer.MPPErrorCode;

/**
 * 简单的播放控制插件
 * 
 * @author LongFan
 * @CreateDate 2013年5月15日10:38:55
 */
@SuppressLint("NewApi")
public class PluginSimplePlayer extends PluginOverlay implements DetailMessage {
	/**
	 * 标题栏部分
	 */
	private LinearLayout titleLayout;
	private ImageView titleBack;// 返回按钮
	private TextView titleTextView;// 播放标题
	private TextView titleQualityBtn;// 清晰度按钮
	private ImageView titleLogo;// logo

	/**
	 * 底部控制条部分
	 */
	private RelativeLayout controlLayout;
	private ImageButton playPauseBtn;// 播放暂停按钮
	private TextView currentTime;// 当前进度
	private SeekBar videoSeekBar;// 进度条
	private HotPointView hotPointView; // 精彩点
	private TextView totalTime;// 总时长
	private ImageView fullSmallBtn;// 全屏按钮

	private LinearLayout mContainerLayout;// 整个布局layout
	// private LinearLayout mainPlayLayout;// 包含播放器的部分
	private YoukuBasePlayerManager mBasePlayerManager;
	private Activity mActivity;
	private ImageView userPlayButton;
	private FrameLayout interactFrameLayout;
	private View containerView;
	private PopupWindow popVideoQuality;
	private RadioButton vq0, vq1, vq2;
	private View seekLoadingContainerView;
	private RelativeLayout loadingInfoLayout;
	private SeekBar loadingSeekBar;

	// private Loading playLoading;

	public PluginSimplePlayer(YoukuBasePlayerManager basePlayerManager,
			IMediaPlayerDelegate mediaPlayerDelegate) {
		super(basePlayerManager.getBaseActivity(), mediaPlayerDelegate);
		mBasePlayerManager = basePlayerManager;
		this.mActivity = basePlayerManager.getBaseActivity();
		LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
		containerView = mLayoutInflater.inflate(
				R.layout.yp_plugin_detail_play_interact, null);
		addView(containerView);
		initPlayLayout();

	}

	/**
	 * 清除不用的对象，释放内存
	 */
	public void clear() {
		seekHandler.removeCallbacksAndMessages(null);
		seekendHandler.removeCallbacksAndMessages(null);
		loadInfoHandler.removeCallbacksAndMessages(null);
		hideHandler.removeCallbacksAndMessages(null);
		playHandler.removeCallbacksAndMessages(null);
		playLoadingBar = null;
		seekLoadingContainerView = null;
		mContainerLayout.setOnClickListener(null);
		mContainerLayout = null;
		interactFrameLayout = null;
		userPlayButton.setOnClickListener(null);
		userPlayButton.setImageBitmap(null);
		userPlayButton = null;
		containerView = null;
	}

	private View retryView;
	private LinearLayout goRetry;

	// 重试
	/**
	 * 重试初始化
	 */
	private void initRetry() {
		if (null == containerView)
			return;
		retryView = containerView.findViewById(R.id.view_restart);
		goRetry = (LinearLayout) containerView.findViewById(R.id.go_retry);
		if (null != goRetry) {
			goRetry.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					error = false;
					if (!Util.hasInternet()) {

						PlayerUtil.showTips(R.string.player_tips_no_network);
						return;
					}
					if (null != retryView)
						retryView.setVisibility(View.GONE);
					if (null != mMediaPlayerDelegate) {
						if (!infoFail) {
							mMediaPlayerDelegate.release();
							mMediaPlayerDelegate.setFirstUnloaded();
							mMediaPlayerDelegate.start();
							mMediaPlayerDelegate.retry();
							showLoading();
						} else {
							if (mMediaPlayerDelegate != null
									&& mMediaPlayerDelegate.videoInfo != null) {
								mMediaPlayerDelegate
										.playVideo(mMediaPlayerDelegate.videoInfo
												.getVid());
								mMediaPlayerDelegate.setFirstUnloaded();
							} else if (!TextUtils
									.isEmpty(mMediaPlayerDelegate.nowVid)) {
								mMediaPlayerDelegate
										.playVideo(mMediaPlayerDelegate.nowVid);
								mMediaPlayerDelegate.setFirstUnloaded();
								// mMediaPlayerDelegate.retry();
							}
							// mMediaPlayerDelegate.replayVideo();
						}
					}
				}
			});
		}
	}

	// 初始化播放区控件
	/**
	 * 找到相关的layout
	 */
	private void initPlayLayout() {
		if (null == containerView)
			return;

		// 整个标题布局
		titleLayout = (LinearLayout) containerView
				.findViewById(R.id.layout_title);
		// 返回按钮
		titleBack = (ImageView) containerView.findViewById(R.id.title_back);
		titleBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 全屏时变小屏，小屏就结束
				if (mMediaPlayerDelegate.isFullScreen) {
					mMediaPlayerDelegate.goSmall();
				} else {
					mActivity.finish();
				}

			}
		});
		// 标题兰
		titleTextView = (TextView) containerView
				.findViewById(R.id.tv_detail_play_title);
		// 清晰度按钮
		titleQualityBtn = (TextView) containerView.findViewById(R.id.cur_vidq);
		// titleLogo
		titleLogo = (ImageView) containerView.findViewById(R.id.title_logo);
		titleLogo.setImageResource(R.drawable.vertical_logo);

		// 播放暂停按钮
		playPauseBtn = (ImageButton) containerView
				.findViewById(R.id.ib_detail_play_control);
		playPauseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == mMediaPlayerDelegate)
					return;

				if (mMediaPlayerDelegate.isPlaying()) {
					mMediaPlayerDelegate.pause();
					playPauseBtn.setImageResource(R.drawable.icon_play);
				} else {
					mMediaPlayerDelegate.start();
					playPauseBtn.setImageResource(R.drawable.icon_pause);
				}
				if (isBack) {
					isBack = false;
					isLoading = true;
					playPauseBtn.setImageResource(R.drawable.icon_pause);
				}
				userAction();
			}
		});

		// 当前进度
		currentTime = (TextView) containerView.findViewById(R.id.current_time);
		// 进度条
		videoSeekBar = (SeekBar) containerView
				.findViewById(R.id.sb_detail_play_progress);
		videoSeekBar.setOnSeekBarChangeListener(mBarChangeListener);
		// 精彩点
		hotPointView = (HotPointView) containerView
				.findViewById(R.id.hotPointView);
		// 总共时长
		totalTime = (TextView) containerView.findViewById(R.id.total_time);

		seekLoadingContainerView = containerView
				.findViewById(R.id.seek_loading_bg);
		seekLoadingContainerView.setVisibility(View.GONE);
		mContainerLayout = (LinearLayout) containerView
				.findViewById(R.id.ll_detail_container);
		mContainerLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onContainerClick();
			}
		});
		if (UIUtils.hasHoneycomb()) {
			mContainerLayout
					.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {

						@Override
						public void onSystemUiVisibilityChange(int visibility) {
							//转屏时只弹出进度条，测试认为是个bug 20160824
							//hideShowControl();
						}
					});
		}
		interactFrameLayout = (FrameLayout) containerView
				.findViewById(R.id.fl_interact);
		mContainerLayout.setClickable(false);

		controlLayout = (RelativeLayout) containerView
				.findViewById(R.id.layout_play_control);
		controlLayout.setVisibility(View.GONE);

		fullSmallBtn = (ImageView) containerView
				.findViewById(R.id.ib_detail_play_full);

		fullSmallBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMediaPlayerDelegate.isFullScreen) {
					mMediaPlayerDelegate.goSmall();
				} else {
					mMediaPlayerDelegate.goFullScreen();
				}

			}
		});
		View pop = LayoutInflater.from(mActivity).inflate(R.layout.vidqitem,
				null);
		
		popVideoQuality = new PopupWindow(pop, -2, -2, true);
		popVideoQuality.setTouchable(true);
		popVideoQuality.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.nonedrawable));
		vq0 = (RadioButton) pop.findViewById(R.id.vq0);
		vq1 = (RadioButton) pop.findViewById(R.id.vq1);
		vq2 = (RadioButton) pop.findViewById(R.id.vq2);
		vq0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeVideoq(VideoQuality.STANDARD);
				popVideoQuality.dismiss();
			}
		});
		vq1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeVideoq(VideoQuality.HIGHT);
				popVideoQuality.dismiss();
			}
		});
		vq2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeVideoq(VideoQuality.SUPER);
				popVideoQuality.dismiss();
			}
		});
		titleQualityBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int popWidth = Util.dip2px(75);
				int quality = Profile.videoQuality;
				boolean b = mMediaPlayerDelegate.videoInfo.playType
						.equals(StaticsUtil.PLAY_TYPE_LOCAL);

				if (b) {
					// titleQualityBtn.setClickable(false);

					String vvid = mMediaPlayerDelegate.videoInfo.getVid();
					DownInfo dinfo = DownLoadManager.getInstance().getDownInfoByVid(
							vvid);
					if (ValidateUtil.isValid(dinfo)) {
						quality = dinfo.getVideoQuality();
					}
				}
				if (quality == (VideoQuality.STANDARD.ordinal())) {
					vq0.setChecked(true);
				}
				if (quality == (VideoQuality.HIGHT.ordinal())) {
					vq1.setChecked(true);
				}
				if (quality == (VideoQuality.SUPER.ordinal())) {
					vq2.setChecked(true);
				}
				popVideoQuality.setWidth(popWidth);
				int xOffset = titleQualityBtn.getLeft()
						+ (titleQualityBtn.getMeasuredWidth() - popWidth) / 2;
				popVideoQuality.showAsDropDown(titleQualityBtn, xOffset,
						Util.dip2px(5));
			}
		});
		initSeekLoading();
		if (null != mMediaPlayerDelegate
				&& null != mMediaPlayerDelegate.videoInfo) {
			int duration = mMediaPlayerDelegate.videoInfo.getDurationMills();
			videoSeekBar.setMax(duration);
		}
		userPlayButton = (ImageView) containerView
				.findViewById(R.id.ib_user_play);
		if (null != userPlayButton) {
			userPlayButton.setOnClickListener(userPlayClickListener);
			userPlayButton.setVisibility(View.GONE);
		}
		// videoBar.setMax(240000);
		if (null != mMediaPlayerDelegate)
			if (mMediaPlayerDelegate.isPlaying())
				playPauseBtn.setImageResource(R.drawable.icon_pause);
			else {
				playPauseBtn.setImageResource(R.drawable.icon_play);
			}
		initRetry();
		initEndPage();
		initLoadInfoPage();
	}

	private View endPageView;
	private LinearLayout nextLayout;
	private LinearLayout replayLayout;

	/**
	 * 初始化播放完成的显示
	 */
	private void initEndPage() {
		if (null == mActivity)
			return;
		LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
		if (null == mLayoutInflater)
			return;
		endPageView = mLayoutInflater.inflate(R.layout.yp_detail_play_end_page,
				null);
		if (null == endPageView)
			return;
		nextLayout = (LinearLayout) endPageView.findViewById(R.id.ll_next_play);
		replayLayout = (LinearLayout) endPageView.findViewById(R.id.ll_replay);
		if (null != nextLayout)
			nextLayout.setOnClickListener(new OnClickListener() {// 播放下一集

						@Override
						public void onClick(View v) {
							if (!Util.hasInternet())
								return;
							playNextVideo();
							hideEndPage();
							restartFromComplete();
						}
					});
		if (null != replayLayout)
			replayLayout.setOnClickListener(new OnClickListener() {// 重播

						@Override
						public void onClick(View v) {
							if (null != mMediaPlayerDelegate) {
								mMediaPlayerDelegate.release();
								mMediaPlayerDelegate.setFirstUnloaded();
								onVideoInfoGetted();
								mMediaPlayerDelegate.start();
								if (null != mMediaPlayerDelegate.videoInfo)
									mMediaPlayerDelegate.videoInfo
											.setProgress(0);
								mMediaPlayerDelegate.seekTo(0);
								hideEndPage();
								restartFromComplete();
							}
						}
					});
	}

	/**
	 * 初始化加载页面
	 */
	private void initLoadInfoPage() {
		if (null == mActivity)
			return;
		LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
		if (null == mLayoutInflater)
			return;
		loadingInfoLayout = (RelativeLayout) mLayoutInflater.inflate(
				R.layout.yp_detail_loading_info_page, null);
		if (null == loadingInfoLayout)
			return;
		loadingSeekBar = (SeekBar) loadingInfoLayout
				.findViewById(R.id.loading_info_seekbar);

	}

	private TextView playNameTextView;
	private SeekBar playLoadingBar;
	private TextView loadingTips;
	private String TAG = "PluginSmallScreenPlay";

	/**
	 * 初始化seek的loading界面
	 */
	private void initSeekLoading() {
		if (null == seekLoadingContainerView)
			return;
		playNameTextView = (TextView) seekLoadingContainerView
				.findViewById(R.id.detail_play_load_name);
		playLoadingBar = (SeekBar) seekLoadingContainerView
				.findViewById(R.id.loading_seekbar);
		loadingTips = (TextView) seekLoadingContainerView
				.findViewById(R.id.loading_tips);
		if (null != playLoadingBar)
			playLoadingBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							if (fromUser) {
								// Track.setTrackPlayLoading(false);
								return;
							} else {
								seekBar.setProgress(progress);
							}

						}
					});
	}

	private int seekcount = 0;

	public static final int SHOW_LOADING = 1111;
	public static final int HIDE_LOADING = 1112;

	/**
	 * 显示加载中
	 */
	public void showLoading() {
		Logger.e(TAG, "showLoading()");
		if (mMediaPlayerDelegate.isADShowing) {
			Logger.e(TAG, "mMediaPlayerDelegate.isADShowing()");
			return;
		}
		if (null != seekLoadingContainerView) {
			if (seekLoadingContainerView.getVisibility() == View.GONE) {
				if (null != seekLoadingContainerView)
					seekLoadingContainerView.setVisibility(View.VISIBLE);
				seekcount = 0;
				if (null != seekendHandler)
					seekHandler.sendEmptyMessageDelayed(SHOW_LOADING, 0);

			}
		}
		if (null != mMediaPlayerDelegate
				&& null != mMediaPlayerDelegate.videoInfo
				&& !DetailUtil.isEmpty(mMediaPlayerDelegate.videoInfo
						.getTitle()))
			if (null != playNameTextView)
				playNameTextView.setText(mMediaPlayerDelegate.videoInfo
						.getTitle());

		if (null != mMediaPlayerDelegate
				&& null != mMediaPlayerDelegate.videoInfo && firstLoaded) {
			if (null != loadingTips)
				loadingTips.setVisibility(View.GONE);
			if (null != playNameTextView)
				playNameTextView.setVisibility(View.GONE);
			if (null != seekLoadingContainerView && firstLoaded)
				seekLoadingContainerView.setBackgroundResource(0);
		} else {
			if (null != loadingTips) {
				loadingTips.setText(getResources().getString(
						R.string.player_tip_loading));
				loadingTips.setVisibility(View.VISIBLE);
			}
			if (null != playNameTextView)
				playNameTextView.setVisibility(View.VISIBLE);
			if (null != seekLoadingContainerView)
				seekLoadingContainerView
						.setBackgroundResource(R.drawable.bg_play);
		}

	}

	/**
	 * 隐藏加载
	 */
	public void hideLoading() {
		if (null == mActivity)
			return;
		((Activity) mActivity).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (null != seekLoadingContainerView) {
					seekLoadingContainerView.setVisibility(View.GONE);
					playLoadingBar.setProgress(0);
				}
				if (null != seekHandler)
					seekHandler.removeCallbacksAndMessages(null);
			}
		});
	}

	private Handler seekHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (null == seekHandler || null == playLoadingBar)
				return;
			if (msg.what == SHOW_LOADING) {
				if (seekcount >= 95) {
					seekcount = 0;
				}
				seekcount += 2;
				seekHandler.sendEmptyMessageDelayed(SHOW_LOADING, 100);

				playLoadingBar.setProgress(seekcount);
			} else {
				seekHandler.removeMessages(SHOW_LOADING);
				if (seekcount >= 90) {
					if (null != playLoadingBar)
						playLoadingBar.setProgress(seekcount);
					if (null != seekLoadingContainerView)
						seekLoadingContainerView.setVisibility(View.GONE);
					return;
				}
				seekcount += 10;
				if (null != playLoadingBar)
					playLoadingBar.setProgress(seekcount);
				seekHandler.sendEmptyMessageDelayed(HIDE_LOADING, 50);
			}

		}

	};
	private Handler seekendHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (seekcount < 100) {
				seekcount++;
				if (null != playLoadingBar)
					playLoadingBar.setProgress(seekcount);
				if (null != seekHandler)
					seekHandler.sendEmptyMessageDelayed(0, 10);
			}

		}

	};

	/**
	 * 显示加载的片名
	 */
	private void showLoadinfo() {
		if (null != mActivity)
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != interactFrameLayout) {
						interactFrameLayout.removeView(loadingInfoLayout);
						interactFrameLayout.addView(loadingInfoLayout);
					}
					if (null != loadInfoHandler)
						loadInfoHandler.sendEmptyMessage(0);
				}
			});

	}

	/**
	 * 隐藏加载的片名
	 */
	private void hideLoadinfo() {
		if (null != mActivity)
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != interactFrameLayout)
						interactFrameLayout.removeView(loadingInfoLayout);

					if (null != loadInfoHandler)
						loadInfoHandler.removeCallbacksAndMessages(null);
				}
			});

	}

	private int loadinfoseek = 0;
	private boolean loadinfoseekend = false;
	private Handler loadInfoHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (loadinfoseek == 0) {
				loadinfoseekend = false;
			}
			if (loadinfoseek == 100) {
				loadinfoseekend = true;
			}
			if (!loadinfoseekend)
				loadinfoseek++;
			else {
				loadinfoseek--;
			}
			if (null != loadingSeekBar)
				loadingSeekBar.setProgress(loadinfoseek);
			if (null != loadInfoHandler)
				loadInfoHandler.sendEmptyMessageDelayed(0, 50);
		}

	};
	private boolean autoPlay = true;

	/**
	 * 设置是否自动播放
	 * 
	 * @param autoplay
	 */
	public void setAutoPlay(boolean autoplay) {
		autoPlay = autoplay;
		if (null == userPlayButton)
			return;

		if (!autoplay) {
			if (null != userPlayButton)
				userPlayButton.setVisibility(View.VISIBLE);
			disableController();
			hideLoading();
		} else if (null != userPlayButton)
			userPlayButton.setVisibility(View.GONE);
	}

	/**
	 * 设置title的高度
	 * 
	 * @return
	 */
	public int getTitleHeight() {
		if (null == titleTextView)
			return 0;
		LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) titleTextView
				.getLayoutParams();
		if (null != mParams)
			return mParams.height;
		else
			return 0;
	}

	/**
	 * 搜索进度
	 * 
	 * @param seekBar
	 */
	protected void seekChange(SeekBar seekBar) {
		if (null == mMediaPlayerDelegate)
			return;
		if (null != seekBar && seekBar.getProgress() == seekBar.getMax()
				&& seekBar.getMax() > 0) {
			if (null != mMediaPlayerDelegate.videoInfo)
				mMediaPlayerDelegate.videoInfo
						.setProgress(mMediaPlayerDelegate.videoInfo
								.getDurationMills());
			// complete = true;
			mMediaPlayerDelegate.onComplete();

			// if (null != playHandler) {
			// playHandler.removeCallbacksAndMessages(null);
			// playHandler.sendEmptyMessage(MSG_COMPLETE);
			// }
		} else if (mMediaPlayerDelegate != null) {
			if (null != mMediaPlayerDelegate.videoInfo)
				mMediaPlayerDelegate.videoInfo.setProgress(seekBar
						.getProgress());
			if (!mMediaPlayerDelegate.isPlaying()) {
				startPlay();
			}
			mMediaPlayerDelegate.seekTo(seekBar.getProgress());
			isLoading = true;
			videoSeekBar.setEnabled(false);
			Logger.e("PlayFlow", "小播放器拖动seekto" + seekBar.getProgress());
		}
	}

	/**
	 * 开始播放
	 */
	private void startPlay() {
		if (null == mMediaPlayerDelegate)
			return;
		if (mMediaPlayerDelegate.isADShowing) {
			mBasePlayerManager.startPlay();
		} else {
			mMediaPlayerDelegate.start();
			playPauseBtn.setImageResource(R.drawable.icon_pause);
		}
	}

	/** 用户上次与控制界面交互的时间。 */
	protected long lastInteractTime = 0;
	// seekbar
	OnSeekBarChangeListener mBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			if (Util.hasInternet())
				if (fromUser) {
					Logger.d(TAG, "onProgressChanged: " + progress);
					seekBar.setProgress(progress);
					currentTime.setText(PlayerUtil.getFormatTime(progress));
				}
			changePlayPause();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Logger.d(TAG, "onStartTrackingTouch");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Logger.d(TAG, "onStopTrackingTouch");
			seekChange(seekBar);

		}

	};

	private void changePlayPause() {
		if (null == playPauseBtn || null == mMediaPlayerDelegate)
			return;
		if (isLoading) {
			return;
		}
		if (mMediaPlayerDelegate.isPlaying())
			playPauseBtn.setImageResource(R.drawable.icon_pause);
		else {
			playPauseBtn.setImageResource(R.drawable.icon_play);
		}
	}

	private OnClickListener userPlayClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			enableController();
			if (null != mMediaPlayerDelegate)
				mMediaPlayerDelegate.start();
			if (null != userPlayButton)
				userPlayButton.setVisibility(View.GONE);
		}
	};

	private void hideShowControl() {
		if (null == controlLayout || null == playPauseBtn)
			return;
		if (controlLayout.getVisibility() == View.VISIBLE) {
			controlLayout.setVisibility(View.GONE);
		} else {
			// Logger.d("sgh","plugin_small, from local: " +
			// mMediaPlayerDelegate.videoInfo.getPlayType());
			if (mMediaPlayerDelegate.videoInfo != null
					&& StaticsUtil.PLAY_TYPE_LOCAL
							.equals(mMediaPlayerDelegate.videoInfo
									.getPlayType())) {
				fullSmallBtn.setVisibility(View.INVISIBLE);
			} else {
				fullSmallBtn.setVisibility(View.VISIBLE);
			}
			controlLayout.setVisibility(View.VISIBLE);
			if (isLoading) {
				playPauseBtn.setImageResource(R.drawable.icon_pause);
				return;
			}
			if (null != mMediaPlayerDelegate
					&& mMediaPlayerDelegate.isPlaying())
				playPauseBtn.setImageResource(R.drawable.icon_pause);
			else {
				playPauseBtn.setImageResource(R.drawable.icon_play);
			}
		}

	}

	protected void onContainerClick() {
		if (null == controlLayout || null == titleLayout)
			return;
		if (null != hideHandler)
			hideHandler.removeCallbacksAndMessages(null);
		if (controlLayout.getVisibility() == View.VISIBLE
				&& titleLayout.getVisibility() == View.INVISIBLE) {
			controlLayout.setVisibility(View.GONE);
			return;
		}
		if (controlLayout.getVisibility() == View.GONE
				&& titleLayout.getVisibility() == View.VISIBLE) {
			hideTitle();
			return;
		}
		hideShowControl();
		showHideTitle();
		userAction();
		return;
	}

	private void showHideTitle() {
		if (null == titleLayout)
			return;
		if (titleLayout.getVisibility() == View.VISIBLE)
			hideTitle();
		else {
			showTitle();
		}

	}

	private void hideTitle() {
		if (null != titleLayout)
			titleLayout.setVisibility(View.INVISIBLE);

		if (popVideoQuality != null) {
			try {
				popVideoQuality.dismiss();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void showTitle() {
		if (null != titleLayout)
			titleLayout.setVisibility(View.VISIBLE);
	}

	private void hideControl() {
		if (null != controlLayout)
			controlLayout.setVisibility(View.GONE);
	}

	private void showControl() {
		if (null != controlLayout)
			controlLayout.setVisibility(View.VISIBLE);
		if (null != mMediaPlayerDelegate && mMediaPlayerDelegate.isPlaying()) {
			playPauseBtn.setImageResource(R.drawable.icon_pause);
		} else {
			playPauseBtn.setImageResource(R.drawable.icon_play);
		}
	}

	private Handler hideHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HIDE_CONTROL: {
				if (null != controlLayout)
					controlLayout.setVisibility(View.GONE);
				break;
			}
			case HIDE_TITLE: {
				hideTitle();
				break;
			}
			case HIDE_ALL: {
				hideTitle();
				hideControl();
			}
			default:
				break;
			}
		}

	};

	/**
	 * 用户操作后，延迟隐藏
	 * 
	 */
	private final int HIDE_CONTROL = 1001;
	private final int HIDE_TITLE = 1002;
	private final int HIDE_ALL = 1003;

	protected void userAction() {
		if (hideHandler != null) {
			hideHandler.removeCallbacksAndMessages(null);
			hideHandler.sendEmptyMessageDelayed(HIDE_ALL, 5000);
		}
	}

	public void setVideoImage(ImageResizer maker, String imageurl) {

	}

	protected int selectedFormat = Profile.FORMAT_FLV_HD;

	/**
	 * 分享
	 * */
	public void share() {

	}

	public void clearPlayState() {
		if (null == mActivity)
			return;
		((Activity) mActivity).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				disableControllerHide();
				if (null != titleTextView)
					titleTextView.setText("");
				if (null != videoSeekBar) {
					{
						videoSeekBar.setProgress(0);
						videoSeekBar.setMax(0);
					}

				}
			}

		});

	}

	int Adaptation_lastPercent = 0;

	@Override
	public void onBufferingUpdateListener(final int percent) {
		if (null == mActivity)
			return;
		((Activity) mActivity).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (percent == 100) {
					videoSeekBar.setSecondaryProgress(videoSeekBar.getMax());
				}
				if (percent == 100 && Adaptation_lastPercent != 100) {
					Adaptation_lastPercent = percent;
					return;
				}
				// hideRetryLayout();
				if (null == mMediaPlayerDelegate
						|| null == mMediaPlayerDelegate.videoInfo)
					return;
				int showSecond = (percent * mMediaPlayerDelegate.videoInfo
						.getDurationMills()) / 100;
				if (null != videoSeekBar)
					videoSeekBar.setSecondaryProgress(showSecond);
			}
		});
	}

	@Override
	public void onCompletionListener() {
		if (null == mMediaPlayerDelegate || error)
			return;
		if (!pluginEnable) {
			return;
		}
		Logger.e("interactplugin", "playComplete");
		((Activity) mActivity).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (null != videoSeekBar)
					videoSeekBar.setProgress(0);

				hideLoading();
				if (null != mMediaPlayerDelegate) {
					// mMediaPlayerDelegate.onEnd();
					// mMediaPlayerDelegate.setOrientionDisable();
				}
				disableController();
				playComplete();
			}
		});
		// if (null != playHandler) {
		// playHandler.removeCallbacksAndMessages(null);
		// playHandler.sendEmptyMessageDelayed(MSG_COMPLETE, 200);
		// }

	}

	private boolean error;

	@Override
	public boolean onErrorListener(int what, int extra) {
		Logger.e(TAG, "播放错误 onErrorListener-->" + what);

		// mMediaPlayerDelegate.mediaState = STATE.ERROR;
		error = true;
		if (null != mActivity && mActivity.isFinishing()) {
			return true;
		}
		mMediaPlayerDelegate.release();

		if (null != mMediaPlayerDelegate) {
			Logger.e(TAG, "播放错误 onErrorListener--> #0");
			if (mMediaPlayerDelegate.isFullScreen) {
				showAlert();
				return false;
			}

			Logger.e(TAG, "播放错误 onErrorListener--> #1");
			mMediaPlayerDelegate.isStartPlay = false;
			if (mMediaPlayerDelegate.isADShowing) {
				showAlert();
				return true;
			}
			if (what == MPPErrorCode.MEDIA_INFO_DATA_SOURCE_ERROR) {
				showAlert();
				return true;
			}
			if (what == MPPErrorCode.MEDIA_INFO_PLAYERROR) {
				showAlert();
				return true;
			}
			if (what == MPPErrorCode.MEDIA_INFO_SEEK_ERROR) {
				showAlert();
				return true;
			}
			if (what == MPPErrorCode.MEDIA_INFO_PREPARE_TIMEOUT_ERROR) {
				// Util.showTips(HttpRequestManager.STATE_ERROR_TIMEOUT);
				showAlert();
				return true;
			}
			if (what == MPPErrorCode.MEDIA_INFO_SEEK_ERROR
					&& mMediaPlayerDelegate.currentOriention == Orientation.VERTICAL) {
				playComplete();
				return true;
			}
			if (null != mMediaPlayerDelegate.videoInfo
					&& StaticsUtil.PLAY_TYPE_LOCAL
							.equals(mMediaPlayerDelegate.videoInfo.playType)) {
				if (what == MPPErrorCode.MEDIA_INFO_NETWORK_DISSCONNECTED) {
					playComplete();
				} else if (what == MPPErrorCode.MEDIA_INFO_DATA_SOURCE_ERROR) {
					PlayerUtil.showTips("本地文件已损坏");
					Track.onError(mActivity,
							mMediaPlayerDelegate.videoInfo.getVid(),
							Profile.GUID,
							mMediaPlayerDelegate.videoInfo.playType,
							PlayCode.VIDEO_NOT_EXIST,
							mMediaPlayerDelegate.videoInfo.mSource,
							mMediaPlayerDelegate.videoInfo.getCurrentQuality(),
							mMediaPlayerDelegate.videoInfo.getProgress(),
							mMediaPlayerDelegate.isFullScreen);
				} else if (what == MPPErrorCode.MEDIA_INFO_PREPARE_ERROR) {
					// Util.showTips("播放器内部出错");
					mMediaPlayerDelegate.finishActivity();
				} else if (what == MPPErrorCode.MEDIA_INFO_NETWORK_ERROR) {
					playComplete();
					return true;
				} else if (what == MPPErrorCode.MEDIA_INFO_SEEK_ERROR) {
					playComplete();
					return true;
				} else {
					// Util.showTips("本地文件已损坏");
				}
				mMediaPlayerDelegate.setFirstUnloaded();
				mMediaPlayerDelegate.release();
				mMediaPlayerDelegate.finishActivity();
				return true;
			}
			if (null != mMediaPlayerDelegate.videoInfo
					&& StaticsUtil.PLAY_TYPE_NET
							.equals(mMediaPlayerDelegate.videoInfo.playType)) {
				if (what == MPPErrorCode.MEDIA_INFO_NETWORK_DISSCONNECTED) {
					// if (Util.hasInternet())
					PlayerUtil.showTips(R.string.tips_not_responding);
				} else if (what == MPPErrorCode.MEDIA_INFO_DATA_SOURCE_ERROR) {
					// if (Util.hasInternet())
					PlayerUtil.showTips(R.string.tips_not_responding);
				} else if (what == MPPErrorCode.MEDIA_INFO_PREPARE_TIMEOUT_ERROR) {
					// if (Util.hasInternet())
					PlayerUtil.showTips(R.string.tips_not_responding);
				}
			}
		}
		showAlert();

		return true;
	}

	public void showAlert() {
		Logger.e(TAG, "showAlert()--> #0");
		if (null != mMediaPlayerDelegate
				&& null != mMediaPlayerDelegate.videoInfo
				&& mMediaPlayerDelegate.videoInfo.getPlayType() == StaticsUtil.PLAY_TYPE_LOCAL) {
			Logger.e(TAG, "showAlert()--> #1");
			PlayerUtil.showTips(R.string.player_error_native);
			alertRetry(mActivity, R.string.player_error_native);
		} else {
			alertRetry(mActivity, R.string.Player_error_timeout);
		}
	}

	public void alertRetry(final Activity c, final int msgId) {
		if (c.isFinishing())
			return;

		c.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				hideLoading();
				hideLoadinfo();
				if (null != mMediaPlayerDelegate)
					mMediaPlayerDelegate.release();
				((Activity) mActivity).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						disableController();
						showRetryLayout();
					}
				});
				if (null != mMediaPlayerDelegate
						&& !mMediaPlayerDelegate.isFullScreen) {
					mMediaPlayerDelegate.isStartPlay = false;
					if (null != mMediaPlayerDelegate.videoInfo
							&& Orientation.VERTICAL
									.equals(mMediaPlayerDelegate.currentOriention))
						mMediaPlayerDelegate.onVVEnd();
				}
			}
		});
	}

	@Override
	public void OnPreparedListener() {
		Logger.e(TAG, " OnPreparedListener()");
		seekcount = 0;
		if (null != retryView)
			retryView.setVisibility(View.GONE);
	}

	@Override
	public void OnSeekCompleteListener() {
		isLoading = false;
		if (videoSeekBar != null) {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					videoSeekBar.setEnabled(true);
				}
			});
		}
	}

	@Override
	public void OnVideoSizeChangedListener(int width, int height) {

	}

	@Override
	public void OnTimeoutListener() {
		if (null == mActivity)
			return;
		((Activity) mActivity).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				disableController();
				showRetryLayout();
			}
		});
		Logger.e(TAG, " OnTimeoutListener()");
		// if (null != playHandler) {
		// playHandler.removeCallbacksAndMessages(null);
		// playHandler.sendEmptyMessage(MSG_TIME_OUT);
		// }

	}

	private boolean firstLoaded = false;
	private boolean isLoading = false;

	@Override
	public void onLoadedListener() {

		Logger.e(TAG, " onLoadedListener()");
		// if (mMediaPlayerDelegate.mediaState == STATE.COMPLETE
		// || mMediaPlayerDelegate.mediaState == STATE.ERROR)
		// return;
		isLoading = false;
		if (null == mMediaPlayerDelegate)
			return;
		if (mMediaPlayerDelegate.isComplete)
			return;
		if (!firstLoaded) {
			// 这个在onrealVideoStart的时候seek过一次了
			// mMediaPlayerDelegate.seekToHistory();
			firstLoaded = true;
		}
		error = false;
		if (null != seekHandler)
			seekHandler.removeCallbacksAndMessages(null);
		if (null != seekendHandler)
			seekendHandler.sendEmptyMessage(0);
		if (null != mActivity) {
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoading();
					hideLoadinfo();
					hideRetryLayout();
				}
			});
		}
		if (null != mActivity && ((Activity) mActivity).isFinishing()) {
			hideLoading();
			hideRetryLayout();
		}
		/*
		 * if (null != playHandler) {
		 * playHandler.removeCallbacksAndMessages(null);
		 * playHandler.sendEmptyMessageDelayed(MSG_LOADED, 500); }
		 */

	}

	/** 显示重试 onerror/timeout/get play data failed */
	private void showRetryLayout() {
		hideEndPage();
		if (null != mActivity) {
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != retryView)
						retryView.setVisibility(View.VISIBLE);
				}
			});
		}

	}

	/** 隐藏重试 */
	private void hideRetryLayout() {
		if (null != mActivity) {
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (null != retryView)
						retryView.setVisibility(View.GONE);
				}
			});
		}

	}

	@Override
	public void onLoadingListener() {
		Logger.e(TAG, "onLoadingListener");
		isLoading = true;
		if (error) {
			Logger.e(TAG, "null == error ");
			return;
		}

		if (null == mMediaPlayerDelegate || mMediaPlayerDelegate.isComplete
				|| mMediaPlayerDelegate.isReleased) {
			Logger.e(TAG, "null == mMediaPlayerDelegate ");
			return;
		}
		if (!autoPlay) {
			Logger.e(TAG, "!autoPlay");
			return;
		}

		if (null != mActivity) {
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideEndPage();
					showLoading();
					hideRetryLayout();
				}
			});
		}

	}

	/**
	 * replay/retry/playnext
	 * */
	private void restartFromComplete() {
		if (null != mMediaPlayerDelegate) {
			mMediaPlayerDelegate.clearEnd();
			mMediaPlayerDelegate.setOrientionDisable();
		}
	}

	protected void playComplete() {
		Logger.e(TAG, "playComplete()");
		if (youkuPlayerListener != null) {
			youkuPlayerListener.onCompletion();
		}
		if (null == mMediaPlayerDelegate)
			return;
		if (!pluginEnable) {
			return;
		}
		clearPlayState();
		if (null != mMediaPlayerDelegate) {
			// mMediaPlayerDelegate.onEnd();
			// mMediaPlayerDelegate.setOrientionDisable();
		}
		if (Profile.from == Profile.PHONE_BROWSER
				|| Profile.from == Profile.PAD_BROWSER) {
			mMediaPlayerDelegate.finishActivity();
			return;
		}
		mMediaPlayerDelegate.isStartPlay = false;
		Track.setplayCompleted(true);
		mMediaPlayerDelegate.isComplete = true;

		if (null != mMediaPlayerDelegate.videoInfo
				&& mMediaPlayerDelegate.videoInfo.getPlayType() != StaticsUtil.PLAY_TYPE_LOCAL) {
			goEndPage();
		} else {
			mMediaPlayerDelegate.finishActivity();
		}
	}

	// private static final int MSG_LOADING = 20131;
	// private static final int MSG_LOADED = 20132;
	// private static final int MSG_BUFFER_UPDATE = 20133;
	// private static final int MSG_ERROR = 20134;
	// private static final int MSG_TIME_OUT = 20135;
	// private static final int MSG_COMPLETE = 20136;
	// private static final int MSG_INFO_GETTING = 20137;
	private static final int MSG_INFO_GETTED = 20138;
	private static final int MSG_INFO_FAILED = 20139;
	// 处理播放相关消息
	private Handler playHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * case MSG_LOADING: { if (!autoPlay && null != mMediaPlayerDelegate
			 * && !mMediaPlayerDelegate.changeAutoPlay) return; ((Activity)
			 * mActivity).runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() { showLoading(); } });
			 * 
			 * break; }
			 */

			/*
			 * case MSG_LOADED: { ((Activity) mActivity).runOnUiThread(new
			 * Runnable() {
			 * 
			 * @Override public void run() { hideLoading(); enableController();
			 * hideRetryLayout(); if (count == 0) if (null !=
			 * mMediaPlayerDelegate && null != mMediaPlayerDelegate.videoInfo) {
			 * videoBar.setMax(mMediaPlayerDelegate.videoInfo
			 * .getDurationMills()); count++; if (null != playTitleTextView)
			 * playTitleTextView .setText(mMediaPlayerDelegate.videoInfo
			 * .getTitle()); } } });
			 * 
			 * break; }
			 */
			// case MSG_BUFFER_UPDATE: {
			// break;
			// }
			/*
			 * case MSG_ERROR: { ((Activity) mActivity).runOnUiThread(new
			 * Runnable() {
			 * 
			 * @Override public void run() { disableController();
			 * showRetryLayout(); } }); break; }
			 */
			/*
			 * case MSG_TIME_OUT: { ((Activity) mActivity).runOnUiThread(new
			 * Runnable() {
			 * 
			 * @Override public void run() { disableController();
			 * showRetryLayout(); } }); break; }
			 */
			/*
			 * case MSG_COMPLETE: ((Activity) mActivity).runOnUiThread(new
			 * Runnable() {
			 * 
			 * @Override public void run() { if (null != videoBar)
			 * videoBar.setProgress(0); hideLoading(); playComplete(); if (null
			 * != mMediaPlayerDelegate) mMediaPlayerDelegate.onEnd();
			 * disableController(); } }); break;
			 */
			// case MSG_INFO_GETTING: {
			// break;
			// }
			// case MSG_INFO_GETTED: {
			// int i = 0;
			//
			// break;
			// }
			// case MSG_INFO_FAILED: {
			//
			// break;
			// }
			default:
				break;
			}
		}

	};

	// public void hidePlayLoading() {
	// if (null != playLoading)
	// playLoading.setVisibility(View.GONE);
	// }

	/**
	 * 播放下一集
	 */
	private void playNextVideo() {
		restartFromComplete();
		clearPlayState();
		firstLoaded = false;
		if (null == mMediaPlayerDelegate
				|| null == mMediaPlayerDelegate.videoInfo)
			return;
		if (!Util.hasInternet()) {
			playLocalNext();
			return;
		}

		// 本地播放询问是否在线
		/*
		 * if ( StaticsUtil.PLAY_TYPE_LOCAL
		 * .equals(mMediaPlayerDelegate.videoInfo.getPlayType())) { IDownload
		 * download = YoukuService.getService(IDownload.class); DownloadInfo
		 * info = download.getDownloadInfo(
		 * mMediaPlayerDelegate.videoInfo.getShowId(),
		 * mMediaPlayerDelegate.videoInfo.getShow_videoseq() + 1); if (info !=
		 * null) { mMediaPlayerDelegate.playVideo(info.videoid,
		 * StaticsUtil.PLAY_TYPE_LOCAL .equals(mMediaPlayerDelegate.videoInfo
		 * .getPlayType())); return; } if (info == null) {
		 * Util.showTips(R.string.download_no_network); //
		 * mMediaPlayerDelegate.finishActivity(); return; }
		 * mMediaPlayerDelegate.playVideo(info.videoid); return; }
		 */

		if (mMediaPlayerDelegate.videoInfo.getHaveNext() == 0) {
			goEndPage();
			return;
		}
		mMediaPlayerDelegate
				.playVideo(mMediaPlayerDelegate.videoInfo.nextVideoId);
	}

	/**
	 * 播放本地下一集
	 */
	private void playLocalNext() {
		if (null == mMediaPlayerDelegate
				|| null == mMediaPlayerDelegate.videoInfo)
			return;
		ICacheInfo download = IMediaPlayerDelegate.mICacheInfo;
		VideoCacheInfo info = download
				.getNextDownloadInfo(mMediaPlayerDelegate.videoInfo.getVid());
		if (info == null) {
			mMediaPlayerDelegate.finishActivity();
			return;
		}
		firstLoaded = false;
		mMediaPlayerDelegate.playVideo(info.videoid,
				StaticsUtil.PLAY_TYPE_LOCAL
						.equals(mMediaPlayerDelegate.videoInfo.getPlayType()));
	}

	// 只有重播页
	private void goReplayPage() {
		Logger.e(TAG, "goReplayPage");
		firstLoaded = false;
		if (null != mActivity)
			((Activity) mActivity).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (null != interactFrameLayout && null != endPageView) {
						interactFrameLayout.removeView(endPageView);
						interactFrameLayout.addView(endPageView);
						LinearLayout nextLayout = (LinearLayout) endPageView
								.findViewById(R.id.ll_next_play);
						if (null != nextLayout)
							nextLayout.setVisibility(View.GONE);
					}

				}
			});

	}

	private void hideEndPage() {
		if (null != mActivity)
			((Activity) mActivity).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (null != interactFrameLayout && null != endPageView) {
						interactFrameLayout.removeView(endPageView);
						LinearLayout nextLayout = (LinearLayout) endPageView
								.findViewById(R.id.ll_next_play);
						if (null != nextLayout)
							nextLayout.setVisibility(View.VISIBLE);
					}

				}
			});

	}

	// 重播+下一集
	private void goReplayNextPage() {
		firstLoaded = false;
		if (null != mActivity)
			((Activity) mActivity).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (null != interactFrameLayout && null != endPageView) {
						interactFrameLayout.removeView(endPageView);
						interactFrameLayout.addView(endPageView);
					}
				}
			});

	}

	/**
	 * 去播放结束页
	 */
	private void goEndPage() {
		firstLoaded = false;
		hideLoadinfo();
		hideLoading();
		hideRetryLayout();
		if (null != mMediaPlayerDelegate
				&& null != mMediaPlayerDelegate.videoInfo) {
			mMediaPlayerDelegate.release();
			mMediaPlayerDelegate.setFirstUnloaded();
			if (mMediaPlayerDelegate.videoInfo.getHaveNext() == 1) {
				goReplayNextPage();
			} else {
				goReplayPage();
			}
		}
	}

	protected void onCurrentPostionUpdate(int currentPostion) {
		// Logger.e(tag, "onCurrentPostionUpdate"+currentPostion);
		enableController();
		if (null != userPlayButton)
			userPlayButton.setVisibility(View.GONE);
		if (null == mMediaPlayerDelegate
				|| mMediaPlayerDelegate.videoInfo == null
				|| mMediaPlayerDelegate.isADShowing
				|| mMediaPlayerDelegate.isReleased)
			return;
		if (Profile.isSkipHeadAndTail() && !mMediaPlayerDelegate.isFullScreen) {
			if (mMediaPlayerDelegate.videoInfo.isHasHead()) {
				int headPosition = mMediaPlayerDelegate.videoInfo
						.getHeadPosition();
				if (currentPostion < headPosition - 15000) {
					// Util.showTips("为您跳过片头");
					if (null != videoSeekBar)
						videoSeekBar.setProgress(headPosition);
					mMediaPlayerDelegate.videoInfo.setProgress(headPosition);
					mMediaPlayerDelegate.seekTo(headPosition);
					return;
				}
			}
			if (mMediaPlayerDelegate.videoInfo.isHasTail()) {
				int tailPosition = mMediaPlayerDelegate.videoInfo
						.getTailPosition();
				if ((tailPosition - currentPostion) <= 2000) {
					// Util.showTips("为您跳过片尾");
					mMediaPlayerDelegate.videoInfo
							.setProgress(tailPosition - 5000);
					playComplete();
					return;
				}
			}
		}
		currentTime.setText(PlayerUtil.getFormatTime(currentPostion));
		// mMediaPlayerDelegate.mediaState = STATE.PLAYING;
		if (null != videoSeekBar)
			videoSeekBar.setProgress(currentPostion);
		mMediaPlayerDelegate.videoInfo.setProgress(currentPostion);
	}

	@Override
	public void OnCurrentPositionChangeListener(int currentPosition) {
		if (null != videoSeekBar) {
			videoSeekBar.setProgress(currentPosition);
			hotPointView.setProgress(currentPosition);
		}
		/* 片头片尾 */
		onCurrentPostionUpdate(currentPosition);

	}

	private Drawable playDrawable;

	public void setPlayImg(Drawable mDrawable) {
		playDrawable = mDrawable;

	}

	@Override
	public void newVideo() {

	}

	@Override
	public void onVolumnUp() {
		if (null != mActivity) {
			AudioManager audioMa = (AudioManager) mActivity
					.getSystemService(Context.AUDIO_SERVICE);
			if (null != mMediaPlayerDelegate
					&& mMediaPlayerDelegate.isFullScreen) {
				audioMa.adjustVolume(AudioManager.ADJUST_SAME,
						AudioManager.FLAG_PLAY_SOUND);
			} else {
				audioMa.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_SAME,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
			}
		}
	}

	@Override
	public void onVolumnDown() {
		if (null != mActivity) {
			AudioManager audioMa = (AudioManager) mActivity
					.getSystemService(mActivity.AUDIO_SERVICE);
			if (null != mMediaPlayerDelegate
					&& mMediaPlayerDelegate.isFullScreen) {
				audioMa.adjustVolume(AudioManager.ADJUST_SAME,
						AudioManager.FLAG_PLAY_SOUND);
			} else {
				audioMa.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_SAME,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
			}
		}
	}

	@Override
	public void onMute(boolean mute) {

	}

	@Override
	public void onVideoChange() {
		firstLoaded = false;
	}

	/**
	 * 设置viwe是否可点击
	 * 
	 * @param state
	 * @param view
	 */
	private void setClickable(boolean state, View view) {
		if (null == view)
			return;
		view.setClickable(state);
	}

	/**
	 * 禁用控制并隐藏
	 */
	private void disableController() {
		if (null != controlLayout)
			controlLayout.setVisibility(View.GONE);
		setClickable(false, playPauseBtn);
		setClickable(false, videoSeekBar);
		setClickable(false, mContainerLayout);
		setClickable(false, fullSmallBtn);
		setClickable(false, videoSeekBar);
		hideTitle();
	}

	/**
	 * 禁用控制
	 */
	private void disableControllerHide() {
		setClickable(false, playPauseBtn);
		setClickable(false, videoSeekBar);
		setClickable(false, mContainerLayout);
		setClickable(false, fullSmallBtn);
		setClickable(false, videoSeekBar);
	}

	/**
	 * 启用控制
	 */
	private void enableController() {
		setClickable(true, playPauseBtn);
		setClickable(true, videoSeekBar);
		setClickable(true, mContainerLayout);
		setClickable(true, fullSmallBtn);
		setClickable(true, videoSeekBar);
	}

	@Override
	public void onVideoInfoGetting() {

		hideRetryLayout();

		playANewVideo();
		Logger.e("interactplugin", "onVideoInfoGetting");
		initSeekLoading();
		// showLoadinfo();
		showLoadinfo();
		disableController();
	}

	private void playANewVideo() {
		firstLoaded = false;
		if (null != userPlayButton)
			userPlayButton.setVisibility(View.GONE);
		clearPlayState();
		hideEndPage();
		disableController();
		restartFromComplete();
	}

	@Override
	public void onVideoInfoGetted() {
		firstLoaded = false;
		Logger.e("interactplugin", "onVideoInfoGetted");
		hideLoadinfo();
		showLoading();
		infoFail = false;
		disableController();
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (null != mMediaPlayerDelegate
						&& null != mMediaPlayerDelegate.videoInfo) {
					if (null != videoSeekBar) {
						int duration = mMediaPlayerDelegate.videoInfo
								.getDurationMills();
						videoSeekBar.setMax(duration);
						totalTime.setText(PlayerUtil.getFormatTime(duration));
					}
					if (null != titleTextView)
						titleTextView.setText(mMediaPlayerDelegate.videoInfo
								.getTitle());
					if (mMediaPlayerDelegate != null) {
						if (mMediaPlayerDelegate.noband) {
							titleLogo.setVisibility(GONE);
						}
					}
				}
			}
		});
	}

	@Override
	public void onVideoInfoGetFail(boolean needRetry) {
		Logger.e("interactplugin", "onVideoInfoGetFail");
		infoFail = true;

		if (null != mActivity)
			((Activity) mActivity).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showRetryLayout();
					hideLoadinfo();
				}
			});
		// if (null != playHandler)
		// playHandler.sendEmptyMessage(MSG_INFO_FAILED);

	}

	private boolean infoFail = false;

	@Override
	public void setVisible(boolean visible) {
		if (null == containerView)
			return;
		if (visible) {
			containerView.setVisibility(View.VISIBLE);
		} else {
			containerView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNotifyChangeVideoQuality() {
	}

	boolean isBack = false;

	public void back() {
		if (firstLoaded) {
			isBack = true;
			showTitle();
			showControl();
			userAction();
		}
	}

	public void onPluginAdded() {
		super.onPluginAdded();
		Logger.e(TAG, "onPluginAdded()");
		if (null != mMediaPlayerDelegate
				&& !mMediaPlayerDelegate.onChangeOrient) {
			Logger.e(TAG, "onChangeOrient()");
			mMediaPlayerDelegate.onChangeOrient = false;
		} else {
			Logger.e(TAG, "black.setBackgroundDrawable(null)()");
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateView();

	}

	private void updateView() {
		changePlayPause();
		if (mMediaPlayerDelegate.isFullScreen) {
			ViewGroup.LayoutParams titleLayoutParams = titleLayout
					.getLayoutParams();
			titleLayoutParams.height = Util.dip2px(44);
			titleLayout.setLayoutParams(titleLayoutParams);
			titleLayout.setBackgroundColor(Color.BLACK);
			// 返回按钮
			titleBack.setImageResource(R.drawable.full_icon_back);// 返回按钮图片
			int backPadPx = Util.dip2px(15);
			titleBack.setPadding(backPadPx, 0, backPadPx, 0);// 返回按钮间距
			// 标题栏
			titleTextView.setTextColor(Color.parseColor("#999999"));// 标题栏颜色
			// 清晰度按钮
			titleQualityBtn.setVisibility(View.VISIBLE);// 全屏可见
			fullSmallBtn.setImageResource(R.drawable.full_icon_out);
			// 标题logo
			RelativeLayout.LayoutParams logoParams = (RelativeLayout.LayoutParams) titleLogo
					.getLayoutParams();
			logoParams.leftMargin = Util.dip2px(25);
			logoParams.rightMargin = Util.dip2px(15);
			titleLogo.setLayoutParams(logoParams); // 标题logo间距
			// 播放暂停按钮
			RelativeLayout.LayoutParams playPauseParams = (android.widget.RelativeLayout.LayoutParams) playPauseBtn
					.getLayoutParams();
			playPauseParams.leftMargin = Util.dip2px(24);
			playPauseParams.leftMargin = Util.dip2px(28);
			playPauseBtn.setLayoutParams(playPauseParams);// 间距
			// 进度条
			RelativeLayout.LayoutParams seekbarParams = (android.widget.RelativeLayout.LayoutParams) videoSeekBar
					.getLayoutParams();
			seekbarParams.leftMargin = Util.dip2px(17);
			seekbarParams.rightMargin = Util.dip2px(17);
			videoSeekBar.setLayoutParams(seekbarParams);// 间距
			// 全屏半屏切换按钮
			RelativeLayout.LayoutParams fullSmallParams = (android.widget.RelativeLayout.LayoutParams) fullSmallBtn
					.getLayoutParams();
//			fullSmallParams.leftMargin = Util.dip2px(17);
//			fullSmallParams.rightMargin = Util.dip2px(20);
			fullSmallBtn.setLayoutParams(fullSmallParams);// 间距
			fullSmallBtn.setImageResource(R.drawable.full_icon_out);
		} else {
			ViewGroup.LayoutParams titleLayoutParams = titleLayout
					.getLayoutParams();
			titleLayoutParams.height = Util.dip2px(30);
			titleLayout.setLayoutParams(titleLayoutParams);
			titleLayout.setBackgroundResource(R.drawable.play_title_bkg);
			// 返回按钮
			titleBack.setImageResource(R.drawable.vertical_icon_back);// 返回按钮图片
			int backPadPx = Util.dip2px(14);
			titleBack.setPadding(backPadPx, 0, backPadPx, 0); // 返回按钮间距
			// 标题栏
			titleTextView.setTextColor(Color.parseColor("#FFFFFF"));// 标题栏颜色
			// 清晰度按钮
			titleQualityBtn.setVisibility(View.GONE);// 小屏时不可见
			fullSmallBtn.setImageResource(R.drawable.icon_fullscreen);
			// 标题logo
			RelativeLayout.LayoutParams logoParams = (RelativeLayout.LayoutParams) titleLogo
					.getLayoutParams();
			logoParams.leftMargin = Util.dip2px(15);
			logoParams.rightMargin = Util.dip2px(10);
			titleLogo.setLayoutParams(logoParams); // 标题logo间距
			// 播放暂停按钮
			RelativeLayout.LayoutParams playPauseParams = (android.widget.RelativeLayout.LayoutParams) playPauseBtn
					.getLayoutParams();
			playPauseParams.leftMargin = Util.dip2px(20);
			playPauseParams.leftMargin = Util.dip2px(23);
			playPauseBtn.setLayoutParams(playPauseParams);// 间距
			// 进度条
			RelativeLayout.LayoutParams seekbarParams = (android.widget.RelativeLayout.LayoutParams) videoSeekBar
					.getLayoutParams();
			seekbarParams.leftMargin = Util.dip2px(6);
			seekbarParams.rightMargin = Util.dip2px(6);
			videoSeekBar.setLayoutParams(seekbarParams);// 间距

			// 全屏半屏切换按钮
			RelativeLayout.LayoutParams fullSmallParams = (android.widget.RelativeLayout.LayoutParams) fullSmallBtn
					.getLayoutParams();
			fullSmallParams.leftMargin = Util.dip2px(15);
			fullSmallParams.rightMargin = Util.dip2px(18);
			fullSmallBtn.setLayoutParams(fullSmallParams);// 间距
			fullSmallBtn.setImageResource(R.drawable.icon_fullscreen);
		}
	}

	public void set3GTips() {
		// if (null != seekLoadingContainerView)
		// seekLoadingContainerView.setVisibility(View.VISIBLE);
		// if (null != loadingTips) {
		// loadingTips.setVisibility(View.VISIBLE);
		// loadingTips.setText(getResources().getString(
		// R.string.detail_3g_tips));
		// }
	}

	private void changeVideoq(VideoQuality quality) {
		try {
			// 通过ApiManager实例更改清晰度设置，返回值（1):成功；（0): 不支持此清晰度
			// 接口详细信息可以参数使用文档
			int result = ApiManager.getInstance().changeVideoQuality(quality,
					mBasePlayerManager);

			if (result == 0) {
				Toast.makeText(mActivity, "不支持此清晰度", 2000).show();
			} else {

			}

		} catch (Exception e) {
			Toast.makeText(mActivity, e.getMessage(), 2000).show();
		}
	}

	protected void startCache() {
		if (IMediaPlayerDelegate.mIUserInfo == null
				|| !IMediaPlayerDelegate.mIUserInfo.isLogin()) {
			// Util.showToast("请先登录");
			return;
		}
	}

	@Override
	public void onRealVideoStart() {
		isLoading = false;
		enableController();
		int quality = Profile.videoQuality;
		boolean b = mMediaPlayerDelegate.videoInfo.playType
				.equals(StaticsUtil.PLAY_TYPE_LOCAL);
		if (b) {
			// titleQualityBtn.setClickable(false);

			String vvid = mMediaPlayerDelegate.videoInfo.getVid();
			DownInfo dinfo = DownLoadManager.getInstance().getDownInfoByVid(
					vvid);
			if (ValidateUtil.isValid(dinfo)) {
				quality = dinfo.getVideoQuality();
			}
		}
		if (quality == (VideoQuality.STANDARD.ordinal())) {
			titleQualityBtn.setText("标清");
		}
		if (quality == (VideoQuality.HIGHT.ordinal())) {
			titleQualityBtn.setText("高清");
		}
		if (quality == (VideoQuality.SUPER.ordinal())) {
			titleQualityBtn.setText("超清");
		}

		hotPointView.setMax(mMediaPlayerDelegate.videoInfo.getDurationMills());
		hotPointView.setPoints(mMediaPlayerDelegate.videoInfo.getPoints());
	}

	@Override
	public void onUp() {

	}

	@Override
	public void onDown() {

	}

	@Override
	public void onFavor() {

	}

	@Override
	public void onUnFavor() {

	}

	@Override
	public void onADplaying() {

	}

	@Override
	public void onRealVideoStarted() {

	}

	@Override
	public void onStart() {

	}

	@Override
	public void onClearUpDownFav() {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onPlayNoRightVideo(GoplayException e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayReleateNoRightVideo() {
		// TODO Auto-generated method stub

	}
}