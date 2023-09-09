package com.atm.atmmachine.dto;

public class DthBill {

	private String userDthCardNumber;
	private Integer cardPin;

	public DthBill() {
		super();
	}

	public String getUserDthCardNumber() {
		return userDthCardNumber;
	}

	public void setUserDthCardNumber(String userDthCardNumber) {
		this.userDthCardNumber = userDthCardNumber;
	}

	public Integer getCardPin() {
		return cardPin;
	}

	public void setCardPin(Integer cardPin) {
		this.cardPin = cardPin;
	}

	public DthBill(String userDthCardNumber, Integer cardPin) {
		super();
		this.userDthCardNumber = userDthCardNumber;
		this.cardPin = cardPin;
	}


}
