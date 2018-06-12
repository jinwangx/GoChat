package com.jw.gochat.adapter

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.jw.business.db.dao.AppDatabase
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.business.model.bean.Contact
import com.jw.business.db.dao.FriendDao
import com.jw.business.model.bean.Account

import java.text.SimpleDateFormat

import de.hdodenhof.circleimageview.CircleImageView

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：对话页面适配器
 */

class MessageAdapter(context: Context, c: Cursor, private val receiver: Contact) : CursorAdapter(context, c) {

    private val me: Account = ChatApplication.getAccount()
    private val friendDao: FriendDao = AppDatabase.getInstance(context).friendDao()

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return View.inflate(context, R.layout.listitem_message, null)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val direct = cursor.getInt(cursor.getColumnIndex("direct"))
        val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
        val create_time = cursor.getInt(cursor.getColumnIndex("create_time"))
        val state = cursor.getInt(cursor.getColumnIndex("state"))
        val format = SimpleDateFormat("MM/dd/yy HH:mm:ss")
        val tvSendTime = view.findViewById<View>(R.id.tv_msg_send_time) as TextView
        tvSendTime.text = format.format(create_time)
        val llMessager = view.findViewById<View>(R.id.ll_msg_sender) as LinearLayout
        val rlMe = view.findViewById<View>(R.id.rl_msg_me) as RelativeLayout
        if (direct == 0) {
            llMessager.visibility = View.GONE
            rlMe.visibility = View.VISIBLE
            val tvSend = view.findViewById<View>(R.id.tv_send) as TextView
            val ivMe = view.findViewById<View>(R.id.iv_msg_me) as CircleImageView
            val ivSendError = view.findViewById<View>(R.id.iv_send_error) as CircleImageView
            Glide.with(context).load(me.icon).into(ivMe)
            tvSend.text = content
            if (state == 3)
                ivSendError.visibility = View.VISIBLE
            else
                ivSendError.visibility = View.GONE
        }
        if (direct == 1) {
            llMessager.visibility = View.VISIBLE
            rlMe.visibility = View.GONE
            val tvReceive = view.findViewById<View>(R.id.tv_msg_receive) as TextView
            val ivMessager = view.findViewById<View>(R.id.iv_msg_sender) as CircleImageView
            Glide.with(context)
                    .load(friendDao.queryFriendByAccount(me.account!!, receiver.account!!)!!.icon)
                    .into(ivMessager)
            tvReceive.text = content
        }
    }
}