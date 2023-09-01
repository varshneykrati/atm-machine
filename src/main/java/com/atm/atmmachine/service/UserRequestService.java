package com.atm.atmmachine.service;


import java.util.List;

import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.exceptions.RequestException;


public interface UserRequestService {

	UserRequest getUserRequestById(String id)throws RequestException;
	UserRequest addRequest(UserRequest newRequest) throws RequestException;
	UserRequest deleteRequest(String request_id)throws RequestException;
	List<UserRequest> getAllUserRequest() throws RequestException;
	UserRequest UpdateRequest(UserRequest newRequest)throws RequestException;
	List<UserRequest> getRequestByUserId(String userId)throws RequestException;
	
}
