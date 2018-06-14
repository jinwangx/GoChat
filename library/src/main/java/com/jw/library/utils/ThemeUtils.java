package com.jw.library.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


/**
 * 创建时间：2017/7/10
 * 更新时间：2017/11/10 下午 7:23
 * 作者：Mr.jin
 * 描述：与系统环境相关操作
 */

public class ThemeUtils {
    private static Window window;
    final public static int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static Toast mToast;


    /**
     *  吐司，单例且保证吐司在主线程运行
     * @param activity
     * @param content
     */
    public static void show(final Activity activity,final String content) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mToast==null)
                    mToast=new Toast(activity);
                mToast.makeText(activity, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 吐司，单例不能保证在主线程运行
     * @param context
     * @param content
     */
    public static void show(final Context context,final String content) {
        if(mToast==null)
            mToast=new Toast(context);
        mToast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }


    public static void show(final Activity activity, final int contentId) {
        if(mToast==null)
            mToast=new Toast(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.makeText(activity, contentId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     * @param activity 传入activity以获取窗口
     * @param color 颜色代码，如"#..."
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
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
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

    /**
     * 得到屏幕的亮度
     * @param activity
     * @return
     */
    public static float getWindowBrightness(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        return lp.screenBrightness;
    }

    /**
     * 设置屏幕亮度
     * @param activity
     * @param brightness
     */
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
     * 释放Assets中的资源
     * @param context
     * @param name 资源名称
     * @param path 释放到的路径
     */
    public static void mkdirsAssets(Context context,String name,String path){
        BufferedInputStream in=null;
        BufferedOutputStream out=null;
        try {
            in = new BufferedInputStream(context.getAssets().open(name));
            File file = new File(path);
            //释放目录
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if(file.exists())
                return;
            out = new BufferedOutputStream(new FileOutputStream(path));
            int len=0;
            while((len=in.read())!=-1){
                out.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(in!=null)
                    in.close();
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到资源文件的输入流
     * @param name 文件名
     * @return
     */
    public static InputStream getAssetsInputStream(Context context,String name){
        InputStream open=null;
        try {
            open = context.getAssets().open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return open;
    }

    /**
     * 从资源文件输入流中读取字符串
     * @param in
     * @return
     */
    public static String readFromAssetsStream(InputStream in)
    {
        String result=null;
        //字节输出流
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        try {
            while((len=in.read(buffer))!=-1)
            {
                out.write(buffer,0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            result=out.toString();
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static ImageView getImageView(Context context){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView iv = new ImageView(context);
        iv.setMaxHeight(300);
        params.gravity= Gravity.CENTER_HORIZONTAL;
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return iv;
    }

    public static TextView getTextView(Context context){
        TextView tv=new TextView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setTextSize(15);
        return tv;
    }

    /**
     *弹出请求窗口
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPermission(Activity activity, String permissionName){
        List<String> permissionStrs = new ArrayList<>();
        permissionStrs.add(permissionName);
        String[] stringArray = permissionStrs.toArray(new String[0]);
        if (permissionStrs.size() > 0) {
            activity.requestPermissions(stringArray,
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }

    /**
     * Android7.0以上时，不能往外部存储写数据，动态弹出权限框点击同意即可
     * @param permissionName
     * @param activity
     */
    public static int checkPermission(Activity activity ,String permissionName) {
        int hasPermission = ContextCompat.checkSelfPermission(
                activity, permissionName);
        return hasPermission;
    }

    /**
     * 判断服务是否运行
     *
     * @param context
     * @param clazz
     *            要判断的服务的class
     * @return
     */
    public static boolean isServiceRunning(Context context,
                                           Class<? extends Service> clazz) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(100);
        for (int i = 0; i < services.size(); i++) {
            String className = services.get(i).service.getClassName();
            if (className.equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对字符串进行MD5加密
     * @param inStr
     * @return
     */
    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}