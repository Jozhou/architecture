/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.architecture.cache.image;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.minivolley.Response;
import com.android.minivolley.VolleyError;
import com.android.minivolley.toolbox.ImageRequest;
import com.architecture.BuildConfig;
import com.architecture.application.App;
import com.architecture.cache.image.ImageCache.ImageCacheParams;
import com.architecture.manager.WindowManager;
import com.architecture.models.api.BaseOperater.OperateManager;
import com.architecture.utils.FileUtils;
import com.architecture.utils.ImageUtils;
import com.architecture.utils.LogcatUtils;
import com.architecture.utils.NetworkUtils;
import com.architecture.utils.OSUtils;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images fetched from a URL.
 */
public class ImageFetcher extends ImageResizer {
    private static final String TAG = "ImageFetcher";
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

    private static final byte[] mLockInstance = new byte[0];
    private static ImageFetcher instance;
    public static ImageFetcher get() {
    	synchronized (mLockInstance) {
			if(instance == null) {
				instance = new ImageFetcher(App.getInstance());
				instance.initCache(App.getInstance());
			}
			return instance;
		}
    }
    
    public static ImageFetcher get(int diskCacheSize) {
    	synchronized (mLockInstance) {
			if(instance == null) {
				instance = new ImageFetcher(App.getInstance());
				instance.initCache(App.getInstance(), diskCacheSize);
			}
			return instance;
		}
    }
    
    private Bitmap mBitmap;

    private ImageFetcher(Context context) {
        super(context);
    }
    
    private void initCache(Context context) {
    	initCache(context, DEFAULT_DISK_CACHE_SIZE);
    }
    
    private void initCache(Context context, int diskCacheSize) {
		ImageCacheParams cacheParams = new ImageCacheParams(App.getInstance());
		if (WindowManager.get().getScreenWidth() > 480 && OSUtils.getMaxMemory() < 70) {
			cacheParams.setMemCacheSizePercent(0.1f);
		} else {
			cacheParams.setMemCacheSizePercent(0.15f);
		}
		cacheParams.setDiskCacheSize(DEFAULT_DISK_CACHE_SIZE);
		addImageCache(cacheParams);
    }
    
    /**
     * get the used space
     * @return
     */
    public long getUsableSpace() {
    	return FileUtils.getFolderLength(mImageCache.getCacheParams().diskCacheDir);
    }

    /**
     * The main process method, which will be called by the ImageWorker in the AsyncTask background
     * thread.
     *
     * @param data The data to load the bitmap, in this case, a regular http URL
     * @return The downloaded and resized bitmap
     */
    @Override
    protected Bitmap processBitmap(Object data, final ProcessCallback callback) {
    	if (BuildConfig.DEBUG) {
            Log.d(TAG, "processBitmap - " + data);
        }
    	final String url = String.valueOf(data);
    	if(TextUtils.isEmpty(url)) {
    		return null;
    	}
        Bitmap bitmap = null;
        // check is file
        File file = new File(url);
        if(file.exists()) {
        	bitmap = ImageUtils.file2Bitmap(url,
        			WindowManager.get().getScreenWidth() * 2, 
        			WindowManager.get().getScreenHeight() * 2);
        } else {
	        try {
	        	bitmap = downloadUrlToStream(url, callback);
	        } catch (Exception e) {
	            Log.e(TAG, "processBitmap - Exception " + e);
	        }
        }
        return bitmap;
    }
    
    /**
     * Download a bitmap from a URL and write the content to an output stream.
     *
     * @param urlString The URL to fetch
     * @return true if successful, false otherwise
     */
    public Bitmap downloadUrlToStream(final String url, final ProcessCallback callback) {
        try {
        	Bitmap bitmap = null;
        	if(!NetworkUtils.isNetWorkConnected()) {
        		return null;
        	}
        	if (callback != null) {
        		new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
		        		callback.onTaskHttpBegin(url);
					}
				});
			}
            LogcatUtils.i(TAG, "get image : " + url);
            final byte[] locktask = new byte[0];
            Response.Listener<Bitmap> succ = new Response.Listener<Bitmap>() {
    			@Override
    			public void onResponse(Bitmap b) {
    				mBitmap = b;
    				synchronized (locktask) {
						try {
							locktask.notify();
						} catch (Exception e) {
							
						}
					}
    			}
    			
    		};
    		Response.ErrorListener error = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                	mBitmap = null;
                	synchronized (locktask) {
						try {
							locktask.notify();
						} catch (Exception e) {
							
						}
					}
                }
            };
            ImageRequest request = new ImageRequest(url, succ, WindowManager.get().getScreenWidth(), 
            		WindowManager.get().getScreenHeight(), null, error);
    		OperateManager.getInstance().onReq(request);
        	synchronized (locktask) {
            	try {
            		locktask.wait();
            		bitmap = mBitmap;
            		mBitmap = null;
            	} catch (Exception e) {
            		
            	}
			}
            return bitmap;
        } catch (Exception e){
            Log.e(TAG, "Error in downloadBitmap - " + e);
        }
        return null;
    }
    
}