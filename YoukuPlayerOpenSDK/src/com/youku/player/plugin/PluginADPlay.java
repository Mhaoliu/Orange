package com.youku.player.plugin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baseproject.utils.Logger;
import com.baseproject.utils.UIUtils;
import com.baseproject.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.youku.player.base.GoplayException;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.goplay.AdvInfo;
import com.youku.player.goplay.VideoAdvInfo;
import com.youku.player.ui.R;
import com.youku.player.ui.interf.IMediaPlayerDelegate;
import com.youku.player.util.DetailMessage;
import com.youku.player.util.DisposableStatsUtils;

public class PluginADPlay extends PluginOverlay implements DetailMessage {
	protected String TAG = PluginADPlay.class.getSimpleName();

	private YoukuBasePlayerManager mBasePlayerManager;
	private Activity mActivity;
	private IMediaPlayerDelegate mediaPlayerDelegate;

	private View containerView;// 整个视图
	private boolean ispause=false;
	/**
	 * 前贴广告视图部分
	 */
	private RelativeLayout mFrontAdView; // 前贴广告整个view。用于处理点击事件、
	// 上部的控制按钮
	private View mAdTopView;//顶部的控制条
	private ImageView backImgView;// 返回按钮。全屏时不显示，半屏时显示。

	private ImageView volumeImgView;// 音量按钮。
	private AudioManager mAudioManager;
	private boolean isVolumeOn = true;
	private int currentVolume;
	
	private View mCountWrapView;//整个倒计时view
	private TextView mCountUpdateTextView;// 倒计时显示
	private TextView mVipNoAdBtn;// 会员免广告按钮
	// 下部的控制按钮
	private View mAdvBottomView;//底部控制布局
	private View mAdDetailBtn;// 广告详情按钮
	private ImageView mSmallFullImgView;// 全屏、半屏切换按钮

	/**
	 * 暂停广告视图部分
	 */
	private View imgAdvLayout;// 整个图片广告布局
	private ImageView imgAdvContentImgView;// 图片内容展示
	private ImageView imgAdvCloseImgView;// 右上角的关闭广告


	public PluginADPlay(YoukuBasePlayerManager basePlayerManager,
			IMediaPlayerDelegate mediaPlayerDelegate) {
		super(basePlayerManager.getBaseActivity(), mediaPlayerDelegate);

		this.mediaPlayerDelegate = mediaPlayerDelegate;
		this.mBasePlayerManager = basePlayerManager;
		this.mActivity = mBasePlayerManager.getBaseActivity();

		init(mActivity);
	}

	private void init(Context context) {

		LayoutInflater mLayoutInflater = LayoutInflater.from(mActivity);
		containerView = mLayoutInflater.inflate(R.layout.yp_player_ad_youku,
				null);
		addView(containerView);

		//顶部控制条
		mAdTopView = containerView.findViewById(R.id.play_controller_header);
		
		// 返回按钮
		backImgView = (ImageView) containerView.findViewById(R.id.header_back);
		backImgView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 关闭当前页面
				if (mActivity != null) {
					mActivity.finish();
				}
			}
		});
		// 音量按钮
		volumeImgView = (ImageView) containerView
				.findViewById(R.id.my_ad_volume);
		mAudioManager = (AudioManager) getContext().getSystemService(
				Context.AUDIO_SERVICE);
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		if (isVolumeOn) {
			volumeImgView.setImageResource(R.drawable.ad_icon_volume);
		} else {
			volumeImgView.setImageResource(R.drawable.ad_icon_volume_off);
		}
		volumeImgView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isVolumeOn) {
					// 变为静音
					currentVolume = mAudioManager
							.getStreamVolume(AudioManager.STREAM_MUSIC);
					isVolumeOn = false;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
							0);
					volumeImgView
							.setImageResource(R.drawable.ad_icon_volume_off);
				} else {
					// 开启声音
					isVolumeOn = true;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
					volumeImgView.setImageResource(R.drawable.ad_icon_volume);
				}
			}
		});

		//整个倒计时view
		mCountWrapView = containerView.findViewById(R.id.my_ad_count_wrap);
		// 广告倒计时
		mCountUpdateTextView = (TextView) containerView
				.findViewById(R.id.my_ad_count);

		// 会员免广告
		mVipNoAdBtn = (TextView) containerView.findViewById(R.id.my_ad_remove);
		mCountWrapView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mediaPlayerDelegate.pauseForVideoAdv();
				ispause=true;
				// 会员免广告
				showInBrowser("http://cloud.youku.com/services/info?serid=1", new OnShowAdvInBrowserResult() {
					@Override
					public void onFail() {
						// 继续播放
						if (!mMediaPlayerDelegate.isAdvShowFinished()) {
							startPlay();
						}
					}

					@Override
					public void onSuccess() {

					}
				});
			}
		});
		// 前贴广告页面点击处理
		mFrontAdView = (RelativeLayout) containerView
				.findViewById(R.id.ad_page_holder);
		mFrontAdView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showFrontAdvDetail();
			}
		});
		
		mAdvBottomView = containerView.findViewById(R.id.my_ad_bottom);
		
		// 广告详情展示
		mAdDetailBtn = containerView.findViewById(R.id.ad_more);
		mAdDetailBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showFrontAdvDetail();
			}
		});
		// 全屏/半屏按钮
		mSmallFullImgView = (ImageView) containerView
				.findViewById(R.id.gofullscreen);
		mSmallFullImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayerDelegate.isFullScreen) {
					mBasePlayerManager.goSmall();
					mSmallFullImgView
							.setImageResource(R.drawable.ad_icon_fullscreen);
					// 半屏显示返回按钮
					backImgView.setVisibility(View.VISIBLE);
				} else {
					mBasePlayerManager.goFullScreen();
					mSmallFullImgView.setImageResource(R.drawable.ad_icon_out);
					// 全屏不显示返回按钮
					backImgView.setVisibility(View.INVISIBLE);
				}
			}
		});

		initPauseAdvView();
		mBasePlayerManager
				.addOnSurfaceStatusChangeListener(new OnSurfaceStatusChangeListener() {

					@Override
					public void onSurfaceCreated() {
						Logger.d(
								TAG,
								"ad onSurfaceCreated() isAdvShowFinished = "
										+ mMediaPlayerDelegate
												.isAdvShowFinished());
						if (!mMediaPlayerDelegate.isAdvShowFinished()) {
							// 如果没有显示选择对话框，就继续播放。
							if (!onChooserDlgShow) {
								startPlay();
							}
						}
					}

				});

	}

	private void showFrontAdvDetail() {
		final AdvInfo curAdvInfo = mediaPlayerDelegate.videoInfo.videoAdvInfo.VAL
				.get(0);
		String clickUrl = curAdvInfo.CU;

		mediaPlayerDelegate.pauseForVideoAdv();
		ispause=true;

		showInBrowser(clickUrl, new OnShowAdvInBrowserResult() {

			@Override
			public void onFail() {
				// 继续播放
				if (!mMediaPlayerDelegate.isAdvShowFinished()) {
					startPlay();
				}
			}

			@Override
			public void onSuccess() {
				// 发送统计
				DisposableStatsUtils.disposeCUM(curAdvInfo);
			}
		});// 展示详情
	}

	private void initPauseAdvView() {
		imgAdvLayout = findViewById(R.id.img_ad_layout);
		imgAdvContentImgView = (ImageView) findViewById(R.id.adv_img_imgview);
		imgAdvCloseImgView = (ImageView) findViewById(R.id.adv_img_close_imgview);

		imgAdvLayout.setVisibility(View.INVISIBLE);
		imgAdvContentImgView.setImageResource(R.drawable.nonedrawable);

		// 广告点击跳转
		imgAdvContentImgView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				final VideoAdvInfo pauseAdvInfo = mediaPlayerDelegate
						.getPauseAdvInfo();
				boolean isIlligal = checkImgAdv(pauseAdvInfo);
				if (isIlligal) {
					String clickUrl = pauseAdvInfo.VAL.get(0).CU;
					showInBrowser(clickUrl, new OnShowAdvInBrowserResult() {

						@Override
						public void onFail() {

						}

						@Override
						public void onSuccess() {
							// 发送统计
							DisposableStatsUtils.disposeCUM(pauseAdvInfo.VAL
									.get(0));
							endPauseAdvShow();
						}
					});// 展示详情

				}
			}
		});
		// 关闭整个广告
		imgAdvCloseImgView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endPauseAdvShow();
			}

		});
	}

	private void endPauseAdvShow() {
		imgAdvLayout.setVisibility(View.GONE);
		imgAdvContentImgView.setImageResource(R.drawable.nonedrawable);
		mBasePlayerManager.isImageADShowing = false;
		// 显示控制条。。
		mBasePlayerManager.updatePlugin(DetailMessage.PLUGIN_SHOW_NOT_SET);
		// 发送结束展示统计
		DisposableStatsUtils
				.disposeSUE(mediaPlayerDelegate.getPauseAdvInfo().VAL.get(0));
	}

	private interface OnShowAdvInBrowserResult {
		public void onFail();

		public void onSuccess();
	}

	boolean choosedActivity = false;
	boolean onChooserDlgShow = false;// 选择打开的应用对话框是否展示中

	private void showInBrowser(String targetUrl,
			final OnShowAdvInBrowserResult result) {
		choosedActivity = false;
		if (TextUtils.isEmpty(targetUrl)) {
			Logger.e(TAG, "无地址");
			if (result != null) {
				result.onFail();
				return;
			}
		}

		try {
			final Intent advIntent = new Intent();
			advIntent.setAction("android.intent.action.VIEW");
			Uri contentUrl = Uri.parse(targetUrl);
			advIntent.setData(contentUrl);

			final List<ResolveInfo> activities = getContext()
					.getPackageManager().queryIntentActivities(advIntent, 0);

			if (activities.size() == 1) {
				getContext().startActivity(advIntent);
				if (result != null) {
					result.onSuccess();
					return;
				}
			}

			List<String> appNames = new ArrayList<String>();
			for (ResolveInfo info : activities) {
				appNames.add(info.loadLabel(getContext().getPackageManager())
						.toString());
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("选择要打开的应用");
			builder.setItems(
					appNames.toArray(new CharSequence[appNames.size()]),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							choosedActivity = true;
							ResolveInfo info = activities.get(item);
							advIntent.setPackage(info.activityInfo.packageName);
							getContext().startActivity(advIntent);
							if (result != null) {
								result.onSuccess();
								return;
							}
						}
					});
			AlertDialog alert = builder.create();
			alert.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface arg0) {
					onChooserDlgShow = false;
					if (!choosedActivity) {
						if (result != null) {
							result.onFail();
							return;
						}
					}
				}
			});
			onChooserDlgShow = true;
			alert.show();
		} catch (Exception e) {
			// 比如没有装浏览器，虽然很少见。。
			Logger.e(TAG, "广告点击打开失败");
			if (result != null) {
				result.onFail();
				return;
			}
		}
	}

	private void startPlay() {
		if (null == mMediaPlayerDelegate)
			return;
		
		if (!mMediaPlayerDelegate.isAdvShowFinished()) {
			if(mMediaPlayerDelegate.isReleased){
				mBasePlayerManager.startPlay();
			}
			//双保险确定一定可以start
			if(!mMediaPlayerDelegate.isPlaying()){
				mMediaPlayerDelegate.start();
			}
		} else {
			mMediaPlayerDelegate.start();
		}
		ispause=false;
	}

	@Override
	public void onBufferingUpdateListener(int percent) {

	}

	@Override
	public void onCompletionListener() {

	}

	@Override
	public boolean onErrorListener(int what, int extra) {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				containerView.setVisibility(View.GONE);
			}
		});
		return false;
	}

	@Override
	public void OnPreparedListener() {

	}

	@Override
	public void OnSeekCompleteListener() {

	}

	@Override
	public void OnVideoSizeChangedListener(int width, int height) {

	}

	@Override
	public void OnTimeoutListener() {

	}

	@Override
	public void OnCurrentPositionChangeListener(int currentPosition) {
	}

	@Override
	public void onLoadedListener() {
	}

	@Override
	public void onLoadingListener() {
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
	public void newVideo() {
	}

	@Override
	public void onVolumnUp() {
	}

	@Override
	public void onVolumnDown() {
	}

	@Override
	public void onMute(boolean mute) {
	}

	@Override
	public void onVideoChange() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mCountUpdateTextView.setText("");
				mSmallFullImgView.setVisibility(View.GONE);
			}
		});
	}

	private boolean isADPluginShowing = false;

	@Override
	public void onVideoInfoGetting() {
		if (isADPluginShowing) {
			mBasePlayerManager.interuptAD();
		}
	}

	@Override
	public void onVideoInfoGetted() {
	}

	@Override
	public void onVideoInfoGetFail(boolean needRetry) {
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			isADPluginShowing = true;
			containerView.setVisibility(View.VISIBLE);
		} else {
			isADPluginShowing = false;
			containerView.setVisibility(View.GONE);
		}
	}

	public void notifyUpdate(int count) {
		if (count <= 0) {
			count = 0;
		}

		String countStr = String.valueOf(count);
		int countLen = countStr.length();
		countStr += " 秒";
		SpannableString msp = new SpannableString(countStr);
		msp.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4222")), 0,
				countLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mCountUpdateTextView.setText(msp);
	}

	@Override
	public void onPluginAdded() {
		super.onPluginAdded();

		if (mBasePlayerManager.isImageADShowing) {

			imgAdvContentImgView.setImageResource(R.drawable.nonedrawable);

			// 处理暂停广告
			final VideoAdvInfo pauseAdvInfo = mediaPlayerDelegate
					.getPauseAdvInfo();
			boolean canShow = checkImgAdv(pauseAdvInfo);
			if (!canShow) {
				mBasePlayerManager.isImageADShowing = false;
				mBasePlayerManager
						.updatePlugin(DetailMessage.PLUGIN_SHOW_NOT_SET);
				return;
			}

			if (mFrontAdView != null) {
				mFrontAdView.setVisibility(View.GONE);
			}

			String rs = pauseAdvInfo.VAL.get(0).RS;

			ImageLoader.getInstance().displayImage(rs, imgAdvContentImgView,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							Logger.d(TAG, "暂停图片广告播放取消");
							// //显示失败，没有统计。。？？
							endPauseAdvShow();
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if(mMediaPlayerDelegate.isPlaying()){
								endPauseAdvShow();
							}else{
								Logger.d(TAG, "暂停图片广告播放成功");
								// 显示暂停图片广告
								imgAdvLayout.setVisibility(VISIBLE);

								Logger.d(TAG, "暂停图片广告开始展示，发送开始统计");
								// 统计
								// //发送开始统计
								DisposableStatsUtils.disposeSUS(pauseAdvInfo.VAL
										.get(0));
								// //发送VC统计
								DisposableStatsUtils.disposeVC(pauseAdvInfo.VAL
										.get(0));
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							Logger.d(TAG, "暂停图片广告播放失败");
							// //显示失败，没有统计。。？？
							endPauseAdvShow();
						}
					});

			return;
		}

		if (mediaPlayerDelegate.isFullScreen) {
			mSmallFullImgView.setImageResource(R.drawable.ad_icon_out);
			// 全屏不显示返回按钮
			backImgView.setVisibility(View.INVISIBLE);
			
			//控制边距
			setAdvTopMargin(15);
			setAdvCountMargin(15);
			setAdvVolumeMargin(9);
			setAdvDetailMargin(15,15);
			setAdvSmallFullMargin(15,15);

		} else {
			mSmallFullImgView.setImageResource(R.drawable.ad_icon_fullscreen);
			// 半屏显示返回按钮
			backImgView.setVisibility(View.VISIBLE);
			
			//控制边距
			setAdvTopMargin(4);
			setAdvCountMargin(12);
			setAdvVolumeMargin(10);
			setAdvDetailMargin(8,4);
			setAdvSmallFullMargin(12,4);
			
		}

		if (UIUtils.hasKitKat()) {
			mBasePlayerManager.hideSystemUI(this);
		}
		mBasePlayerManager.setPluginHolderPaddingZero();

	}

	private void setAdvSmallFullMargin(int right, int bottom) {
		RelativeLayout.LayoutParams bottomViewParams = (RelativeLayout.LayoutParams) mSmallFullImgView.getLayoutParams();
		bottomViewParams.rightMargin = Util.dip2px(right);
		bottomViewParams.bottomMargin = Util.dip2px(bottom);
		mSmallFullImgView.setLayoutParams(bottomViewParams);
	}

	private void setAdvDetailMargin(int left, int bottom) {
		RelativeLayout.LayoutParams bottomViewParams = (RelativeLayout.LayoutParams) mAdDetailBtn.getLayoutParams();
		bottomViewParams.leftMargin = Util.dip2px(left);
		bottomViewParams.bottomMargin = Util.dip2px(bottom);
		mAdDetailBtn.setLayoutParams(bottomViewParams);
	}

	private void setAdvVolumeMargin(int dp) {
		RelativeLayout.LayoutParams bottomViewParams = (RelativeLayout.LayoutParams) volumeImgView.getLayoutParams();
		bottomViewParams.rightMargin = Util.dip2px(dp);
		volumeImgView.setLayoutParams(bottomViewParams);
	}

	private void setAdvCountMargin(int dp) {
		RelativeLayout.LayoutParams bottomViewParams = (RelativeLayout.LayoutParams) mCountWrapView.getLayoutParams();
		bottomViewParams.rightMargin = Util.dip2px(dp);
		mCountWrapView.setLayoutParams(bottomViewParams);
	}

	private void setAdvTopMargin(int dp) {
		RelativeLayout.LayoutParams bottomViewParams = (RelativeLayout.LayoutParams) mAdTopView.getLayoutParams();
		bottomViewParams.topMargin = Util.dip2px(dp);
		mAdTopView.setLayoutParams(bottomViewParams);
	}

	private boolean checkImgAdv(VideoAdvInfo pauseAdvInfo) {
		if (pauseAdvInfo == null) {
			return false;
		}

		if (pauseAdvInfo.VAL == null || pauseAdvInfo.VAL.isEmpty()) {
			return false;
		}

		// 暂停广告应该只有一个值
		AdvInfo imgAdvItem = pauseAdvInfo.VAL.get(0);
		if (!"img".equals(imgAdvItem.RST)) {
			// 不是图片形式的广告
			return false;
		}
		String rs = imgAdvItem.RS;
		if (TextUtils.isEmpty(rs)) {
			// 没有内容
			return false;
		}
		return true;
	}

	@Override
	public void onNotifyChangeVideoQuality() {

	}

	@Override
	public void onRealVideoStart() {
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

	public void showPlayIcon() {
	}

	@Override
	public void back() {
	}

	@Override
	public void onPlayNoRightVideo(GoplayException e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayReleateNoRightVideo() {
		// TODO Auto-generated method stub

	}

	public static void setAdMoreBackgroundColor(boolean isTudouPlatform) {
	}

	public boolean isCountUpdateVisible() {
		if (mCountUpdateTextView != null) {
			return mCountUpdateTextView.getVisibility() == View.VISIBLE ? true
					: false;
		}
		return false;
	}

	public void setSkipVisible(boolean visible) {
	}
}
