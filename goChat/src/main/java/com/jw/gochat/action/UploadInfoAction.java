package com.jw.gochat.action;

import android.content.Context;
import android.content.Intent;

import com.jw.business.bean.Account;
import com.jw.business.bean.BackTask;
import com.jw.business.bean.Contact;
import com.jw.chat.GoChatURL;
import com.jw.chat.ThreadManager;
import com.jw.chat.db.bean.Message;
import com.jw.business.bean.NetTask;
import com.jw.business.db.dao.AccountDao;
import com.jw.business.db.dao.BackTaskDao;
import com.jw.business.db.dao.FriendDao;
import com.jw.chat.db.dao.MessageDao;
import com.jw.gochat.service.BackgroundService;
import com.jw.gochat.utils.BackTaskFactory;
import com.jw.gochat.utils.CommonUtil;
import com.jw.library.utils.FileUtils;

import java.io.File;


/**
 * 创建时间：2017/11/2 20:14
 * 更新时间 2017/11/2 20:14
 * 版本：
 * 作者：Mr.jin
 * 描述：
 */

public class UploadInfoAction {

    public String getAction() {
        return "uploadInfo";
    }

    public void doAction(final Context context, final Account me,
                         final String name, final File iconFile) {
        ThreadManager.getInstance().createLongPool(3, 3, 2L).execute(new Runnable() {
            @Override
            public void run() {
                FriendDao friendDao = new FriendDao(context);
                // 初始化通讯录
                Contact friend = new Contact();
                friend.setOwner(me.getAccount());
                friend.setAccount("GoChat");
                friend.setAlpha("G");
                friend.setArea("");
                // /data/Android/com.jw.qq/friendIcon/
                File file = new File(CommonUtil.getFriendIconDir(context), friend.getAccount());
                String path = file.getAbsolutePath();
                //存储默认头像到本地头像文件夹
                String inPath = CommonUtil.getIconDir(context) + "/default_icon_user.png";
                FileUtils.copy(inPath, path);
                friend.setIcon(path);
                friend.setName("小旺");
                friend.setNickName("");
                friend.setSort(1000);

                friendDao.addFriend(friend);
                MessageDao messageDao = new MessageDao(context);
                Message message = new Message();
                message.setAccount("GoChat");
                message.setContent("欢迎使用GoChat，有你更精彩");
                message.setCreateTime(System.currentTimeMillis());
                //未接收
                message.setDirection(1);
                message.setOwner(me.getAccount());
                message.setRead(false);
                messageDao.addMessage(message);
            }
        });

        me.setName(name);
        me.setIcon(GoChatURL.BASE_HTTP+"/repo/icon/"+me.getAccount()+".png");
        AccountDao dao = new AccountDao(context);
        dao.updateAccount(me);
        doAddTask(context,me,iconFile,"addName");
        doAddTask(context,me,iconFile,"addIcon");

    }

    /**
     * 添加后台任务
     * @param context
     * @param me
     * @param iconFile
     * @param type
     */
    public void doAddTask(Context context, Account me, File iconFile, String type) {
        String path = null;
        NetTask netTask = null;
        // 存储到后台任务中
        String fileName = me.getAccount() + "_" + System.currentTimeMillis();
        // /data/Android/com.jw.qq/task/
        path = new File(CommonUtil.getTaskDir(context), fileName).getAbsolutePath();
        if (type.equals("addName")) {
            netTask = BackTaskFactory.userNameChangeTask(me.getName());
        }
        if (type.equals("addIcon")) {
            netTask = BackTaskFactory.userIconChangeTask(me.getAccount(),iconFile.getAbsolutePath());
        }
        //将后台任务添加进数据库
        BackTask task = new BackTask();
        task.setOwner(me.getAccount());
        task.setPath(path);
        task.setState(0);
        new BackTaskDao(context).addTask(task);

        try {
            // 把网络任务属性序列化入path中
            FileUtils.write(netTask, path);

            // 开启后台服务
            context.startService(
                    new Intent(context, BackgroundService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
