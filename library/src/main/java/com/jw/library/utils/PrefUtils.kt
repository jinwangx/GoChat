package com.jw.library.utils

import android.content.Context

object PrefUtils {
    var PREF_NAME = "config"

    fun setCacheDirectoryName(name: String) {
        PREF_NAME = name
    }

    fun setBoolean(ctx: Context, key: String, value: Boolean) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).apply()
    }

    fun setString(ctx: Context, key: String, value: String) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
    }

    fun setInt(ctx: Context, key: String, value: Int) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putInt(key, value).apply()
    }

    fun setFloat(ctx: Context, key: String, value: Float) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putFloat(key, value).apply()
    }

    fun setLong(ctx: Context, key: String, value: Long) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().putLong(key, value).apply()
    }

    /**
     *
     * @param ctx
     * @param key
     * @param defaultValue 得到对应键的缓存，如果没有该键值，返回一个默认值
     * @return
     */
    fun getString(ctx: Context, key: String, defaultValue: String): String? {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getString(key, defaultValue)
    }

    fun getInt(ctx: Context, key: String, defaultValue: Int): Int {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getInt(key, defaultValue)
    }

    fun getBoolean(ctx: Context, key: String, defaultValue: Boolean): Boolean {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getBoolean(key, defaultValue)
    }

    fun getLong(ctx: Context, key: String, defaultValue: Long): Long {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getLong(key, defaultValue)
    }

    fun getFloat(ctx: Context, key: String, defaultValue: Float): Float {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.getFloat(key, defaultValue)
    }

    /**
     * 移除对应键的缓存
     */
    fun remove(ctx: Context, key: String) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().remove(key).apply()
    }

    fun clear(ctx: Context) {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        sp.edit().clear().apply()
    }

    fun contains(ctx: Context, key: String): Boolean {
        val sp = ctx.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE)
        return sp.contains(key)
    }
}