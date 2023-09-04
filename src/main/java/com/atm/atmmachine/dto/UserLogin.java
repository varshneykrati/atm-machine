package com.atm.atmmachine.dto;


public class UserLogin {

	String emailId;
	String password;
	public UserLogin() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserLogin(String emailId, String password) {
		super();
		this.emailId = emailId;
		this.password = password;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
