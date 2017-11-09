package Lib.core;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import Lib.msg.SequenceCreater;

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:52
 * 版本：
 * 作者：Mr.jin
 * 描述：认证请求封装(account,token,uuid)
 */

public class AuthRequest extends ChatRequest {
	private String account;
	private String token;

	private String sequence;

	public AuthRequest(String account, String token) {
		super(null, null);

		this.account = account;
		this.token = token;

		this.sequence = SequenceCreater.createSequence();
	}

	/**
	 * @return 返回一个该请求的uuid
	 */
	@Override
	public String getSequence() {
		return sequence;
	}

	/**
	 * 自动封装Auth请求
	 * @return 返回一个json化的Auth请求
	 */
	@Override
	public String getTransport() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("action", "auth");
		map.put("type", "request");
		map.put("sequence", sequence);
		map.put("sender", account);
		map.put("token", token);
		return new Gson().toJson(map);
	}

}
