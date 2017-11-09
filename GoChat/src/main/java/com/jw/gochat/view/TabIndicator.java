package com.jw.gochat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jw.gochat.R;

/**
 * Created by Administrator on 2017/3/26.
 */

public class TabIndicator extends LinearLayout {

    private ImageView ivIconTab;
    private TextView tvTab;
    private TextView tvUnread;

    public TabIndicator(Context context) {
        this(context,null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view= View.inflate(context,R.layout.item_indecator,this);
        ivIconTab = (ImageView) view.findViewById(R.id.iv_tab_icon);
        tvTab = (TextView) view.findViewById(R.id.tv_tab_des);
        tvUnread = (TextView) view.findViewById(R.id.tv_tab_unread_count);
    }

    public void setDrawableBackground(int background){
        ivIconTab.setBackgroundResource(background);
    }
    public void setText(String tab){
        tvTab.setText(tab);
    }

    public void setUnreadVisible(int visible){
        tvUnread.setVisibility(visible);
    }
    public void setUnread(int count){
        tvUnread.setText(count+"");
    }
}
