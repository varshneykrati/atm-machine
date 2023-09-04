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
public class DTH {
	
	@ManyToOne
    @JoinColumn(name = "card_id")
    private CardDetails cardDetails;
	
	@Id
	@GeneratedValue(generator = "dth_id",strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "dth_id", strategy = "com.atm.atmmachine.idGenerator.StringPrefixedSequenceIdGenerator",
    parameters = {
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "dth"),
    		@org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "1000%d")
            })
	private String userDthCardNumber;
	
	
	private Double amountToBePaid;  //we are fixing it (All Airtel vendor are providing same amount that User has to Pay) and user have to pay this much.
	
	@ManyToOne
	@JoinColumn(name = "vendor_id")
	private Vendors vendors;

	public DTH() {
		super();
		// TODO Auto-generated constructor stub
	}



	

	

	public String getUserDthCardNumber() {
		return userDthCardNumber;
	}

	public DTH(CardDetails cardDetails, Double amountToBePaid, Vendors vendors) {
	super();
	this.cardDetails = cardDetails;
	
	this.amountToBePaid = amountToBePaid;
	this.vendors = vendors;
}

	public void setUserDthCardNumber(String userDthCardNumber) {
		this.userDthCardNumber = userDthCardNumber;
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
