package com.atm.atmmachine.service;

import com.atm.atmmachine.dto.EmailDto;
import com.atm.atmmachine.dto.OtpGeneration;
import com.atm.atmmachine.dto.Password;
import com.atm.atmmachine.dto.UserLogin;
import com.atm.atmmachine.entity.Address;
import com.atm.atmmachine.entity.CardDetails;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.exception.HandleException;

public interface UserRegistrationService {

	public OtpGeneration userRegistrationDetails(UserRegistration userRegisteration) throws HandleException;
	
	public UserRegistration saveUserDetail(UserRegistration userRegisteration) throws HandleException;

	public UserRegistration checkLoginDetails(UserLogin userLogin) throws HandleException;

	public CardDetails addUserCard(String userId, CardDetails cardDetails) throws HandleException;

	public CardDetails viewUserProfile(String userId) throws HandleException;

	public Integer otpForUpdatingPhoneNumber(UserRegistration userRegistration,String userId) throws HandleException;

	public Integer sendOtpOnEmail(EmailDto emailDto, String string) throws HandleException;

	public UserRegistration savePassword(Password password, String userId) throws HandleException;
	
	public UserRegistration fetchingUser(UserLogin userLogin) throws HandleException;

	public UserRegistration updatingUserPhoneNumber(UserRegistration userRegistration, String userId) throws HandleException;

	public Address getAddress(String userId) throws HandleException;

	public UserRegistration changeUserAddress(Address address, String userId) throws HandleException;

	public Integer sendUserEmailForUpdatePin(String userId) throws HandleException;

	public CardDetails changeUserCardPin(CardDetails cardDetails, String userId) throws HandleException;
}
