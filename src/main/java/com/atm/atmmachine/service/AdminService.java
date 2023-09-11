package com.atm.atmmachine.service;


import java.util.List;
import java.util.Optional;

import com.atm.atmmachine.dto.CardLimit;
import com.atm.atmmachine.dto.TransactionDateInfo;
import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRegistration.UserRegistrationApproval;
import com.atm.atmmachine.exceptions.AdminException;
import com.atm.atmmachine.entity.UserRequest;


public interface AdminService {
	
	public Optional<UserRegistration> findByUserId(String userId);
	
	public List<UserRequest> displayAllRequest();
	
	public List<UserRequest> displayRequestByStatus();
	
	public Optional<UserRequest> getRequestById(String requestId)throws AdminException;
	
	public Optional<UserRegistration> getUsersById(String userId) throws AdminException;
	

	
	public List<UserRequest> displayByRequest(String request);
	
	
	
	public UserRequest updateUserRequestStatus(String requestId) throws AdminException;
	
	public List<UserRegistration> displayUsersWithStatusInactive();
	
	public UserRegistrationApproval updateUserRegistrationApproval(String id) throws AdminException;
	
	public String setAdminRemark(String reqId,String remark) throws AdminException;
	
	public Double changeCardLimit(CardLimit cardLimit) throws AdminException;
	
	public Boolean validAadharCard(String aadharNumber);
	
	public List<TransactionDateInfo> sumOfTodayTransaction();
}
