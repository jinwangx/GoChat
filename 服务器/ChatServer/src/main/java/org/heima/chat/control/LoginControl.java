package org.heima.chat.control;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.heima.chat.pojo.User;
import org.heima.chat.service.UserService;
import org.heima.chat.vo.ClientAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class LoginControl {
	private final static int	LOGIN_PASSWORD_ERROR	= 100;
	private final static int	LOGIN_ACCOUNT_MISS		= 101;

	private final static int	REGISTER_ACCOUNT_EXIST	= 150;

	@Autowired
	UserService					userService;

	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(String account, String password) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (account == null || password == null) {
			map.put("flag", false);
			map.put("errorCode", LOGIN_ACCOUNT_MISS);
			map.put("errorString", "用户不存在");
		} else {
			User user = userService.findUserByAccount(account);
			if (user == null) {
				map.put("flag", false);
				map.put("errorCode", LOGIN_ACCOUNT_MISS);
				map.put("errorString", "用户不存在");
			} else {
				if (password.equals(user.getPassword())) {
					user.setToken(UUID.randomUUID().toString());
					userService.updateToken(user);

					user = userService.findUserByAccount(account);
					
					map.put("flag", true);
					map.put("data", ClientAccount.toAccount(user));
				} else {
					map.put("flag", false);
					map.put("errorCode", LOGIN_PASSWORD_ERROR);
					map.put("errorString", "用户密码错误");
				}
			}
		}
		return new Gson().toJson(map);
	}

	@RequestMapping(value = "/register")
	@ResponseBody
	public String register(String account, String password) {

		Map<String, Object> map = new HashMap<String, Object>();

		if (account == null || password == null) {
			map.put("flag", false);
			map.put("errorCode", REGISTER_ACCOUNT_EXIST);
			map.put("errorString", "用户已经存在");
		} else {
			if (!userService.isExist(account)) {
				map.put("flag", true);

				//				try {
				//					StringBuilder sb = new StringBuilder(1024);
				//					sb.append("Name:TomPandas");
				//					sb.append("\r\n");
				//					sb.append("WebSite:http://www.cnblogs.com/tompandas");
				//					sb.append("\r\n");
				//					String str = sb.toString();
				//
				//					String picFormat = "png";//文件格式
				//					String path = "d:/test_qrcode";
				//					File file = new File(path + "." + picFormat);
				//					Hashtable hints = new Hashtable();
				//					BitMatrix bitMatrix;
				//					bitMatrix =
				//								new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 200,
				//																200, hints);
				//					MatrixToImageWriter.writeToFile(bitMatrix, picFormat, file);
				//				} catch (Exception e) {
				//					e.printStackTrace();
				//				}

				User user = userService.addUser(account, password);
				user.setToken(UUID.randomUUID().toString());
				userService.updateToken(user);
				
				user = userService.findUserByAccount(account);

				map.put("data", ClientAccount.toAccount(user));

			} else {
				map.put("flag", false);
				map.put("errorCode", REGISTER_ACCOUNT_EXIST);
				map.put("errorString", "用户已经存在");
			}
		}
		return new Gson().toJson(map);
	}

	@RequestMapping(value = "/logout")
	public void logout(String account) {

	}

}