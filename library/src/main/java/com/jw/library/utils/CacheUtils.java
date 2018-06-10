package com.jw.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;

/**
 * 缓存工具类
 * 
 * @author Kevin
 * 
 */
public class CacheUtils {
	public static String PREF_NAME="config";

	/**
     * 设置缓存目录的名字，默认为"config"
	 * @param name
	 */
	public static void setCacheDirectoryName(String name){
		PrefUtils.setCacheDirectoryName(name);
	}

	/**
	 * 设置缓存
	 * @param key url
	 * @param value json
	 * @param ctx
	 */
	public static void setCache(String key, String value, Context ctx) {
		PrefUtils.setString(ctx, key, value);
	}
	public static void setCache(String key, boolean value, Context ctx) {
		PrefUtils.setBoolean(ctx, key, value);
	}
	public static void setCache(String key, int value, Context ctx) {
		PrefUtils.setInt(ctx, key, value);
	}
	public static void setCache(String key, float value, Context ctx) {
		PrefUtils.setFloat(ctx, key, value);
	}
	public static void setCache(String key, long value, Context ctx) {
		PrefUtils.setLong(ctx, key, value);
	}


	/**
	 * 获取缓存
	 * @param key
	 * @param ctx
	 * @return
	 */
	public static String getCacheString(String key, String defaultString,Context ctx) {
		return PrefUtils.getString(ctx, key, defaultString);
	}
	public static int getCacheInt(String key,int defaultInt ,Context ctx) {
		return PrefUtils.getInt(ctx, key, defaultInt);
	}

	/**
	 * 默认为false
	 * @param key
	 * @param ctx
	 * @return
	 */
	public static boolean getCacheBoolean(String key,boolean defaultBoolean,Context ctx) {
		return PrefUtils.getBoolean(ctx, key, defaultBoolean);
	}
	public static float getCacheFloat(String key, Context ctx) {
		return PrefUtils.getFloat(ctx, key, 0);
	}
	public static long getCacheLong(String key, Context ctx) {
		return PrefUtils.getLong(ctx, key, 0);
	}

	/**
	 * 清除缓存
	 * @param key
	 * @param ctx
	 */
	public static void clearCache(String key,Context ctx){
		PrefUtils.remove(ctx,key);
	}

	public static void clearCacheAll(Context ctx){
		PrefUtils.clear(ctx);
	}

	public static boolean CacheContains(String key,Context ctx){
		return PrefUtils.contains(ctx,key);
	}

	public static void clearImageCache(Context context){
		Glide.get(context).clearDiskCache();
		//Glide.get(context).clearMemory();
	}

	public static void clear(Context context){
		clearCacheAll(context);
		clearImageCache(context);
	}

	public static Double getCacheSize(Context context,String jsonCachePath,String imageCachePath){
		jsonCachePath=context.getFilesDir().getParent()+"/shared_prefs/"+PREF_NAME+".xml";
		return getImageCacheSize(imageCachePath)+getJsonCacheSize(jsonCachePath);
	}

	public static Double getJsonCacheSize(String jsonCachePath){
		return FileUtils.getFileOrFilesSize(jsonCachePath,3);
	}

	public static Double getImageCacheSize(String imageCachePath){
		return FileUtils.getFileOrFilesSize(imageCachePath,3);
	}


	public static class PrefUtils {

		public static void setCacheDirectoryName(String name){
			PREF_NAME=name;
		}

		public static void setBoolean(Context ctx, String key, boolean value) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().putBoolean(key, value).commit();
		}
		public static void setString(Context ctx, String key, String value) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().putString(key, value).commit();
		}
		public static void setInt(Context ctx, String key, int value) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().putInt(key, value).commit();
		}
		public static void setFloat(Context ctx, String key, float value) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().putFloat(key, value).commit();
		}
		public static void setLong(Context ctx, String key, long value) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().putLong(key, value).commit();
		}

		/**
		 *
		 * @param ctx
		 * @param key
		 * @param defaultValue 得到对应键的缓存，如果没有该键值，返回一个默认值
		 * @return
		 */
		public static String getString(Context ctx, String key, String defaultValue) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			return sp.getString(key, defaultValue);
		}
		public static int getInt(Context ctx, String key, int defaultValue) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			return sp.getInt(key, defaultValue);
		}
		public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			return sp.getBoolean(key, defaultValue);
		}
		public static long getLong(Context ctx, String key, long defaultValue) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			return sp.getLong(key, defaultValue);
		}
		public static float getFloat(Context ctx, String key, float defaultValue) {
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			return sp.getFloat(key, defaultValue);
		}

		/**
         * 移除对应键的缓存
		 */
		public static void remove(Context ctx,String key){
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().remove(key).commit();
		}

		public static void clear(Context ctx){
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			sp.edit().clear().commit();
		}

		public static boolean contains(Context ctx,String key){
			SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
					Context.MODE_PRIVATE);
			return sp.contains(key);
		}
	}

}
