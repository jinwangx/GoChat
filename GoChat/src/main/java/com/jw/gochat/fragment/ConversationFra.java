package com.jw.gochat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.activity.MessageActivity;
import com.jw.gochat.adapter.ChatAdapter;
import com.jw.gochat.base.BaseFragment;
import com.jw.gochat.bean.Account;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.MessageDao;
import com.jw.gochat.receiver.PushReceiver;
import com.jw.gochat.view.ListChat;

import butterknife.BindView;

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话列表页面
 */

public class ConversationFra extends BaseFragment implements AdapterView.OnItemClickListener
        ,ChatAdapter.ChatListener{
    @BindView(R.id.lv_cvst)
    ListChat lvChat;
    private ChatAdapter adapter;
    private Account me= ChatApplication.getAccount();
    private PushReceiver pushReceiver = new PushReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String to = intent.getStringExtra(PushReceiver.KEY_TO);
            if (me.getAccount().equalsIgnoreCase(to)) {
                loadData();
            }
        }
    };

    @Override
    protected View bindView() {
        View view=View.inflate(getActivity(), R.layout.fragment_cvst, null);
        return view;
    }

    @Override
    protected void init() {
        super.init();
        //动态注册一个收到消息的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        getActivity().registerReceiver(pushReceiver, filter);
    }

    protected void loadData() {
        MessageDao dao = new MessageDao(getActivity());
        Cursor cursor = dao.queryConversation(me.getAccount());
        adapter = new ChatAdapter(getActivity(), cursor);
        lvChat.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        lvChat.setOnItemClickListener(this);
        adapter.setChatListener(this);
    }

    //会话列表Item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("me", me);
        bundle.putSerializable("receiver", (Friend) view.getTag());
        intent.setClass(getActivity(), MessageActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //item侧滑后，点击删除时，执行该操作
    @Override
    public void delete(int position) {

    }

    //item侧滑后，点击置顶时，执行该操作
    @Override
    public void toTop(int position) {

    }
}

