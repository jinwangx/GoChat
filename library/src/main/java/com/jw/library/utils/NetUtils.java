package com.jw.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Author: Administrator
 * Created on:  2017/8/1.
 * Description:
 */

public class NetUtils {

    /**
     * 判断当前网络是否连接
     * @param context
     * @return 若已连接，则返回true
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取手机联网类型
     * @param context
     * @return wifi或者mobile
     */
    public static String isNetworkAvailable(final Context context) {
        String type="没联网";

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {

            if (net.getTypeName().equalsIgnoreCase("WIFI")) {
                if (net.isConnected()) {
                    type="WIFI";
                }
            }

            if (net.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (net.isConnected()) {
                    type="MOBILE";
                }
            }
        }
        return type;

    }


    /**
     * 获取手机卡类型
     * @param context
     * @return 无卡或者移动、联通、电信
     */
    private static String getMobileType(Context context)
    {
        String type = "无卡";
        TelephonyManager iPhoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iNumeric = iPhoneManager.getSimOperator();
        if (iNumeric.length() > 0)
        {
            if (iNumeric.equals("46000") || iNumeric.equals("46002"))
            {
                type="中国移动";
            }
            else if (iNumeric.equals("46001"))
            {
                type="中国联通";
            }
            else if (iNumeric.equals("46003"))
            {
                type="中国电信";
            }
        }
        return type;
    }


}
