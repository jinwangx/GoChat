package com.jw.gochatbase;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：
 * 更新时间 2017/11/2 16:31
 * 版本：
 * 作者：Mr.jin
 */

public abstract class BaseActivity extends FragmentActivity {

    protected List<BroadcastReceiver> receivers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //ThemeUtils.changeStatusBar(this, Color.BLACK);
        //((ChatApplication)getApplication()).addActivity(this);
        bindView();
        init();
        initView();
        loadData();
        initEvent();
    }

    /**
     * 在初始化视图之前执行，如注册广播接收者，还有一些初始化操作
     */
    protected void init() {

    }

    /**
     * 绑定视图
     */
    protected abstract void bindView();

    /**
     * 初始化视图
     */
    protected void initView() {
    }

    /**
     * 初始化监听事件
     */
    protected void initEvent() {

    }

    /**
     * 读取数据
     */
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receivers.size() != 0) {
            for (int i = 0; i < receivers.size(); i++) {
                unregisterReceiver(receivers.get(i));
            }
        }
        //((ChatApplication)getApplication()).removeActivity(this);
    }
}
