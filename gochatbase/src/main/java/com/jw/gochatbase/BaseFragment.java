package com.jw.gochatbase;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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

public abstract class BaseFragment extends Fragment {
    protected View view;
    protected List<BroadcastReceiver> receivers = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 防止fragment视图重建，浪费资源
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = bindView();
            initView();
            loadData();
            initEvent();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    /**
     * 绑定视图
     *
     * @return
     */
    protected abstract View bindView();

    /**
     * 初始化一些与Activity无关的全局变量，还有注册广播接收者等
     */
    protected void init() { }

    /**
     * 初始化视图
     */
    protected void initView() {
        loadData();
    }

    /**
     * 初始化各种事件
     */
    protected void initEvent() { }

    /**
     * 读取数据
     */
    protected void loadData() { }

    /**
     * fragment交互时，重读数据
     */
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * 在fragment销毁时解绑butterknife与视图id的绑定，遍历并反注册广播接收者
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receivers.size() != 0) {
            for (int i = 0; i < receivers.size(); i++) {
                getActivity().unregisterReceiver(receivers.get(i));
            }
        }
    }
}