package com.architecture.cache.image;

import android.graphics.Bitmap;

import com.architecture.cache.image.ImageCache.CompressFormat;

public interface IDiskLruCache {
	boolean contains(String key);
	Bitmap getBitmap(String key, ImageCache cache);
	void putBitmap(String key, Bitmap data, String ext);
	void clearCache();
	void setCompressParams(CompressFormat compressFormat, int quality);
}
