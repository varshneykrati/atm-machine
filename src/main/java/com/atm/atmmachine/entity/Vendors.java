package com.atm.atmmachine.entity;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;

@Entity
public class Vendors {
	
	//enumerated
	public enum TypeOfVendor{
		DTH,ElectricityBill;
	}

	@Id
	@GeneratedValue(generator = "vendor_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "vendor_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "vendor"),

            })
	private String vendorId;
	
	@Enumerated(EnumType.STRING)
	private TypeOfVendor typeOfVendor;
	
	private String vendorName;
	
	
	@Column(length = 12,unique = true)
	@NotBlank(message="This field cant be empty or null")
	private BigInteger vendorAccountNumber;
	
	private Double vendorAccountAmount;

	public Vendors() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Vendors(TypeOfVendor typeOfVendor, String vendorName,
			@NotBlank(message = "This field cant be empty or null") BigInteger vendorAccountNumber,
			Double vendorAccountAmount) {
		super();
		this.vendorId = vendorId;
		this.typeOfVendor = typeOfVendor;
		this.vendorName = vendorName;
		this.vendorAccountNumber = vendorAccountNumber;
		this.vendorAccountAmount = vendorAccountAmount;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public TypeOfVendor getTypeOfVendor() {
		return typeOfVendor;
	}

	public void setTypeOfVendor(TypeOfVendor typeOfVendor) {
		this.typeOfVendor = typeOfVendor;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public BigInteger getVendorAccountNumber() {
		return vendorAccountNumber;
	}

	public void setVendorAccountNumber(BigInteger vendorAccountNumber) {
		this.vendorAccountNumber = vendorAccountNumber;
	}

	public Double getVendorAccountAmount() {
		return vendorAccountAmount;
	}

	public void setVendorAccountAmount(Double vendorAccountAmount) {
		this.vendorAccountAmount = vendorAccountAmount;
	}
	
	
}
