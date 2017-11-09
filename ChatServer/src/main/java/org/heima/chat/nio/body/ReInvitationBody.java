package org.heima.chat.nio.body;

import java.util.HashMap;
import java.util.Map;

public class ReInvitationBody	extends
								TextBody
{
	private boolean	agree;
	private String	icon;
	private String	name;

	public ReInvitationBody(String content, boolean agree, String acceptorIcon, String acceptorName)
	{
		super(content);
		this.agree = agree;
		this.icon = acceptorIcon;
		this.name = acceptorName;
	}

	@Override
	public Map<String, Object> getMap() {
		Map<String, Object> map = super.getMap();
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		map.put("agree", agree);
		map.put("icon", icon);
		map.put("name", name);
		return map;
	}
}
