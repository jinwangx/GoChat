package com.jw.gochat.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;

/**
 * 创建时间：2017/11/2 1:34
 * 更新时间 2017/11/2 1:34
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class CommonUtil {
    //得到任务目录
    public static String getTaskDir(Context context) {
        String dir = getDir(context, "task");
        Log.v("GoChat_taskDir",dir);
        return dir;
    }

    //得到头像目录
    public static String getIconDir(Context context) {
        String dir = getDir(context, "icon");
        Log.v("GoChat_iconDir",dir);
        return dir;
    }

    public static String getFriendIconDir(Context context){
        String dir=getDir(context,"friendIcon");
        Log.v("GoChat_friendIcon",dir);
        return dir;
    }

    private static String getDir(Context context, String path) {
        String state = Environment.getExternalStorageState();
        //如果存储卡挂载
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root, "Android/data/"
                    + context.getPackageName() + "/" + path);
            File parentFile = dir.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            return dir.getAbsolutePath();
        } else {
            File dir = new File(context.getFilesDir(), path);
            File parentFile = dir.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            return dir.getAbsolutePath();
        }
    }


    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }



    public static final String[] NAMES = new String[] {"了解会员特权","我的钱包","个性装扮","我的收藏","我的相册","我的文件" };



}
