package com.jw.gochatbase.base.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.jw.gochatbase.RefreshHelper
import com.sencent.library.base.IBaseFramework
import com.sencent.library.base.fragment.IFragmentData
import com.sencent.library.base.fragment.IOtherFragmentData
import com.sencent.library.base.helper.FragmentFrameworkHelper
import com.sencent.library.base.helper.StarterHelper

/**
 * Created by Administrator on .
 */

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

abstract class BaseFragment : Fragment(),IBaseFramework, IFragmentData, IOtherFragmentData {
    private val mRefreshHelper: RefreshHelper = RefreshHelper()
    private val mFragmentFrameworkHelper = FragmentFrameworkHelper()

    abstract fun doInflate(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    abstract fun doConfig(arguments: Bundle?)

    override fun doLaunch() {
    }

    override fun doRefresh() {
    }

    override fun isActivity(): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = doInflate(inflater, container, savedInstanceState)

    @Suppress("UNCHECKED_CAST")
    override fun <T : View> findViewById(viewId: Int): T? {
        return try {
            view?.findViewById(viewId)
        } catch (e: ClassCastException) {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doConfig(arguments)
    }

    override fun onDestroyView() {
        mRefreshHelper.destroy()
        super.onDestroyView()
    }

    override fun start(intent: Intent) {
        start(intent, -1, null)
    }

    override fun start(intent: Intent, requestCode: Int) {
        start(intent, requestCode, null)
    }

    override fun start(intent: Intent, bundle: Bundle?) {
        start(intent, -1, bundle)
    }

    override fun start(intent: Intent, requestCode: Int, bundle: Bundle?) {
        StarterHelper.start(this, intent, requestCode, bundle)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentFrameworkHelper.onAttach(context)
    }

    override fun onDetach() {
        mFragmentFrameworkHelper.onDetach()
        super.onDetach()
    }

    override fun onPause() {
        mRefreshHelper.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mRefreshHelper.firstEnter()) {
            doLaunch()
        } else if (mRefreshHelper.shouldRefresh()) {
            doRefresh()
        }
    }

    final override fun setRefreshThreshold(refreshThreshold: Long) {
        mRefreshHelper.setRefreshThreshold(refreshThreshold)
    }

    override fun setToolbar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener {
            onToolbarNavigationClick()
        }
        toolbar.setOnMenuItemClickListener {
            onToolbarMenuItemClick(it)
        }
    }

    override fun onToolbarNavigationClick() {
    }

    override fun onToolbarMenuItemClick(menuItem: MenuItem) = false

    override fun sendDataToActivity(name: String, data: Any?) {
        mFragmentFrameworkHelper.sendDataToActivity(this, name, data)
    }

    override fun sendDataToOtherFragment(receiverTag: String, name: String, data: Any?) {
        mFragmentFrameworkHelper.sendDataToOtherFragment(this, receiverTag, name, data)
    }

    override fun onReceiveDataFromActivity(name: String, data: Any?) {
    }

    override fun onReceiveDataFromOtherFragment(senderTag: String, name: String, data: Any?) {
    }

    override fun onQueryDataFromActivity(name: String): Any? = null

    override fun queryActivityData(name: String) = mFragmentFrameworkHelper.queryActivityData(this, name)
}