package com.jw.gochat.action;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import com.jw.gochat.bean.BackTask;
import com.jw.gochat.bean.Friend;
import com.jw.gochat.bean.Invitation;
import com.jw.gochat.bean.NetTask;
import com.jw.gochat.db.BackTaskDao;
import com.jw.gochat.db.FriendDao;
import com.jw.gochat.db.InvitationDao;
import com.jw.gochat.service.BackgroundService;
import com.jw.gochat.utils.BackTaskFactory;
import com.jw.gochat.utils.CommonUtil;
import com.jw.gochat.utils.CommonUtils;
import com.jw.gochat.utils.FileUtils;

import java.io.File;

/**
 * 创建时间：2017/11/2 20:20
 * 更新时间 2017/11/2 20:20
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class AcceptInvitationAction{

    public String getAction(){
        return "AcceptInvitation";
    }
    public void doAction(Activity activity,Object o){
        // 更新数据库
        InvitationDao dao = new InvitationDao(activity);
        Invitation invitation = (Invitation) o;
        invitation.setAgree(true);
        dao.updateInvitation(invitation);

        // 添加到好友列表
        FriendDao friendDao = new FriendDao(activity);
        Friend friend = friendDao.queryFriendByAccount(
                invitation.getOwner(), invitation.getAccount());
        if (friend == null) {
            friend = new Friend();
            friend.setAccount(invitation.getAccount());
            friend.setAlpha(CommonUtils.getFirstAlpha(invitation.getName()));
            friend.setName(invitation.getName());
            friend.setOwner(invitation.getOwner());
            friend.setSort(0);
            friend.setIcon(invitation.getIcon());
            friendDao.addFriend(friend);
        }

        // 存储到后台任务中
        String taskDir = CommonUtil.getTaskDir(activity);
        String file = CommonUtils.string2MD5(invitation.getAccount() + "_"
                + SystemClock.currentThreadTimeMillis());
        String path = new File(taskDir, file).getAbsolutePath();

        BackTask task = new BackTask();
        task.setOwner(invitation.getOwner());
        task.setPath(path);
        task.setState(0);
        new BackTaskDao(activity).addTask(task);

        NetTask netTask = BackTaskFactory.newFriendAcceptTask(
                invitation.getAccount(), invitation.getOwner());
        try {
            // 写入到缓存
            FileUtils.write(netTask, path);
            // 开启后台服务
            activity.startService(new Intent(activity,
                    BackgroundService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
