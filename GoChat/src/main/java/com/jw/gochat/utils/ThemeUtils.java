package com.jw.gochat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ThemeUtils {
    private static Window window;
    final public static int REQUEST_CODE_ASK_PERMISSIONS = 123;

    /**
     *
     * @param activity 传入activity以获取窗口
     * @param color 颜色代码，如"Color."
     */
    public static void changeStatusBar(Activity activity,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }  else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (color);
        }
    }

    /**
     *
     * @param activity 传入activity以获取窗口
     * @param color 颜色代码，如"#..."
     */
    public static void changeStatusBar(Activity activity,String color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }  else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (Color.parseColor(color));
        }
    }

    public static int getStatusBarHeight(Context context){
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     *
     * @param view 要改变背景颜色的view
     * @param color  颜色代码，如"#..."
     */
    public static void changeViewColor(View view,int color){
        if (view != null)
            view.setBackgroundColor(color);
    }

    /**
     * 得到屏幕宽
     * @param activity
     * @return
     */
    public static int getWindowWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 得到屏幕高
     * @param activity
     * @return
     */
    public static int getWindowHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        return display.getHeight();
    }

    public static float getWindowBrightness(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        return lp.screenBrightness;
    }

    public static void setWindowBrightness(Activity activity,float brightness){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
    }

    /**
     * dip 转 px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转 dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Android7.0以上时，不能往外部存储写数据，动态弹出权限框点击同意即可
     * @param permissionName
     * @param activity
     */
    public static void checkPermission(Activity activity ,String permissionName) {
        if(Build.VERSION.SDK_INT >= 23) {
            List<String> permissionStrs = new ArrayList<>();
            int hasWriteSdcardPermission =
                    ContextCompat.checkSelfPermission(
                            activity, permissionName);
            if(hasWriteSdcardPermission !=
                    PackageManager.PERMISSION_GRANTED) {
                permissionStrs.add(
                        permissionName
                );
            }
            String[] stringArray = permissionStrs.toArray(new String[0]);
            if (permissionStrs.size() > 0) {
                activity.requestPermissions(stringArray,
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
    }
}
