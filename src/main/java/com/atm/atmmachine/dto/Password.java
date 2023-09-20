package com.atm.atmmachine.dto;

public class Password {

	private String password;
	private String cpassword;

	public Password() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Password(String password,String cpassword) {
		super();
		this.password = password;
		this.cpassword = cpassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
