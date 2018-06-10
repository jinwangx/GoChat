package org.heima.chat.nio.body;

import java.util.HashMap;
import java.util.Map;

import org.heima.chat.nio.MessageBody;

public class TextBody	implements
						MessageBody
{
	private String	content;

	public TextBody(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@Override
	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", content);
		return map;
	}
}
