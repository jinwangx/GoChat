package com.jw.chat

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.jw.business.model.bean.Contact
import com.jw.chat.db.dao.MessageDao
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.adapter.ChatAdapter
import com.jw.gochat.databinding.FragmentCvstBinding
import com.jw.gochat.receiver.PushReceiver
import com.jw.gochatbase.BaseFragment

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话列表页面
 */

class ConversationFra : BaseFragment(), AdapterView.OnItemClickListener, ChatAdapter.ChatListener {
    private var adapter: ChatAdapter? = null
    private val me = ChatApplication.getAccount()
    private val pushReceiver = object : PushReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val to = intent.getStringExtra(PushReceiver.KEY_TO)
            if (me.account!!.equals(to, ignoreCase = true)) {
                loadData()
            }
        }
    }
    private var mBinding: FragmentCvstBinding? = null

    override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_cvst, null, false)
        return mBinding!!.root
    }

    override fun init() {
        super.init()
        //动态注册一个收到消息的广播
        val filter = IntentFilter()
        filter.addAction(PushReceiver.ACTION_TEXT)
        activity!!.registerReceiver(pushReceiver, filter)
    }

    override fun loadData() {
        val dao = MessageDao(activity!!)
        val cursor = dao.queryConversation(me.account!!)
        adapter = ChatAdapter(activity!!, cursor)
        mBinding!!.lvCvst.adapter = adapter
    }

    override fun initEvent() {
        super.initEvent()
        mBinding!!.lvCvst.onItemClickListener = this
        adapter!!.setChatListener(this)
    }

    //会话列表Item点击事件
    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("me", me)
        bundle.putSerializable("receiver", view.tag as Contact)
        intent.setClass(activity!!, MessageActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    //item侧滑后，点击删除时，执行该操作
    override fun delete(position: Int) {}

    //item侧滑后，点击置顶时，执行该操作
    override fun toTop(position: Int) {}
}