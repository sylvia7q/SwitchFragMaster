/**
 * 文件名：User.java
 * 时间：2015年5月9日上午10:23:19
 * 作者：修维康
 */
package com.kanban.switchfragmaster.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 类名：User 说明：用户对象
 */
@Table(name="T_User")
public class User{

	@Id(column = "userId")
	private String userId;
	@Column(column = "userName")
	private String userName;
	@Column(column = "password")
	private String password;
	@Column(column = "loginTime")
	private String loginTime;
	@Column(column = "curAccount")
	private String curAccount;

	public User() {
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getCurAccount() {
		return curAccount;
	}

	public void setCurAccount(String curAccount) {
		this.curAccount = curAccount;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", loginTime='" + loginTime + '\'' +
				", curAccount='" + curAccount + '\'' +
				'}';
	}
}
