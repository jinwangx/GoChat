package com.jw.gochat.bean;

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：邀请类，如该邀请是谁发送，是否同意等等信息
 */

public class Invitation {

	private long id;
	private String owner;
	private String account;
	private String name;
	private String icon;
	private String content;
	private boolean agree;

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

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Invitation [id=" + id + ", owner=" + owner + ", account="
				+ account + ", name=" + name + ", icon=" + icon + ", content="
				+ content + ", agree=" + agree + "]";
	}

}
