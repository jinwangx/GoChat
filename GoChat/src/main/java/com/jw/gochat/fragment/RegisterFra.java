package com.jw.gochat.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jw.gochat.R;
import com.jw.gochat.activity.LoginActivity;
import com.jw.gochat.base.BaseFragment;
import com.jw.gochat.bean.Account;
import com.jw.gochat.db.AccountDao;
import com.jw.gochat.utils.ThemeUtils;
import com.jw.gochat.view.DialogLoading;
import com.jw.gochat.view.NormalTopBar;

import Lib.GoChatManager;
import Lib.GoChatError;
import Lib.callback.GoChatObjectCallBack;
import butterknife.BindView;

/**
 * 创建时间：2017/3/25
 * 更新时间 2017/10/30 0:22
 * 版本：
 * 作者：Mr.jin
 * 描述：用户注册页面
 */

public class RegisterFra extends BaseFragment implements View.OnClickListener,NormalTopBar.BackListener{

    @BindView(R.id.nt_register)
    NormalTopBar ntRegister;
    @BindView(R.id.btn_register)
    Button btnSignUp;
    @BindView(R.id.et_register_act)
    EditText etAccount;
    @BindView(R.id.et_register_psw)
    EditText etPassword;

    private DialogLoading dialog;
    private static final int SUCCESS = 0;
    private static final int ERROR_NET = 1;
    private static final int ERROR_WEB = 2;
    private static final int ERROR_EXISTED = 3;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    ThemeUtils.show(getActivity(), "登陆成功");
                    break;
                case ERROR_NET:
                    ThemeUtils.show(getActivity(), "客户端网络异常");
                    break;
                case ERROR_WEB:
                    ThemeUtils.show(getActivity(), "服务器异常");
                    break;
                case ERROR_EXISTED:
                    ThemeUtils.show(getActivity(), "用户已存在");
                    break;
            }
            return false;
        }
    });

    @Override
    public View bindView() {
        View view=View.inflate(getActivity(), R.layout.fragment_register, null);
        return view;
    }

    @Override
    protected void initView() {
        btnSignUp.setOnClickListener(this);
        ntRegister.setBackListener(this);
    }

    @Override
    public void onClick(View v) {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        dialog = new DialogLoading(getActivity());
        dialog.show();
        GoChatManager.getInstance().register(account, password, registerCallBack);
    }

    private GoChatObjectCallBack registerCallBack=new GoChatObjectCallBack<Account>() {
        @Override
        public void onSuccess(Account account) {
            Message message = Message.obtain();
            // 初始化用户连接安全信息
            GoChatManager.getInstance().initAccount(
                    account.getAccount(), account.getToken());
            message.what = SUCCESS;
            mHandler.sendMessage(message);
            // 存储用户
            AccountDao dao = new AccountDao(getActivity());
            account.setCurrent(true);
            dao.addAccount(account);
            dialog.dismiss();
            ((LoginActivity) getActivity()).goFillInfoFra();
        }

        @Override
        public void onError(int error, String msg) {
            Message message = Message.obtain();
            dialog.dismiss();
            switch (error) {
                case GoChatError.ERROR_CLIENT_NET:
                    message.what = ERROR_NET;
                    break;
                case GoChatError.ERROR_SERVER:
                    message.what = ERROR_WEB;
                    break;
                case GoChatError.Register.ACCOUNT_EXIST:
                    message.what = ERROR_EXISTED;
                    break;
                default:
                    break;
            }
            mHandler.sendMessage(message);
        }
    };

    @Override
    public void back() {
        getFragmentManager().popBackStack();
    }
}
