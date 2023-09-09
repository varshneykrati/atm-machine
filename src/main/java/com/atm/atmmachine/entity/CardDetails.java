package com.atm.atmmachine.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CardDetails {
	
	public enum CardType {
		Silver,Gold,Platinum
	}
	
	public enum CardStatus{
		Active,Inactive;
	}
	
	public enum UserTotallyRegister{
		True,False;
	}
	
	@Id
	@GeneratedValue(generator = "card_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "card_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "card"),

            })
	private String cardId;
	
	
	@Column(length = 12)
	@NotBlank(message="This field cant be empty or null")
	private BigInteger accountNumber; 
	
	
	@Column(length = 16)
	@NotBlank(message="This field cant be empty or null")
	private BigInteger cardNumber;
	
	@Column(length = 3)
	@NotBlank(message="This field cant be empty or null")
	private Integer cvv;
	

	@NotBlank(message="This field cant be empty or null")
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate validThrough;
	
	@NotBlank(message="This field cant be empty or null")
	@Enumerated(EnumType.STRING)
	private CardType cardType;
	
	
	@NotBlank(message="This field cant be empty or null")
	private Double cardLimit ;
	
	@NotBlank(message="This field cant be empty or null")
	private Double cardLimit;
	
	@NotBlank(message="This field cant be empty or null")
	@Enumerated(EnumType.STRING)
	private CardStatus cardstatus;
	
	@NotBlank(message="This field cant be empty or null")
	private Double amount;
	
	
	@Column(length = 6)
	private Integer cardPin;
	
	@Enumerated(EnumType.STRING)
	private UserTotallyRegister userTotallyRegister;
	

	@OneToOne(mappedBy = "cardDetails")
	@JoinColumn(name= "user_id" ,referencedColumnName = "userId")
	private UserRegistration userRegistration;


	public double getcardLimit;

	public CardDetails() {
		super();
		}

	public CardDetails(@NotBlank(message = "This field cant be empty or null") BigInteger accountNumber,
			@NotBlank(message = "This field cant be empty or null") BigInteger cardNumber,
			@NotBlank(message = "This field cant be empty or null") Integer cvv,
			@NotBlank(message = "This field cant be empty or null") LocalDate validThrough,
			@NotBlank(message = "This field cant be empty or null") CardType cardType,
			@NotBlank(message = "This field cant be empty or null") Double cardLimit,
			@NotBlank(message = "This field cant be empty or null") CardStatus cardstatus,
			@NotBlank(message = "This field cant be empty or null") Double amount,
			@NotBlank(message = "This field cant be empty or null") Integer cardPin,
			UserTotallyRegister userTotallyRegister, UserRegistration userRegistration) {
		super();
		this.accountNumber = accountNumber;
		this.cardNumber = cardNumber;
		this.cvv = cvv;
		this.validThrough = validThrough;
		this.cardType = cardType;
		this.cardLimit = cardLimit;
		this.cardstatus = cardstatus;
		this.amount = amount;
		
		this.cardPin = cardPin;
		this.userTotallyRegister = userTotallyRegister;
		this.userRegistration = userRegistration;
	}

	public Double getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(Double cardLimit) {
		this.cardLimit = cardLimit;
	}

	public Double getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(Double cardLimit) {
		this.cardLimit = cardLimit;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public BigInteger getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(BigInteger accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigInteger getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(BigInteger cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public LocalDate getValidThrough() {
		return validThrough;
	}

	public void setValidThrough(LocalDate validThrough) {
		this.validThrough = validThrough;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	

	public CardStatus getCardstatus() {
		return cardstatus;
	}

	public void setCardstatus(CardStatus cardstatus) {
		this.cardstatus = cardstatus;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCardPin() {
		return cardPin;
	}

	public void setCardPin(Integer cardPin) {
		this.cardPin = cardPin;
	}

	public UserTotallyRegister getUserTotallyRegister() {
		return userTotallyRegister;
	}

	public void setUserTotallyRegister(UserTotallyRegister userTotallyRegister) {
		this.userTotallyRegister = userTotallyRegister;
	}

	public UserRegistration getUserRegistration() {
		return userRegistration;
	}

	public void setUserRegistration(UserRegistration userRegistration) {
		this.userRegistration = userRegistration;
	}

	
}
