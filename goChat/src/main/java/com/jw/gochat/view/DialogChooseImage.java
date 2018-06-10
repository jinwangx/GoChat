package com.jw.gochat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.jw.gochat.R;

public class DialogChooseImage extends Dialog implements View.OnClickListener{

	private DialogChooseImageListener mListener;

	public DialogChooseImage(Context context) {
		super(context, R.style.ActionSheetDialogStyle);
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		setContentView(R.layout.dialog_choose_img);
		Button btnCamera = (Button) findViewById(R.id.btn_dialog_camera);
		Button btnGallery = (Button) findViewById(R.id.btn_dialog_gallery);
		Button btnCancel = (Button) findViewById(R.id.btn_dialog_cancel);
		btnCamera.setOnClickListener(this);
		btnGallery.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_dialog_camera:
				mListener.camera();
				break;
			case R.id.btn_dialog_gallery:
				mListener.gallery();
				break;
			case R.id.btn_dialog_cancel:
				dismiss();
				break;
		}
	}

	public void setDialogChooseImageListener(DialogChooseImageListener listener){
		mListener=listener;
	}

	public interface DialogChooseImageListener{
		void camera();
		void gallery();
	}

}
