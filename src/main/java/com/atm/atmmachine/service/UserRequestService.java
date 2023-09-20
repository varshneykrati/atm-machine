package com.atm.atmmachine.service;


import java.util.List;

import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.exceptions.RequestException;

/********************************************************************************************************
 * @author :Sidhi Jain
 * Description: It is  a service  that provide the service for raising a request, view all the request
 *              ,for updating and deleting the request.
 * Version: 1.0
 * Created date: 04-09-2023
 *********************************************************************************************************/
 
public interface UserRequestService {

	UserRequest getUserRequestById(String id)throws RequestException;
	UserRequest addRequest(UserRequest newRequest,String userId) throws RequestException;
	UserRequest deleteRequest(String requestId)throws RequestException;
	List<UserRequest> getAllUserRequest() throws RequestException;
	UserRequest updateRequest(UserRequest newRequest, String userId)throws RequestException;
	List<UserRequest> getRequestByUserId(String userId)throws RequestException;
	CardType getCardType(String userId) throws RequestException;
}
