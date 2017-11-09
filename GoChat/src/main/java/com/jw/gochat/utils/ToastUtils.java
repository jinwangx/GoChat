package com.jw.gochat.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * 创建时间：
 * 更新时间 2017/11/1 21:26
 * 版本：
 * 作者：Mr.jin
 * 描述：吐司工具
 */

public class ToastUtils {
	private static Toast mToast;
	public static void show(final Activity activity, final String content) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mToast==null)
					mToast=new Toast(activity);
				mToast.makeText(activity, content, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void show(final Activity activity, final int contentId) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(mToast==null)
					mToast=new Toast(activity);
				mToast.makeText(activity, contentId, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void show(final Context context, final String content) {
		if(mToast==null)
			mToast=new Toast(context);
		mToast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public static void show(final Context context, final int contentId) {
		if(mToast==null)
			mToast=new Toast(context);
		mToast.makeText(context, contentId, Toast.LENGTH_SHORT).show();
	}
}