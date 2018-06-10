package com.jw.gochat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jw.gochat.R;

/**
 * Created by Administrator on 2017/3/25.
 */

public class NormalTopBar extends LinearLayout {

    private LinearLayout llBtnBack;
    private BackListener mListener;
    private AddFriendListener mListener2;
    private SendValidateListener mListener3;
    private TextView tvTitle;
    private String title;
    private ImageView ivIcon;
    private ImageView ivAddFriend;
    private TextView tvValidate;

    public NormalTopBar(Context context) {
        this(context,null);
    }

    public NormalTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义组件的属性
        TypedArray types = context.obtainStyledAttributes(attrs,
                R.styleable.NormalTopBar);
        try {
            title = types.getString(R.styleable.NormalTopBar_title);
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
        initView();
    }
    private void initView(){
        View view= View.inflate(getContext(), R.layout.layout_normal_top_bar,this);
        llBtnBack = (LinearLayout) view.findViewById(R.id.btn_ll_back);
        ivAddFriend = (ImageView) findViewById(R.id.iv_nt_add);
        tvTitle = (TextView) view.findViewById(R.id.tv_nt_title);
        ivIcon = (ImageView) view.findViewById(R.id.iv_nt_icon);
        tvValidate = (TextView) view.findViewById(R.id.tv_nt_validate);
        tvTitle.setText(title);
    }
    public interface BackListener{
        void back();
    }
    public void setBackListener(BackListener listener){
        this.mListener=listener;
        llBtnBack.setVisibility(View.VISIBLE);
        llBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.back();
            }
        });
    }

    public interface AddFriendListener{
        void add();
    }
    public void setAddFriendListener(AddFriendListener listener,int visible){
        this.mListener2=listener;
        ivAddFriend.setVisibility(visible);
        if(mListener2==null)
            return;
        ivAddFriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener2.add();
            }
        });
    }
    public void setTitle(String name){
        this.title=name;
        tvTitle.setText(title);
    }
    public void setIcon(){
        ivIcon.setVisibility(View.VISIBLE);
    }

    public interface SendValidateListener{
        void send();
    }
    public void setSendListener(SendValidateListener listener){
        this.mListener3=listener;
        tvValidate.setVisibility(View.VISIBLE);
        tvValidate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener3.send();
            }
        });
    }
}
