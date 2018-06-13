package com.jw.gochat.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import com.jw.business.business.AccountInfoBusiness
import com.jw.gochat.ChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivitySplashBinding
import com.jw.gochat.utils.CommonUtil
import com.jw.gochatbase.BaseActivity
import com.jw.library.utils.ThemeUtils
import com.jw.login.LoginActivity

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：闪屏页面，包括资源的释放（如消息提示音，默认头像等）
 * 如果本地账户不为空且名字不为空，则直接跳转HomeActivity
 */

class SplashActivity : BaseActivity() {
    private val me = ChatApplication.getAccountInfo()
    private var mBinding: ActivitySplashBinding? = null

    public override fun bindView() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    override fun initView() {
        super.initView()
        initAnimation()
        Thread {
            run {
                initMusic()
                initDefaultIcon()
            }
        }.start()
    }

    private fun initAnimation() {
        val alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation.duration = 1000
        val startTime = System.currentTimeMillis()
        mBinding!!.ivSplashLogo.startAnimation(alphaAnimation)
        //ObjectAnimator.ofFloat(rl, "alpha", 0.3f, 1.0f).setDuration(1000).start();
        //固定停留本页面2s钟，2s钟后检查相关权限是否开启，如没开启，则弹出请求框请求用户开启
        Thread {
            run {
                try {
                    val endTime = System.currentTimeMillis()
                    val time = endTime - startTime
                    Thread.sleep(2000 - time)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    //检查需要系统同意的请求是否开启
                    val hasPermission = ThemeUtils.checkPermission(
                            this@SplashActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    //如果开启
                    if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                        if (me?.name != null) {
                            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        } else
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        //弹出请求框请求用户开启
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            ThemeUtils.requestPermission(this@SplashActivity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    }
                }
            }
        }.start()
    }

    private fun initMusic() {
        ThemeUtils.mkdirsAssets(this, "msgReceive.mp3", filesDir.toString() + "/video/" + "msgReceive.mp3")
    }

    private fun initDefaultIcon() {
        ThemeUtils.mkdirsAssets(this, "default_icon_user.png", CommonUtil.getIconDir(this) + "/default_icon_user.png")
    }

    /**
     * 权限设置后的回调函数，判断相应设置
     * @param requestCode
     * @param permissions  requestPermissions传入的参数为几个权限
     * @param grantResults 对应权限的设置结果
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ThemeUtils.REQUEST_CODE_ASK_PERMISSIONS -> {
                //可以遍历每个权限设置情况
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里写你需要相关权限的操作
                } else {
                    Toast.makeText(this@SplashActivity,
                            "权限没有开启,将无法加载图片", Toast.LENGTH_SHORT).show()
                }
                if (me?.name != null) {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                } else
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults)
    }
}