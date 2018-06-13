package com.jw.gochat.adapter

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jw.business.business.FriendBusiness
import com.jw.business.db.AppDatabase
import com.jw.business.db.dao.FriendDao
import com.jw.chat.db.GCDB
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：消息页面适配器
 */

class ChatAdapter(context: Context, c: Cursor) : CursorAdapter(context, c) {
    private var mListener: ChatListener? = null

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return View.inflate(context, R.layout.listitem_conversation, null)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val ivAccount = view.findViewById<View>(R.id.iv_cvst_act) as CircleImageView
        val tvName = view.findViewById<View>(R.id.tv_cvst_act) as TextView
        val tvLastNews = view.findViewById<View>(R.id.tv_lastNews) as TextView
        val tvUnread = view.findViewById<View>(R.id.tv_cvst_unread_count) as TextView
        val account = cursor.getString(cursor.getColumnIndex(GCDB.Conversation.COLUMN_ACCOUNT))
        val content = cursor.getString(cursor.getColumnIndex(GCDB.Conversation.COLUMN_CONTENT))
        val unread = cursor.getInt(cursor.getColumnIndex(GCDB.Conversation.COLUMN_UNREAD))
        if (unread == 0)
            tvUnread.visibility = View.INVISIBLE
        else
            tvUnread.visibility = View.VISIBLE
        val friend = FriendBusiness.getFriendById(ChatApplication.getAccountInfo().account!!, account)
        Glide.with(context).load(friend!!.icon).into(ivAccount)
        tvName.text = account
        tvLastNews.text = content
        tvUnread.text = unread.toString() + ""
        view.tag = friend
    }

    fun setChatListener(listener: ChatListener) {
        this.mListener = listener
    }

    interface ChatListener {
        fun delete(position: Int)
        fun toTop(position: Int)
    }
}