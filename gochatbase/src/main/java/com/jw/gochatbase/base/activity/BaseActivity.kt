package com.jw.gochatbase.base.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.jw.gochatbase.RefreshHelper
import com.jw.library.utils.ThemeUtils
import com.sencent.library.base.IBaseFramework
import com.sencent.library.base.activity.IActivityData
import com.sencent.library.base.activity.IActivityOtherFragmentData
import com.sencent.library.base.helper.ActivityFrameworkHelper
import com.sencent.library.base.helper.StarterHelper
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:31
 * 版本：
 * 作者：Mr.jin
 */

abstract class BaseActivity : AppCompatActivity(), IBaseFramework, IActivityData, IActivityOtherFragmentData {
    private val mRefreshHelper = RefreshHelper()
    private val mActivityFrameworkHelper = ActivityFrameworkHelper()
    private val addFragmentTagList = ArrayList<String>()
    protected var mCurrFragmentTag = ""

    abstract fun doInflate(activity: BaseActivity, savedInstanceState: Bundle?)

    abstract fun doConfig(arguments: Intent)

    override fun doLaunch() {
    }

    override fun doRefresh() {
    }

    override fun isActivity(): Boolean = true

    override fun getActivity(): FragmentActivity? = this

    protected open fun useImmersive() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        /*if (useImmersive() && Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }*/
        ThemeUtils.changeStatusBar(this, Color.BLACK)
        doInflate(this, savedInstanceState)
    }

    protected fun switchFragment(fragment: Fragment, resId: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val currFragment = this.supportFragmentManager
                .findFragmentByTag(mCurrFragmentTag)
        if (currFragment != null && currFragment !== fragment) {
            fragmentTransaction.hide(currFragment)
        }

        val fragmentClassName = fragment.javaClass
                .name
        if (!addFragmentTagList.contains(fragmentClassName)) {
            addFragmentTagList.add(fragmentClassName)
            fragmentTransaction.add(resId, fragment, fragmentClassName)
        } else {
            if (fragment.isAdded) {
                fragmentTransaction.show(fragment)
            } else {
                fragmentTransaction.add(resId, fragment, fragmentClassName)
            }
        }
        mCurrFragmentTag = fragment.javaClass
                .name
        fragmentTransaction.commitAllowingStateLoss()
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

    override fun onDestroy() {
        mRefreshHelper.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        mRefreshHelper.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        doLaunchOrRefresh()
    }

    fun isResume() = mRefreshHelper.isResume()

    protected open fun doLaunchOrRefresh() {
        val refreshHelper = mRefreshHelper
        refreshHelper.resume()
        if (refreshHelper.firstEnter()) {
            doConfig(intent)
            doLaunch()
        } else if (refreshHelper.shouldRefresh()) {
            doRefresh()
        }
    }

    final override fun setRefreshThreshold(refreshThreshold: Long) {
        mRefreshHelper.setRefreshThreshold(refreshThreshold)
    }

    override fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onToolbarNavigationClick()
        }
        toolbar.setOnMenuItemClickListener {
            onToolbarMenuItemClick(it)
        }
    }

    override fun setTitle(title: CharSequence?) {
        val supportActionBar = supportActionBar
        if (supportActionBar == null) {
            super.setTitle(title)
        } else {
            supportActionBar.title = title
        }
    }

    fun setSubTitle(subTitle: CharSequence?) {
        val supportActionBar = supportActionBar
        if (supportActionBar == null) {
            return
        } else {
            supportActionBar.subtitle = subTitle
        }
    }

    override fun onToolbarNavigationClick() {
        onBackPressed()
    }

    override fun onToolbarMenuItemClick(menuItem: MenuItem) = false

    @SuppressLint("RestrictedApi")
    override fun onPrepareOptionsPanel(view: View?, menu: Menu?): Boolean {
        if (menu != null) {
            if (menu::class.java.simpleName.equals("MenuBuilder", true)) {
                try {
                    val method = menu::class.java.getDeclaredMethod("setOptionalIconsVisible", java.lang.Boolean.TYPE)
                    method.isAccessible = true
                    method.invoke(menu, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu)
    }

    override fun sendDataToFragment(tag: String, name: String, data: Any?) {
        mActivityFrameworkHelper.sendDataToFragment(this, tag, name, data)
    }

    override fun onReceiveDataFromFragment(tag: String, name: String, data: Any?) {
    }

    final override fun onReceiveOtherFragmentData(senderTag: String, receiverTag: String, name: String, data: Any?) {
        mActivityFrameworkHelper.onReceiveOtherFragmentData(this, senderTag, receiverTag, name, data)
    }

    override fun onQueryDataFromFragment(tag: String, name: String) = null

    final override fun queryFragmentData(tag: String, name: String): Any? = mActivityFrameworkHelper.queryFragmentData(this, tag, name)
}