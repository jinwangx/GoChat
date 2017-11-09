package com.jw.gochat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.activity.FriendDetailActivity;
import com.jw.gochat.activity.InvitationActivity;
import com.jw.gochat.adapter.FriendsAdapter;
import com.jw.gochat.base.BaseFragment;
import com.jw.gochat.bean.Account;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.db.InvitationDao;
import com.jw.gochat.receiver.PushReceiver;
import com.jw.gochat.utils.ToastUtils;

import butterknife.BindView;

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 0:23
 * 版本：
 * 作者：Mr.jin
 * 描述：好友页面
 */

public class FriendsFra extends BaseFragment implements View.OnClickListener
        ,AdapterView.OnItemClickListener{

    @BindView(R.id.lv_friends)
    ListView lvContacts;
    private View headView;
    private ImageView ivUnread;
    private Account me = ChatApplication.getAccount();
    private PushReceiver invitedReceiver = new PushReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String to = intent.getStringExtra(PushReceiver.KEY_TO);
            ToastUtils.show(getActivity(),"接收到邀请");
            if (me.getAccount().equalsIgnoreCase(to)) {
                loadData();
            }
        }
    };
    private PushReceiver reInvitationReceiver=new PushReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String to = intent.getStringExtra(PushReceiver.KEY_TO);
            String from = intent.getStringExtra(PushReceiver.KEY_FROM);
            ToastUtils.show(getActivity(),from+"已接受邀请");
            if (me.getAccount().equalsIgnoreCase(to)) {
                loadData();
            }
        }
    };

    @Override
    protected void init() {
        super.init();
        //动态注册一个收到邀请的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_INVATION);
        getActivity().registerReceiver(invitedReceiver, filter);

        //动态注册一个朋友接收邀请的广播
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(PushReceiver.ACTION_REINVATION);
        getActivity().registerReceiver(reInvitationReceiver, filter2);

        receivers.add(invitedReceiver);
        receivers.add(reInvitationReceiver);
    }

    @Override
    public View bindView() {
        View view=View.inflate(getActivity(), R.layout.fragment_friends, null);
        return view;
    }

    @Override
    protected void initView() {
        lvContacts.addHeaderView(initHeaderView());
        headView.setOnClickListener(this);
        lvContacts.setOnItemClickListener(this);
    }

    private View initHeaderView() {
        headView = View.inflate(getActivity(), R.layout.include_head_friends, null);
        ivUnread = (ImageView) headView.findViewById(R.id.iv_has_new);
        headView.measure(0, 0);
        int headHeight = headView.getHeight();
        int headWidth = headView.getWidth();
        headView.setPadding(0, 0, headWidth, headHeight);
        return headView;
    }

    protected void loadData() {
        FriendDao friendDao = new FriendDao(getActivity());
        Cursor cursor = friendDao.queryFriends(me.getAccount());
        InvitationDao invitationDao = new InvitationDao(getActivity());
        boolean hasUnAgree = invitationDao.hasUnagree(me.getAccount());
        if (hasUnAgree)
            ivUnread.setVisibility(View.VISIBLE);
        else
            ivUnread.setVisibility(View.INVISIBLE);
        FriendsAdapter adapter = new FriendsAdapter(getActivity(), cursor);
        lvContacts.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), InvitationActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Friend friend = (Friend) view.getTag();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("friend", friend);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), FriendDetailActivity.class);
        startActivity(intent);
    }
}
