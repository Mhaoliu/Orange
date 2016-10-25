package com.youku.player;

import android.content.Context;
import android.os.Handler;

import com.decapi.DecAPI;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.youku.player.ui.R;
import com.youku.service.download.DownloadManager;


public abstract class YoukuPlayerBaseConfiguration extends YoukuPlayerConfiguration {

	public YoukuPlayerBaseConfiguration(Context applicationContext) {
		super(applicationContext);
		DownloadManager.getInstance();		

		DecAPI.init(context,R.raw.aes);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DownloadManager.getInstance().startNewTask();
			}
		}, 1000);
		
		ImageLoader.getInstance().init(buildDefaultILC(applicationContext));
	}

	  private ImageLoaderConfiguration buildDefaultILC(Context context) {
          // This configuration tuning is custom. You can tune every option, you may tune some of them,
          // or you can create default configuration by
          //  ImageLoaderConfiguration.createDefault(this); method.
          ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
          config.threadPriority(Thread.NORM_PRIORITY - 2);
          config.denyCacheImageMultipleSizesInMemory();
          config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
          config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
          config.tasksProcessingOrder(QueueProcessingType.LIFO);
          config.writeDebugLogs(); // Remove for release app

          return config.build();
      }
	

	@Override
	public int getNotifyLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.notify;
	}
	
	public static void exit(){
		YoukuPlayerConfiguration.exit();
		DownloadManager.getInstance().unregister();
	}


}
