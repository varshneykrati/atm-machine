package com.atm.atmmachine.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.entity.CardDetails.CardType;
import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.exceptions.RequestException;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;
import com.atm.atmmachine.service.UserRequestService;

@RestController
@CrossOrigin(origins="http://localhost:4200/")
public class RequestController {
	@Autowired
	 private UserRequestService userRequestService;
	@Autowired
	 private UserRegistrationRepository userRegistrationRepository;
	@Autowired
	 private UserRequestRepository request;
  
	
	//to get all user request
	@GetMapping("/request")
	public List<UserRequest> getUserRequest() throws RequestException
	{
		List<UserRequest>allrequest;
		allrequest=this.userRequestService.getAllUserRequest();
		return allrequest;
	}
	//to get particular request by id
	@GetMapping("/request/{id}")//https://localhost8999/
	public UserRequest getUserRequestById(@PathVariable("id") String Id) throws RequestException
	{
		return this.userRequestService.getUserRequestById(Id);
	}
	//to get request of particular user by userId 
	@GetMapping("/requests/{userId}")
	public List<UserRequest> getRequestOfUser(@PathVariable("userId") String userId) throws RequestException
	{
		List<UserRequest> getAllRequest;
		getAllRequest = this.userRequestService.getRequestByUserId(userId);
		return getAllRequest;
	}
	@PostMapping("/request/")//https://localhost8999/
	public UserRequest addRequest(@Valid @RequestBody UserRequest newRequest) throws RequestException
	{
		    
			return this.userRequestService.addRequest(newRequest);
	}
   
	@DeleteMapping("/request/{id}")
	public UserRequest deleteRequest(@PathVariable("id") String Id) throws RequestException {

		return this.userRequestService.deleteRequest(Id);
	}
	@PutMapping("/request/")
	public UserRequest updateRequest(@RequestBody UserRequest newRequest ) throws RequestException {

		return this.userRequestService.updateRequest(newRequest);
	}
	@GetMapping("/request/cardtype/{userId}")
	public CardType getCardType(@PathVariable("userId") String userId)throws RequestException{
		return this.userRequestService.getCardType(userId);
	}
	
	
	
}
