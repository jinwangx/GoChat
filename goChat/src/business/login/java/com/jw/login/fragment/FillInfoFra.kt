package com.jw.login.fragment

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bumptech.glide.Glide
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.action.UploadInfoAction
import com.jw.gochat.activity.HomeActivity
import com.jw.gochat.databinding.FragmentFillInfoBinding
import com.jw.gochat.utils.CommonUtil
import com.jw.gochat.view.DialogChooseImage
import com.jw.gochat.view.NormalTopBar
import com.jw.gochatbase.BaseFragment
import com.jw.library.utils.FileUtils
import java.io.File

/**
 * 创建时间：2017/4/8
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：个人信息填充页面
 */

class FillInfoFra : BaseFragment(), View.OnClickListener, TextWatcher, NormalTopBar.BackListener {
    private val crop = 200
    private val me = ChatApplication.getAccount()
    private var iconFile: File? = null
    private var dialog: DialogChooseImage? = null
    private var mBinding: FragmentFillInfoBinding? = null


    //选择头像dialog
    private var dialogChooseImageListener: DialogChooseImage.DialogChooseImageListener = object : DialogChooseImage.DialogChooseImageListener {
        override fun camera() {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra("output", Uri.fromFile(iconFile))
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
            dialog!!.dismiss()
        }

        override fun gallery() {
            val intent = Intent("android.intent.action.PICK")
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    "image/*")
            intent.putExtra("output", Uri.fromFile(iconFile))
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1)// 裁剪框比例
            intent.putExtra("aspectY", 1)
            intent.putExtra("outputX", crop)// 输出图片大小
            intent.putExtra("outputY", crop)
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
            dialog!!.dismiss()
        }
    }


    public override fun bindView(): View {
        mBinding = DataBindingUtil.inflate(activity!!.layoutInflater, R.layout.fragment_fill_info, null, false)
        return mBinding!!.root
    }

    override fun init() {
        super.init()
        //存储默认头像到本地头像文件夹 /data/Android/com.jw.qq/icon/
        val inPath = CommonUtil.getIconDir(this.activity!!) + "/default_icon_user.png"
        iconFile = File(CommonUtil.getIconDir(this.activity!!), me.account!!)
        FileUtils.copy(inPath, iconFile!!.absolutePath)
    }

    override fun initEvent() {
        super.initEvent()
        mBinding!!.ntFillInfo.setBackListener(this)
        mBinding!!.etFillName.addTextChangedListener(this)
        mBinding!!.btnFill.setOnClickListener(this)
        mBinding!!.ivFillIcon.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_fill_icon -> {
                dialog = DialogChooseImage(activity)
                dialog!!.show()
                dialog!!.setDialogChooseImageListener(dialogChooseImageListener)
            }
            R.id.btn_fill -> {
                val name = mBinding!!.etFillName.text.toString()
                val uploadInfoAction = UploadInfoAction()
                uploadInfoAction.doAction(this.activity!!, me, name, this.iconFile!!)
                startActivity(Intent(activity, HomeActivity::class.java))
                activity!!.finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_GALLERY -> Glide.with(this).load(iconFile!!.absolutePath).into(mBinding!!.ivFillIcon)
            REQUEST_CODE_CAMERA -> {
                val uri = Uri.fromFile(iconFile)
                val intent = Intent("com.android.camera.action.CROP")
                intent.setDataAndType(uri, "image/*")
                intent.putExtra("output", uri)
                intent.putExtra("crop", "true")
                intent.putExtra("aspectX", 1)// 裁剪框比例
                intent.putExtra("aspectY", 1)
                intent.putExtra("outputX", crop)// 输出图片大小
                intent.putExtra("outputY", crop)
                startActivityForResult(intent, REQUEST_CODE_CROP)
            }
            REQUEST_CODE_CROP -> Glide.with(this).load(iconFile!!.absolutePath).into(mBinding!!.ivFillIcon)
            else -> {
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    //监听文本框中的文字变化
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val length = mBinding!!.etFillName.text.toString().trim { it <= ' ' }.length
        mBinding!!.btnFill.isEnabled = length > 0
    }

    override fun afterTextChanged(s: Editable) {

    }

    override fun back() {
        fragmentManager!!.popBackStack()
    }

    companion object {

        private const val REQUEST_CODE_GALLERY = 0x11
        private const val REQUEST_CODE_CAMERA = 0x12
        private const val REQUEST_CODE_CROP = 0x13
    }
}