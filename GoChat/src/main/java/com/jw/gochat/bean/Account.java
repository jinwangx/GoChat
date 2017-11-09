package com.jw.gochat.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建时间：
 * 更新时间 2017/10/30 2017/10/30
 * 版本：
 * 作者：Mr.jin
 * 描述：账户信息类
 */

public class Account implements Parcelable {

	private String account;// 账号
	private String name;// 用户名
	private String icon;// 用户图像
	private int sex;// 性别 0:未设置 1:女 2:男 3:其他
	private String sign;// 用户个性签名
	private String area;// 用户所在区域
	private String token;// 用户与服务器交互的唯一标
	private boolean current;// 是否是当前用户

	public Account() {

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

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	private Account(Parcel parcel) {
		Bundle val = parcel.readBundle();

		account = val.getString("account");
		name = val.getString("name");
		icon = val.getString("icon");
		sex = val.getInt("sex");
		sign = val.getString("sign");
		area = val.getString("area");
		token = val.getString("token");
		current = val.getBoolean("current");
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle val = new Bundle();
		val.putString("account", account);
		val.putString("name", name);
		val.putString("icon", icon);
		val.putInt("sex", sex);
		val.putString("sign", sign);
		val.putString("area", area);
		val.putString("token", token);
		val.putBoolean("current", current);
		dest.writeBundle(val);
	}

	public static final Creator<Account> CREATOR = new Creator<Account>() {

		@Override
		public Account[] newArray(int size) {
			return new Account[size];
		}

		@Override
		public Account createFromParcel(Parcel source) {
			return new Account(source);
		}
	};

}