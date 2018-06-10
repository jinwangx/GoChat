package org.heima.chat.nio;

import java.util.HashMap;
import java.util.Map;

public class ChatRequest {

	private Map<String, Object>	map	= new HashMap<String, Object>();

	public ChatRequest(Map<String, Object> map) {
		this.map = map;
	}

	public Object get(String key) {
		return map.get(key);
	}

	public String getAction() {
		return map.get("action").toString();
	}

	public String getSender() {
		return map.get("sender").toString();
	}

}
