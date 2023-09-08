package com.atm.atmmachine.service;

import com.atm.atmmachine.dto.EmailDto;
import com.atm.atmmachine.dto.Password;
import com.atm.atmmachine.dto.UserLogin;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.exception.HandleException;

public interface UserRegistrationService {

	public Integer userRegistrationDetails(UserRegistration userRegisteration) throws HandleException;
	
	public UserRegistration saveUserDetail(UserRegistration userRegisteration) throws HandleException;

	public String checkLoginDetails(UserLogin userLogin) throws HandleException;

	public CardDetails addUserCard(UserRegistration userRegisterDetails, CardDetails cardDetails) throws HandleException;

	public UserRegistration viewUserProfile(String userId) throws HandleException;

	public UserRegistration updateUserAddress(UserRegistration userRegistration,String userId) throws HandleException;

	public Integer sendOtpOnEmail(EmailDto emailDto, String string) throws HandleException;

	public UserRegistration savePassword(Password password, String userId) throws HandleException;
}
