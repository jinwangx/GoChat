package org.heima.chat.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_user")
public class User {

	@Id
	@GenericGenerator(	name = "generator",
						strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column
	private String		id;

	@Column
	private String		account;

	@Column
	private String		name;

	@Column
	private String		phone;

	@Column
	private String		icon;

	@Column
	private String		password;

	@Column(nullable = false,
			columnDefinition = "int default 0")
	private int			state;			// 0 : 离线	1: 在线

	@Column(nullable = true)
	private Long		session;		// session 编号

	@Column
	private Timestamp	createTime;

	@Column
	private Timestamp	lastLoginTime;

	@Column
	private String		sign;			//个性签名

	@Column
	private String		area;			//地区

	@Column
	private Integer		sex;			//性别：0:未设置 1:女 2:男 3:其他

	@Column
	private String		qrPath;		//二维码存储路径

	@Column
	private String		token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Long getSession() {
		return session;
	}

	public void setSession(Long session) {
		this.session = session;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getQrPath() {
		return qrPath;
	}

	public void setQrPath(String qrPath) {
		this.qrPath = qrPath;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", name=" + name + ", phone=" + phone
				+ ", icon=" + icon + ", password=" + password + ", state=" + state + ", session="
				+ session + ", createTime=" + createTime + ", lastLoginTime=" + lastLoginTime
				+ ", sign=" + sign + ", area=" + area + ", sex=" + sex + ", qrPath=" + qrPath
				+ ", token=" + token + "]";
	}

}
