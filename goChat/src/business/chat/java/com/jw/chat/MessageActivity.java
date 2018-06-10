package com.jw.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jw.business.bean.Account;
import com.jw.business.bean.Contact;
import com.jw.chat.callback.GoChatCallBack;
import com.jw.chat.db.bean.Message;
import com.jw.chat.db.dao.MessageDao;
import com.jw.chat.msg.ChatMessage;
import com.jw.chat.msg.TextBody;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.adapter.MessageAdapter;
import com.jw.gochat.databinding.ActivityMessageBinding;
import com.jw.gochat.receiver.PushReceiver;
import com.jw.gochat.utils.CommonUtil;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseActivity;
import com.jw.library.utils.ThemeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：对话界面
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener, NormalTopBar.BackListener {

    //语音听写对象
    private RecognizerDialog mIatDialog;
    // 语音合成对象
    //private SpeechSynthesizer mTts;
    // 默认发音人
    //private String voicer = "xiaoyan";
    // 引擎类型
    //private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Account me = ChatApplication.getAccount();
    private Contact receiver;
    private MessageAdapter adapter;
    private PushReceiver pushReceiver = new PushReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String from = intent.getStringExtra(PushReceiver.KEY_FROM);
            if (receiver.getAccount().equalsIgnoreCase(from)) {
                loadData();
            }

        }
    };
    private Message msg;
    private MessageDao dao;
    private ActivityMessageBinding mBinding;

    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        registerReceiver(pushReceiver, filter);
        receiver = (Contact) getIntent().getSerializableExtra("receiver");
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.ntMsg.setTitle(receiver.getAccount());
        mBinding.ntMsg.setBackListener(this);
        MessageDao dao = new MessageDao(this);
        Cursor cursor = dao.queryMessage(me.getAccount(), receiver.getAccount());
        adapter = new MessageAdapter(this, cursor, receiver);
        mBinding.lvMsg.setAdapter(adapter);
        mBinding.lvMsg.setDividerHeight(0);
        mBinding.lvMsg.setClickable(false);
        mBinding.ivMsgSpeech.setOnClickListener(this);
        mBinding.btnMsgSend.setOnClickListener(this);
        //初始化语音听写对象
        mIatDialog = new RecognizerDialog(this, mInitListener);
    }

    protected void loadData() {
        MessageDao dao = new MessageDao(this);
        final Cursor cursor = dao.queryMessage(me.getAccount(), receiver.getAccount());
        adapter.changeCursor(cursor);
        mBinding.lvMsg.post(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mBinding.lvMsg.smoothScrollToPositionFromTop(cursor.getCount(), 0);
                } else {
                    mBinding.lvMsg.smoothScrollToPosition(cursor.getCount());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg_speech:
                // 设置参数
                setSpeechRecognizerParam();
                boolean isShowDialog = true;
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                }
                break;
            case R.id.btn_msg_send:
                send();
                break;
            default:
                break;
        }
    }

    public void send() {
        String content = mBinding.etMsgContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            mBinding.etMsgContent.setText("");
            // 存储到本地
            dao = new MessageDao(this);
            msg = new Message();
            msg.setAccount(receiver.getAccount());
            msg.setContent(content);
            msg.setCreateTime(System.currentTimeMillis());
            msg.setDirection(0);
            msg.setOwner(me.getAccount());
            msg.setRead(true);
            msg.setState(1);
            msg.setType(0);
            dao.addMessage(msg);
            loadData();

            ChatMessage message = ChatMessage.createMessage(ChatMessage.Type.TEXT);
            message.setAccount(me.getAccount());
            message.setToken(me.getToken());
            message.setReceiver(receiver.getAccount());
            message.setBody(new TextBody(content));
            GoChatManager.getInstance(ChatApplication.getOkHttpClient()).sendMessage(message, messageSendCallBack);
        }
    }

    private GoChatCallBack messageSendCallBack = new GoChatCallBack() {

        @Override
        public void onSuccess() {
            msg.setState(2);
            dao.updateMessage(msg);
            // 更新ui
            loadData();
        }

        @Override
        public void onProgress(int progress) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onError(int error, String errorString) {
            ThemeUtils.show(MessageActivity.this, errorString);
            msg.setState(3);
            dao.updateMessage(msg);
            // 更新ui
            loadData();
        }
    };

    /**
     * 语音听写初始化
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
            }
        }
    };

    /**
     * 设置语音听写参数
     */
    public void setSpeechRecognizerParam() {
        // 清空参数
        mIatDialog.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");


        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, 4000 + "");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, 1000 + "");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog.setParameter(SpeechConstant.ASR_PTT, 1 + "");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIatDialog.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIatDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = CommonUtil.parseIatResult(results.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            mBinding.etMsgContent.setText(resultBuffer.toString());
            mBinding.etMsgContent.setSelection(mBinding.etMsgContent.length());
            if (isLast)
                send();
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        MessageDao dao = new MessageDao(this);
        dao.clearUnread(me.getAccount(), receiver.getAccount());
    }

    @Override
    public void back() {
        finish();
    }
}
