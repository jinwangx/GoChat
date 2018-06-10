package com.jw.gochat.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * 创建时间：2017/3/26
 * 更新时间 2017/10/30 0:27
 * 版本：
 * 作者：Mr.jin
 * 描述：自定义侧拉控件
 */

public class HomeDrag extends RelativeLayout {

    private ViewDragHelper viewDragHelper;
    private int maxLeft;
    private View main_view;
    private View left_view;
    public State state=State.Close;
    private HomeStateListener mListener;
    private int realHeight1;
    private int realWidth1;
    private int realHeight2;
    private int realWidth2;

    public enum State{
        Close,Open
    }
    public HomeDrag(Context context) {
        this(context,null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int x= (int) ev.getX();
        int y= (int) ev.getY();
        if(state==State.Open && viewDragHelper.isViewUnder(main_view, x, y)){
            return true;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= (int) event.getX();
        int y= (int) event.getY();
        if(state==State.Open && viewDragHelper.isViewUnder(main_view, x, y)){
            if(event.getAction()==MotionEvent.ACTION_UP){
                close();
                state=State.Close;
            }
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public HomeDrag(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                viewDragHelper.captureChildView(main_view,pointerId);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int mLeft=left;
                if(mLeft<0)
                    mLeft=0;
                if(mLeft>maxLeft)
                    mLeft=maxLeft;
                return mLeft;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return maxLeft;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
/*                if(changedView==left_view){
                    //left_view.layout(0,0,maxLeft,mHeight);
                    if(left>maxLeft)
                        main_view.layout(maxLeft,0,maxLeft+mWidth,main_view.getBottom());
                    else
                        main_view.layout(main_view.getLeft()+dx,0,main_view.getRight()+dx,main_view.getBottom());
                }*/
                float percent = main_view.getLeft()/(float)maxLeft;
                excuteAnimation(percent);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if(xvel>0&&main_view.getLeft()>(maxLeft/2)) {
                    open();
                    state=State.Open;
                }
                else {
                    close();
                    state=State.Close;
                }

            }


            private void excuteAnimation(float percent) {
                //左面板随着右拉逐渐变大
                ViewHelper.setScaleX(left_view, 0.5f + 0.5f * percent);
                ViewHelper.setScaleY(left_view, 0.5f + 0.5f * percent);

                //右面板随着右拉逐渐变小
                ViewHelper.setScaleX(main_view, 1 - percent * 0.2f);
                ViewHelper.setScaleY(main_view, 1 - percent * 0.2f);

                //设置平移
                ViewHelper.setTranslationX(left_view, -maxLeft / 2 + maxLeft / 2
                        * percent);

                //左面板透明度随右拉逐渐加深
                ViewHelper.setAlpha(left_view, percent);

                //ViewGroup随
                //getBackground().setAlpha((int) ((1 - percent) * 255));
            }
        });
        //使左边界可以被抓住
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    //获取各view的宽高属性
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        left_view = getChildAt(0);
        main_view = getChildAt(1);
        realHeight1=left_view.getMeasuredHeight();
        realWidth1=left_view.getMeasuredWidth();
        realHeight2=main_view.getMeasuredHeight();
        realWidth2=main_view.getMeasuredWidth();
        maxLeft = left_view.getMeasuredWidth();
    }

    public void open() {
        mListener.open();
        state=State.Open;
        viewDragHelper.smoothSlideViewTo(main_view,maxLeft,0);
        //1.要用ViewDragHelper.smooth必须这么写
        ViewCompat.postInvalidateOnAnimation(HomeDrag.this);
    }

    public void close() {
        mListener.close();
        state=State.Open;
        viewDragHelper.smoothSlideViewTo(main_view,0,0);
        ViewCompat.postInvalidateOnAnimation(HomeDrag.this);
    }

    public void init(){
        if(left_view!=null&&main_view!=null) {
            //左面板随着右拉逐渐变大
            ViewHelper.setScaleX(left_view, 0.5f);
            ViewHelper.setScaleY(left_view, 0.5f);

            //右面板随着右拉逐渐变小
            ViewHelper.setScaleX(main_view, 1);
            ViewHelper.setScaleY(main_view, 1);

            //设置平移
            ViewHelper.setTranslationX(left_view, -maxLeft / 2);

            //左面板透明度随右拉逐渐加深
            ViewHelper.setAlpha(left_view, 0);
        }
    }

    //2.要用ViewDragHelper.smooth必须这么写
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public interface HomeStateListener{
        void open();
        void close();
    }
    public void setHomeStateListener(HomeStateListener listener){
        this.mListener=listener;
    }
}
