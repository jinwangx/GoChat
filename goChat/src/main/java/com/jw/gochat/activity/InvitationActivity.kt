package com.jw.gochat.activity

import android.databinding.DataBindingUtil
import android.view.View
import com.jw.business.bean.Invitation
import com.jw.business.db.dao.InvitationDao
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.action.AcceptInvitationAction
import com.jw.gochat.adapter.InvitationAdapter
import com.jw.gochat.databinding.ActivityInvitationBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseActivity

class InvitationActivity : BaseActivity(), NormalTopBar.BackListener {
    private var adapter: InvitationAdapter? = null
    private val me = ChatApplication.getAccount()
    private var dao: InvitationDao? = null
    private var mBinding: ActivityInvitationBinding? = null


    private var acceptListener: View.OnClickListener = View.OnClickListener { v ->
        val o = v.tag ?: return@OnClickListener
        val action = AcceptInvitationAction()
        action.doAction(this@InvitationActivity, o)
        // ui更新
        adapter!!.changeCursor(InvitationDao(this@InvitationActivity).queryCursor((o as Invitation).owner!!))

        finish()
    }


    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_invitation)
    }

    override fun initView() {
        super.initView()
        mBinding!!.topBarInvitation.setTitle("新的朋友")
        adapter = InvitationAdapter(this, null, acceptListener)
        mBinding!!.lvInvitation.adapter = adapter
        mBinding!!.topBarInvitation.setBackListener(this)
    }

    override fun loadData() {
        dao = InvitationDao(this)
        val cursor = dao!!.queryCursor(me.account!!)
        adapter!!.changeCursor(cursor)
    }

    override fun back() {
        finish()
    }
}