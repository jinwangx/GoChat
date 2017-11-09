package com.jw.gochat.drag;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SlideDeleteDrag extends RelativeLayout {

    private ViewDragHelper mHelper;
    private int minLeft;
    private View chatView;
    public static State state=State.Close;
    private int startX;
    private int startY;

    public enum State{
        Close,Open,Sliding
    }

    public SlideDeleteDrag(Context context) {
        this(context,null);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        mHelper.processTouchEvent(event);
        return false;
    }


    public SlideDeleteDrag(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child==chatView;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 0;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int mLeft=left;
                if(left<minLeft)
                    mLeft=minLeft;
                if(left>0)
                    mLeft=0;
                return mLeft;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                changedView.layout(left,0,left+getWidth(),getHeight());
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if(xvel<0)
                    open();
                else
                    close();
            }
        });
    }

    public void open(){
        mHelper.smoothSlideViewTo(chatView,minLeft,0);
        ViewCompat.postInvalidateOnAnimation(this);
    }
    public void close(){
        mHelper.smoothSlideViewTo(chatView,0,0);
        ViewCompat.postInvalidateOnAnimation(this);
    }
    @Override
    public void computeScroll() {
        if(mHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        View deleteView=getChildAt(0);
        minLeft = -deleteView.getMeasuredWidth();
        chatView = getChildAt(1);
    }
}
