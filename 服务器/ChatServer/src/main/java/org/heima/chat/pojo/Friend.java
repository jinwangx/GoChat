package org.heima.chat.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_friend")
public class Friend {

	@Id
	@GenericGenerator(	name = "generator",
						strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column
	private String		id;

	@Column
	private String		ownerAccount;

	@Column
	private String		friendAccount;

	@Column
	private String		nickName;

	@Column
	private String		friendName;

	@Column
	private Timestamp	addTime;

	@Column
	private Timestamp	updateTime;

	@Column(nullable = false,
			columnDefinition = "int default 0")
	private int			state;			//是否激活0:为激活 1:已经激活

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerAccount() {
		return ownerAccount;
	}

	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}

	public String getFriendAccount() {
		return friendAccount;
	}

	public void setFriendAccount(String friendAccount) {
		this.friendAccount = friendAccount;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Friend [id=" + id + ", ownerAccount=" + ownerAccount + ", friendAccount="
				+ friendAccount + ", nickName=" + nickName + ", friendName=" + friendName
				+ ", addTime=" + addTime + ", updateTime=" + updateTime + ", state=" + state + "]";
	}

}
