package com.atm.atmmachine.service;


import java.util.List;

import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.exceptions.RequestException;


public interface UserRequestService {

	UserRequest getUserRequestById(String id)throws RequestException;
	UserRequest addRequest(UserRequest newRequest) throws RequestException;
	UserRequest deleteRequest(String requestId)throws RequestException;
	List<UserRequest> getAllUserRequest() throws RequestException;
	UserRequest updateRequest(UserRequest newRequest)throws RequestException;
	List<UserRequest> getRequestByUserId(String userId)throws RequestException;
	
}
