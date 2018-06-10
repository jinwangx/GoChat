package com.jw.chat

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.View
import com.iflytek.cloud.*
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.jw.business.bean.Contact
import com.jw.chat.callback.GoChatCallBack
import com.jw.chat.db.bean.Message
import com.jw.chat.db.dao.MessageDao
import com.jw.chat.msg.ChatMessage
import com.jw.chat.msg.TextBody
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.adapter.MessageAdapter
import com.jw.gochat.databinding.ActivityMessageBinding
import com.jw.gochat.receiver.PushReceiver
import com.jw.gochat.utils.CommonUtil
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseActivity
import com.jw.library.utils.ThemeUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*


/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：对话界面
 */

class MessageActivity : BaseActivity(), View.OnClickListener, NormalTopBar.BackListener {

    //语音听写对象
    private var mIatDialog: RecognizerDialog? = null
    // 语音合成对象
    //private SpeechSynthesizer mTts;
    // 默认发音人
    //private String voicer = "xiaoyan";
    // 引擎类型
    //private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private val mIatResults = LinkedHashMap<String?, String>()
    private val me = ChatApplication.getAccount()
    private var receiver: Contact? = null
    private var adapter: MessageAdapter? = null
    private val pushReceiver = object : PushReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val from = intent.getStringExtra(PushReceiver.KEY_FROM)
            if (receiver!!.account!!.equals(from, ignoreCase = true)) {
                loadData()
            }

        }
    }
    private var msg: Message? = null
    private var dao: MessageDao? = null
    private var mBinding: ActivityMessageBinding? = null

    private val messageSendCallBack = object : GoChatCallBack {

        override fun onSuccess() {
            msg!!.state = 2
            dao!!.updateMessage(msg!!)
            // 更新ui
            loadData()
        }

        override fun onProgress(progress: Int) {
        }

        override fun onError(error: Int, errorString: String) {
            ThemeUtils.show(this@MessageActivity, errorString)
            msg!!.state = 3
            dao!!.updateMessage(msg!!)
            // 更新ui
            loadData()
        }
    }

    /**
     * 语音听写初始化
     */
    private val mInitListener = InitListener { code ->
        if (code != ErrorCode.SUCCESS) {
        }
    }

    /**
     * 听写UI监听器
     */
    private val mRecognizerDialogListener = object : RecognizerDialogListener {
        override fun onResult(results: RecognizerResult, isLast: Boolean) {
            val text = CommonUtil.parseIatResult(results.resultString)

            var sn: String? = null
            // 读取json结果中的sn字段
            try {
                val resultJson = JSONObject(results.resultString)
                sn = resultJson.optString("sn")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            mIatResults[sn] = text
            val resultBuffer = StringBuffer()
            for (key in mIatResults.keys) {
                resultBuffer.append(mIatResults[key])
            }
            mBinding!!.etMsgContent.setText(resultBuffer.toString())
            mBinding!!.etMsgContent.setSelection(mBinding!!.etMsgContent.length())
            if (isLast)
                send()
        }

        /**
         * 识别回调错误.
         */
        override fun onError(error: SpeechError) {}
    }

    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        val filter = IntentFilter()
        filter.addAction(PushReceiver.ACTION_TEXT)
        registerReceiver(pushReceiver, filter)
        receiver = intent.getSerializableExtra("receiver") as Contact
    }

    override fun initView() {
        super.initView()
        mBinding!!.ntMsg.setTitle(receiver!!.account)
        mBinding!!.ntMsg.setBackListener(this)
        val dao = MessageDao(this)
        val cursor = dao.queryMessage(me.account!!, receiver!!.account!!)
        adapter = MessageAdapter(this, cursor, receiver!!)
        mBinding!!.lvMsg.adapter = adapter
        mBinding!!.lvMsg.dividerHeight = 0
        mBinding!!.lvMsg.isClickable = false
        mBinding!!.ivMsgSpeech.setOnClickListener(this)
        mBinding!!.btnMsgSend.setOnClickListener(this)
        //初始化语音听写对象
        mIatDialog = RecognizerDialog(this, mInitListener)
    }

    override fun loadData() {
        val dao = MessageDao(this)
        val cursor = dao.queryMessage(me.account!!, receiver!!.account!!)
        adapter!!.changeCursor(cursor)
        mBinding!!.lvMsg.post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mBinding!!.lvMsg.smoothScrollToPositionFromTop(cursor.count, 0)
            } else {
                mBinding!!.lvMsg.smoothScrollToPosition(cursor.count)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_msg_speech -> {
                // 设置参数
                setSpeechRecognizerParam()
                val isShowDialog = true
                // 显示听写对话框
                mIatDialog!!.setListener(mRecognizerDialogListener)
                mIatDialog!!.show()
            }
            R.id.btn_msg_send -> send()
            else -> {
            }
        }
    }

    fun send() {
        val content = mBinding!!.etMsgContent.text.toString()
        if (!TextUtils.isEmpty(content)) {
            mBinding!!.etMsgContent.setText("")
            // 存储到本地
            dao = MessageDao(this)
            msg = Message()
            msg!!.account = receiver!!.account
            msg!!.content = content
            msg!!.createTime = System.currentTimeMillis()
            msg!!.direction = 0
            msg!!.owner = me.account
            msg!!.isRead = true
            msg!!.state = 1
            msg!!.type = 0
            dao!!.addMessage(msg!!)
            loadData()

            val message = ChatMessage.createMessage(ChatMessage.Type.TEXT)
            message.setAccount(me.account!!)
            message.setToken(me.token!!)
            message.receiver = receiver!!.account
            message.body = TextBody(content)
            GoChatManager.getInstance(ChatApplication.getOkHttpClient()).sendMessage(message, messageSendCallBack)
        }
    }

    /**
     * 设置语音听写参数
     */
    private fun setSpeechRecognizerParam() {
        // 清空参数
        mIatDialog!!.setParameter(SpeechConstant.PARAMS, null)
        // 设置听写引擎
        mIatDialog!!.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
        // 设置返回结果格式
        mIatDialog!!.setParameter(SpeechConstant.RESULT_TYPE, "json")
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIatDialog!!.setParameter(SpeechConstant.VAD_BOS, 4000.toString() + "")
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIatDialog!!.setParameter(SpeechConstant.VAD_EOS, 1000.toString() + "")
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIatDialog!!.setParameter(SpeechConstant.ASR_PTT, 1.toString() + "")
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIatDialog!!.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
        mIatDialog!!.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory().toString() + "/msc/iat.wav")
    }

    override fun onPause() {
        super.onPause()
        val dao = MessageDao(this)
        dao.clearUnread(me.account!!, receiver!!.account!!)
    }

    override fun back() {
        finish()
    }
}