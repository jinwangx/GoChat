package com.jw.gochat.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.utils.CommonUtil;
import com.jw.gochat.utils.CommonUtils;
import com.jw.gochat.utils.ThemeUtils;

import Lib.ThreadManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建时间：
 * 更新时间 2017/10/29 2017/10/29
 * 版本：
 * 作者：Mr.jin
 * 描述：闪屏页面，包括资源的释放（如消息提示音，默认头像等）
 *       如果本地账户不为空且名字不为空，则直接跳转HomeActivity
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_splash_logo)
    ImageView iv_logo;
    private Account me= ChatApplication.getAccount();

    @Override
    public void bindView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        super.initView();
        initAnimation();
        ThreadManager.getInstance().createLongPool(3, 3, 2L).execute(new Runnable() {
            @Override
            public void run() {
                initMusic();
                initDefaultIcon();
            }
        });
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f,1.0f);
        alphaAnimation.setDuration(1000);
        iv_logo.startAnimation(alphaAnimation);
        //ObjectAnimator.ofFloat(rl, "alpha", 0.3f, 1.0f).setDuration(1000).start();
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                ThemeUtils.checkPermission(SplashActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                while(true){
                    int permission = ContextCompat.checkSelfPermission(
                            SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(permission==android.content.pm.PackageManager.PERMISSION_GRANTED )
                        break;
                }
                finish();
                if(me!=null&&me.getName()!=null) {
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                }else
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        },2000);
    }

    private void initMusic() {
        CommonUtils.mkdirsAssets(this, "msgReceive.mp3", getFilesDir() + "/video/" + "msgReceive.mp3");
    }

    private void initDefaultIcon() {
        CommonUtils.mkdirsAssets(this, "default_icon_user.png", CommonUtil.getIconDir(this) + "/default_icon_user.png");
    }
}
