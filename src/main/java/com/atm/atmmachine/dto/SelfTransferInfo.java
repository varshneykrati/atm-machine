package com.atm.atmmachine.dto;

import javax.persistence.Column;

public class SelfTransferInfo {

	String userId;
	Double transactionAmount;
	
	@Column(length = 4)
    Integer cardPin;
	
	public SelfTransferInfo() {
		super();
		// TODO Auto-generated constructor stub
		
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
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
