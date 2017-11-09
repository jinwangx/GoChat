package com.jw.gochat.bean;

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：会话信息类
 */

public class Conversation {
	// String COLUMN_ID = "_id";
	// String COLUMN_OWNER = "owner";
	// String COLUMN_ACCOUNT = "account";
	// String COLUMN_ICON = "icon";
	// String COLUMN_NAME = "name";
	// String COLUMN_CONTENT = "content";
	// String COLUMN_UNREAD = "unread_count";
	// String COLUMN_UPDATE_TIME = "update_time";

	private String owner;
	private String account;
	private String icon;
	private String name;
	private String content;
	private int unread;
	private long updateTime;

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
