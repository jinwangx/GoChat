package com.jw.gochat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

import com.jw.gochat.R;

/**
 * 创建时间：
 * 更新时间 2017/10/30 0:28
 * 版本：
 * 作者：Mr.jin
 * 描述：正在加载dialog
 */

public class DialogLoading extends Dialog {

	public DialogLoading(Context context) {
		super(context, R.style.dialog_logout);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.CENTER;
		setContentView(R.layout.dialog_loading);
	}

}
