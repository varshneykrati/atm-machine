package com.atm.atmmachine.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ElectricityBill {
	
	@ManyToOne
    @JoinColumn(name = "card_id")
    private CardDetails cardDetails;
	
	@Id
	@GeneratedValue(generator = "electricityBill_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "electricityBill_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "elec"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "1000%d")
            })
	private String userElectricityId;
	
	
	private Double amountToBePaid;  //we are fixing it and user have to pay this much.
	
	@ManyToOne
	@JoinColumn(name = "vendor_id")
	private Vendors vendors;

	public ElectricityBill() {
		super();
		// TODO Auto-generated constructor stub
	}


	

	public String getUserElectricityId() {
		return userElectricityId;
	}

	public ElectricityBill(CardDetails cardDetails, Double amountToBePaid, Vendors vendors) {
	super();
	this.cardDetails = cardDetails;
	this.userElectricityId = userElectricityId;
	this.amountToBePaid = amountToBePaid;
	this.vendors = vendors;
}

	public void setUserElectricityId(String userElectricityId) {
		this.userElectricityId = userElectricityId;
	}

	public Double getAmountToBePaid() {
		return amountToBePaid;
	}

	public void setAmountToBePaid(Double amountToBePaid) {
		this.amountToBePaid = amountToBePaid;
	}

	public Vendors getVendors() {
		return vendors;
	}

	public void setVendors(Vendors vendors) {
		this.vendors = vendors;
	}
	
	
	

}
