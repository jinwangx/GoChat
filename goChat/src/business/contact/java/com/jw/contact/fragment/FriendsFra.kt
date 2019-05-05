package com.jw.contact.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.jw.business.business.FriendBusiness
import com.jw.business.business.InvitationBusiness
import com.jw.business.db.model.AccountInfo
import com.jw.business.db.model.Friend
import com.jw.contact.FriendDetailActivity
import com.jw.contact.activity.InvitationActivity
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.adapter.FriendsAdapter
import com.jw.gochat.databinding.FragmentFriendsBinding
import com.jw.gochat.event.AcceptInvitationEvent
import com.jw.gochat.event.InvitationEvent
import com.jw.library.utils.ThemeUtils
import com.jw.gochat.GoChatBindingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 0:23
 * 版本：
 * 作者：Mr.jin
 * 描述：好友页面
 */

class FriendsFra : GoChatBindingFragment<FragmentFriendsBinding>(), View.OnClickListener, AdapterView.OnItemClickListener {
    private var headView: View? = null
    private var ivUnread: ImageView? = null
    private lateinit var me: AccountInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //动态注册一个收到邀请的广播
        //动态注册一个朋友接收邀请的广播
        EventBus.getDefault().register(this)
        me = GoChatApplication.getAccountInfo()!!
    }

    override fun getLayoutId() = R.layout.fragment_friends

    override fun doConfig(arguments: Bundle?) {
        binding!!.lvFriends.addHeaderView(initHeaderView())
        headView!!.setOnClickListener(this)
        binding!!.lvFriends.onItemClickListener = this
    }

    override fun doLaunch() {
        super.doLaunch()
        doRefresh()
    }

    override fun doRefresh() {
        super.doRefresh()
        val cursor = FriendBusiness.getFriendAll(me.account!!)
        val hasUnAgree = InvitationBusiness.hasUnagree(me.account!!)
        if (hasUnAgree)
            ivUnread!!.visibility = View.VISIBLE
        else
            ivUnread!!.visibility = View.INVISIBLE
        val adapter = FriendsAdapter(activity!!, cursor!!)
        binding!!.lvFriends.adapter = adapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(textEvent: InvitationEvent) {
        val to = textEvent.message!!.owner
        ThemeUtils.show(activity, "接收到邀请")
        if (me.account!!.equals(to, ignoreCase = true)) {
            doRefresh()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(textEvent: AcceptInvitationEvent) {
        val to = textEvent.message!!.owner
        val from = textEvent.message!!.account
        ThemeUtils.show(activity, from + "已接受邀请")
        if (me.account!!.equals(to, ignoreCase = true)) {
            doRefresh()
        }
    }

    private fun initHeaderView(): View {
        headView = View.inflate(activity, R.layout.include_head_friends, null)
        ivUnread = headView!!.findViewById<View>(R.id.iv_has_new) as ImageView
        headView!!.measure(0, 0)
        val headHeight = headView!!.height
        val headWidth = headView!!.width
        headView!!.setPadding(0, 0, headWidth, headHeight)
        return headView as View
    }

    override fun onClick(v: View) {
        startActivity(Intent(activity, InvitationActivity::class.java))
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val friend = view.tag as Friend
        val intent = Intent()
        val bundle = Bundle()
        bundle.putSerializable("friend", friend)
        intent.putExtras(bundle)
        intent.setClass(activity!!, FriendDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}