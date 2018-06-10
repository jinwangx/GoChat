package com.jw.gochat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jw.gochat.R;
import com.jw.business.bean.Invitation;
import com.jw.business.db.GCDB;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：新的朋友页面适配器
 */

public class InvitationAdapter extends CursorAdapter {
    private View.OnClickListener mAcceptListener;

    public InvitationAdapter(Context context, Cursor c, View.OnClickListener acceptListener) {
        super(context, c);
        this.mAcceptListener=acceptListener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.listitem_invitation, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CircleImageView ivIcon = (CircleImageView) view
                .findViewById(R.id.iv_new_friend_icon);
        TextView tvName = (TextView) view
                .findViewById(R.id.tv_new_friend_name);
        TextView tvAccept = (TextView) view
                .findViewById(R.id.tv_new_friend_accept);
        Button btnAccept = (Button) view
                .findViewById(R.id.btn_new_friend_accept);

        String account = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ACCOUNT));
        String name = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_NAME));
        String icon = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_INVITATOR_ICON));
        boolean agree = cursor.getInt(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_AGREE)) == 1;
        String content = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_CONTENT));
        String owner = cursor.getString(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_OWNER));
        long id = cursor.getLong(cursor
                .getColumnIndex(GCDB.Invitation.COLUMN_ID));

        Invitation invitation = new Invitation();
        invitation.setAccount(account);
        invitation.setAgree(agree);
        invitation.setContent(content);
        invitation.setIcon(icon);
        invitation.setName(name);
        invitation.setOwner(owner);
        invitation.setId(id);

        if (!agree) {
            btnAccept.setVisibility(View.VISIBLE);
            tvAccept.setVisibility(View.GONE);
        } else {
            btnAccept.setVisibility(View.GONE);
            tvAccept.setVisibility(View.VISIBLE);
        }

        //tvName.setText(name);
        Glide.with(context).load(invitation.getIcon()).into(ivIcon);

        btnAccept.setOnClickListener(mAcceptListener);
        btnAccept.setTag(invitation);
    }
}
