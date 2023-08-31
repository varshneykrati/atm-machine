package com.atm.atmmachine.service;


import java.util.List;

import com.atm.atmmachine.entity.UserRequest;


public interface UserRequestService {

	UserRequest getUserRequestById(String id);
	UserRequest addRequest(UserRequest newRequest) ;
	UserRequest deleteRequest(String request_id);
	List<UserRequest> getAllUserRequest();
	UserRequest UpdateRequest(UserRequest newRequest);
	List<UserRequest> getRequestByUserId(String userId);
	
}
