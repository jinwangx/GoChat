package com.jw.gochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.view.NormalTopBar;

import Lib.GoChatURL;
import butterknife.BindView;

/**
 * 创建时间：2017/4/15
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：联系人详细信息
 */

public class FriendDetailActivity extends BaseActivity implements View.OnClickListener
        ,NormalTopBar.BackListener{

    @BindView(R.id.nt_friend_detail)
    NormalTopBar ntFriendDetail;
    @BindView(R.id.iv_list_item_friend_icon)
    de.hdodenhof.circleimageview.CircleImageView ivFriend;
    @BindView(R.id.tv_list_item_friend_name)
    TextView tvNameFriend;
    @BindView(R.id.tv_list_item_friend_act)
    TextView tvAccountFriend;
    @BindView(R.id.btn_friend_add)
    Button btnAdd;
    @BindView(R.id.btn_friend_send)
    Button btnSend;
    private Friend friend;
    private Account me = ChatApplication.getAccount();
    private FriendDao dao;

    @Override
    public void bindView() {
        setContentView(R.layout.activity_friend_detail);
    }

    @Override
    protected void initView() {
        super.initView();
        friend = (Friend) getIntent().getSerializableExtra("friend");
        dao = new FriendDao(FriendDetailActivity.this);
        if ((dao.queryFriendByAccount(me.getAccount(), friend.getAccount())) != null) {
            btnAdd.setVisibility(View.GONE);
            Glide.with(this).load(friend.getIcon()).into(ivFriend);
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            Glide.with(this).load(GoChatURL.BASE_HTTP+friend.getIcon().replace("\\","/")).into(ivFriend);
        }
        tvNameFriend.setText(friend.getName());
        tvAccountFriend.setText(friend.getAccount());
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        ntFriendDetail.setBackListener(this);
        btnAdd.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_friend_add:
                finish();
                Intent intent1 = new Intent();
                intent1.putExtra("receiver", friend.getAccount());
                intent1.setClass(FriendDetailActivity.this, FriendValidateActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_friend_send:
                Intent intent2 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("receiver", friend);
                bundle.putParcelable("me", me);
                intent2.putExtras(bundle);
                intent2.setClass(FriendDetailActivity.this, MessageActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void back() {
        finish();
    }
}
