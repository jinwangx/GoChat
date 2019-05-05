package com.jw.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.jw.business.db.model.AccountInfo
import com.jw.business.db.model.Friend
import com.jw.chat.business.ConversationBusiness
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.adapter.ChatAdapter
import com.jw.gochat.databinding.FragmentCvstBinding
import com.jw.gochat.event.TextEvent
import com.jw.gochat.GoChatBindingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话列表页面
 */

class ConversationFra : GoChatBindingFragment<FragmentCvstBinding>(), AdapterView.OnItemClickListener, ChatAdapter.ChatListener {
    private lateinit var adapter: ChatAdapter
    private lateinit var me: AccountInfo

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(textEvent: TextEvent) {
        val owner = textEvent.message!!.owner
        if (me.account!!.equals(owner, ignoreCase = true)) {
            doRefresh()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        me = GoChatApplication.getAccountInfo()!!
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId() = R.layout.fragment_cvst

    override fun doConfig(arguments: Bundle?) {
        val cursor = ConversationBusiness.query(me.account!!)
        adapter = ChatAdapter(activity!!, cursor!!)
        adapter.setChatListener(this)
        binding!!.lvCvst.adapter=adapter
        binding!!.lvCvst.onItemClickListener = this
    }

    override fun doRefresh() {
        super.doRefresh()
        val cursor = ConversationBusiness.query(me.account!!)
        adapter.changeCursor(cursor)
    }

    //会话列表Item点击事件
    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("me", me)
        bundle.putSerializable("receiver", view.tag as Friend)
        intent.setClass(activity!!, MessageActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    //item侧滑后，点击删除时，执行该操作
    override fun delete(position: Int) {}

    //item侧滑后，点击置顶时，执行该操作
    override fun toTop(position: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}