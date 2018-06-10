package org.heima.chat.pojo;

import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_message")
public class Message {

	@Id
	@GenericGenerator(	name = "generator",
						strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column
	private String		id;

	@Column
	private String		sender;

	@Column
	private String		receiver;

	@Column(nullable = false,
			columnDefinition = "int default 0")
	private int			type;		//什么类型的信息

	@Column
	private String		content;	//用于文本信息内容

	@Column
	private String		url;		//用于附件信息的文件url:image video等

	@Column(columnDefinition = "BLOB",
			nullable = true)
	private byte[]		data;		//存储image，video的微缩图

	@Column(nullable = false,
			columnDefinition = "int default 0")
	private int			state;		// 0.等待发送 1.正在发送 2.已经成功发送 3.发送失败

	@Column
	private Timestamp	createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", type="
				+ type + ", content=" + content + ", url=" + url + ", data="
				+ Arrays.toString(data) + ", state=" + state + ", createTime=" + createTime + "]";
	}

}
