package com.jw.contact;

import android.databinding.DataBindingUtil;

import com.jw.business.bean.Account;
import com.jw.chat.GoChatManager;
import com.jw.chat.callback.GoChatCallBack;
import com.jw.chat.msg.ChatMessage;
import com.jw.chat.msg.InvitationBody;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityFriendValidateBinding;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseActivity;
import com.jw.library.utils.ThemeUtils;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：朋友验证页面
 */

public class FriendValidateActivity extends BaseActivity implements NormalTopBar.BackListener {

    private String receiver;
    private ActivityFriendValidateBinding mBinding;

    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_validate);
    }

    @Override
    protected void initView() {
        super.initView();
        receiver = getIntent().getStringExtra("receiver");
        mBinding.ntValidate.setBackListener(this);
        mBinding.ntValidate.setSendListener(sendValidateListener);
    }

    private NormalTopBar.SendValidateListener sendValidateListener = new NormalTopBar.SendValidateListener() {
        @Override
        public void send() {
            String content = mBinding.etValidateMsg.getText().toString();
            if (content != null) {
                Account account = ChatApplication.getAccount();
                ChatMessage message = ChatMessage
                        .createMessage(ChatMessage.Type.INVITATION);
                message.setBody(new InvitationBody(content));
                message.setAccount(account.getAccount());
                message.setToken(account.getToken());
                message.setReceiver(receiver);

                GoChatManager.getInstance(ChatApplication.getOkHttpClient()).sendMessage(message, new GoChatCallBack() {

                    @Override
                    public void onSuccess() {
                        ThemeUtils.show(FriendValidateActivity.this, "邀请发送成功");
                        finish();
                    }

                    @Override
                    public void onProgress(int progress) {
                    }

                    @Override
                    public void onError(int error, String msg) {
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
