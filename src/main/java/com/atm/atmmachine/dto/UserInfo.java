package com.atm.atmmachine.dto;

import javax.persistence.Column;

public class UserInfo {

	String userId;
	
	@Column(length = 4)
    Integer cardPin;

	public UserInfo() {
		super();
	}

	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public Integer getCardPin() {
		return cardPin;
	}

	public void setCardPin(Integer cardPin) {
		this.cardPin = cardPin;
	}
	
}
