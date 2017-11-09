package com.jw.gochat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.jw.gochat.R;

/**
 * 创建时间：
 * 更新时间 2017/10/30 0:28
 * 版本：
 * 作者：Mr.jin
 * 描述：登出dialog，即退出账号dialog
 */

public class DialogLogout extends Dialog {

	public DialogLogout(Context context) {
		super(context, R.style.dialog_logout);
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		setContentView(R.layout.dialog_logout);

		findViewById(R.id.btn_dialog_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
	}

	public void setClickLogoutListener(View.OnClickListener listener) {
		findViewById(R.id.btn_dialog_logout).setOnClickListener(listener);
	}

}
