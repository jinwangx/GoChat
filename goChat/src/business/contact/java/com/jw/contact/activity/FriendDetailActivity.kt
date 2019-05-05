package com.jw.contact

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.jw.business.business.FriendBusiness
import com.jw.business.db.model.AccountInfo
import com.jw.business.db.model.Friend
import com.jw.chat.MessageActivity
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityFriendDetailBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.gochat.GoChatURL
import com.jw.gochat.GoChatBindingActivity

/**
 * 创建时间：2017/4/15
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：联系人详细信息
 */

class FriendDetailActivity : GoChatBindingActivity<ActivityFriendDetailBinding>(), NormalTopBar.BackListener {
    private var friend: Friend? = null
    private lateinit var me: AccountInfo

    override fun getLayoutId() = R.layout.activity_friend_detail

    override fun doConfig(arguments: Intent) {
        val binding = binding
        me = GoChatApplication.getAccountInfo()!!
        friend = intent.getSerializableExtra("friend") as Friend
        binding.apply {
            if (FriendBusiness.getFriendById(me.account!!, friend!!.account!!) != null) {
                btnFriendAdd.visibility = View.GONE
                Glide.with(this@FriendDetailActivity).load(friend!!.icon).into(binding!!.ivListItemFriendIcon)
            } else {
                btnFriendAdd.visibility = View.VISIBLE
                Glide.with(this@FriendDetailActivity).load(GoChatURL.BASE_HTTP + friend!!.icon!!.replace("\\", "/")).into(ivListItemFriendIcon)
            }
            binding.tvListItemFriendName.text = friend!!.name
            binding.tvListItemFriendAct.text = friend!!.account
            ntFriendDetail.setBackListener(this@FriendDetailActivity)
            clickListener = View.OnClickListener {
                when (it.id) {
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
        }
    }

    override fun back() {
        finish()
    }
}