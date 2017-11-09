package com.jw.gochat.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.view.NormalTopBar;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.iv_qr_code)
    ImageView ivQr;
    @BindView(R.id.iv_qr_icon)
    CircleImageView ivMIcon2;
    @BindView(R.id.tv_qr_act)
    TextView tvMAccount;
    @BindView(R.id.nt_qr)
    NormalTopBar ntQr;
    private Account me = ChatApplication.getAccount();

    @Override
    public void bindView() {
        setContentView(R.layout.activity_mqr);
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).load(me.getIcon()).into(ivMIcon2);
        ntQr.setBackListener(this);
    }

    @Override
    public void back() {
        finish();
    }
}
