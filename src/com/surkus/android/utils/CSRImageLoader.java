package com.surkus.android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.surkus.android.R;

public class CSRImageLoader {
	
	private  ImageLoader mImageLoader;
	private  DisplayImageOptions mDisplayImageOptions;
	private CSRImageLoader() {}
	
	private static CSRImageLoader mSingletonRef;

	public static CSRImageLoader getSingletonRef(Context context) {
		if (mSingletonRef == null)
		{
			mSingletonRef = new CSRImageLoader();
			mSingletonRef.init(context);
		}
		return mSingletonRef;
	}
	
	public ImageLoader getImageLoader()
	{
		return mImageLoader;
	}
	
	public DisplayImageOptions getDisplayOptions()
	{
		return mDisplayImageOptions;
	}
	
	/**
	 *  Initialize ImageLoader
	 *  @param context Activity context
	 */
	public void init(Context context) {
		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory limit
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}
		
		 mDisplayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_empty)
		.resetViewBeforeLoading()
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 1)
				.memoryCacheSize(memoryCacheSize)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.threadPoolSize(5)
				.tasksProcessingOrder(QueueProcessingType.FIFO).enableLogging() 
				.build();
		
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
	}

}
