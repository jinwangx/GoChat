package com.jw.login;

import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jw.business.bean.Account;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityLoginBinding;
import com.jw.gochatbase.BaseActivity;
import com.jw.login.fragment.FillInfoFra;
import com.jw.login.fragment.LoginFra;
import com.jw.login.fragment.RegisterFra;

/**
 * 创建时间：
 * 更新时间 2017/11/2 21:46
 * 版本：
 * 作者：Mr.jin
 * 描述：
 * 默认进入登陆页面
 * 如果存在本地账户，但是没有完成详细信息，则进入资料填写页面
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG_SIGN_UP = "sign_up";
    private static final String TAG_SIGN_IN = "sign_IN";
    private static final String TAG_FILL_INFO = "fill_info";
    private FragmentManager fm;
    private LoginFra signInFra;
    private RegisterFra signUpFra;
    private FillInfoFra fillInfoFra;
    private Account me = ChatApplication.getAccount();
    private ActivityLoginBinding mBinding;


    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    @Override
    protected void init() {
        super.init();
        fm = getSupportFragmentManager();
        if ((me != null) && (me.getName() == null))
            goFillInfoFra();
        else
            goLoginFra();
    }

    //进入登陆页面
    public void goLoginFra() {
        if (signInFra == null) {
            signInFra = new LoginFra();
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fl_login, signInFra, TAG_SIGN_IN);
        fragmentTransaction.addToBackStack(TAG_SIGN_IN);
        fragmentTransaction.commit();
    }

    //进入注册页面
    public void goRegisterFra() {
        if (signUpFra == null) {
            signUpFra = new RegisterFra();
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.fl_login, signUpFra, TAG_SIGN_UP);
        fragmentTransaction.addToBackStack(TAG_SIGN_UP);
        fragmentTransaction.commit();
    }

    //进入填写信息页面
    public void goFillInfoFra() {
        if (fillInfoFra == null) {
            fillInfoFra = new FillInfoFra();
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.replace(R.id.fl_login, fillInfoFra, TAG_FILL_INFO);
        fragmentTransaction.addToBackStack(TAG_FILL_INFO);
        fragmentTransaction.commit();
    }
}
