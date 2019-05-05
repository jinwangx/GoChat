package com.jw.contact

import android.content.Intent
import android.view.View
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityNewFriendBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.library.utils.ThemeUtils
import com.jw.gochat.GoChatBindingActivity

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：更多朋友页面
 */

class FriendAddActivity : GoChatBindingActivity<ActivityNewFriendBinding>(), NormalTopBar.BackListener {
    
    override fun getLayoutId() = R.layout.activity_new_friend
    
    override fun doConfig(arguments: Intent) {
        binding.apply {
            ntNewFriend.setBackListener(this@FriendAddActivity)
            clickListener = View.OnClickListener {
                when (it.id) {
                    R.id.ll_friend_search -> {
                        finish()
                        startActivity(Intent(this@FriendAddActivity, FriendSearchActivity::class.java))
                    }
                    R.id.rl_scan_code -> {
                        ThemeUtils.show(this@FriendAddActivity, "暂未开放该功能")
                    }
                }
            }
        }
    }

    override fun back() {
        finish()
    }
}