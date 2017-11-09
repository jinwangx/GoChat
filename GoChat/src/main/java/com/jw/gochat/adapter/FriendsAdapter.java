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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建时间：2017/8/3
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：好友页面适配器
 */

public class FriendsAdapter extends CursorAdapter {
    FriendDao dao;

    public FriendsAdapter(Context context, Cursor c) {
        super(context, c);
        dao = new FriendDao(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.listitem_friends, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvFriend = (TextView) view.findViewById(R.id.tv_list_item_friend_act);
        CircleImageView ivIcon = (CircleImageView) view.findViewById(R.id.iv_list_item_friend_icon);
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String account = cursor.getString(cursor.getColumnIndex("account"));
        String iconPath = cursor.getString(cursor.getColumnIndex("icon"));
        Glide.with(context).load(iconPath).into(ivIcon);
        Friend friend = dao.queryFriendByAccount(ChatApplication.getAccount().getAccount(), account);
        tvFriend.setText(name);
        view.setTag(friend);
    }
}
