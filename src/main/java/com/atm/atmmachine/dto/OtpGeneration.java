package com.atm.atmmachine.dto;

public class OtpGeneration {

	private Integer emailOtp;
	private Integer phoneNumberOtp;
	public OtpGeneration() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OtpGeneration(Integer emailOtp, Integer phoneNumberOtp) {
		super();
		this.emailOtp = emailOtp;
		this.phoneNumberOtp = phoneNumberOtp;
	}
	public Integer getEmailOtp() {
		return emailOtp;
	}
	public void setEmailOtp(Integer emailOtp) {
		this.emailOtp = emailOtp;
	}
	public Integer getPhoneNumberOtp() {
		return phoneNumberOtp;
	}
	public void setPhoneNumberOtp(Integer phoneNumberOtp) {
		this.phoneNumberOtp = phoneNumberOtp;
	}
	
	
}
