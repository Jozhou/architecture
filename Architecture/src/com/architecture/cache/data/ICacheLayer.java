package com.architecture.cache.data;

public interface ICacheLayer {
	
	public void put(String key, Object item);
	
	public void put(String key, Object item, long timeout);
	
	public void remove(String key);
	
	public boolean isTimeout(String key);
	
	public Object get(String key);
	
	public void clearCache();
	
	
}
