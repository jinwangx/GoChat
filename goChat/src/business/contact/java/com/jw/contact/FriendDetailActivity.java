package com.jw.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.jw.business.bean.Account;
import com.jw.business.bean.Contact;
import com.jw.business.db.dao.FriendDao;
import com.jw.chat.GoChatURL;
import com.jw.chat.MessageActivity;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityFriendDetailBinding;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseActivity;

/**
 * 创建时间：2017/4/15
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：联系人详细信息
 */

public class FriendDetailActivity extends BaseActivity implements View.OnClickListener
        , NormalTopBar.BackListener {

    private Contact friend;
    private Account me = ChatApplication.getAccount();
    private FriendDao dao;
    private ActivityFriendDetailBinding mBinding;

    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_detail);
    }

    @Override
    protected void initView() {
        super.initView();
        friend = (Contact) getIntent().getSerializableExtra("friend");
        dao = new FriendDao(FriendDetailActivity.this);
        if ((dao.queryFriendByAccount(me.getAccount(), friend.getAccount())) != null) {
            mBinding.btnFriendAdd.setVisibility(View.GONE);
            Glide.with(this).load(friend.getIcon()).into(mBinding.ivListItemFriendIcon);
        } else {
            mBinding.btnFriendAdd.setVisibility(View.VISIBLE);
            Glide.with(this).load(GoChatURL.BASE_HTTP + friend.getIcon().replace("\\", "/")).into(mBinding.ivListItemFriendIcon);
        }
        mBinding.tvListItemFriendName.setText(friend.getName());
        mBinding.tvListItemFriendAct.setText(friend.getAccount());
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mBinding.ntFriendDetail.setBackListener(this);
        mBinding.btnFriendAdd.setOnClickListener(this);
        mBinding.btnFriendSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
