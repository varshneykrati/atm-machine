package com.atm.atmmachine.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;

@Entity
public class Address {

	@Id
	@GeneratedValue(generator = "address_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "address_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "add"),

            })
	private String addressId;
	
	@NotBlank(message="This field cant be empty or null")
	private String street;
	
	@NotBlank(message="This field cant be empty or null")
	private String district;
	
	@NotBlank(message="This field cant be empty or null")
	private Integer pincode;
	
	@NotBlank(message="This field cant be empty or null")
	private String state;
	

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(@NotBlank(message = "This field cant be empty or null") String street,
			@NotBlank(message = "This field cant be empty or null") String district,
			@NotBlank(message = "This field cant be empty or null") Integer pincode,
			@NotBlank(message = "This field cant be empty or null") String state) {
		super();
		this.street = street;
		this.district = district;
		this.pincode = pincode;
		this.state = state;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	
	
}
