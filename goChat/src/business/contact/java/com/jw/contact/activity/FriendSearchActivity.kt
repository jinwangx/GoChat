package com.jw.contact

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.jw.business.business.FriendBusiness
import com.jw.business.db.model.Friend
import com.jw.chat.GoChatManager
import com.jw.chat.callback.GoChatObjectCallBack
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityFriendSearchBinding
import com.jw.gochat.view.DialogLoading
import com.jw.gochatbase.BaseActivity
import com.jw.library.utils.ThemeUtils

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：搜索用户页面
 */

class FriendSearchActivity : BaseActivity(), TextWatcher, View.OnClickListener {

    private var dialog: DialogLoading? = null
    private var mBinding: ActivityFriendSearchBinding? = null

    private val friendSearchCallBack = object : GoChatObjectCallBack<Friend>() {
        override fun onSuccess(friend: Friend) {
            dialog!!.dismiss()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putSerializable("friend", friend)
            finish()
            intent.putExtras(bundle)
            intent.setClass(this@FriendSearchActivity, FriendDetailActivity::class.java)
            startActivity(intent)
        }

        override fun onError(error: Int, msg: String) {
            dialog!!.dismiss()
            ThemeUtils.show(this@FriendSearchActivity, msg)
        }
    }

    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_friend_search)
    }

    override fun initEvent() {
        super.initEvent()
        mBinding!!.ivSearchBack.setOnClickListener(this)
        mBinding!!.llSearchAct.setOnClickListener(this)
        mBinding!!.etSearchAct.addTextChangedListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_search_back -> finish()
            R.id.ll_search_act -> {
                val friendAccount = mBinding!!.etSearchAct.text.toString().trim { it <= ' ' }
                val me = ChatApplication.getAccountInfo()
                if (me.account == friendAccount) {
                    ThemeUtils.show(this, "不要找自己啦")
                    return
                }
                // 已有的朋友
                val friend = FriendBusiness.getFriendById(me.account!!, friendAccount)
                if (friend != null) {
                    val intent = Intent()
                    val bundle = Bundle()
                    bundle.putSerializable("friend", friend)
                    intent.putExtras(bundle)
                    intent.setClass(this, FriendDetailActivity::class.java)
                    this.startActivity(intent)
                    return
                }
                dialog = DialogLoading(this)
                dialog!!.show()
                if (friendAccount != null) {
                    GoChatManager.getInstance(ChatApplication.getOkHttpClient()).searchContact(friendAccount, friendSearchCallBack)
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s != null) {
            mBinding!!.llSearchAct.visibility = View.VISIBLE
            mBinding!!.tvSearchAct.text = s
        }

    }

    override fun afterTextChanged(s: Editable?) {
        if (s == null) {
            mBinding!!.llSearchAct.visibility = View.GONE
        }
    }
}