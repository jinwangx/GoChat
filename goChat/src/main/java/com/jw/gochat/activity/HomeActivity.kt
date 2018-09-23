package com.jw.gochat.activity

import android.content.Intent
import android.media.MediaPlayer
import android.support.v4.app.FragmentTabHost
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TabHost
import com.bumptech.glide.Glide
import com.jw.acccount.MQrActivity
import com.jw.acccount.SettingActivity
import com.jw.chat.ConversationFra
import com.jw.chat.business.ConversationBusiness
import com.jw.contact.FriendAddActivity
import com.jw.contact.fragment.FriendsFra
import com.jw.gochat.GoChatApplication
import com.jw.gochat.R
import com.jw.gochat.databinding.ActivityHomeBinding
import com.jw.gochat.event.TextEvent
import com.jw.gochat.fragment.MeFra
import com.jw.gochat.service.IMService
import com.jw.gochat.utils.CommonUtil
import com.jw.gochat.view.HomeDrag
import com.jw.gochat.view.NormalTopBar
import com.jw.gochat.view.TabIndicator
import com.jw.library.utils.ThemeUtils
import com.sencent.mm.GoChatBindingActivity
import kotlinx.android.synthetic.main.layout_normal_top_bar.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException

/**
 * 创建时间：
 * 更新时间 2017/11/4 20:47
 * 版本：
 * 作者：Mr.jin
 * 描述：应用主界面
 */

class HomeActivity : GoChatBindingActivity<ActivityHomeBinding>(), View.OnClickListener, HomeDrag.HomeStateListener, TabHost.OnTabChangeListener, NormalTopBar.AddFriendListener {
    private val specTags = arrayOf("消息", "联系人", "动态")
    private val clazzs = arrayOf<Class<*>>(ConversationFra::class.java, FriendsFra::class.java, MeFra::class.java)
    private val btnTabSelector = intArrayOf(R.drawable.action_btn_tab_news_selector, R.drawable.action_btn_tab_contacts_selector, R.drawable.action_btn_tab_me_selector)
    private val indicators = arrayOfNulls<TabIndicator>(3)
    private val tabSpecs = arrayOfNulls<TabHost.TabSpec>(3)
    private var allUnread: Int = 0
    private val me = GoChatApplication.getAccountInfo()
    private var player: MediaPlayer? = null
    private var tabHost: FragmentTabHost? = null

    override fun getLayoutId() = R.layout.activity_home

    override fun doConfig(intent: Intent) {
        EventBus.getDefault().register(this)
        if (!ThemeUtils.isServiceRunning(this@HomeActivity, IMService::class.java)) {
            startService(Intent(this@HomeActivity, IMService::class.java))
        }
        binding.apply {
            tabHost = findViewById(android.R.id.tabhost)
            tabHost!!.setOnTabChangedListener(this@HomeActivity)
            contentMain!!.ntHome.iv_nt_icon!!.setOnClickListener(this@HomeActivity)
            contentMain.ntHome.setTitle(specTags[0])
            //初始化面板上相关信息，如加载个人头像
            contentLeft!!.tvLeftAct.text = me!!.account
            Glide.with(this@HomeActivity).load(me.icon).into(contentMain.ntHome.iv_nt_icon!!)
            Glide.with(this@HomeActivity).load(me.icon).into(contentLeft!!.ivLeftIcon)
            contentMain.ntHome.setIcon()
            //初始化左面板相关信息
            contentLeft.llLeftMenu.adapter = ArrayAdapter(this@HomeActivity, android.R.layout.simple_list_item_1, CommonUtil.NAMES)
            clickListener = View.OnClickListener {
                when (it.id) {
                    R.id.tv_left_setting -> {
                        startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
                    }
                    R.id.iv_left_mqr -> {
                        startActivity(Intent(this@HomeActivity, MQrActivity::class.java))
                    }
                    R.id.ll_left_bg -> {
                        ThemeUtils.show(this@HomeActivity, "该功能将后续开放")
                    }
                }
            }
            hdHome.setHomeStateListener(this@HomeActivity)
        }
        //初始化tabHost
        tabHost!!.setup(this, supportFragmentManager, R.id.fl_content)
        for (i in btnTabSelector.indices) {
            tabSpecs[i] = tabHost!!.newTabSpec(specTags[i])
            indicators[i] = TabIndicator(this)
            indicators[i]!!.setText(specTags[i])
            indicators[i]!!.setDrawableBackground(btnTabSelector[i])
            tabSpecs[i]!!.setIndicator(indicators[i])
            tabSpecs[i]?.let { tabHost!!.addTab(it, clazzs[i], null) }
        }
        //初始化tab状态
        initTabState()
    }

    override fun doRefresh() {
        //读取消息数据后，返回时，重新刷新未读消息
        binding!!.hdHome.init()
        initTabState()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(textEvent: TextEvent) {
        try {
            player = MediaPlayer()
            player!!.setDataSource(filesDir.path + "/video/msgReceive.mp3")
            player!!.prepareAsync()
            player!!.setOnPreparedListener {
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_nt_icon -> if (binding!!.hdHome.state == HomeDrag.State.Close)
                binding!!.hdHome.open()
            else
                binding!!.hdHome.close()
        }
    }

    private fun initTabState() {
        allUnread = ConversationBusiness.getAllUnread(me!!.account!!)
        if (allUnread != 0) {
            indicators[0]!!.setUnreadVisible(View.VISIBLE)
            indicators[0]!!.setUnread(allUnread)
        } else {
            indicators[0]!!.setUnreadVisible(View.INVISIBLE)
        }
    }

    //当tab状态变化时
    override fun onTabChanged(tabId: String) {
        when (tabId) {
            "消息" -> binding!!.contentMain!!.ntHome.setAddFriendListener(null, View.GONE)
            "联系人" -> binding!!.contentMain!!.ntHome.setAddFriendListener(this, View.VISIBLE)
            "动态" -> binding!!.contentMain!!.ntHome.setAddFriendListener(null, View.GONE)
        }
        binding!!.contentMain!!.ntHome.setTitle(tabId)
    }

    //左面板打开动作时
    override fun open() {

    }

    //左面板关闭动作时
    override fun close() {
        val anim = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.shake)
        binding?.contentMain!!.ntHome.iv_nt_icon!!.startAnimation(anim)
    }

    //当点击friends面板上的添加朋友按钮时，执行该动作
    override fun add() {
        startActivity(Intent(this@HomeActivity, FriendAddActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}