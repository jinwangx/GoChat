package com.jw.gochat.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.media.MediaPlayer
import android.support.v4.app.FragmentTabHost
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TabHost
import com.bumptech.glide.Glide
import com.jw.acccount.MQrActivity
import com.jw.acccount.SettingActivity
import com.jw.chat.ConversationFra
import com.jw.chat.business.ConversationBusiness
import com.jw.contact.FriendAddActivity
import com.jw.gochat.ChatApplication
import com.jw.gochat.ChatApplication.getAccountInfo
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityHomeBinding
import com.jw.gochat.fragment.MeFra
import com.jw.gochat.receiver.PushReceiver
import com.jw.gochat.service.ChatCoreService
import com.jw.gochat.utils.CommonUtil
import com.jw.gochat.view.HomeDrag
import com.jw.gochat.view.NormalTopBar
import com.jw.gochat.view.TabIndicator
import com.jw.gochatbase.BaseActivity
import com.jw.library.utils.ThemeUtils
import com.jw.login.fragment.FriendsFra
import java.io.IOException

/**
 * 创建时间：
 * 更新时间 2017/11/4 20:47
 * 版本：
 * 作者：Mr.jin
 * 描述：应用主界面
 */

class HomeActivity : BaseActivity(), View.OnClickListener, HomeDrag.HomeStateListener, TabHost.OnTabChangeListener, NormalTopBar.AddFriendListener {

    private val specTags = arrayOf("消息", "联系人", "动态")
    private val clazzs = arrayOf<Class<*>>(ConversationFra::class.java, FriendsFra::class.java, MeFra::class.java)
    private val btnTabSelector = intArrayOf(R.drawable.action_btn_tab_news_selector, R.drawable.action_btn_tab_contacts_selector, R.drawable.action_btn_tab_me_selector)
    private val indicators = arrayOfNulls<TabIndicator>(3)
    private val tabSpecs = arrayOfNulls<TabHost.TabSpec>(3)

    private var allUnread: Int = 0
    private val me = ChatApplication.getAccountInfo()
    private var player: MediaPlayer? = null
    private val pushReceiver = object : PushReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                player = MediaPlayer()
                player!!.setDataSource(filesDir.path + "/video/msgReceive.mp3")
                player!!.prepareAsync()
                player!!.setOnPreparedListener() {
                    //准备完毕时，此方法调用
                    run {
                        player!!.start()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            initTabState()
        }
    }
    private var mBinding: ActivityHomeBinding? = null
    private var tabHost: FragmentTabHost? = null
    private var ntHome: NormalTopBar? = null
    private var ivNtIcon: ImageView? = null


    public override fun bindView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        ntHome = mBinding!!.contentMain!!.ntHome
    }

    override fun init() {
        super.init()
        val filter = IntentFilter()
        filter.addAction(PushReceiver.ACTION_TEXT)
        registerReceiver(pushReceiver, filter)
        if (!ThemeUtils.isServiceRunning(this@HomeActivity, ChatCoreService::class.java)) {
            startService(Intent(this@HomeActivity, ChatCoreService::class.java))
        }
    }

    override fun initView() {
        super.initView()
        tabHost = findViewById(android.R.id.tabhost)
        tabHost!!.setOnTabChangedListener(this)
        ivNtIcon = ntHome!!.findViewById(R.id.iv_nt_icon)
        ivNtIcon!!.setOnClickListener(this)
        //初始化tabHost
        initTabHost()
        //初始化面板上相关信息，如加载个人头像
        initInfo()
        //初始化左面板相关信息
        initMenuList()
        //初始化tab状态
        initTabState()

        mBinding!!.contentLeft!!.tvLeftSetting.setOnClickListener(this)
        mBinding!!.contentLeft!!.ivLeftMqr.setOnClickListener(this)
        mBinding!!.contentLeft!!.llLeftBg.setOnClickListener(this)
        mBinding!!.hdHome.setHomeStateListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_nt_icon -> if (mBinding!!.hdHome.state == HomeDrag.State.Close)
                mBinding!!.hdHome.open()
            else
                mBinding!!.hdHome.close()
            R.id.tv_left_setting -> startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
            R.id.iv_left_mqr -> startActivity(Intent(this@HomeActivity, MQrActivity::class.java))
            R.id.ll_left_bg -> ThemeUtils.show(this@HomeActivity, "该功能将后续开放")
        }
    }

    private fun initTabHost() {
        tabHost!!.setup(this, supportFragmentManager, R.id.fl_content)
        for (i in btnTabSelector.indices) {
            tabSpecs[i] = tabHost!!.newTabSpec(specTags[i])
            indicators[i] = TabIndicator(this)
            indicators[i]!!.setText(specTags[i])
            indicators[i]!!.setDrawableBackground(btnTabSelector[i])
            tabSpecs[i]!!.setIndicator(indicators[i])
            tabSpecs[i]?.let { tabHost!!.addTab(it, clazzs[i], null) }
        }
        mBinding!!.contentMain!!.ntHome.setTitle(specTags[0])
    }

    private fun initInfo() {
        mBinding!!.contentLeft!!.tvLeftAct.text = me.account
        Glide.with(this).load(me.icon).into(ivNtIcon!!)
        Glide.with(this).load(me.icon).into(mBinding!!.contentLeft!!.ivLeftIcon)
        mBinding!!.contentMain!!.ntHome.setIcon()
    }

    private fun initMenuList() {
        mBinding!!.contentLeft!!.llLeftMenu.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, CommonUtil.NAMES)
    }


    private fun initTabState() {
        allUnread = ConversationBusiness.getAllUnread(getAccountInfo().account!!)
        if (allUnread != 0) {
            indicators[0]!!.setUnreadVisible(View.VISIBLE)
            indicators[0]!!.setUnread(allUnread)
        } else {
            indicators[0]!!.setUnreadVisible(View.INVISIBLE)
        }
    }

    //读取消息数据后，返回时，重新刷新未读消息
    override fun onResume() {
        super.onResume()
        mBinding!!.hdHome.init()
        initTabState()
    }

    //当tab状态变化时
    override fun onTabChanged(tabId: String) {
        when (tabId) {
            "消息" -> mBinding!!.contentMain!!.ntHome.setAddFriendListener(null, View.GONE)
            "联系人" -> mBinding!!.contentMain!!.ntHome.setAddFriendListener(this, View.VISIBLE)
            "动态" -> mBinding!!.contentMain!!.ntHome.setAddFriendListener(null, View.GONE)
        }
        mBinding!!.contentMain!!.ntHome.setTitle(tabId)
    }

    //左面板打开动作时
    override fun open() {

    }

    //左面板关闭动作时
    override fun close() {
        val anim = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.shake)
        ivNtIcon!!.startAnimation(anim)
    }

    //当点击friends面板上的添加朋友按钮时，执行该动作
    override fun add() {
        startActivity(Intent(this@HomeActivity, FriendAddActivity::class.java))
    }
}