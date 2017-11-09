package com.jw.gochat.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.activity.HomeActivity;
import com.jw.gochat.activity.LoginActivity;
import com.jw.gochat.base.BaseFragment;
import com.jw.gochat.bean.Account;
import com.jw.gochat.db.AccountDao;
import com.jw.gochat.utils.CacheUtils;
import com.jw.gochat.utils.ToastUtils;
import com.jw.gochat.view.DialogSheet;

import Lib.GoChatManager;
import Lib.GoChatURL;
import Lib.callback.GoChatObjectCallBack;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 0:22
 * 版本：
 * 作者：Mr.jin
 * 描述：登陆页面
 */

public class LoginFra extends BaseFragment implements View.OnClickListener,TextWatcher{

    Dialog dialog;
    @BindView(R.id.iv_login_icon)
    CircleImageView ivIcon;
    @BindView(R.id.et_login_act)
    EditText etAccount;
    @BindView(R.id.et_login_psw)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_login_psw_miss)
    TextView tvForPassword;
    private AccountDao accountDao;

    @Override
    public View bindView() {
        View view=View.inflate(getActivity(), R.layout.fragment_login, null);
        accountDao=new AccountDao(getActivity());
        return view;
    }

    @Override
    protected void initView() {
        Glide.with(this).load(R.drawable.default_icon_user).into(ivIcon);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForPassword.setOnClickListener(this);
        etPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String account=etAccount.getText().toString();
                String password=etPassword.getText().toString();
                GoChatManager.getInstance().login(account,password ,loginCallBack);
                break;
            case R.id.tv_register:
                ((LoginActivity) getActivity()).goRegisterFra();
                break;
            case R.id.tv_login_psw_miss:
                final DialogSheet dialogSheet=new DialogSheet(getContext());
                dialogSheet.setPassListener(dialogListener);
                dialogSheet.show();//显示对话框
                break;
        }
    }

    /**
     * 登陆请求监听，如果成功，保存账户，进入主界面HomeActivity
     */
    private GoChatObjectCallBack loginCallBack=new GoChatObjectCallBack<Account>() {
        @Override
        public void onSuccess(Account account) {
            AccountDao dao = new AccountDao(getContext());
            account.setCurrent(true);
            account.setIcon(GoChatURL.BASE_HTTP+account.getIcon()
                    .replace("\\","/"));
            // 初始化用户连接安全信息
            GoChatManager.getInstance().initAccount(
                    account.getAccount(), account.getToken());
            if(dao.getByAccount(account.getAccount())!=null)
                dao.updateAccount(account);
            else
                dao.addAccount(account);
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }

        @Override
        public void onError(int error, String msg) {
            ToastUtils.show(getActivity(),"错误码:"+error+",错误信息:"+msg);
        }
    };

    /**
     * 忘记密码dialog点击监听
     */
    private DialogSheet.DialogSheetListener dialogListener=new DialogSheet.DialogSheetListener() {
        @Override
        public void foundPass() {
            Toast.makeText(getActivity(), "找回密码", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void loginSms() {
            Toast.makeText(getActivity(), "短信验证登录", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void changeIp() {
            final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            final AlertDialog ipDialog = builder.create();
            View view=View.inflate(getActivity(),R.layout.dialog_ip,null);
            final EditText etIp = (EditText) view.findViewById(R.id.et_ip_new);
            Button btnIpOk = (Button) view.findViewById(R.id.btn_ip_ok);
            Button btnIpCancel = (Button) view.findViewById(R.id.btn_ip_cancel);
            btnIpOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ipHost=etIp.getText().toString();
                    if(!TextUtils.isEmpty(ipHost)) {
                        ipDialog.dismiss();
                        dialog.dismiss();
                        CacheUtils.setCache("BASE_QQ_HOST",ipHost,getActivity());
                        ToastUtils.show(getActivity(),"应用将在2s后关闭,请重新启动以完成ip初始化");
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((ChatApplication)getActivity().getApplication()).closeApplication();
                            }
                        },2000);
                    }
                    else
                        etIp.setError("ip地址不能为空");
                }
            });
            btnIpCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ipDialog.dismiss();
                }
            });
            ipDialog.setView(view);
            ipDialog.show();
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null) {
            btnLogin.setEnabled(true);
            Account account=accountDao.getByAccount(s.toString());
            if(account!=null) {
                Glide.with(getActivity()).load(account.getIcon()).into(ivIcon);
            }
        } else
            btnLogin.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
