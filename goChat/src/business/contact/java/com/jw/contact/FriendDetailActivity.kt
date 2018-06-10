package com.jw.contact

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.jw.business.model.bean.Contact
import com.jw.business.db.dao.FriendDao
import com.jw.chat.GoChatURL
import com.jw.chat.MessageActivity
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityFriendDetailBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseActivity

/**
 * 创建时间：2017/4/15
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：联系人详细信息
 */

class FriendDetailActivity : BaseActivity(), View.OnClickListener, NormalTopBar.BackListener {

    private var friend: Contact? = null
    private val me = ChatApplication.getAccount()
    private var dao: FriendDao? = null
    private var mBinding: ActivityFriendDetailBinding? = null

    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_detail)
    }

    override fun initView() {
        super.initView()
        friend = intent.getSerializableExtra("friend") as Contact
        dao = FriendDao(this@FriendDetailActivity)
        if (dao!!.queryFriendByAccount(me.account!!, friend!!.account!!) != null) {
            mBinding!!.btnFriendAdd.visibility = View.GONE
            Glide.with(this).load(friend!!.icon).into(mBinding!!.ivListItemFriendIcon)
        } else {
            mBinding!!.btnFriendAdd.visibility = View.VISIBLE
            Glide.with(this).load(GoChatURL.BASE_HTTP + friend!!.icon!!.replace("\\", "/")).into(mBinding!!.ivListItemFriendIcon)
        }
        mBinding!!.tvListItemFriendName.text = friend!!.name
        mBinding!!.tvListItemFriendAct.text = friend!!.account
    }

    override fun initEvent() {
        super.initEvent()
        mBinding!!.ntFriendDetail.setBackListener(this)
        mBinding!!.btnFriendAdd.setOnClickListener(this)
        mBinding!!.btnFriendSend.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_friend_add -> {
                finish()
                val intent1 = Intent()
                intent1.putExtra("receiver", friend!!.account)
                intent1.setClass(this@FriendDetailActivity, FriendValidateActivity::class.java)
                startActivity(intent1)
            }
            R.id.btn_friend_send -> {
                val intent2 = Intent()
                val bundle = Bundle()
                bundle.putSerializable("receiver", friend)
                bundle.putParcelable("me", me)
                intent2.putExtras(bundle)
                intent2.setClass(this@FriendDetailActivity, MessageActivity::class.java)
                startActivity(intent2)
            }
        }
    }

    override fun back() {
        finish()
    }
}