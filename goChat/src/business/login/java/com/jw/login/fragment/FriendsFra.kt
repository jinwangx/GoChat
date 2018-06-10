package com.jw.login.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.jw.business.bean.Contact
import com.jw.business.db.dao.FriendDao
import com.jw.business.db.dao.InvitationDao
import com.jw.contact.FriendDetailActivity
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.activity.InvitationActivity
import com.jw.gochat.adapter.FriendsAdapter
import com.jw.gochat.databinding.FragmentFriendsBinding
import com.jw.gochat.receiver.PushReceiver
import com.jw.gochatbase.BaseFragment
import com.jw.library.utils.ThemeUtils

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 0:23
 * 版本：
 * 作者：Mr.jin
 * 描述：好友页面
 */

class FriendsFra : BaseFragment(), View.OnClickListener, AdapterView.OnItemClickListener {

    private var headView: View? = null
    private var ivUnread: ImageView? = null
    private val me = ChatApplication.getAccount()
    private val invitedReceiver = object : PushReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val to = intent.getStringExtra(PushReceiver.KEY_TO)
            ThemeUtils.show(activity, "接收到邀请")
            if (me.account!!.equals(to, ignoreCase = true)) {
                loadData()
            }
        }
    }
    private val reInvitationReceiver = object : PushReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val to = intent.getStringExtra(PushReceiver.KEY_TO)
            val from = intent.getStringExtra(PushReceiver.KEY_FROM)
            ThemeUtils.show(activity, from + "已接受邀请")
            if (me.account!!.equals(to, ignoreCase = true)) {
                loadData()
            }
        }
    }
    private var mBinding: FragmentFriendsBinding? = null

    override fun init() {
        super.init()
        //动态注册一个收到邀请的广播
        val filter = IntentFilter()
        filter.addAction(PushReceiver.ACTION_INVATION)
        activity!!.registerReceiver(invitedReceiver, filter)
        //动态注册一个朋友接收邀请的广播
        val filter2 = IntentFilter()
        filter2.addAction(PushReceiver.ACTION_REINVATION)
        activity!!.registerReceiver(reInvitationReceiver, filter2)
        receivers.add(invitedReceiver)
        receivers.add(reInvitationReceiver)
    }

    public override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_friends, null, false)
        return mBinding!!.root
    }

    override fun initView() {
        mBinding!!.lvFriends.addHeaderView(initHeaderView())
        headView!!.setOnClickListener(this)
        mBinding!!.lvFriends.onItemClickListener = this
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

    override fun loadData() {
        val friendDao = FriendDao(this.activity!!)
        val cursor = friendDao.queryFriends(me.account!!)
        val invitationDao = InvitationDao(this.activity!!)
        val hasUnAgree = invitationDao.hasUnagree(me.account!!)
        if (hasUnAgree)
            ivUnread!!.visibility = View.VISIBLE
        else
            ivUnread!!.visibility = View.INVISIBLE
        val adapter = FriendsAdapter(activity!!, cursor)
        mBinding!!.lvFriends.adapter = adapter
    }

    override fun onClick(v: View) {
        startActivity(Intent(activity, InvitationActivity::class.java))
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val friend = view.tag as Contact
        val intent = Intent()
        val bundle = Bundle()
        bundle.putSerializable("friend", friend)
        intent.putExtras(bundle)
        intent.setClass(activity!!, FriendDetailActivity::class.java)
        startActivity(intent)
    }
}