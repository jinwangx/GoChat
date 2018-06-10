package org.heima.chat.nio.body;

import java.util.HashMap;
import java.util.Map;

public class InvitationBody	extends
							TextBody
{
	private String	invitorName;
	private String	invitorIcon;

	public InvitationBody(String content, String invitor_name, String invitor_icon) {
		super(content);
		this.invitorName = invitor_name;
		this.invitorIcon = invitor_icon;
	}

	@Override
	public Map<String, Object> getMap() {
		Map<String, Object> map = super.getMap();
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		map.put("invitor_name", invitorName);
		map.put("invitor_icon", invitorIcon);
		return map;
	}
}
