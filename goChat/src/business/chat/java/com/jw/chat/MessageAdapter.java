package com.jw.gochat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.business.bean.Account;
import com.jw.business.bean.Contact;
import com.jw.business.db.dao.FriendDao;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：对话页面适配器
 */

public class MessageAdapter extends CursorAdapter {


    private final Account me;
    private final FriendDao friendDao;
    private Contact receiver;

    public MessageAdapter(Context context, Cursor c ,Contact receiver) {
        super(context, c);
        me = ChatApplication.getAccount();
        friendDao = new FriendDao(context);
        this.receiver=receiver;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listitem_message, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int direct = cursor.getInt(cursor.getColumnIndex("direct"));
        String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        int create_time = cursor.getInt(cursor.getColumnIndex("create_time"));
        int state=cursor.getInt(cursor.getColumnIndex("state"));
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        TextView tvSendTime = (TextView) view.findViewById(R.id.tv_msg_send_time);
        tvSendTime.setText(format.format(create_time));
        LinearLayout llMessager = (LinearLayout) view.findViewById(R.id.ll_msg_sender);
        RelativeLayout rlMe = (RelativeLayout) view.findViewById(R.id.rl_msg_me);

        if (direct == 0) {
            llMessager.setVisibility(View.GONE);
            rlMe.setVisibility(View.VISIBLE);
            TextView tvSend = (TextView) view.findViewById(R.id.tv_send);
            CircleImageView ivMe = (CircleImageView) view.findViewById(R.id.iv_msg_me);
            CircleImageView ivSendError = (CircleImageView) view.findViewById(R.id.iv_send_error);
            Glide.with(context).load(me.getIcon()).into(ivMe);
            tvSend.setText(content);
            if(state==3)
                ivSendError.setVisibility(View.VISIBLE);
            else
                ivSendError.setVisibility(View.GONE);
        }
        if (direct == 1) {
            llMessager.setVisibility(View.VISIBLE);
            rlMe.setVisibility(View.GONE);
            TextView tvReceive = (TextView) view.findViewById(R.id.tv_msg_receive);
            CircleImageView ivMessager = (CircleImageView) view.findViewById(R.id.iv_msg_sender);
            Glide.with(context)
                    .load(friendDao.queryFriendByAccount(me.getAccount(),receiver.getAccount()).getIcon())
                    .into(ivMessager);
            tvReceive.setText(content);
        }
    }
}
