package org.heima.chat.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_push_task")
public class BackPushTask {

	@Id
	@GenericGenerator(	name = "generator",
						strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column
	private String		id;

	@Column
	private String		receiver;

	@Column
	private Timestamp	createTime;

	@Column
	private int			state;		//0:等待推送; 1:推送中; 2:推送成功; 3:推送失败

	@Column
	private String		path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "BackPushTask [id=" + id + ", receiver=" + receiver + ", createTime=" + createTime
				+ ", state=" + state + ", path=" + path + "]";
	}
}
