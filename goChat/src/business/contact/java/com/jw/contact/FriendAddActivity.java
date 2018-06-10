package com.jw.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityNewFriendBinding;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseActivity;
import com.jw.library.utils.ThemeUtils;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：更多朋友页面
 */

public class FriendAddActivity extends BaseActivity implements View.OnClickListener, NormalTopBar.BackListener {

    private ActivityNewFriendBinding mBinding;

    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_friend);
    }

    @Override
    protected void initView() {
        mBinding.ntNewFriend.setBackListener(this);
        mBinding.llFriendSearch.setOnClickListener(this);
        mBinding.rlScanCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_friend_search:
                finish();
                startActivity(new Intent(FriendAddActivity.this, FriendSearchActivity.class));
                break;
            case R.id.rl_scan_code:
                ThemeUtils.show(FriendAddActivity.this, "暂未开放该功能");
                break;
        }
    }

    @Override
    public void back() {
        finish();
    }
}
