package com.architecture.common.splash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.architecture.utils.FileUtils;
import com.architecture.utils.PreferenceUtils;


public class SplashManager{

	private static final String KEY_SPLASH_PIC = "default_splash_pic";
	
	/**
	 * 判断服务器返回的url是否和本地相同
	 * @param 	url 	with pic address
	 * @return 	true 	和本地相同，无需下载
	 * 		   	false 	和本地不同，需要下载
	 */
	public static boolean isEqualsCache(String url){
		String localPath = PreferenceUtils.getString(KEY_SPLASH_PIC,"");
		if(localPath.equals(url))
			return true;
		else
			return false;
	}
	/**
	 * 更新缓存 更新本地存储路径
	 * @param 	url 	with pic address
	 * @param 	bitmap  
	 * @return 	
	 */
	public static void updateCache(String url, Bitmap bitmap){
		saveSplashPic2Cache(bitmap, url);
		removeSplashPicCache(getCacheFileUrl());
		putCacheFileUrl(url);
	}
	/**
	 * 清除缓存 清除本地存储路径
	 * @return 	
	 */
	public static void clearCache(){
		removeSplashPicCache(getCacheFileUrl());
		putCacheFileUrl("");
	}
	/**
	 * 获取Bitmap从程序缓存目录
	 * @param 	url 	with pic address
	 * @return 	bitmap
	 */ 
	public static Bitmap getSplashPicFromCache(){
		String cacheUrl = SplashManager.getCacheFileUrl();
		Bitmap bitmap = null;
		String filePath = getSplashPicPath(cacheUrl);
		if(TextUtils.isEmpty(filePath))
			return null;

		File f = new File(filePath);
		if(!f.exists()) {
			putCacheFileUrl("");
			return null;
		}
		bitmap = BitmapFactory.decodeFile(filePath);

		return bitmap;
	}
	/**
	 * 获得本地记录的spalsh pic url地址
	 * @return 	cache url
	 */
	private static String getCacheFileUrl(){
		 return PreferenceUtils.getString(KEY_SPLASH_PIC,"");
	}
	/**
	 * 记录spalsh pic url地址到本地
	 * @param 		url 	with pic address
	 * @return 
	 */
	private static void putCacheFileUrl(String url){
		 PreferenceUtils.putString(KEY_SPLASH_PIC,url);
	}
	/**
	 * 把url转换成本地文件路径
	 * @param 	url 	with pic address
	 * @return 	本地缓存的pic文件路径
	 */
	private static String getSplashPicPath(String url){
		String filename = "";
		String cachePath = FileUtils.getCachePath();
		if(TextUtils.isEmpty(cachePath)||TextUtils.isEmpty(url))
			return null;

		if(url.contains("/")){
			int index = url.lastIndexOf("/");
			filename = url.substring(index + 1);
		} else {
			filename = url;
		}
		String filePath = cachePath + "/splash/pic/" + filename;
		return filePath;
	}
	/**
	 * 保存Bitmap至程序缓存目录
	 * @param 	bitmap
	 * @param 	url with pic address
	 * @return 	本地缓存的pic文件路径
	 */
	private static String saveSplashPic2Cache(Bitmap bitmap,String url){
		String filePath = getSplashPicPath(url);
		if(TextUtils.isEmpty(filePath))
			return null;
        FileOutputStream fos = null;     
        try {     
            fos = FileUtils.openFileOutputStream(filePath);   
            if (fos != null) {  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);     
            }     
        } catch (Exception e) {     
            e.printStackTrace();     
        }  finally {
        	if(bitmap != null) {
        		if(!bitmap.isRecycled()) {
        			bitmap.recycle();
        		}
        		bitmap = null;
        	}
        	if(fos != null) {
        		try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
		return filePath;
	}

	/**
	 * 删除本地缓存的pic文件
	 * @param 	url 	with pic address
	 * @return 
	 */ 
	private static void removeSplashPicCache(String url){
		String path = getSplashPicPath(url);
		if(!TextUtils.isEmpty(path))
			FileUtils.removeFile(null, path);
	}
	
	
}