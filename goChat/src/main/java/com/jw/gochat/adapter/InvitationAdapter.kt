package com.jw.gochat.adapter

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jw.gochat.R
import com.jw.business.model.bean.Invitation
import com.jw.business.db.GCDB
import de.hdodenhof.circleimageview.CircleImageView

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：新的朋友页面适配器
 */

class InvitationAdapter(context: Context, c: Cursor?, private val mAcceptListener: View.OnClickListener) : CursorAdapter(context, c) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return View.inflate(context, R.layout.listitem_invitation, null)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val ivIcon = view
                .findViewById<View>(R.id.iv_new_friend_icon) as CircleImageView
        val tvName = view
                .findViewById<View>(R.id.tv_new_friend_name) as TextView
        val tvAccept = view
                .findViewById<View>(R.id.tv_new_friend_accept) as TextView
        val btnAccept = view
                .findViewById<View>(R.id.btn_new_friend_accept) as Button

        val account = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT))
        val name = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_NAME))
        val icon = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ICON))
        val agree = cursor.getInt(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_AGREE)) == 1
        val content = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_CONTENT))
        val owner = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_OWNER))
        val id = cursor.getLong(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_ID))

        val invitation = Invitation()
        invitation.account = account
        invitation.isAgree = agree
        invitation.content = content
        invitation.icon = icon
        invitation.name = name
        invitation.owner = owner
        invitation.id = id

        if (!agree) {
            btnAccept.visibility = View.VISIBLE
            tvAccept.visibility = View.GONE
        } else {
            btnAccept.visibility = View.GONE
            tvAccept.visibility = View.VISIBLE
        }

        //tvName.setText(name);
        Glide.with(context).load(invitation.icon).into(ivIcon)

        btnAccept.setOnClickListener(mAcceptListener)
        btnAccept.tag = invitation
    }
}