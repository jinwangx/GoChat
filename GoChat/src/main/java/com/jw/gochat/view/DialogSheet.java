package com.jw.gochat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jw.gochat.R;

/**
 * 创建时间：2017/11/2 21:13
 * 更新时间 2017/11/2 21:13
 * 版本：
 * 作者：Mr.jin
 * 描述：登陆页面忘记密码dialog，从bottom滑入
 */

public class DialogSheet extends Dialog implements View.OnClickListener {
    private DialogSheetListener mListener;

    public DialogSheet(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.y=20;
        setContentView(R.layout.dialog_loss_pwd);
        //初始化控件
        TextView tvFoundPass = (TextView) findViewById(R.id.tv_dialog_find_pwd);
        TextView tvLoginSms = (TextView) findViewById(R.id.tv_dialog_login_sms);
        TextView tvChangeIp = (TextView) findViewById(R.id.tv_change_ip);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvFoundPass.setOnClickListener(this);
        tvLoginSms.setOnClickListener(this);
        tvChangeIp.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_find_pwd:
                mListener.foundPass();
                break;
            case R.id.tv_dialog_login_sms:
                mListener.loginSms();
                break;
            case R.id.tv_change_ip:
                mListener.changeIp();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public interface DialogSheetListener{
        void foundPass();
        void loginSms();
        void changeIp();
    }
    public void setPassListener(DialogSheetListener passListener){
        mListener=passListener;
    }
}

