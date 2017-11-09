package com.jw.gochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.utils.ToastUtils;
import com.jw.gochat.view.DialogLoading;

import Lib.GoChatManager;
import Lib.callback.GoChatObjectCallBack;
import butterknife.BindView;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：搜索用户页面
 */

public class FriendSearchActivity extends BaseActivity implements TextWatcher,View.OnClickListener {

    @BindView(R.id.iv_search_back)
    ImageView ivBack;
    @BindView(R.id.et_search_act)
    EditText etSearch;
    @BindView(R.id.tv_search_act)
    TextView tvSearch;
    @BindView(R.id.ll_search_act)
    LinearLayout llSearch;
    private DialogLoading dialog;

    @Override
    public void bindView() {
        setContentView(R.layout.activity_friend_search);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        ivBack.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        etSearch.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.ll_search_act:
                String friendAccount = etSearch.getText().toString().trim();
                Account me = ChatApplication.getAccount();
                if (me.getAccount().equals(friendAccount)) {
                    ToastUtils.show(this, "不要找自己啦");
                    return;
                }
                // 已有的朋友
                FriendDao dao = new FriendDao(this);
                Friend friend = dao.queryFriendByAccount(me.getAccount(), friendAccount);
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
                    GoChatManager.getInstance().searchContact(friendAccount,friendSearchCallBack);
                }
                break;
        }
    }

    private GoChatObjectCallBack friendSearchCallBack=new GoChatObjectCallBack<Friend>() {
        @Override
        public void onSuccess(Friend friend) {
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
            ToastUtils.show(FriendSearchActivity.this, msg);
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            llSearch.setVisibility(View.VISIBLE);
            tvSearch.setText(s);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            llSearch.setVisibility(View.GONE);
        }
    }
}
