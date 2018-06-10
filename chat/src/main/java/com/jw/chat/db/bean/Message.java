package com.jw.chat.db.bean;

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：消息类，如该消息属于谁，内容，发送成功或者失败
 */

public class Message {
	// String COLUMN_ID = "_id";
	// String COLUMN_OWNER = "owner";
	// String COLUMN_ACCOUNT = "account";// 接收者或发送者
	// String COLUMN_DIRECTION = "direct";// 0:发送 1:接收
	// String COLUMN_TYPE = "type";
	// String COLUMN_CONTENT = "content";
	// String COLUMN_URL = "url";
	// String COLUMN_STATE = "state";// 发送状态: 1.正在发送 2.已经成功发送 3.发送失败
	// String COLUMN_CREATE_TIME = "create_time";

	private long id;
	private String owner;
	private String account;
	private int direction;// 0:发送 1:接收
	private int type;// 0:text 1:image
	private String content;
	private String url;
	private int state;
	private boolean read;
	private long createTime;

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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
