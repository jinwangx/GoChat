package com.jw.gochat.action

import android.app.Activity
import android.content.Intent
import android.os.SystemClock
import com.jw.business.model.bean.BackTask
import com.jw.business.model.bean.Contact
import com.jw.business.model.bean.Invitation
import com.jw.business.db.dao.BackTaskDao
import com.jw.business.db.dao.FriendDao
import com.jw.business.db.dao.InvitationDao
import com.jw.gochat.service.BackgroundService
import com.jw.gochat.utils.BackTaskFactory
import com.jw.gochat.utils.CommonUtil
import com.jw.library.utils.CommonUtils
import com.jw.library.utils.FileUtils
import com.jw.library.utils.ThemeUtils
import java.io.File

/**
 * 创建时间：2017/11/2 20:20
 * 更新时间 2017/11/2 20:20
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

class AcceptInvitationAction {

    val action: String
        get() = "AcceptInvitation"

    fun doAction(activity: Activity, o: Any) {
        // 更新数据库
        val dao = InvitationDao(activity)
        val invitation = o as Invitation
        invitation.isAgree = true
        dao.updateInvitation(invitation)

        // 添加到好友列表
        val friendDao = FriendDao(activity)
        var friend = friendDao.queryFriendByAccount(
                invitation.owner!!, invitation.account!!)
        if (friend == null) {
            friend = Contact()
            friend.account = invitation.account
            friend.alpha = CommonUtils.getFirstAlpha(invitation.name)
            friend.name = invitation.name
            friend.owner = invitation.owner
            friend.sort = 0
            friend.icon = invitation.icon
            friendDao.addFriend(friend)
        }

        // 存储到后台任务中
        val taskDir = CommonUtil.getTaskDir(activity)
        val file = ThemeUtils.string2MD5(invitation.account + "_"
                + SystemClock.currentThreadTimeMillis())
        val path = File(taskDir, file).absolutePath

        val task = BackTask()
        task.owner = invitation.owner
        task.path = path
        task.state = 0
        BackTaskDao(activity).addTask(task)

        val netTask = BackTaskFactory.newFriendAcceptTask(
                invitation.account!!, invitation.owner!!)
        try {
            // 写入到缓存
            FileUtils.write(netTask, path)
            // 开启后台服务
            activity.startService(Intent(activity,
                    BackgroundService::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
