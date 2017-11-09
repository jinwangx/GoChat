package com.jw.gochat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.db.GCDB;

import de.hdodenhof.circleimageview.CircleImageView;
/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：消息页面适配器
 */

public class ChatAdapter extends CursorAdapter {
    private ChatListener mListener;
    private FriendDao friendDao;

    public ChatAdapter(Context context, Cursor c) {
        super(context, c);
        friendDao = new FriendDao(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listitem_conversation, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CircleImageView ivAccount = (CircleImageView) view.findViewById(R.id.iv_cvst_act);
        TextView tvName = (TextView) view.findViewById(R.id.tv_cvst_act);
        TextView tvLastNews = (TextView) view.findViewById(R.id.tv_lastNews);
        TextView tvUnread = (TextView) view.findViewById(R.id.tv_cvst_unread_count);
        String account = cursor.getString(cursor.getColumnIndex(GCDB.Conversation.COLUMN_ACCOUNT));
        String content = cursor.getString(cursor.getColumnIndex(GCDB.Conversation.COLUMN_CONTENT));
        int unread = cursor.getInt(cursor.getColumnIndex(GCDB.Conversation.COLUMN_UNREAD));
        if (unread == 0)
            tvUnread.setVisibility(View.INVISIBLE);
        else
            tvUnread.setVisibility(View.VISIBLE);
        Friend friend = friendDao.queryFriendByAccount(ChatApplication.getAccount().getAccount(), account);
        Glide.with(context).load(friend.getIcon()).into(ivAccount);
        tvName.setText(account);
        tvLastNews.setText(content);
        tvUnread.setText(unread + "");
        view.setTag(friend);
    }
    public void setChatListener(ChatListener listener) {
        this.mListener = listener;
    }

    public interface ChatListener {

        void delete(int position);

        void toTop(int position);
    }
}
