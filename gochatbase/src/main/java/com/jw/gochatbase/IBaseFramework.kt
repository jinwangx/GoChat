package com.sencent.library.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

/**
 * 由 jinwangx 创建于 2017/9/22.
 */
interface IBaseFramework {
    fun doLaunch()

    fun doRefresh()

    fun isActivity(): Boolean

    fun getActivity(): FragmentActivity?

    fun <T : View> findViewById(viewId: Int): T?

    /**
    设置刷新阈值
    当onPause() -> onResume()发生的间隔大于刷新阈值后，会触发doResume(binding)函数，通知代码刷新页面内容
     */
    fun setRefreshThreshold(refreshThreshold: Long)

    fun start(intent: Intent)

    fun start(intent: Intent, requestCode: Int)

    fun start(intent: Intent, bundle: Bundle?)

    fun start(intent: Intent, requestCode: Int, bundle: Bundle?)

    fun setToolbar(toolbar: Toolbar)

    fun onToolbarNavigationClick()

    fun onToolbarMenuItemClick(menuItem: MenuItem): Boolean
}