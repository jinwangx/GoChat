package com.jw.gochat.activity;

import android.widget.EditText;

import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.utils.ThemeUtils;
import com.jw.gochat.view.NormalTopBar;

import Lib.GoChatManager;
import Lib.callback.GoChatCallBack;
import Lib.msg.ChatMessage;
import Lib.msg.InvitationBody;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：朋友验证页面
 */

public class FriendValidateActivity extends BaseActivity implements NormalTopBar.BackListener{

    private EditText etValidate;
    private NormalTopBar ntValidate;
    private String receiver;

    @Override
    public void bindView() {
        setContentView(R.layout.activity_friend_validate);
    }

    @Override
    protected void initView() {
        super.initView();
        etValidate = (EditText) findViewById(R.id.et_validate_msg);
        ntValidate = (NormalTopBar) findViewById(R.id.nt_validate);
        receiver = getIntent().getStringExtra("receiver");
        ntValidate.setBackListener(this);
        ntValidate.setSendListener(sendValidateListener);
    }

    private NormalTopBar.SendValidateListener sendValidateListener=new NormalTopBar.SendValidateListener() {
        @Override
        public void send() {
            String content=etValidate.getText().toString();
            if(content!=null){
                Account account = ChatApplication.getAccount();
                ChatMessage message = ChatMessage
                        .createMessage(ChatMessage.Type.INVITATION);
                message.setBody(new InvitationBody(content));
                message.setAccount(account.getAccount());
                message.setToken(account.getToken());
                message.setReceiver(receiver);

                GoChatManager.getInstance().sendMessage(message, new GoChatCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        ThemeUtils.show(FriendValidateActivity.this, "邀请发送成功");
                        finish();
                    }

                    @Override
                    public void onProgress(int progress) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int error, String msg) {
                        // TODO Auto-generated method stub
                        ThemeUtils.show(FriendValidateActivity.this, "邀请发送失败");
                    }
                });
            }
        }
    };

    @Override
    public void back() {
        finish();
    }
}
