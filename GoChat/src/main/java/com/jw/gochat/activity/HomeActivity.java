package com.jw.gochat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;

import com.bumptech.glide.Glide;
import com.jw.acccount.MQrActivity;
import com.jw.acccount.SettingActivity;
import com.jw.business.bean.Account;
import com.jw.chat.ConversationFra;
import com.jw.chat.db.dao.MessageDao;
import com.jw.contact.FriendAddActivity;
import com.jw.gochat.ChatApplication;
import com.jw.gochat.R;
import com.jw.gochat.databinding.ActivityHomeBinding;
import com.jw.gochat.fragment.MeFra;
import com.jw.gochat.receiver.PushReceiver;
import com.jw.gochat.service.ChatCoreService;
import com.jw.gochat.utils.CommonUtil;
import com.jw.gochat.view.HomeDrag;
import com.jw.gochat.view.NormalTopBar;
import com.jw.gochat.view.TabIndicator;
import com.jw.gochatbase.BaseActivity;
import com.jw.library.utils.ThemeUtils;
import com.jw.login.fragment.FriendsFra;

import java.io.IOException;

import static com.jw.gochat.ChatApplication.getAccount;

/**
 * 创建时间：
 * 更新时间 2017/11/4 20:47
 * 版本：
 * 作者：Mr.jin
 * 描述：应用主界面
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener
        , HomeDrag.HomeStateListener, TabHost.OnTabChangeListener, NormalTopBar.AddFriendListener {

    private String[] specTags = {"消息", "联系人", "动态"};
    private Class[] clazzs = {ConversationFra.class, FriendsFra.class, MeFra.class};
    private int[] btnTabSelector = {R.drawable.action_btn_tab_news_selector,
            R.drawable.action_btn_tab_contacts_selector, R.drawable.action_btn_tab_me_selector};
    private TabIndicator[] indicators = new TabIndicator[3];
    private TabHost.TabSpec[] tabSpecs = new TabHost.TabSpec[3];

    private int allUnread;
    private Account me = ChatApplication.getAccount();
    private MediaPlayer player;
    private PushReceiver pushReceiver = new PushReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                player = new MediaPlayer();
                player.setDataSource(getFilesDir().getPath() + "/video/msgReceive.mp3");
                player.prepareAsync();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    //准备完毕时，此方法调用
                    public void onPrepared(MediaPlayer mp) {
                        player.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            initTabState();
        }
    };
    private ActivityHomeBinding mBinding;
    private FragmentTabHost tabHost;
    private NormalTopBar ntHome;
    private ImageView ivNtIcon;


    @Override
    public void bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        ntHome = mBinding.contentMain.ntHome;
    }

    @Override
    protected void init() {
        super.init();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PushReceiver.ACTION_TEXT);
        registerReceiver(pushReceiver, filter);
        if (!ThemeUtils.isServiceRunning(HomeActivity.this, ChatCoreService.class)) {
            startService(new Intent(HomeActivity.this, ChatCoreService.class));
        }
    }

    @Override
    protected void initView() {
        super.initView();
        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setOnTabChangedListener(this);
        ivNtIcon = ntHome.findViewById(R.id.iv_nt_icon);
        ivNtIcon.setOnClickListener(this);
        //初始化tabHost
        initTabHost();
        //初始化面板上相关信息，如加载个人头像
        initInfo();
        //初始化左面板相关信息
        initMenuList();
        //初始化tab状态
        initTabState();

        mBinding.contentLeft.tvLeftSetting.setOnClickListener(this);
        mBinding.contentLeft.ivLeftMqr.setOnClickListener(this);
        mBinding.contentLeft.llLeftBg.setOnClickListener(this);
        mBinding.hdHome.setHomeStateListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_nt_icon:
                if (mBinding.hdHome.state == HomeDrag.State.Close)
                    mBinding.hdHome.open();
                else
                    mBinding.hdHome.close();
                break;
            case R.id.tv_left_setting:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
            case R.id.iv_left_mqr:
                startActivity(new Intent(HomeActivity.this, MQrActivity.class));
                break;
            case R.id.ll_left_bg:
                ThemeUtils.show(HomeActivity.this, "该功能将后续开放");
        }
    }

    private void initTabHost() {
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_content);
        for (int i = 0; i < btnTabSelector.length; i++) {
            tabSpecs[i] = tabHost.newTabSpec(specTags[i]);
            indicators[i] = new TabIndicator(this);
            indicators[i].setText(specTags[i]);
            indicators[i].setDrawableBackground(btnTabSelector[i]);
            tabSpecs[i].setIndicator(indicators[i]);
            tabHost.addTab(tabSpecs[i], clazzs[i], null);
        }
        mBinding.contentMain.ntHome.setTitle(specTags[0]);
    }

    private void initInfo() {
        mBinding.contentLeft.tvLeftAct.setText(me.getAccount());
        Glide.with(this).load(me.getIcon()).into(ivNtIcon);
        Glide.with(this).load(me.getIcon()).into(mBinding.contentLeft.ivLeftIcon);
        mBinding.contentMain.ntHome.setIcon();
    }

    private void initMenuList() {
        mBinding.contentLeft.llLeftMenu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CommonUtil.NAMES));
    }


    private void initTabState() {
        MessageDao dao = new MessageDao(this);
        allUnread = dao.getAllUnread(getAccount().getAccount());
        if (allUnread != 0) {
            indicators[0].setUnreadVisible(View.VISIBLE);
            indicators[0].setUnread(allUnread);
        } else {
            indicators[0].setUnreadVisible(View.INVISIBLE);
        }
    }

    //读取消息数据后，返回时，重新刷新未读消息
    @Override
    protected void onResume() {
        super.onResume();
        mBinding.hdHome.init();
        initTabState();
    }

    //当tab状态变化时
    @Override
    public void onTabChanged(String tabId) {
        switch (tabId) {
            case "消息":
                mBinding.contentMain.ntHome.setAddFriendListener(null, View.GONE);
                break;
            case "联系人":
                mBinding.contentMain.ntHome.setAddFriendListener(this, View.VISIBLE);
                break;
            case "动态":
                mBinding.contentMain.ntHome.setAddFriendListener(null, View.GONE);
                break;
        }
        mBinding.contentMain.ntHome.setTitle(tabId);
    }

    //左面板打开动作时
    @Override
    public void open() {

    }

    //左面板关闭动作时
    @Override
    public void close() {
        Animation anim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.shake);
        ivNtIcon.startAnimation(anim);
    }

    //当点击friends面板上的添加朋友按钮时，执行该动作
    @Override
    public void add() {
        startActivity(new Intent(HomeActivity.this, FriendAddActivity.class));
    }
}
