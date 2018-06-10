package org.heima.chat.vo;

import org.heima.chat.pojo.Friend;
import org.heima.chat.pojo.User;

public class ClientFriend {

	private String	owner;
	private String	account;
	private String	name;
	private String	sign;
	private String	area;
	private String	icon;
	private int		sex;
	private String	nickName;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

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

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public static ClientFriend toFriend(Friend friend, User user, User owner) {
		ClientFriend f = new ClientFriend();
		f.account = friend.getFriendAccount();
		f.area = user.getArea();
		f.icon = user.getIcon();
		f.name = friend.getFriendName();
		f.nickName = friend.getNickName();
		f.owner = owner.getAccount();
		f.sex = user.getSex() == null ? 0 : user.getSex();
		f.sign = user.getSign();
		return f;
	}
}
