package com.jw.chat

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.jw.business.db.model.Friend
import com.jw.chat.business.ConversationBusiness
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.adapter.ChatAdapter
import com.jw.gochat.databinding.FragmentCvstBinding
import com.jw.gochat.event.TextEvent
import com.jw.gochatbase.BaseFragment
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

class ConversationFra : BaseFragment(), AdapterView.OnItemClickListener, ChatAdapter.ChatListener {
    private var adapter: ChatAdapter? = null
    private val me = ChatApplication.getAccountInfo()
    private var mBinding: FragmentCvstBinding? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(textEvent: TextEvent) {
        val owner = textEvent.message!!.owner
        if (me.account!!.equals(owner, ignoreCase = true)) {
            loadData()
        }

    }

    override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_cvst, null, false)
        return mBinding!!.root
    }

    override fun init() {
        super.init()
        EventBus.getDefault().register(this)
    }

    override fun loadData() {
        val cursor = ConversationBusiness.query(me.account!!)
        adapter = ChatAdapter(activity!!, cursor!!)
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