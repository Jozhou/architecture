package com.tool.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ContentValues;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/** 
 * log日志统计保存 
 *  
 * @author mlj 
 *  
 */  
public class LogcatUtils {
	
	private static File file = null;
	private static String createLogFileTime = null;
	private static final String SDF_YYYYMMDD = "yyyy-MM-dd";
	private static final String SDF_YYYYMMDDHHMMSS = "MM-dd HH:mm:ss,SSS";
	private static ExecutorService executorAppendLog = Executors.newSingleThreadExecutor();
	
	static {
		
	}

	private static void init() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {
				createLogFileTime = new SimpleDateFormat(SDF_YYYYMMDD).format(new Date());
				file = new File(FileUtils.getCachePath() + FileUtils.CACHELOGPATH
						+ "/dcar-" + createLogFileTime + ".log");
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists())
					file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			Log.w("LogUtils", "can't find sdcard for log store.");
		}
	}

	private static boolean checkStoreLog() {
		return file != null && file.exists() && !TextUtils.isEmpty(createLogFileTime) 
				&& createLogFileTime.equals(new SimpleDateFormat(SDF_YYYYMMDD).format(new Date()));
	}
	
	private static void appendLog(final File f, final String c, final int l) {
		if (file == null || !file.exists()) {
			return;
		}
		final File file = f;
		final String content = c;
		final int level = l;
		executorAppendLog.execute(new Runnable() {
					@Override
					public void run() {
						synchronized (LogcatUtils.class) {
							BufferedWriter out = null;
							try {
								out = new BufferedWriter(new OutputStreamWriter(
										new FileOutputStream(file, true), "UTF-8"), 8192);
								StringBuffer sb = new StringBuffer();
								sb.append(new SimpleDateFormat(SDF_YYYYMMDDHHMMSS).format(new Date()));
								sb.append("\t ");
								sb.append(level == 0 ? "debug" : level == 1 ? "info" : level == 2 ? "warn"
										: level == 100 ? "crash" : "error");
								sb.append("\t");
								sb.append(content);
								sb.append("\r\n");
								out.write(sb.toString());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									if (out != null) {
										out.close();
										out = null;
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			);
	}
	
	public static void d(String tag, String msg) {
		if(getIsDebug()) {
			Log.d(tag, "thread Id: " + Thread.currentThread().getId() + "  " + msg);
		}
		if(getIsStoreLog()){
			if (!checkStoreLog()) {
				init();
			}
			appendLog(file, tag + "\t" + "thread Id: " + Thread.currentThread().getId() + "  " + msg, 0);
		}
	}
	
	public static void i(String tag, String msg) {
		if(getIsDebug()) {
			Log.i(tag, "thread Id: " + Thread.currentThread().getId() + "  " + msg);
		}
		if(getIsStoreLog()){
			if (!checkStoreLog()) {
				init();
			}
			appendLog(file, tag + "\t" + "thread Id: " + Thread.currentThread().getId() + "  " + msg, 1);
		}
	}

	public static void w(String tag, String msg) {
		if(getIsDebug()) {
			Log.w(tag, "thread Id: " + Thread.currentThread().getId() + "  " + msg);
		}
		if(getIsStoreLog()){
			if (!checkStoreLog()) {
				init();
			}
			appendLog(file, tag + "\t" + "thread Id: " + Thread.currentThread().getId() + "  " + msg, 2);
		}
	}
	
	public static void e(String tag, String msg) {
		if(getIsDebug()) {
			Log.e(tag, "thread Id: " + Thread.currentThread().getId() + "  " + msg);
		}
		if(getIsStoreLog()){
			if (!checkStoreLog()) {
				init();
			}
			appendLog(file, tag + "\t" + "thread Id: " + Thread.currentThread().getId() + "  " + msg, 0);
		}
	}
	
	public static void e(String tag, Throwable e) {
		String msg = " StackTrace:" + getErrorInfo(e);
		if(getIsDebug()) {
			Log.e(tag, "thread Id: " + Thread.currentThread().getId() + "  " + msg);	
		}
		if(getIsStoreLog()){
			if (!checkStoreLog()) {
				init();
			}
			appendLog(file, tag + "\t" + "thread Id: " + Thread.currentThread().getId() + "  " + msg, 0);
		}
	}
	
	public static void e(String tag, String msg, Throwable e) {
		msg += " StackTrace:" + getErrorInfo(e);
		if(getIsDebug()) {
			Log.e(tag, "thread Id: " + Thread.currentThread().getId() + "  " + msg);	
		}
		if(getIsStoreLog()){
			if (!checkStoreLog()) {
				init();
			}
			appendLog(file, tag + "\t" + "thread Id: " + Thread.currentThread().getId() + "  " + msg, 0);
		}
	}
	
	public static String getErrorInfo(Throwable throwable) {
		try {
			if (throwable != null) {
				Writer writer = new StringWriter();
				PrintWriter pw = new PrintWriter(writer);
				throwable.printStackTrace(pw);
				pw.close();
				return writer.toString();
			}
		} catch (Exception e) {
			
		}
		return "";
	}
	
	public static String crash(String msg) {
		if(getIsDebug()) {
			Log.e("DCar", "crash occured : " + Thread.currentThread().getId() + "  " + msg);
		}
		String path = FileUtils.getCachePath() + FileUtils.CACHECRASHPATH
				+ "/dcar-" + new SimpleDateFormat(SDF_YYYYMMDD).format(new Date()) + ".log";
		try {
			File desFile = new File(path);
			if (!desFile.getParentFile().exists()) {
				desFile.getParentFile().mkdirs();
			}
			if (!desFile.exists()) {
				desFile.createNewFile();
			}
			RandomAccessFile newFile = new RandomAccessFile(desFile, "rw");
			// If file size lager than 100K,do not write again
			if (desFile.length() > 1024 * 1024) {
				newFile.seek(0);
			} else {
				newFile.seek(desFile.length());
			}
			newFile.write((msg + "\n").getBytes());
			newFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public static void sqllog(String tablename, String action, String exec, ContentValues initialValues, 
			String whereClause, String[] whereArgs) {
		StringBuffer sbmsg = new StringBuffer();
		boolean isdebug = getIsDebug();
		boolean isstorelog = getIsStoreLog();
		if(isdebug || isstorelog) {
			sbmsg.append("table:").append(tablename).append("; action:").append(action).
				append("; exec:").append(exec).append("; values:");
			if (initialValues != null && initialValues.size() > 0) {
				Set<Map.Entry<String, Object>> entrySet = initialValues.valueSet();
	            Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
	            boolean needSeparator = false;
	            while (entriesIter.hasNext()) {
	                if (needSeparator) {
	                	sbmsg.append(",");
	                }
	                needSeparator = true;
	                Map.Entry<String, Object> entry = entriesIter.next();
	                sbmsg.append("[").append(entry.getKey()).append("] ");
	                sbmsg.append(entry.getValue());
	            }
	        }
			sbmsg.append("; where:[").append(whereClause).append("] ");
			if(whereArgs != null && whereArgs.length > 0) {
				boolean needSeparator = false;
				for(int i=0; i<whereArgs.length; i++) {
					if (needSeparator) {
	                	sbmsg.append(",");
	                }
	                needSeparator = true;
					sbmsg.append(whereArgs[i]);
				}
			}
		}
		if(isdebug) {
			Log.i("DCar", Thread.currentThread().getId() + "  " + sbmsg.toString());
		}
		if(isstorelog) {
			String path = FileUtils.getCachePath() + FileUtils.CACHEDBPATH
					+ "/dcar-" + new SimpleDateFormat(SDF_YYYYMMDD).format(new Date()) + ".log";
			try {
				File desFile = new File(path);
				if (!desFile.getParentFile().exists()) {
					desFile.getParentFile().mkdirs();
				}
				if (!desFile.exists()) {
					desFile.createNewFile();
				}
				RandomAccessFile newFile = new RandomAccessFile(desFile, "rw");
				// If file size lager than 100K,do not write again
				if (desFile.length() > 1024 * 1024) {
					newFile.seek(0);
				} else {
					newFile.seek(desFile.length());
				}
				newFile.write((sbmsg.toString() + "\n").getBytes());
				newFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static boolean getIsDebug() {
		return true;
	}
	
	private static boolean getIsStoreLog() {
		return true;
	}
	
}