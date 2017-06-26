package com.architecture.cache.data;

import android.content.Context;

import com.architecture.application.App;

public class CacheManager implements ICacheLayer {

	private static final byte[] mLock = new byte[0];
	private static CacheManager mInstance = null;
	public final static CacheManager get() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new CacheManager(App.getInstance());
            }
            return mInstance;
        }
    }
	
	private ICacheLayer cacheLayer;
	
	public CacheManager(Context context) {
		cacheLayer = new MemoryCacheLayerImpl();
	}
	
	@Override
	public void put(String key, Object item) {
		if (cacheLayer != null) {
			cacheLayer.put(key, item);
		}
	}

	@Override
	public void put(String key, Object item, long timeout) {
		if (cacheLayer != null) {
			cacheLayer.put(key, item, timeout);
		}
	}

	@Override
	public void remove(String key) {
		if (cacheLayer != null) {
			cacheLayer.remove(key);
		}
	}

	@Override
	public boolean isTimeout(String key) {
		if (cacheLayer != null) {
			return cacheLayer.isTimeout(key);
		}
		return false;
	}

	@Override
	public Object get(String key) {
		if (cacheLayer != null) {
			return cacheLayer.get(key);
		}
		return null;
	}

	@Override
	public void clearCache() {
		if (cacheLayer != null) {
			cacheLayer.clearCache();
		}
	}

}
