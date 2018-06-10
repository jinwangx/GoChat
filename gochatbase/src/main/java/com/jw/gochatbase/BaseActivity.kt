package com.jw.gochatbase

import android.content.BroadcastReceiver
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import java.util.*

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:31
 * 版本：
 * 作者：Mr.jin
 */

abstract class BaseActivity : FragmentActivity() {

    private var receivers: List<BroadcastReceiver> = ArrayList()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        //ThemeUtils.changeStatusBar(this, Color.BLACK);
        //((ChatApplication)getApplication()).addActivity(this);
        bindView()
        init()
        initView()
        loadData()
        initEvent()
    }

    /**
     * 在初始化视图之前执行，如注册广播接收者，还有一些初始化操作
     */
    protected open fun init() {

    }

    /**
     * 绑定视图
     */
    protected abstract fun bindView()

    /**
     * 初始化视图
     */
    protected open fun initView() {}

    /**
     * 初始化监听事件
     */
    protected open fun initEvent() {}

    /**
     * 读取数据
     */
    protected open fun loadData() {}

    override fun onResume() {
        super.onResume()
        loadData()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (receivers.size != 0) {
            for (i in receivers.indices) {
                unregisterReceiver(receivers[i])
            }
        }
        //((ChatApplication)getApplication()).removeActivity(this);
    }
}