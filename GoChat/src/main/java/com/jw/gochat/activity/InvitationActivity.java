package com.jw.gochat.activity;

import android.database.Cursor;
import android.view.View;
import android.widget.ListView;

import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.action.AcceptInvitationAction;
import com.jw.gochat.adapter.InvitationAdapter;
import com.jw.gochat.base.BaseActivity;
import com.jw.gochat.bean.Account;
import com.jw.gochat.bean.Invitation;
import com.jw.gochat.db.InvitationDao;
import com.jw.gochat.view.NormalTopBar;

import butterknife.BindView;

public class InvitationActivity extends BaseActivity implements NormalTopBar.BackListener{
    @BindView(R.id.top_bar_invitation)
    NormalTopBar mTopBar;
    @BindView(R.id.lv_invitation)
    ListView listView;
    private InvitationAdapter adapter;
    private  Account me = ChatApplication.getAccount();
    private InvitationDao dao;


    @Override
    public void bindView() {
        setContentView(R.layout.activity_invitation);
    }

    @Override
    protected void initView() {
        super.initView();
        mTopBar.setTitle("新的朋友");
        adapter = new InvitationAdapter(this, null,acceptListener);
        listView.setAdapter(adapter);
        mTopBar.setBackListener(this);
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
