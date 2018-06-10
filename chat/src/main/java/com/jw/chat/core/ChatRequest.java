package com.jw.chat.core;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import com.jw.chat.callback.GoChatCallBack;
import com.jw.chat.msg.ChatMessage;

/**
 * 创建时间：
 * 更新时间 2017/11/1 20:52
 * 版本：
 * 作者：Mr.jin
 * 描述：消息请求操作类，可以拿到请求的uuid以及json化的字符串
 */

public class ChatRequest {
	private GoChatCallBack callBack;
	private ChatMessage message;

	private String sequence;
	private Map<String, Object> map;

	public ChatRequest(GoChatCallBack callBack, ChatMessage message) {
		super();
		this.callBack = callBack;
		this.message = message;

		map = new HashMap<>();
		if (message != null) {
			map.putAll(this.message.getMap());
			sequence = (String) map.get("sequence");
		}
	}

	/**
	 * @return 返回原生的请求序列
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @return 返回Json化后的请求序列
	 */
	public String getTransport() {
		return new Gson().toJson(map);
	}

	public GoChatCallBack getCallBack() {
		return callBack;
	}

	/**
	 *  给请求序列设置一个key
	 * @param account
	 */
	public void setAccount(String account) {
		if (map != null) {
			map.put("account", account);
		}
	}
}
