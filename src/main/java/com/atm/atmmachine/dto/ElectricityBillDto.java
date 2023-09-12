package com.atm.atmmachine.dto;

public class ElectricityBillDto {
	private String userElectricityId;
	private Integer cardPin;


	public ElectricityBillDto(String userElectricityId, Integer cardPin) {
		super();
		this.userElectricityId = userElectricityId;
		this.cardPin = cardPin;
	}

	
	public ElectricityBillDto() {
		super();
	}

	public String getUserElectricityId() {
		return userElectricityId;
	}

	public void setUserElectricityId(String userElectricityId) {
		this.userElectricityId = userElectricityId;
	}


	public Integer getCardPin() {
		return cardPin;
	}


	public void setCardPin(Integer cardPin) {
		this.cardPin = cardPin;
	}

}
