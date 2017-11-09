package org.heima.chat.vo;

import org.heima.chat.pojo.User;

public class ClientAccount {

	private String	account;	// 账号
	private String	name;		// 用户名
	private Integer	sex;		// 性别
	private String	icon;		// 用户图像
	private String	sign;		// 用户个性签名
	private String	area;		// 用户所在区域
	private String	token;		// 用户与服务器交互的唯一标

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public static ClientAccount toAccount(User user) {
		ClientAccount account = new ClientAccount();
		account.account = user.getAccount();
		account.area = user.getArea();
		account.icon = user.getIcon();
		account.name = user.getName();
		account.sex = user.getSex();
		account.sign = user.getSign();
		account.token = user.getToken();
		return account;
	}

}
