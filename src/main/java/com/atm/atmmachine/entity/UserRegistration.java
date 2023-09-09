package com.atm.atmmachine.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;

import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserRegistration {

	// enumerated

	public enum UserRegistrationApproval {
		Inactive, Active
	}

	@Id
	@GeneratedValue(generator = "custom_id", strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "custom_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
			@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "user"),

	})
	private String userId;

	@NotBlank(message = "This field cant be empty or null")
	private String userName;

	@NotBlank(message = "This field cant be empty or null")
	@JsonFormat(pattern = "YYYY-MM-dd")
	private LocalDate userDOB;

	@Column(unique = true)
	@NotBlank(message = "This field cant be empty or null")
	private String phoneNo;

	@Column(unique = true)
	@Email(message = "Email is not valid")
	@NotBlank(message = "This field cant be empty or null")
	private String emailId;

	@NotBlank(message = "This field cant be empty or null")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
	private String password;

//	@Transient
	@NotBlank(message = "This field cant be empty or null")
	private String confirmPassword;

	// @Enumerated(EnumType.STRING)
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
	private String userRegistrationApproval;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id", referencedColumnName = "addressId")
	private Address address;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_id", referencedColumnName = "cardId")
	private CardDetails cardDetails;

	public UserRegistration() {
		super();
		}

	public UserRegistration(@NotBlank(message = "This field cant be empty or null") String userName,
			@NotBlank(message = "This field cant be empty or null") LocalDate userDOB,
			@NotBlank(message = "This field cant be empty or null") String phoneNo,
			@Email(message = "Email is not valid") @NotBlank(message = "This field cant be empty or null") String emailId,
			@NotBlank(message = "This field cant be empty or null") String password,
			@NotBlank(message = "This field cant be empty or null") String confirmPassword,
			String userRegistrationApproval, Address address, CardDetails cardDetails) {
		super();
		this.userName = userName;
		this.userDOB = userDOB;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.userRegistrationApproval = userRegistrationApproval;
		this.address = address;
		this.cardDetails = cardDetails;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDate getUserDOB() {
		return userDOB;
	}

	public void setUserDOB(LocalDate userDOB) {
		this.userDOB = userDOB;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUserRegistrationApproval() {
		return userRegistrationApproval;
	}

	public void setUserRegistrationApproval(String userRegistrationApproval) {
		this.userRegistrationApproval = userRegistrationApproval;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public CardDetails getCardDetails() {
		return cardDetails;
	}

	public void setCardDetails(CardDetails cardDetails) {
		this.cardDetails = cardDetails;
	}

}
