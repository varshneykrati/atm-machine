package com.atm.atmmachine.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.atm.atmmachine.CustomDate.CustomLocalDateDeserializer;
import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
public class UserRequest {
	//enumerated
	public enum RequestStatus{
		Pending,Approved,NotAcceptable;
	}
	@Id
	@GeneratedValue(generator = "request_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "request_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "req"),

            })
	private String requestId;
	
	

	@Column(length = 12)
	@NotNull
	@NotBlank(message="It can' be empty")
	private BigInteger accountNumber;
	
	@NotNull
	@NotBlank(message="It can' be empty")
	private String request;
	
	@NotNull
	@NotBlank(message="It can' be empty")
	private String requestDesc;
	
	
	@NotBlank(message="It can' be empty")
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	//@JsonFormat(pattern="YYYY-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dateOfRequest;
	
	@Enumerated(EnumType.STRING)
	private RequestStatus requestStatus;
	
	
	private String adminRemark;
	

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	private UserRegistration userRegistration;
	
	
	
	public UserRequest() {
		super();
	}


	public UserRequest(@NotBlank(message = "It can' be empty") BigInteger accountNumber,
			@NotBlank(message = "It can' be empty") String request,
			@NotBlank(message = "It can' be empty") String requestDesc,
			@NotBlank(message = "It can' be empty") LocalDate dateOfRequest, RequestStatus requestStatus, String adminRemark,
			UserRegistration userRegistration) {
		super();
		this.accountNumber = accountNumber;
		this.request = request;
		this.requestDesc = requestDesc;
		this.dateOfRequest = dateOfRequest;
		this.requestStatus = requestStatus;
		this.adminRemark = adminRemark;
		this.userRegistration = userRegistration;
	}


	public String getRequestId() {
		return requestId;
	}


	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}


	public BigInteger getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(BigInteger accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getRequest() {
		return request;
	}


	public void setRequest(String request) {
		this.request = request;
	}


	public String getRequestDesc() {
		return requestDesc;
	}


	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}


	public LocalDate getDateOfRequest() {
		return dateOfRequest;
	}


	public void setDateOfRequest(LocalDate dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}


	public RequestStatus getRequestStatus() {
		return requestStatus;
	}


	public void setRequestStatus(RequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}


	public String getAdminRemark() {
		return adminRemark;
	}


	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}


	public UserRegistration getUserRegistration() {
		return userRegistration;
	}


	public void setUserRegistration(UserRegistration userRegistration) {
		this.userRegistration = userRegistration;
	}


	

	
	
	
	
}
