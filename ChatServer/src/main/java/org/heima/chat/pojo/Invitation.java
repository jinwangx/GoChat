package org.heima.chat.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_invitation")
public class Invitation {

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

	@Column
	private String		content;	//邀请的信息

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "Invitation [id=" + id + ", sender=" + sender + ", receiver=" + receiver
				+ ", content=" + content + ", state=" + state + ", createTime=" + createTime + "]";
	}

}
