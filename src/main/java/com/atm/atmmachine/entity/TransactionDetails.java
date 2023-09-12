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
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.atm.atmmachine.dto.TransactionDateInfo;
import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@NamedNativeQuery(name = "TransactionDetails.getSumOfTransaction",
query = "SELECT transaction_date as transactionDate,sum(balance) as Balance from transaction_details group by transaction_date",
resultSetMapping = "Mapping.TransactionDateInfo")
@SqlResultSetMapping(name = "Mapping.TransactionDateInfo",
   classes = @javax.persistence.ConstructorResult(targetClass = TransactionDateInfo.class,
                                columns = {@javax.persistence.ColumnResult(name = "transactionDate",type=LocalDate.class),
                                           @javax.persistence.ColumnResult(name = "Balance",type=Double.class)}))

@Entity
public class TransactionDetails {
	
	public enum TransactionType{
		Deposit,Withdrawal
	}
	
	@Id
	@GeneratedValue(generator = "transaction_id", strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "transaction_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
			@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "trans"),
})
	private String transactionId;

	@ManyToOne
	@JoinColumn(name = "card_id", referencedColumnName = "cardId")
	private CardDetails cardDetails;

	@NotNull
	@NotBlank(message = "It should contain 12 numbers")
	private BigInteger toAccountNumber;

	@NotNull
	@NotBlank(message = "It should contain 12 numbers")
	private BigInteger fromAccountNumber;

	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
	private LocalDateTime transactionDate;

	@NotNull
	@NotBlank(message = "Amount paid")
	private Double balance;

	private String Particulars;
	@OneToOne
	private ElectricityBill electricityBill;

	@OneToOne
	private DTH dth;
	
	private TransactionType transactionType;
	
	public TransactionDetails() {
		super();
		}

	public TransactionDetails(CardDetails cardDetails,
			@NotBlank(message = "It should contain 12 numbers") BigInteger toAccountNumber,
			@NotBlank(message = "It should contain 12 numbers") BigInteger fromAccountNumber,
			@NotBlank(message = "It can't be empty") LocalDateTime transactionDate,
			@NotBlank(message = "Amount paid") Double balance,String particulars, ElectricityBill electricityBill, DTH dth,TransactionType transactionType) {
		super();
//		this.transactionId = transactionId;
     	this.cardDetails = cardDetails;
		this.toAccountNumber = toAccountNumber;
		this.fromAccountNumber = fromAccountNumber;
		this.transactionDate = transactionDate;
		this.balance = balance;
		this.Particulars=particulars;
		this.electricityBill = electricityBill;
		this.dth = dth;
		this.transactionType=transactionType;
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

	
	public BigInteger getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(BigInteger fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getParticulars() {
		return Particulars;
	}

	public void setParticulars(String particulars) {
		Particulars = particulars;
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

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	

}
