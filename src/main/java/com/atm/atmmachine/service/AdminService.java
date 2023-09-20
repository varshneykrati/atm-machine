package com.atm.atmmachine.service;

import java.util.List;

import com.atm.atmmachine.dto.CardLimit;
import com.atm.atmmachine.dto.TransactionDateInfo;

import com.atm.atmmachine.exceptions.AdminException;
import com.atm.atmmachine.entity.UserRequest;

public interface AdminService {

	public List<UserRequest> displayAllRequest();

	public List<UserRequest> displayRequestByStatus();

	public List<UserRequest> displayByRequest(String request);

	public UserRequest updateUserRequestStatus(String requestId) throws AdminException;

	public String setAdminRemark(String reqId, String remark) throws AdminException;

	public Double changeCardLimit(CardLimit cardLimit) throws AdminException;

	public List<TransactionDateInfo> sumOfTodayTransaction();
}
