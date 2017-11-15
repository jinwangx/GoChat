package com.jw.gochat.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jw.gochat.R;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.utils.ThemeUtils;
import com.jw.gochat.view.NormalTopBar;

import butterknife.BindView;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：更多朋友页面
 */

public class FriendAddActivity extends BaseActivity implements View.OnClickListener,NormalTopBar.BackListener{

    @BindView(R.id.nt_new_friend)
    NormalTopBar ntNewFriend;
    @BindView(R.id.ll_friend_search)
    LinearLayout llSearch;
    @BindView(R.id.rl_scan_code)
    RelativeLayout rlScanCode;
    @Override
    public void bindView() {
        setContentView(R.layout.activity_new_friend);
    }

    @Override
    protected void initView() {
        ntNewFriend.setBackListener(this);
        llSearch.setOnClickListener(this);
        rlScanCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_friend_search:
                finish();
                startActivity(new Intent(FriendAddActivity.this, FriendSearchActivity.class));
                break;
            case R.id.rl_scan_code:
                ThemeUtils.show(FriendAddActivity.this,"暂未开放该功能");
                break;
        }
    }

    @Override
    public void back() {
        finish();
    }
}
