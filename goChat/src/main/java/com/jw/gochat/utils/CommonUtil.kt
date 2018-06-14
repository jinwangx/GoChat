package com.jw.gochat.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File

/**
 * 创建时间：2017/11/2 1:34
 * 更新时间 2017/11/2 1:34
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

object CommonUtil {


    val NAMES = arrayOf("了解会员特权", "我的钱包", "个性装扮", "我的收藏", "我的相册", "我的文件")
    //得到任务目录
    fun getTaskDir(context: Context): String {
        val dir = getDir(context, "task")
        Log.v("taskDir", dir)
        return dir
    }

    //得到头像目录
    fun getIconDir(context: Context): String {
        val dir = getDir(context, "icon")
        Log.v("iconDir", dir)
        return dir
    }

    fun getFriendIconDir(context: Context): String {
        val dir = getDir(context, "friendIcon")
        Log.v("friendIcon", dir)
        return dir
    }

    private fun getDir(context: Context, path: String): String {
        val state = Environment.getExternalStorageState()
        //如果存储卡挂载
        if (Environment.MEDIA_MOUNTED.equals(state, ignoreCase = true)) {
            val root = Environment.getExternalStorageDirectory()
            val dir = File(root, "Android/data/"
                    + context.packageName + "/" + path)
            val parentFile = dir.parentFile
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            return dir.absolutePath
        } else {
            val dir = File(context.filesDir, path)
            val parentFile = dir.parentFile
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            return dir.absolutePath
        }
    }


    fun parseIatResult(json: String): String {
        val ret = StringBuffer()
        try {
            val tokener = JSONTokener(json)
            val joResult = JSONObject(tokener)
            val words = joResult.getJSONArray("ws")
            for (i in 0 until words.length()) {
                // 转写结果词，默认使用第一个结果
                val items = words.getJSONObject(i).getJSONArray("cw")
                val obj = items.getJSONObject(0)
                ret.append(obj.getString("w"))
                //				如果需要多候选结果，解析数组其他字段
                //				for(int j = 0; j < items.length(); j++)
                //				{
                //					JSONObject obj = items.getJSONObject(j);
                //					ret.append(obj.getString("w"));
                //				}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret.toString()
    }
}