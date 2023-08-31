package com.atm.atmmachine.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.entity.UserRequest;
import com.atm.atmmachine.repository.UserRegistrationRepository;
import com.atm.atmmachine.repository.UserRequestRepository;
import com.atm.atmmachine.service.UserRequestService;

@RestController
public class RequestController {
	@Autowired
	UserRequestService userRequestService;
	@Autowired
	UserRegistrationRepository userRegistrationRepository;
	@Autowired
	UserRequestRepository request;
	
	@GetMapping("/admin1/")
	public String krati() {
		return "Hello welcome Sidhi";
	}
	//to get all user
	@GetMapping("/request")
	public List<UserRequest> getUserRequest()
	{
		List<UserRequest>allrequest=this.userRequestService.getAllUserRequest();
		return allrequest;
	}
	//to get particular user by id
	@GetMapping("/request/{id}")//https://localhost8999/
	public UserRequest getUserRequestById(@PathVariable("id") String Id)
	{
		return this.userRequestService.getUserRequestById(Id);
	}
	
	@PostMapping("/request/")//https://localhost8999/
	public UserRequest addRequest(@Valid @RequestBody UserRequest newRequest)
	{
		    
			return this.userRequestService.addRequest(newRequest);
	}

	@DeleteMapping("/request/{id}")
	public UserRequest deleteRequest(@PathVariable("id") String Id) {

		return this.userRequestService.deleteRequest(Id);
	}
	@PutMapping("/request11/")
	public UserRequest UpdateRequest(  @RequestBody UserRequest newRequest ) {
//		this.userRequestService.deleteRequest(Id);
		return this.userRequestService.UpdateRequest(newRequest);
	}
	
	@GetMapping("/request1/{userId}")
	public List<UserRequest> getRequestOfUser(@PathVariable("userId") String userId)
	{
		System.out.println("Hii-----------------");
		
		List<UserRequest> getAllRequest = this.userRequestService.getRequestByUserId(userId);
		
		return getAllRequest;
	}
	
}
