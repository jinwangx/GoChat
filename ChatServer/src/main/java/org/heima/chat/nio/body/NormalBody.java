package org.heima.chat.nio.body;

import java.util.HashMap;
import java.util.Map;

import org.heima.chat.nio.MessageBody;

public class NormalBody	implements
						MessageBody
{
	private String				type;
	private Map<String, Object>	data;

	public NormalBody(String type, Map<String, Object> data) {
		this.type = type;
		this.data = data;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public Map<String, Object> getMap() {

		if (this.data == null) {
			this.data = new HashMap<String, Object>();
		}

		this.data.put("action", type);
		return this.data;
	}
}
