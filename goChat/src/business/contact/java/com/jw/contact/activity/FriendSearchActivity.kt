package com.jw.contact

import android.content.Intent
import android.databinding.adapters.TextViewBindingAdapter
import android.os.Bundle
import android.view.View
import com.jw.business.db.model.Friend
import com.jw.contact.activity.SearchView
import com.jw.contact.presenter.SearchPresenter
import com.jw.contact.presenter.SearchPresenterImpl
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityFriendSearchBinding
import com.jw.gochat.view.DialogLoading
import com.jw.library.utils.ThemeUtils
import com.sencent.mm.GoChatBindingActivity

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：搜索用户页面
 */

class FriendSearchActivity : GoChatBindingActivity<ActivityFriendSearchBinding>(), SearchView {
    private var presenter: SearchPresenter? = null
    private var dialog: DialogLoading? = null

    override fun getLayoutId() = R.layout.activity_friend_search

    override fun doConfig(arguments: Intent) {
        presenter = SearchPresenterImpl(this)
        binding.apply {
            clickListener = View.OnClickListener {
                when (it.id) {
                    R.id.iv_search_back -> finish()
                    R.id.ll_search_act -> presenter?.validate(etSearchAct.text.toString().trim { it <= ' ' })
                }
            }
            accountChangeListener = TextViewBindingAdapter.OnTextChanged { s, _, _, _ ->
                if (s != null) {
                    llSearchAct.visibility = View.VISIBLE
                    tvSearchAct.text = s
                } else {
                    llSearchAct.visibility = View.GONE
                }
            }
        }
    }

    override fun showError(msg: String) {
        dialog!!.dismiss()
        ThemeUtils.show(this, msg)
    }

    override fun showSuccess(friend: Friend) {
        dialog!!.dismiss()
        val intent = Intent()
        val bundle = Bundle()
        bundle.putSerializable("friend", friend)
        finish()
        intent.putExtras(bundle)
        intent.setClass(this@FriendSearchActivity, FriendDetailActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
        dialog = DialogLoading(this)
        dialog!!.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
    }
}