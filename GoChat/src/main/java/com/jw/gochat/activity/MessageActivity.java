package com.jw.gochat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.adapter.MessageAdapter;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.bean.Message;
import com.jw.gochat.db.MessageDao;
import com.jw.gochat.receiver.PushReceiver;
import com.jw.gochat.utils.CommonUtil;
import com.jw.gochat.utils.ToastUtils;
import com.jw.gochat.view.NormalTopBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import Lib.GoChatManager;
import Lib.callback.GoChatCallBack;
import Lib.msg.ChatMessage;
import Lib.msg.TextBody;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：对话界面
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener,NormalTopBar.BackListener{

    @BindView(R.id.nt_msg)
    NormalTopBar ntMsg;
    @BindView(R.id.lv_msg)
    ListView lvSendMessage;
    @BindView(R.id.et_msg_content)
    EditText etSendContent;
    @BindView(R.id.iv_msg_speech)
    ImageView sendSpeech;
    @BindView(R.id.btn_msg_send)
    ImageView sendText;

    //语音听写对象
    private RecognizerDialog mIatDialog;
    // 语音合成对象
    //private SpeechSynthesizer mTts;
    // 默认发音人
    //private String voicer = "xiaoyan";
    // 引擎类型
    //private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Account me= ChatApplication.getAccount();
    private Friend receiver;
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

    @Override
    public void bindView() {
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        registerReceiver(pushReceiver, filter);
        receiver = (Friend) getIntent().getSerializableExtra("receiver");
    }

    @Override
    protected void initView() {
        super.initView();
        ntMsg.setTitle(receiver.getAccount());
        ntMsg.setBackListener(this);
        MessageDao dao = new MessageDao(this);
        Cursor cursor = dao.queryMessage(me.getAccount(), receiver.getAccount());
        adapter = new MessageAdapter(this, cursor,receiver);
        lvSendMessage.setAdapter(adapter);
        lvSendMessage.setDividerHeight(0);
        lvSendMessage.setClickable(false);
        sendSpeech.setOnClickListener(this);
        sendText.setOnClickListener(this);
        //初始化语音听写对象
        mIatDialog = new RecognizerDialog(this, mInitListener);
    }

    protected void loadData() {
        MessageDao dao = new MessageDao(this);
        final Cursor cursor = dao.queryMessage(me.getAccount(), receiver.getAccount());
        adapter.changeCursor(cursor);
        lvSendMessage.post(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    lvSendMessage.smoothScrollToPositionFromTop(cursor.getCount(), 0);
                } else {
                    lvSendMessage.smoothScrollToPosition(cursor.getCount());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        String content = etSendContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            etSendContent.setText("");
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
            GoChatManager.getInstance().sendMessage(message,messageSendCallBack);
        }
    }

    private GoChatCallBack messageSendCallBack=new GoChatCallBack() {

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
            ToastUtils.show(MessageActivity.this, errorString);
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
            etSendContent.setText(resultBuffer.toString());
            etSendContent.setSelection(etSendContent.length());
            if(isLast)
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
