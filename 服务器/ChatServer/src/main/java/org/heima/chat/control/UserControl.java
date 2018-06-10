package org.heima.chat.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.heima.chat.core.SerializableUtil;
import org.heima.chat.nio.BackTaskPusher;
import org.heima.chat.pojo.BackPushTask;
import org.heima.chat.pojo.Friend;
import org.heima.chat.pojo.User;
import org.heima.chat.service.BackPushTaskService;
import org.heima.chat.service.FriendService;
import org.heima.chat.service.UserService;
import org.heima.chat.vo.ClientPushTask;
import org.heima.chat.vo.ClientPushTaskFactory;
import org.heima.chat.vo.ClientSearchContactInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

@Controller
public class UserControl {
	private final static int	SEARCH_USER_MISS	= 200;

	@Autowired
	HttpSession					session;

	@Autowired
	UserService					userService;

	@Autowired
	BackPushTaskService			taskService;

	@Autowired
	FriendService				friendService;

	@Autowired
	BackTaskPusher				pusher;

	@RequestMapping(value = "/user/search")
	@ResponseBody
	public String search(HttpServletRequest request, String search) {
		Map<String, Object> map = new HashMap<String, Object>();

		User user = userService.findUserByAccount(search);
		if (user == null) {
			map.put("flag", false);
			map.put("errorCode", SEARCH_USER_MISS);
			map.put("errorString", "不存在用户");
		} else {
			map.put("flag", true);
			map.put("data", ClientSearchContactInfo.toUser(user));
		}
		return new Gson().toJson(map);
	}

	@RequestMapping(value = "/user/icon")
	@ResponseBody
	public String iconChanged(HttpServletRequest request,
								HttpServletResponse response)
	{
		String dir =
						session.getServletContext().getRealPath("/") + File.separator + "repo"
								+ File.separator + "icon";
		System.out.println("dir : " + dir);
		Map<String, Object> map = new HashMap<String, Object>();

		// 转型为MultipartHttpRequest：   
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String account = multipartRequest.getHeader("account");

		// 获得文件：   
		MultipartFile file = multipartRequest.getFile("file");
		// 获得文件名：   
		String filename = file.getOriginalFilename();
		// 获得输入流：   
		try {
			InputStream input = file.getInputStream();

			File localFile = new File(dir, filename);

			if (!localFile.getParentFile().exists()) {
				localFile.getParentFile().mkdirs();
			}

			System.out.println(localFile.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(localFile);

			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = input.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				fos.flush();
			}

			input.close();
			fos.close();

			map.put("flag", true);

			//更新用户信息
			User user = userService.findUserByAccount(account);
			user.setIcon(File.separator + "repo" + File.separator + "icon" + File.separator
							+ filename);
			userService.updateToken(user);

			List<Friend> friends = friendService.queryAllFriends(user.getAccount());
			System.out.println("1");
			if (friends != null) {
				System.out.println("1");
				String taskDir =
									session.getServletContext().getRealPath("/") + File.separator
											+ "repo"
											+ File.separator + "task";
				for (Friend friend : friends) {
					System.out.println(friend);
					ClientPushTask cpt =
											ClientPushTaskFactory.iconChangeTask(	account,
																					friend.getFriendAccount(),
																					user.getIcon());
					try {
						String absolutePath =
												new File(taskDir, friend.getFriendAccount() + "_"
																	+ System.currentTimeMillis()).getAbsolutePath();

						SerializableUtil.write(cpt, absolutePath);

						BackPushTask task = new BackPushTask();
						task.setCreateTime(new Timestamp(System.currentTimeMillis()));
						task.setReceiver(friend.getFriendAccount());
						task.setState(0);
						task.setPath(absolutePath);
						taskService.addTask(task);

						//发起推送服务
						pusher.doPush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();

			map.put("flag", false);
			map.put("errorCode", 300);
			map.put("errorString", "文件上传失败");
		}
		return new Gson().toJson(map);
	}

	@RequestMapping(value = "/user/name")
	@ResponseBody
	public String nameChanged(HttpServletRequest request, String name) {
		Map<String, Object> map = new HashMap<String, Object>();

		String account = request.getHeader("account");

		//更新用户信息
		User user = userService.findUserByAccount(account);
		user.setName(name);
		userService.updateToken(user);

		try {
			List<Friend> friends = friendService.queryAllFriends(user.getAccount());
			if (friends != null) {
				String taskDir =
									session.getServletContext().getRealPath("/") + File.separator
											+ "repo"
											+ File.separator + "task";
				for (Friend friend : friends) {
					ClientPushTask cpt =
											ClientPushTaskFactory.nameChangeTask(	account,
																					friend.getFriendAccount(),
																					user.getIcon());
					try {
						String absolutePath =
												new File(taskDir, friend.getFriendAccount() + "_"
																	+ System.currentTimeMillis()).getAbsolutePath();

						SerializableUtil.write(cpt, absolutePath);

						BackPushTask task = new BackPushTask();
						task.setCreateTime(new Timestamp(System.currentTimeMillis()));
						task.setReceiver(friend.getFriendAccount());
						task.setState(0);
						task.setPath(absolutePath);
						taskService.addTask(task);

						//发起推送服务
						pusher.doPush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			map.put("flag", true);
		} catch (Exception e) {
			map.put("flag", false);
			map.put("errorCode", 301);
			map.put("errorString", "名字改变失败");
		}

		return new Gson().toJson(map);
	}

}
