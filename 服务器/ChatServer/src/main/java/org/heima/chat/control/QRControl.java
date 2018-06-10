package org.heima.chat.control;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.heima.chat.pojo.User;
import org.heima.chat.service.UserService;
import org.heima.chat.vo.ClientSearchContactInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class QRControl {
	private final static int	SEARCH_USER_MISS	= 200;

	@Autowired
	UserService					userService;

	@RequestMapping(value = "/QR/{str}")
	@ResponseBody
	public String qr(@PathVariable String str) {

		String account = decode(str, 5);

		System.out.println(account);

		Map<String, Object> map = new HashMap<String, Object>();

		User user = userService.findUserByAccount(account);
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

	private String encode(String str, int time) {
		String result = new String(str);
		for (int i = 0; i < time; i++) {
			result = new String(Base64.encodeBase64(result.getBytes()));
		}
		return result;
	}

	private String decode(String str, int time) {
		String result = new String(str);
		for (int i = 0; i < time; i++) {
			result = new String(Base64.decodeBase64(result.getBytes()));
		}
		return result;
	}
}
