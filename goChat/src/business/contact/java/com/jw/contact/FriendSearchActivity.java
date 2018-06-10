package com.jw.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.jw.business.bean.Account;
import com.jw.business.bean.Contact;
import com.jw.business.db.dao.FriendDao;
import com.jw.chat.GoChatManager;
import com.jw.chat.callback.GoChatObjectCallBack;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityFriendSearchBinding;
import com.jw.gochat.view.DialogLoading;
import com.jw.gochatbase.BaseActivity;
import com.jw.library.utils.ThemeUtils;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：搜索用户页面
 */

public class FriendSearchActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private DialogLoading dialog;
    private ActivityFriendSearchBinding mBinding;

    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_search);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mBinding.ivSearchBack.setOnClickListener(this);
        mBinding.llSearchAct.setOnClickListener(this);
        mBinding.etSearchAct.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.ll_search_act:
                String friendAccount = mBinding.etSearchAct.getText().toString().trim();
                Account me = ChatApplication.getAccount();
                if (me.getAccount().equals(friendAccount)) {
                    ThemeUtils.show(this, "不要找自己啦");
                    return;
                }
                // 已有的朋友
                FriendDao dao = new FriendDao(this);
                Contact friend = dao.queryFriendByAccount(me.getAccount(), friendAccount);
                if (friend != null) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friend", friend);
                    intent.putExtras(bundle);
                    intent.setClass(this, FriendDetailActivity.class);
                    this.startActivity(intent);
                    return;
                }

                dialog = new DialogLoading(this);
                dialog.show();
                if (friendAccount != null) {
                    GoChatManager.getInstance(ChatApplication.getOkHttpClient()).searchContact(friendAccount, friendSearchCallBack);
                }
                break;
        }
    }

    private GoChatObjectCallBack friendSearchCallBack = new GoChatObjectCallBack<Contact>() {
        @Override
        public void onSuccess(Contact friend) {
            dialog.dismiss();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("friend", friend);
            finish();
            intent.putExtras(bundle);
            intent.setClass(FriendSearchActivity.this, FriendDetailActivity.class);
            startActivity(intent);
        }

        @Override
        public void onError(int error, String msg) {
            dialog.dismiss();
            ThemeUtils.show(FriendSearchActivity.this, msg);
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            mBinding.llSearchAct.setVisibility(View.VISIBLE);
            mBinding.tvSearchAct.setText(s);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            mBinding.llSearchAct.setVisibility(View.GONE);
        }
    }
}
