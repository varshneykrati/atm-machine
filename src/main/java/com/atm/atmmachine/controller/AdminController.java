package com.atm.atmmachine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atm.atmmachine.entity.UserRegistration;
import com.atm.atmmachine.repository.UserRegistrationRepository;

@RestController
public class AdminController {
	@Autowired
	UserRegistrationRepository user;
	
	@GetMapping("/admin/")
	public String krati() {
		return "Hello welcome";
	}

	//this is comment
	
//	@PostMapping("/")
//	public UserRegistration createAdmin(@RequestBody UserRegistration userReg) {
//		System.out.println(userReg.getUserStatus());
//		userReg.setUserStatus("Active");
//		 return this.user.save(userReg);
//		 
//	}

}
