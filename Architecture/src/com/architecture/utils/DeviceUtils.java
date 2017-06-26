package com.architecture.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tool.utils.WindowManager;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtils {

    /**
     * 获取SIM卡运营商
     * @return
     */
    public static String getOperators(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = null;
        String IMSI = tm.getSubscriberId();
        if (IMSI == null || IMSI.equals("")) {
            return operator;
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            operator = "移动";
        } else if (IMSI.startsWith("46001")) {
            operator = "联通";
        } else if (IMSI.startsWith("46003")) {
            operator = "电信";
        }
        return operator;
    }

    public static String getImei(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        return IMEI;
    }

    /** 判断手机是否root，不弹出root请求框<br/> */
    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath))
            return true;
        if (new File(xBinPath).exists() && isExecutable(xBinPath))
            return true;
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(p!=null){
                p.destroy();
            }
        }
        return false; 
    }

    public static String getScreenResolution(){
        WindowManager wManager = WindowManager.getInstance() ;
        int widthPixels=wManager.getScreenWidth();
        int heightPixels=wManager.getScreenHeight();
        return "width = "+String.valueOf(widthPixels)+"; height = "+String.valueOf(heightPixels);
    }
}
