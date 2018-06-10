package org.heima.chat.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.heima.chat.pojo.User;
import org.heima.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;

public class UserValidateInterceptor extends
									HandlerInterceptorAdapter
{
	private final static int	HEADER_MISS			= 0;
	private final static int	TOKEN_FAILED		= 1;
	private final static int	LOGIN_ACCOUNT_MISS	= 101;

	@Autowired
	UserService					userService;

	private List<String>		ignoreUrls;

	@Override
	public boolean preHandle(HttpServletRequest request,
								HttpServletResponse response,
								Object handler) throws Exception
	{
		String requestUrl = request.getServletPath();

		if (ignoreUrls != null && ignoreUrls.contains(requestUrl)) { return true; }

		String account = request.getHeader("account");
		String token = request.getHeader("token");

		System.out.println("account : " + account);
		System.out.println("token : " + token);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
		if (account != null && token != null) {
			User user = userService.findUserByAccount(account);
			if (user == null) {
				map.put("errorCode", LOGIN_ACCOUNT_MISS);
				map.put("errorString", "用户不存在");
			} else {
				String tokenLocal = user.getToken();
				if (token.equals(tokenLocal)) {
					return true;
				} else {
					map.put("errorCode", TOKEN_FAILED);
					map.put("errorString", "token失效");
				}
			}
		} else {
			map.put("errorCode", HEADER_MISS);
			map.put("errorString", "用户信息缺失");
		}

		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(new Gson().toJson(map));
		out.flush();
		out.close();

		return false;
	}

	public List<String> getIgnoreUrls() {
		return ignoreUrls;
	}

	public void setIgnoreUrls(List<String> ignoreUrls) {
		this.ignoreUrls = ignoreUrls;
	}
}
