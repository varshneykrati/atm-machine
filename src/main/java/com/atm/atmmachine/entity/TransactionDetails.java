package com.atm.atmmachine.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TransactionDetails {
	
	@Id
	@GeneratedValue(generator = "transaction_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "transaction_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "trans"),

            })
	private String transactionId;
	
	
	@ManyToOne
	@JoinColumn(name="card_id", referencedColumnName = "cardId")
	private CardDetails cardDetails;
	
	@NotNull
	@NotBlank(message="It should contain 12 numbers")
	private BigInteger toAccountNumber;
	
	@NotNull
	@JsonFormat(pattern="YYYY-MM-dd")
	private LocalDate transactionDate;
	
	@NotNull
	@NotBlank(message="Amount paid")
	private Double balance;

	@OneToOne
	private ElectricityBill electricityBill;
	
	@OneToOne
	private DTH dth;

	public TransactionDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionDetails(CardDetails cardDetails,
			@NotBlank(message = "It should contain 12 numbers") BigInteger toAccountNumber,
			@NotBlank(message = "It can't be empty") LocalDate transactionDate,
			@NotBlank(message = "Amount paid") Double balance, ElectricityBill electricityBill, DTH dth) {
		super();
		this.transactionId = transactionId;
		this.cardDetails = cardDetails;
		this.toAccountNumber = toAccountNumber;
		this.transactionDate = transactionDate;
		this.balance = balance;
		this.electricityBill = electricityBill;
		this.dth = dth;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public CardDetails getCardDetails() {
		return cardDetails;
	}

	public void setCardDetails(CardDetails cardDetails) {
		this.cardDetails = cardDetails;
	}

	public BigInteger getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(BigInteger toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public ElectricityBill getElectricityBill() {
		return electricityBill;
	}

	public void setElectricityBill(ElectricityBill electricityBill) {
		this.electricityBill = electricityBill;
	}

	public DTH getDth() {
		return dth;
	}

	public void setDth(DTH dth) {
		this.dth = dth;
	}
	
	
	
	
	
	
	
}
