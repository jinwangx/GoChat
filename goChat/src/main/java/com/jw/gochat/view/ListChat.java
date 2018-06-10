package com.jw.gochat.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jw.gochat.R;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/3/27.
 */

public class ListChat extends ListView {
    private Context context;
    private int mHeight;
    private int mWidth;
    private static final int REFRESH_PULL=0;
    private static final int REFRESH_RELEASE=1;
    private static final int REFRESHING=2;
    private static int currentState=REFRESH_PULL;
    private View headView;
    private RotateAnimation upAnim;
    private RotateAnimation downAnim;
    private ImageView ivStateRefresh;
    private TextView tvStateRefresh;
    private TextView tvTimeRefresh;
    private String lastTime;
    private int minPadding;
    private int maxPadding;
    private ProgressBar ivRefreshing;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvTimeRefresh.setText("最后刷新："+lastTime);
            switch (msg.what){
                case REFRESH_PULL:
                    ivStateRefresh.startAnimation(downAnim);
                    tvStateRefresh.setText("下拉刷新");
                    break;
                case REFRESH_RELEASE:
                    ivStateRefresh.startAnimation(upAnim);
                    tvStateRefresh.setText("松开刷新");
                    break;
                case REFRESHING:
                    lastTime=getCurrentTime();
                    ivStateRefresh.clearAnimation();
                    tvStateRefresh.setText("正在刷新");
                    ivStateRefresh.setVisibility(View.INVISIBLE);
                    ivRefreshing.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Thread(){
                        public void run(){
                            currentState=REFRESH_PULL;
                            tvStateRefresh.setText("下拉刷新");
                            tvTimeRefresh.setText("最后刷新："+lastTime);
                            ivStateRefresh.setVisibility(View.VISIBLE);
                            ivRefreshing.setVisibility(View.INVISIBLE);
                            headView.setPadding(0,minPadding,0,0);
                        }
                    },2000);
                    break;
            }
            return false;
        }
    });

    public ListChat(Context context) {
        this(context,null);
    }

    public ListChat(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
        initRotateAnimation();
    }

    private void initView(){
        initHeadView();
    }

    private void initHeadView(){
        headView = View.inflate(context, R.layout.include_head_listview,null);
        ivStateRefresh = (ImageView) headView.findViewById(R.id.iv_refresh_state);
        ivRefreshing = (ProgressBar) headView.findViewById(R.id.iv_refreshing);
        tvStateRefresh = (TextView) headView.findViewById(R.id.tv_refresh_state);
        tvTimeRefresh = (TextView) headView.findViewById(R.id.tv_refresh_time);
        tvStateRefresh.setText("下拉刷新");
        tvTimeRefresh.setText("最后刷新"+lastTime);
        //通知测量
        headView.measure(0,0);
        mWidth = getWidth();
        mHeight = headView.getMeasuredHeight();
        minPadding = -mHeight;
        maxPadding = 20;
        headView.setPadding(0,-mHeight,mWidth,0);
        this.addHeaderView(headView);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            startY= (int) ev.getY();
        }
        return super.onInterceptTouchEvent(ev);
    }

    int startY,currentY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentY= (int) ev.getY();
                int offsetY=currentY-startY;
                int paddingTop=minPadding+offsetY;
                Log.v("paddingTop:    ","      startY"+startY+"      offsetY"+offsetY+"      paddingTop"+paddingTop);
                if(getFirstVisiblePosition()==0) {
                    if(paddingTop>=20)
                        headView.setPadding(0, 20, 0, 0);
                    else if(paddingTop>minPadding&&paddingTop<20)
                        headView.setPadding(0,paddingTop,0,0);
                    else
                        headView.setPadding(0,minPadding,0,0);


                    if (paddingTop >= 0 && currentState == REFRESH_PULL) {
                        currentState = REFRESH_RELEASE;
                        Message msg = Message.obtain();
                        msg.what = currentState;
                        mHandler.sendMessage(msg);
                    } else if (paddingTop < 0 && currentState == REFRESH_RELEASE) {
                        currentState = REFRESH_PULL;
                        Message msg = Message.obtain();
                        msg.what = currentState;
                        mHandler.sendMessage(msg);
                    }
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(getFirstVisiblePosition()==0) {
                    int endY = (int) ev.getY();
                    if (endY - startY <= mHeight) {
                        headView.setPadding(0, minPadding, 0, 0);
                    }
                    if ((endY - startY) > mHeight) {
                        currentState = REFRESHING;
                        Message msg = Message.obtain();
                        msg.what = currentState;
                        mHandler.sendMessage(msg);
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    //初始化刷新箭头动画
    private void initRotateAnimation(){
        upAnim = new RotateAnimation(0,-180,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        upAnim.setDuration(200);
        upAnim.setFillAfter(true);

        downAnim = new RotateAnimation(-180,-360,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        downAnim.setDuration(200);
        downAnim.setFillAfter(true);
    }

    //得到当前时间
    private String getCurrentTime(){
        long time = System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        format.format(time);
        return format.format(time);
    }

}
