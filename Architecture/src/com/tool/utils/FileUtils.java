package com.tool.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

import com.architecture.application.App;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {
	
	public static final String COREFOLDERNAME = ".architecture";
	
	public static final String CACHEPATH = File.separator + "Logs";
	public static final String CACHELOGPATH = CACHEPATH + File.separator + "Log";
	public static final String CACHECRASHPATH = CACHEPATH + File.separator + "Crash";
	public static final String CACHEDBPATH = CACHEPATH + File.separator + "Database";
	public static final String CACHEAPKPATH = CACHEPATH + File.separator + "Apk";
	public static final String CACHEMEMPATH = CACHEPATH + File.separator + "Mem";
	
	/**
	 * 获取程序缓存的目录
	 * @return
	 */
	public static String getCachePath() {
		String cachePath;
    	Context context = App.getInstance();
        try {
        	boolean hasExternalStorage = false;
        	boolean hasInternalStorage = false;
            String exterState = Environment.getExternalStorageState();
            hasExternalStorage = exterState.equalsIgnoreCase(Environment.MEDIA_MOUNTED);
            if(!hasExternalStorage) {
            	if(OSUtils.hasGingerbread() && !Environment.isExternalStorageRemovable() &&
            			!exterState.equalsIgnoreCase(Environment.MEDIA_SHARED)) {
            		hasInternalStorage = true;
            	}
            }
            if(hasExternalStorage || hasInternalStorage) {
            	if (OSUtils.hasFroyo()) {
            		cachePath = context.getExternalCacheDir().getPath();
		        } else {
			        cachePath = Environment.getExternalStorageDirectory().getPath() + 
			        		"/Android/data/" + context.getPackageName() + "/cache/";
		        }
            } else {
            	cachePath = context.getCacheDir().getPath();
            }
        } catch (Exception e) {
            File cacheDir = context.getCacheDir();
            if (cacheDir == null) {
                return null;
            }
            cachePath = cacheDir.getPath();
        }
        if(cachePath.endsWith(File.separator)) {
        	cachePath = cachePath.substring(0, cachePath.length() - 1);
        }
        return cachePath;
	}
	
	/**
	 * 获取sd卡根目录的core隐藏文件夹
	 * @return
	 */
	public static String getSDCardCorePath() {
		return getCachePath() + File.separator + COREFOLDERNAME;
	}

	
	/**
	 * 获取sd卡根目录
	 * @return 
	 */
	public static String getSDCardRootPath() {
		String cachePath;
    	Context context = App.getInstance();
        try {
            String exterState = Environment.getExternalStorageState();
            boolean hasExternalStorage = exterState.equalsIgnoreCase(Environment.MEDIA_MOUNTED);
            if(hasExternalStorage) {
            	cachePath = Environment.getExternalStorageDirectory().getPath();
            } else {
            	cachePath = context.getCacheDir().getPath();
            }
        } catch (Exception e) {
            File cacheDir = context.getCacheDir();
            if (cacheDir == null) {
                return null;
            }
            cachePath = cacheDir.getPath();
        }
        if(cachePath != null && cachePath.endsWith(File.separator)) {
        	cachePath = cachePath.substring(0, cachePath.length() - 1);
        }
        return cachePath;
	}
	
	/**
	 * 从程序获取文件内容
	 * @param filePath
	 * @return
	 */
	public static String getStringFromFile(String filePath) {
		return getStringFromFile(filePath, null);
	}
	
	/**
	 * 从程序获取文件内容
	 * @param filePath
	 * @param encoding
	 * @return
	 */
	public static String getStringFromFile(String filePath, String encoding) {
		if(TextUtils.isEmpty(filePath))
			return null;
		File file = new File(filePath);
		if(!file.exists()) {
			return "";
		}
		String content = "";
		InputStream input = null;
		InputStreamReader reader = null;
		StringWriter sw = new StringWriter();
		try {
			input = new FileInputStream(filePath);
			if(TextUtils.isEmpty(encoding)) {
				reader = new InputStreamReader(input);
			} else {
				reader = new InputStreamReader(input, encoding);
			}
			char[] buffer = new char[1024 * 4];
			int n = 0;
			while ((n = reader.read(buffer)) != -1) {
				sw.write(buffer, 0, n);
			}
			content = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(sw != null) {
				try {
					sw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sw = null;
			}
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reader = null;
			}
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				input = null;
			}
		}
		return content;
	}
	
	/**
	 * 保存字符串至程序缓存目录
	 * @param data
	 * @param filename
	 * @return
	 */
	public static String saveString2File(String data, String filePath) {
		return saveString2File(data, filePath, null);
	}
	
	/**
	 * 保存字符串至程序缓存目录
	 * @param data
	 * @param encoding
	 * @param filename
	 * @return
	 */
	public static String saveString2File(String data, String filePath, String encoding) {
		if(TextUtils.isEmpty(filePath))
			return null;
		OutputStream out = null;
		try {
			out = openFileOutputStream(filePath);
			if(TextUtils.isEmpty(encoding)) {
				out.write(data.getBytes());
			} else {
				out.write(data.getBytes(encoding));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				out = null;
			}
		}
		return filePath;
	}

	/**
	 * 打开文件流
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openFileOutputStream(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null && parent.exists() == false) {
				if (parent.mkdirs() == false) {
					throw new IOException("File '" + file + "' could not be created");
				}
			}
			file.createNewFile();
		}
		return new FileOutputStream(file);
	}
	
}