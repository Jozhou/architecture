/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.architecture.widget.imageview;

import java.io.File;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.architecture.cache.image.ImageCache;
import com.architecture.cache.image.ImageFetcher;
import com.architecture.cache.image.ImageWorker.ProcessCallback;
import com.architecture.utils.ImageUtils;
import com.architecture.R;

/**
 * 带有缓存功能的ImageView
 * @author lijunma
 *
 */
public class MThumbImageView extends MRecycleImageView {

	protected final static int NOTSET = 0;
	protected final static int LOADING = 1;
	protected final static int SUCC = 2;
	protected final static int ERROR = 3;
	
	protected String mImageUrl;
	protected String mImageFolder;
	protected int mLoadingResID;
	protected int mLoadErrResID;
	protected int mLoadStatus;

	protected IProcessCallback mProcessCallback;
	protected IStatusChanged mStatusListener;
	protected OnClickListener mClickListener;
	
	protected Handler mHandler = new Handler();
	
    public MThumbImageView(Context context) {
        super(context);
        initializeImageView();
    }

    public MThumbImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeImageView();
    }

    public MThumbImageView(Context context, AttributeSet attrs, int paramInt) {
        super(context, attrs, paramInt);
        initializeImageView();
    }
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mLoadStatus = NOTSET;
		mImageUrl = null;
	}
    
	@Override
	protected void initializeAttr(AttributeSet attrs, int defStyle) {
		super.initializeAttr(attrs, defStyle);
		
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.MImageView, defStyle, 0);
		
		String folder = typedArray.getString(R.styleable.MImageView_MImageView_cacheFolder);
		setImageCacheFolder(folder);
		
		int loading = typedArray.getResourceId(R.styleable.MImageView_MImageView_loading, 0);
		setLoadingResID(loading);
		
		int loaderr = typedArray.getResourceId(R.styleable.MImageView_MImageView_loadErr, 0);
		setLoadErrResID(loaderr);
		
    	super.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	    		if(mClickListener != null) {
	    			mClickListener.onClick(v);
	    		} else {
					if(mLoadStatus == NOTSET || mLoadStatus == ERROR) {
			    		loadImageWeb();
			    	}
	    		}
			}
		});
		
		typedArray.recycle();
	}
    
    protected void initializeImageView() {
    	setImageUrl(null);
    }
    
    @Override
    public void setOnClickListener(OnClickListener listener) {
    	mClickListener = listener;
    }
    
    /**
     * set the image process listener
     * @param listener
     */
    public void setProcessCallbackListener(IProcessCallback listener) {
    	mProcessCallback = listener;
    }
    
    /**
     * set the status changed listener
     * @param listener
     */
    public void setStatusListener(IStatusChanged listener) {
    	mStatusListener = listener;
    }

    /**
     * set the loading image resource id
     * @param value
     */
    public void setLoadingResID(int value) {
    	mLoadingResID = value;
    }
    
    /**
     * set the error image resource id
     * @param value
     */
    public void setLoadErrResID(int value) {
    	mLoadErrResID = value;
    }
    
    /**
     * set the image cache folder
     * @param value
     */
    public void setImageCacheFolder(String value) {
    	mImageFolder = value;
    }

    /**
     * get the image url
     * @return
     */
    public String getImageUrl() {
    	return mImageUrl;
    }
    
    /**
     * set the image url
     * @param value
     */
    public void setImageUrl(String value) {
    	setImageUrl(value, false);
    }
    
    /**
     * set the image url
     * @param value
     * @param loadlocal only load from local file
     */
    public void setImageUrl(String value, boolean loadlocal) {
    	if((mLoadStatus == SUCC || mLoadStatus == LOADING) && ((value == null && mImageUrl == null) || 
    		value != null && value.equals(mImageUrl))) {
    		return;
    	}
    	mImageUrl = value;
    	mLoadStatus = NOTSET;
		if(loadlocal) {
			loadImageLocal();
		} else {
			loadImageWeb();
		}
    }
    
    protected void loadImageLocal() {
    	if(TextUtils.isEmpty(mImageUrl)) {
    		setImageDrawable(null);
    		setBackgroundResource(mLoadingResID);
    		return;
    	}
    	mLoadStatus = LOADING;
    	ImageFetcher.get().loadImage(this, mImageUrl, 0, 0, false, 
				new ProcessCallback() {
					@Override
					public Bitmap loadBitmapFromFile(String url) {
						if(!TextUtils.isEmpty(mImageFolder)) {
							String file = mImageFolder + 
									(mImageFolder.endsWith(File.separator) ? "" : File.separator) +
									ImageCache.hashKeyForDisk(url);
							return ImageUtils.file2Bitmap(file, -1, -1);
						}
						return null;
					};
					
    				@Override
					public void onMemoryOver(String url, Drawable drawable) {
    					if(url.equals(mImageUrl)) {
	    					setBackgroundResource(0);
	    					mLoadStatus = SUCC;
    					}
    				};
    				
    				public void onTaskBegin(String url) {
						if(url.equals(mImageUrl)) {
	    					setBackgroundResource(mLoadingResID);
	    					setImageDrawable(null);
    					}
    				};
					
					@Override
					public void onTaskOver(String url, Drawable drawable) {
						if(url.equals(mImageUrl)) {
	    					setBackgroundResource(0);
    						mLoadStatus = SUCC;
    					}
					};
					
					public void onTaskError(String url) {
						if(url.equals(mImageUrl)) {
	    					setBackgroundResource(mLoadErrResID);
    						mLoadStatus = ERROR;
    					}
					};
				}
		);
    }
    
    protected void loadImageWeb() {
    	if(TextUtils.isEmpty(mImageUrl)) {
    		setImageDrawable(null);
    		setBackgroundResource(mLoadingResID);
    		return;
    	}
    	mLoadStatus = LOADING;
    	ImageFetcher.get().loadImage(this, mImageUrl, 0, 0, true, 
				new ProcessCallback() {
					@Override
					public Bitmap loadBitmapFromFile(String url) {
						if(!TextUtils.isEmpty(mImageFolder)) {
							String file = mImageFolder + 
									(mImageFolder.endsWith(File.separator) ? "" : File.separator) +
									ImageCache.hashKeyForDisk(url);
							return ImageUtils.file2Bitmap(file, -1, -1);
						}
						return null;
					};
					
					@Override
					public Bitmap processBitmap(String url, Bitmap bitmap) {
						if(mProcessCallback != null) {
							bitmap = mProcessCallback.processBitmap(url, bitmap);
						}
						if(!TextUtils.isEmpty(mImageFolder)) {
							ImageUtils.saveBitma2Disk(bitmap, mImageFolder, 
									ImageCache.hashKeyForDisk(url));
						}
						return super.processBitmap(url, bitmap);
					}
					
    				@Override
					public void onMemoryOver(String url, Drawable drawable) {
    					if(url.equals(mImageUrl)) {
    				    	setBackgroundResource(0);
    						mLoadStatus = SUCC;
    						if(mStatusListener != null)
    							mStatusListener.onEnd();
    					}
    				};
    				
    				public void onTaskBegin(String url) {
						if(url.equals(mImageUrl)) {
	    					setBackgroundResource(mLoadingResID);
    					}
    				};
    				
    				@Override
    				public void onTaskHttpBegin(String url) {
    					if(url.equals(mImageUrl)) {
    						if(mStatusListener != null)
    							mStatusListener.onBegin();
    					}
    				}
    				
    				@Override
    				public void onTaskHttpProcess(String url, final long current, final long total) {
    					if(url.equals(mImageUrl)) {
    						int rate = (int) ((current * 100) / (total <= 0 ? 1024 * 150 : total));
    						if(rate >= 100)
    							rate = 100;
    						if(mStatusListener != null)
    							mStatusListener.onProgress(rate);
    					}
    				}
					
					@Override
					public void onTaskOver(String url, Drawable drawable) {
						if(url.equals(mImageUrl)) {
    				    	setBackgroundResource(0);
    						mLoadStatus = SUCC;
    						if(mStatusListener != null)
    							mStatusListener.onEnd();
    					}
					};
					
					public void onTaskError(String url) {
						if(url.equals(mImageUrl)) {
    				    	setBackgroundResource(mLoadErrResID);
    						mLoadStatus = ERROR;
    						if(mStatusListener != null)
    							mStatusListener.onEnd();
						}
					};
				}
		);
    }
}
