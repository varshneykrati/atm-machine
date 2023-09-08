package com.atm.atmmachine.dto;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FundTransferInfo {

	String userId;
	Double transactionAmount;
	
	@NotNull
	@NotBlank(message="It should contain 12 numbers")
	BigInteger toAccountNumber;

	@Column(length = 4)
    Integer cardPin;
	
	public FundTransferInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigInteger getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(BigInteger toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public Integer getCardPin() {
		return cardPin;
	}

	public void setCardPin(Integer cardPin) {
		this.cardPin = cardPin;
	}

	
	
}
