package org.heima.chat.vo;

import org.heima.chat.pojo.User;

public class ClientSearchContactInfo {

	private String	account;
	private String	name;
	private String	sign;
	private String	area;
	private String	icon;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static ClientSearchContactInfo toUser(User user) {
		ClientSearchContactInfo u = new ClientSearchContactInfo();
		u.setAccount(user.getAccount());
		u.setArea(user.getArea());
		u.setIcon(user.getIcon());
		u.setName(user.getName());
		u.setSign(user.getSign());
		return u;
	}
}
