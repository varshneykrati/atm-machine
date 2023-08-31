package com.atm.atmmachine.service;


import java.util.List;
import java.util.Optional;

import com.atm.atmmachine.dto.CardLimit;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.entity.UserRequest;


public interface AdminService {
	
	public Optional<UserRegistration> findByUserId(String userId);
	
	public List<UserRequest> displayAllRequest();
	
	public List<UserRequest> displayRequestByStatus();
	
	public Optional<UserRequest> getRequestById(String requestId);
	
	public Optional<UserRegistration> getUsersById(String userId);
	

	
	public List<UserRequest> displayByRequest();
	
	public List<UserRequest> displayAllCardReplacementRequests();
	
	public UserRequest updateUserRequestStatus(String requestId);
	
	public List<UserRegistration> displayUsersWithStatusInactive();
	
	public UserRegistrationApproval updateUserRegistrationApproval(String id);
	
	public String setAdminRemark(String reqId,String remark);
	
	public Double changeCardLimit(CardLimit cardLimit);
	
	public Boolean validAadharCard(String AadharNumber);
}
