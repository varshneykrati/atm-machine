package com.atm.atmmachine.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDateInfo {
	
	LocalDate date;
	

	Double sumOfAmount;
	
	
	public TransactionDateInfo() {
		super();
	}


	public TransactionDateInfo(LocalDate date, Double sumOfAmount) {
		super();
		this.date = date;
		this.sumOfAmount = sumOfAmount;
	}


	public LocalDate getDate() {
		return date;
	}


	


	public Double getSumOfAmount() {
		return sumOfAmount;
	}


	
	
	
	
	
	
}
