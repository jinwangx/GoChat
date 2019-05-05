package com.jw.contact.activity

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jw.business.business.InvitationBusiness
import com.jw.business.db.AppDatabase
import com.jw.business.db.GCDB
import com.jw.business.db.model.AccountInfo
import com.jw.business.db.model.Invitation
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.action.AcceptInvitationAction
import com.jw.gochat.databinding.ActivityInvitationBinding
import com.jw.gochat.view.NormalTopBar
import com.jw.gochat.GoChatBindingActivity
import de.hdodenhof.circleimageview.CircleImageView

class InvitationActivity : GoChatBindingActivity<ActivityInvitationBinding>(), NormalTopBar.BackListener {
    private var adapter: InvitationAdapter? = null
    private var me: AccountInfo? = null

    override fun getLayoutId() = R.layout.activity_invitation

    override fun doConfig(arguments: Intent) {
        me = GoChatApplication.getAccountInfo()
        val binding=binding
        binding.topBarInvitation.setTitle("新的朋友")
        adapter = InvitationAdapter(this, null, acceptListener)
        binding.lvInvitation.adapter = adapter
        binding.topBarInvitation.setBackListener(this)
    }

    override fun doLaunch() {
        super.doLaunch()
        doRefresh()
    }

    override fun doRefresh() {
        val cursor = InvitationBusiness.getInvitationByOwner(me?.account!!)
        adapter!!.changeCursor(cursor)
    }


    private var acceptListener: View.OnClickListener = View.OnClickListener { v ->
        val o = v.tag ?: return@OnClickListener
        val action = AcceptInvitationAction()
        action.doAction(this@InvitationActivity, o)
        // ui更新
        adapter!!.changeCursor(AppDatabase.getInstance().invitationDao().getInvitationByOwner((o as Invitation).owner!!))

        finish()
    }

    override fun back() {
        finish()
    }

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
            invitation.invitator_account = account
            invitation.agree = agree
            invitation.content = content
            invitation.invitator_icon = icon
            invitation.invitator_name = name
            invitation.owner = owner
            invitation._id = id

            if (!agree) {
                btnAccept.visibility = View.VISIBLE
                tvAccept.visibility = View.GONE
            } else {
                btnAccept.visibility = View.GONE
                tvAccept.visibility = View.VISIBLE
            }

            //tvName.setText(invitator_name);
            Glide.with(context).load(invitation.invitator_icon).into(ivIcon)

            btnAccept.setOnClickListener(mAcceptListener)
            btnAccept.tag = invitation
        }
    }
}