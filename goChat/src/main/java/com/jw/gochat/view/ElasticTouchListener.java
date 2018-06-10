package com.jw.gochat.view;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Administrator on 2017/5/26.
 */

public class ElasticTouchListener implements View.OnTouchListener {
    private View inner;
    View[] children;
    private float y;
    private Rect normal = new Rect();
    private boolean animationFinish = true;
    private int[] tops;
    private int[] bottoms;

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (inner == null && children == null) {
            if (v instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) v;
                int count = group.getChildCount();
                if (count > 0) {
                    children = new View[count];
                    tops = new int[count];
                    bottoms = new int[count];
                    for (int i = 0; i < count; i++) {
                        children[i] = group.getChildAt(i);
                        tops[i] = children[i].getTop();
                        bottoms[i] = children[i].getBottom();
                    }
                }
            }
            inner = v;
        }
        if (animationFinish && (inner != null || children != null)) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
//              System.out.println("ACTION_DOWN");
                    y = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
//              System.out.println("ACTION_UP");
                    y = 0;
                    if (isNeedAnimation()) {
                        animation();
                    }
                    inner.invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
//              System.out.println("ACTION_MOVE");
                    final float preY = y == 0 ? ev.getY() : y;
                    float nowY = ev.getY();
                    int deltaY = (int) (preY - nowY);
                    y = nowY;
                    // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                    if (isNeedMove()) {
                        if (normal.isEmpty()) {
                            // 保存正常的布局位置
                            normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                        }
                        if (children != null) {
                            View view = null;
                            for (int i = 0; i < children.length; i++) {
                                view = children[i];
                                view.layout(view.getLeft(), view.getTop() - deltaY / 2, view.getRight(), view.getBottom() - deltaY / 2);
                            }
                        } else {
                            // 移动布局
                            inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2, inner.getRight(), inner.getBottom() - deltaY / 2);
                        }
                    }
                    inner.invalidate();
                    break;
                default:
                    break;
            }
        } else {
            return false;
        }
        return true;
    }

    // 开启动画移动

    public void animation() {
        if (children == null) {
            // 开启移动动画
            TranslateAnimation trans = new TranslateAnimation(0, 0, 0, normal.top - inner.getTop());
            trans.setDuration(200);
            trans.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    animationFinish = false;
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    inner.clearAnimation();
                    // 设置回到正常的布局位置
                    inner.layout(normal.left, normal.top, normal.right, normal.bottom);
                    normal.setEmpty();
                    animationFinish = true;
                }
            });
            inner.startAnimation(trans);
        } else {
            for (int i = 0; i < children.length; i++) {
                final View view = children[i];
                if (view.getVisibility() == View.VISIBLE) {
                    final int index = i;
                    // 开启移动动画
                    TranslateAnimation trans = new TranslateAnimation(0, 0, 0, tops[i] - view.getTop());
                    trans.setDuration(200);
                    trans.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            animationFinish = false;
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.clearAnimation();
                            // 设置回到正常的布局位置
                            view.layout(view.getLeft(), tops[index], view.getRight(), bottoms[index]);
                            normal.setEmpty();
                            animationFinish = true;
                        }
                    });
                    view.startAnimation(trans);
                }
            }
        }

    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
//      int offset = inner.getMeasuredHeight() - getHeight();
//      int scrollY = getScrollY();
//      if (scrollY == 0 || scrollY == offset) {
//          return true;
//      }
//      return false;

//      if (children != null && children.length > 0
//              && (children[children.length - 1].getBottom() <= inner.getPaddingTop()/*inner.getTop()*/
//              || children[0].getTop() >= inner.getHeight()
//              )) {
//          return false;
//      }

        return true;
    }
}