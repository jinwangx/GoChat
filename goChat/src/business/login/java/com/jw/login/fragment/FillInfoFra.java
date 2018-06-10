package com.jw.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import com.bumptech.glide.Glide;
import com.jw.business.bean.Account;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.action.UploadInfoAction;
import com.jw.gochat.activity.HomeActivity;
import com.jw.gochat.databinding.FragmentFillInfoBinding;
import com.jw.gochat.utils.CommonUtil;
import com.jw.gochat.view.DialogChooseImage;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseFragment;
import com.jw.library.utils.FileUtils;
import java.io.File;

/**
 * 创建时间：2017/4/8
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：个人信息填充页面
 */

public class FillInfoFra extends BaseFragment implements View.OnClickListener, TextWatcher, NormalTopBar.BackListener {

    private final static int REQUEST_CODE_GALLERY = 0x11;
    private final static int REQUEST_CODE_CAMERA = 0x12;
    private final static int REQUEST_CODE_CROP = 0x13;
    private int crop = 200;
    private Account me = ChatApplication.getAccount();
    private File iconFile;
    private DialogChooseImage dialog;
    private FragmentFillInfoBinding mBinding;


    @Override
    public View bindView() {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_fill_info, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected void init() {
        super.init();
        //存储默认头像到本地头像文件夹 /data/Android/com.jw.qq/icon/
        String inPath = CommonUtil.getIconDir(getActivity()) + "/default_icon_user.png";
        iconFile = new File(CommonUtil.getIconDir(getActivity()), me.getAccount());
        FileUtils.copy(inPath, iconFile.getAbsolutePath());
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mBinding.ntFillInfo.setBackListener(this);
        mBinding.etFillName.addTextChangedListener(this);
        mBinding.btnFill.setOnClickListener(this);
        mBinding.ivFillIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fill_icon:
                dialog = new DialogChooseImage(getActivity());
                dialog.show();
                dialog.setDialogChooseImageListener(dialogChooseImageListener);
                break;
            case R.id.btn_fill:
                String name = mBinding.etFillName.getText().toString();
                UploadInfoAction uploadInfoAction = new UploadInfoAction();
                uploadInfoAction.doAction(getActivity(), me, name, iconFile);
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_GALLERY:
                Glide.with(this).load(iconFile.getAbsolutePath()).into(mBinding.ivFillIcon);
                break;
            case REQUEST_CODE_CAMERA:
                Uri uri = Uri.fromFile(iconFile);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("output", uri);
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);// 裁剪框比例
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", crop);// 输出图片大小
                intent.putExtra("outputY", crop);
                startActivityForResult(intent, REQUEST_CODE_CROP);
                break;
            case REQUEST_CODE_CROP:
                Glide.with(this).load(iconFile.getAbsolutePath()).into(mBinding.ivFillIcon);
                break;
            default:
                break;
        }
    }


    //选择头像dialog
    DialogChooseImage.DialogChooseImageListener dialogChooseImageListener =
            new DialogChooseImage.DialogChooseImageListener() {
                @Override
                public void camera() {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra("output", Uri.fromFile(iconFile));
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    dialog.dismiss();
                }

                @Override
                public void gallery() {
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                            "image/*");
                    intent.putExtra("output", Uri.fromFile(iconFile));
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);// 裁剪框比例
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", crop);// 输出图片大小
                    intent.putExtra("outputY", crop);
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                    dialog.dismiss();
                }
            };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //监听文本框中的文字变化
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = mBinding.etFillName.getText().toString().trim().length();
        if (length > 0)
            mBinding.btnFill.setEnabled(true);
        else
            mBinding.btnFill.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void back() {
        getFragmentManager().popBackStack();
    }
}
