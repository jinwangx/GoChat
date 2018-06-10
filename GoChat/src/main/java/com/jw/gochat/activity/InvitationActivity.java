package com.jw.gochat.activity;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.jw.business.bean.Account;
import com.jw.business.bean.Invitation;
import com.jw.business.db.dao.InvitationDao;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.action.AcceptInvitationAction;
import com.jw.gochat.adapter.InvitationAdapter;
import com.jw.gochat.databinding.ActivityInvitationBinding;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochatbase.BaseActivity;

public class InvitationActivity extends BaseActivity implements NormalTopBar.BackListener{
    private InvitationAdapter adapter;
    private Account me = ChatApplication.getAccount();
    private InvitationDao dao;
    private ActivityInvitationBinding mBinding;


    @Override
    public void bindView() {
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_invitation);
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.topBarInvitation.setTitle("新的朋友");
        adapter = new InvitationAdapter(this, null,acceptListener);
        mBinding.lvInvitation.setAdapter(adapter);
        mBinding.topBarInvitation.setBackListener(this);
    }

    protected void loadData() {
        dao = new InvitationDao(this);
        Cursor cursor = dao.queryCursor(me.getAccount());
        adapter.changeCursor(cursor);
    }


    View.OnClickListener acceptListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object o = v.getTag();
            if (o == null) {
                return;
            }
            AcceptInvitationAction action=new AcceptInvitationAction();
            action.doAction(InvitationActivity.this,o);
            // ui更新
            adapter.changeCursor(new InvitationDao(InvitationActivity.this).
                    queryCursor(((Invitation)o).getOwner()));

            finish();
        }
    };

    @Override
    public void back() {
        finish();
    }
}
