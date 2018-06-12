package com.jw.gochat.adapter

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.bumptech.glide.Glide
import com.jw.business.db.dao.AppDatabase
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.business.db.dao.FriendDao

import de.hdodenhof.circleimageview.CircleImageView

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：好友页面适配器
 */

class FriendsAdapter(context: Context, c: Cursor) : CursorAdapter(context, c) {
    private var dao: FriendDao? = AppDatabase.getInstance(context).friendDao()

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return View.inflate(context, R.layout.listitem_friends, null)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val tvFriend = view.findViewById<View>(R.id.tv_list_item_friend_act) as TextView
        val ivIcon = view.findViewById<View>(R.id.iv_list_item_friend_icon) as CircleImageView
        val name = cursor.getString(cursor.getColumnIndex("name"))
        val account = cursor.getString(cursor.getColumnIndex("account"))
        val iconPath = cursor.getString(cursor.getColumnIndex("icon"))
        Glide.with(context).load(iconPath).into(ivIcon)
        val friend = dao!!.queryFriendByAccount(ChatApplication.getAccount().account!!, account)
        tvFriend.text = name
        view.tag = friend
    }
}