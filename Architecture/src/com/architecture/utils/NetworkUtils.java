package com.architecture.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.architecture.application.App;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

public class NetworkUtils extends com.android.volley.utils.NetworkUtil {
	
	private final static String mTag = "HttpUtils";
	
	public static final String PREFERRED_APN_URI = "content://telephony/carriers/preferapn";
	public static final String NETWROK_REACHABLE_DOMAIN = "www.baidu.com";

	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected() {
		boolean isOk = true;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivityManager == null){
				return false;
			}
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetInfo != null && !wifiNetInfo.isConnectedOrConnecting()) {
				if (mobNetInfo != null && !mobNetInfo.isConnectedOrConnecting()) {
					NetworkInfo info = connectivityManager.getActiveNetworkInfo();
					if (info == null) {
						isOk = false;
					}
				}
			}
			mobNetInfo = null;
			wifiNetInfo = null;
			connectivityManager = null;
		} catch (Exception e) {
			LogcatUtils.e(mTag, e);
		}
		return isOk;
	}
	
	/**
	 * 判断网络是否可以ping的通
	 * @return
	 */
	public static boolean isNetworkReachable() {
		try {  
            Callable<Boolean> task = new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					try {
						Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + NETWROK_REACHABLE_DOMAIN);
						int status = p.waitFor();
						if (status == 0) {
							return true;
						}
					} catch (IOException e) {
						
					} catch (InterruptedException e) {
						
					}
					return false;
				}
			};
			List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
			tasks.add(task);
            ExecutorService executorService = Executors.newSingleThreadExecutor();// 单线程executor  
            List<Future<Boolean>> futures = executorService.invokeAll(tasks, 5L, TimeUnit.SECONDS);  
            executorService.shutdown();  
            Future<Boolean> future = futures.get(0);  
            if (future.isCancelled()) {  
                return false;
            } else {
            	return future.get();
            }
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;
        }
	}
	
	/**
	 * 获取网络类型
	 * @param context
	 * @return
	 */
	public static NetType getNetworkType(Context context) {
		NetType netType = NetType.Unknown;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivityManager == null){
				return  netType;
			}
			NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetInfo != null && wifiNetInfo.isConnectedOrConnecting()) {
				netType = NetType.Wifi;
			} else {
				NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobNetInfo != null && mobNetInfo.isConnectedOrConnecting()) {
					String info = mobNetInfo.getExtraInfo().toLowerCase();
					if(!info.contains("wap") && !info.contains("net")) {
				        Cursor cursor =  context.getContentResolver().query(Uri.parse(PREFERRED_APN_URI), null, null, null, "name ASC");
				        if (cursor.getCount() > 0) {
				            cursor.moveToFirst();
				            info = cursor.getString(5);
				        }
				        cursor.close();
					}
					if(info.contains("cmnet"))
						netType = NetType.CMNet;
					else if(info.contains("cmwap"))
						netType = NetType.CMWap;
					else if(info.contains("uninet"))
						netType = NetType.UNNet;
					else if(info.contains("uniwap"))
						netType = NetType.UNWap;
					else if(info.contains("ctnet"))
						netType = NetType.CTNet;
					else if(info.contains("ctwap"))
						netType = NetType.CTWap;
					else if(info.contains("3gnet"))
						netType = NetType.G3Net;
					else if(info.contains("3gwap"))
						netType = NetType.G3Wap;
					else
						netType = NetType.Unknown;
				} else {
					netType = NetType.Unknown;
				}
				mobNetInfo = null;
			}
			wifiNetInfo = null;
			connectivityManager = null;
		} catch (Exception e) {
			LogcatUtils.e(mTag, e);
		}
		return netType;
	}
	
	/**
	 * 判断是否是2G网络
	 * @return
	 */
	public static boolean isNetWork2G(Context context){
		boolean res = false;
		try {
			ConnectivityManager connectivityManager = 
					(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(connectivityManager == null){
				return true;
			}
			NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetInfo != null && wifiNetInfo.isConnectedOrConnecting()) {
				return false;
			}
			NetworkInfo  activeNetInfo = connectivityManager.getActiveNetworkInfo();
			if(activeNetInfo == null){
				res = false;
			}else if(activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE){
				int subType = activeNetInfo.getSubtype();
				if(subType==TelephonyManager.NETWORK_TYPE_1xRTT || subType == TelephonyManager.NETWORK_TYPE_EDGE
						||subType == TelephonyManager.NETWORK_TYPE_EVDO_0 || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
						||subType == TelephonyManager.NETWORK_TYPE_GPRS){
					res = true;
				}else{
					res = false;
				}
			}else if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI){
				res = false;
			}else{
				res = false;
			}
		} catch (Exception e) {
			LogcatUtils.e(mTag, e);
		}
		return res;
	}
	
	/**
	 * 判断是否是cmwap网络
	 * @return
	 */
	public static boolean isNetWorkCMWap(Context context){
		boolean res = false;
		NetType state = getNetworkType(context);
		if(state == NetType.CMWap || state == NetType.UNWap || state == NetType.G3Wap)
			res = true;
		return res;
	}
	
	public static String replaceURLHost(String original, String host) {
        String re = "";
        final String http = "http://";
        int idx = 0;
        if (original.startsWith(http)) {
            idx = http.length()
                    + original.substring(http.length(), original.length())
                            .indexOf('/');
        } else {
            idx = original.indexOf('/');
        }
        re = original.substring(idx + 1, original.length());
        return host + re;
    }
	
	/**
	 * 网络连接类型
	 * @author lijunma
	 *
	 */
	public static enum NetType {
		Unknown(0),
		Wifi(1),
		CMNet(2),
		CMWap(3),
		UNNet(4),
		UNWap(5),
		CTNet(6),
		CTWap(7),
		G3Wap(8),
		G3Net(9);
		
		private int value = 0;
	    private NetType(int value) {
	        this.value = value;
	    }
	    public static NetType valueOf(int value) {
	        switch (value) {
	        case 0:
	            return Unknown;
	        case 1:
	            return Wifi;
	        case 2:
	            return CMNet;
	        case 3:
	            return CMWap;
	        case 4:
	            return UNNet;
	        case 5:
	            return UNWap;
	        case 6:
	            return CTNet;
	        case 7:
	            return CTWap;
	        case 8:
	            return G3Wap;
	        case 9:
	            return G3Net;
	        default:
	            return Unknown;
	        }
	    }

	    public int value() {
	        return this.value;
	    }
	}
	
}