package com.jw.acccount;

import android.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.jw.business.bean.Account;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityMqrBinding;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseActivity;

/**
 * Created by Administrator on 2017/4/15.
 */

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：扫二维码，添加自己为好友页面
 */

public class MQrActivity extends BaseActivity implements NormalTopBar.BackListener{
    private Account me = ChatApplication.getAccount();
    private ActivityMqrBinding mBinding;

    @Override
    public void bindView() {
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_mqr);
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).load(me.getIcon()).into(mBinding.ivQrIcon);
        mBinding.ntQr.setBackListener(this);
    }

    @Override
    public void back() {
        finish();
    }
}
