package com.architecture.cache.data;

import java.util.HashMap;
import java.util.Map;

public class MemoryCacheLayerImpl implements ICacheLayer {

	private final Map<String, Object> cache;

	private final Map<String, CacheItem> cacheinfo;

	public MemoryCacheLayerImpl() {
		cache = new HashMap<String, Object>();
		cacheinfo = new HashMap<String, MemoryCacheLayerImpl.CacheItem>();
	}

	@Override
	public void put(String key, Object item) {
		put(key, item, 0);
	}

	public void put(String key, Object item, long timeout) {
		if (!cacheinfo.containsKey(key)) {
			cacheinfo.put(key, new CacheItem(System.currentTimeMillis(),
					timeout));
		} else {
			CacheItem ci = cacheinfo.get(key);
			ci.lastupdate = System.currentTimeMillis();
		}
		cache.put(key, item);
	}

	@Override
	public void remove(String key) {
		if (cache.containsKey(key)) {
			cache.remove(key);
		}
		if (cacheinfo.containsKey(key)) {
			cacheinfo.remove(key);
		}
	}
	
	@Override
	public boolean isTimeout(String key) {
		if (cacheinfo.containsKey(key)) {
			CacheItem ci = cacheinfo.get(key);
			return ci.isTimeout();
		} else {
			return true;
		}
	}

	@Override
	public Object get(String key) {
		return cache.get(key);
	}

	private class CacheItem {
		long lastupdate;
		long timeout;

		public CacheItem(long lastupdate, long timeout) {
			this.lastupdate = lastupdate;
			this.timeout = timeout;
		}

		public boolean isTimeout() {

			if (timeout == 0) {
				return false;
			} else {
				return (System.currentTimeMillis() - lastupdate) > timeout ? true
						: false;
			}
		}
	}

	@Override
	public void clearCache() {
		cache.clear();
		cacheinfo.clear();
	}

}
