package com.atm.atmmachine.dto;

public class AdminRemark {
	private String remarkOnRequest;

	public AdminRemark() {
		super();
	}

	public AdminRemark(String remarkOnRequest) {
		super();
		this.remarkOnRequest = remarkOnRequest;
	}

	public String getRemarkOnRequest() {
		return remarkOnRequest;
	}

	public void setRemarkOnRequest(String remarkOnRequest) {
		this.remarkOnRequest = remarkOnRequest;
	}

}
