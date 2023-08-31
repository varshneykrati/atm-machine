package com.atm.atmmachine.dto;

public class CardLimit {
	public enum CardTypeInCardLimit {
		Silver, Gold, Platinum;
	}

	private String cardType;

	private Double changedCardLimit;

	public CardLimit() {
		super();
	}

	public CardLimit(String cardType, Double changedCardLimit) {
		super();
		this.cardType = cardType;
		this.changedCardLimit = changedCardLimit;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public Double getChangedCardLimit() {
		return changedCardLimit;
	}

	public void setChangedCardLimit(Double changedCardLimit) {
		this.changedCardLimit = changedCardLimit;
	}

}
