package org.heima.chat.nio;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.google.gson.Gson;

public class ChatResponse {

	private IoSession			session;

	private Map<String, Object>	map	= new HashMap<String, Object>();

	public ChatResponse(IoSession session) {
		this.session = session;
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}

	public void writeResponse() {
		String json = new Gson().toJson(map);
		System.out.println(json);
		session.write(json);
	}

	public void disconnect() {
		session.closeOnFlush();
	}
}
